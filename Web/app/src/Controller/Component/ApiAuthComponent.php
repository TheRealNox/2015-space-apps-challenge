<?php

namespace App\Controller\Component;

use Cake\Controller\Component;
use Cake\Event\Event;
use Cake\Network\Exception\ForbiddenException;
use Cake\ORM\TableRegistry;
use Cake\I18n\Time;

/**
 * A simpler version of the AuthComponent
 *
 * Class ApiAuthComponent
 * @package App\Controller\Component
 */
class ApiAuthComponent extends Component
{
    const TOKEN_PARAM = 'auth_token';
    const TOKEN_EXPIRY = '+7 days';
    const USERNAME_FIELD = 'email_address';
    const PASSWORD_FIELD = 'password';

    /**
     * @var array
     */
    protected $allowedActions = [];

    /**
     * @var bool
     */
    protected $allowAll = false;

    /**
     * @var array
     */
    protected $currentUser = [];

    /**
     * @var \App\Controller\AppController
     */
    protected $controller;

    /**
     * @return bool
     */
    protected function _isAllowed()
    {
        if ($this->allowAll) {
            return true;
        }
        $action = strtolower($this->controller->request->params['action']);
        if (in_array($action, array_map('strtolower', $this->allowedActions))) {
            return true;
        }
        return false;
    }

    /**
     * @throws ForbiddenException
     */
    protected function _unauthenticated()
    {
        throw new ForbiddenException();
    }

    /**
     * @param $controller
     */
    public function setController($controller)
    {
        $this->controller = $controller;
    }

    /**
     * @param null $actions
     */
    public function allow($actions = null)
    {
        if ($actions === null) {
            $this->allowAll = true;
            return;
        }
        $this->allowedActions = array_merge($this->allowedActions, (array)$actions);
    }

    /**
     * @param Event $event
     */
    public function startup(Event $event)
    {
        $this->setController($event->subject());

        $action = strtolower($this->controller->request->params['action']);
        if (!$this->controller->isAction($action)) {
            return;
        }

        if ($this->_isAllowed($this->controller)) {
            return;
        }

        if ($this->isLoggedIn()) {
            return;
        }

        $event->stopPropagation();
        return $this->_unauthenticated();
    }

    /**
     * @return $this|mixed|null
     */
    protected function getToken()
    {
        $token = null;

        // $_GET data
        if ($this->controller->request->query(self::TOKEN_PARAM)) {
            $token = $this->controller->request->query(self::TOKEN_PARAM);
        }

        // $_POST data
        if ($this->controller->request->data(self::TOKEN_PARAM)) {
            $token = $this->controller->request->data(self::TOKEN_PARAM);
        }

        return $token;
    }

    /**
     * @return bool
     */
    public function isLoggedIn($cache = true, $expiry = true)
    {
        static $results = [];
        $token = $this->getToken();
        $result = false;
        if ($token === null) {
            return false;
        }

        // Basic caching
        if ($cache) {
            if (isset($results[$token])) {
                return $results[$token];
            }
        }

        $userAuthTokens = TableRegistry::get('UserAuthTokens');
        $users = TableRegistry::get('Users');
        $conditions = [
            'token' => $token,
        ];
        if ($expiry) {
            $conditions[] = 'expires > NOW()';
        }
        $userAuthToken = $userAuthTokens->find()->where($conditions)->first();

        // Logged in
        if ($userAuthToken) {
            if (!isset($this->currentUser['id']) || $this->currentUser['id'] !== $userAuthToken['user_id']) {
                $user = $users->find()->where([
                    'id' => $userAuthToken['user_id']
                ])->first();

                if ($user) {
                    $this->setCurrentUser($user);
                    $result = true;
                }
            } else {
                $result = true;
            }
        }
        return $result;
    }

    /**
     * @param string $username
     * @param string $password
     * @return bool
     */
    public function login($username, $password)
    {
        $users = TableRegistry::get('Users');
        $conditions = [
            self::USERNAME_FIELD => $username,
            self::PASSWORD_FIELD => $users->hashPassword($password)
        ];
        $user = $users->find()->where($conditions)->first();

        if (!$user) {
            return false;
        }

        $this->setCurrentUser($user);

        return $this->createToken();
    }

    protected function createToken()
    {
        $expires = Time::now();
        $expires->modify(self::TOKEN_EXPIRY);

        $data = [
            'user_id' => $this->currentUser->id,
            'token' => $this->generateToken(),
            'expires' => $expires
        ];

        $userAuthTokens = TableRegistry::get('UserAuthTokens');
        $userAuthToken = $userAuthTokens->newEntity($data);
        $userAuthTokens->save($userAuthToken);

        return $userAuthToken['token'];
    }

    /**
     * @param $user
     */
    protected function setCurrentUser($user)
    {
        if (isset($user['password'])) {
            unset($user['password']);
        }
        $this->currentUser = $user;
    }

    /**
     * @param null $key
     * @return array|bool
     */
    public function user($key = null)
    {
        if (!$this->isLoggedIn()) {
            return false;
        }

        if ($key) {
            return $this->currentUser[$key];
        }
        return $this->currentUser;
    }

    public function refreshAuthToken()
    {
        if ($this->isLoggedIn()) {
            return true;
        } else if ($this->isLoggedIn(false, false)) {
            return $this->createToken();
        }
        return false;
    }

    /**
     * @return string
     */
    protected function generateToken()
    {
        $token = openssl_random_pseudo_bytes(128);
        $token = bin2hex($token);
        return hash('whirlpool', $token);
    }
}
