<?php

namespace App\Controller\Component;

use Cake\Controller\Component;
use Cake\Event\Event;
use Cake\Network\Exception\ForbiddenException;
use Cake\ORM\TableRegistry;

/**
 * A simpler version of the AuthComponent
 *
 * Class ApiAuthComponent
 * @package App\Controller\Component
 */
class ApiAuthComponent extends Component
{
    const TOKEN_PARAM = 'auth_token';
    const USERNAME_FIELD = 'email_address';
    const PASSWORD_FIELD = 'password';
    const PASSWORD_HASH = 'whirlpool';

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

    protected function _unauthenticated()
    {
        throw new ForbiddenException();
    }

    public function setController($controller)
    {
        $this->controller = $controller;
    }

    public function allow($actions = null)
    {
        if ($actions === null) {
            $this->allowAll = true;
            return;
        }
        $this->allowedActions = array_merge($this->allowedActions, (array)$actions);
    }

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

    public function isLoggedIn()
    {
        static $results = [];
        $token = $this->getToken();
        $result = false;
        if ($token === null) {
            return false;
        }

        // Basic caching
        if (isset($results[$token])) {
            return $results[$token];
        }

        $userAuthTokens = TableRegistry::get('UserAuthTokens');
        $userAuthToken = $userAuthTokens->find()->where([
            'token' => $token,
            'expires < NOW()'
        ])->first();

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

    public function login($username, $password)
    {
        $users = TableRegistry::get('Users');
        $user = $users->find()->where([
            self::USERNAME_FIELD => $username,
            self::PASSWORD_FIELD => $this->hashPassword($password)
        ])->first();

        if (!$user) {
            return false;
        }

        $this->setCurrentUser($user);

        $data = [
            'user_id' => $user->id,
            'token' => $this->generateToken()
        ];

        $userAuthTokens = TableRegistry::get('UserAuthTokens');
        $userAuthToken = $userAuthTokens->newEntity($data);
        $userAuthTokens->save($userAuthToken);

        return $userAuthToken['token'];
    }

    protected function setCurrentUser($user)
    {
        if (isset($user['password'])) {
            unset($user['password']);
        }
        $this->currentUser = $user;
    }

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

    protected function generateToken($len = 128)
    {
        $token = openssl_random_pseudo_bytes($len / 2);
        $token = bin2hex($token);
        return $token;
    }

    protected function hashPassword($password)
    {
        return hash(self::PASSWORD_HASH, $password);
    }
}
