<?php
namespace App\Controller;

use App\Controller\AppController;
use Cake\Event\Event;
use Cake\Network\Exception\BadRequestException;
use Cake\Network\Exception\ForbiddenException;
use Cake\Network\Exception\UnauthorizedException;

/**
 * Users Controller
 *
 * @property \App\Model\Table\UsersTable $Users
 */
class UsersController extends AppController
{
    public function beforeFilter(Event $event)
    {
        parent::beforeFilter($event);
        $this->ApiAuth->allow(['login', 'register', 'refresh_auth_token']);
    }

    /**
     * Login method
     *
     * @return void
     */
    public function login()
    {
        if (!$this->request->is('post')) {
            throw new BadRequestException();
        }

        // Can't log in if already logged in :)
        if ($this->ApiAuth->isLoggedIn()) {
            return;
        }

        $emailAddress = $this->request->data('email_address');
        $password = $this->request->data('password');

        $auth_token = $this->ApiAuth->login($emailAddress, $password);

        // Login failure
        if ($auth_token === false) {
            throw new UnauthorizedException();
        }

        $success = true;

        $this->set(compact('auth_token', 'success'));
        $this->set('_serialize', ['auth_token', 'success']);
    }

    /**
     * Register method
     *
     * @return void Redirects on successful add, renders view otherwise.
     */
    public function register()
    {
        if (!$this->request->is('post')) {
            throw new BadRequestException();
        }

        $auth_token = null;
        $success = false;

        $user = $this->Users->newEntity($this->request->data);
        $errors = $user->errors();
        if (!$errors) {
            if ($this->Users->save($user)) {
                $auth_token = $this->ApiAuth->login($user->email_address, $this->request->data('password'));
                if ($auth_token) {
                    $success = true;
                }
            }
        }

        $this->set(compact('auth_token', 'errors', 'success'));
        $this->set('_serialize', ['auth_token', 'errors', 'success']);
    }

    public function refresh_auth_token()
    {
        $auth_token = $this->ApiAuth->refreshAuthToken();

        if (is_string($auth_token)) {
            $success = false;
        } else if ($auth_token) {
            // still logged in
            $success = true;
            $auth_token = null;
        } else {
            // not logged in, and invalid
            $success = false;
            $auth_token = null;
        }

        $this->set(compact('auth_token', 'success'));
        $this->set('_serialize', ['auth_token', 'success']);
    }
}
