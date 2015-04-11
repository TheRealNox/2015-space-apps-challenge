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
        $this->ApiAuth->allow(['login', 'register']);
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

        $token = $this->ApiAuth->login($emailAddress, $password);

        // Login failure
        if ($token === false) {
            throw new UnauthorizedException();
        }

        $response = [
            'token' => $token,
            'success' => true
        ];

        $this->set(compact('response'));
        $this->set('_serialize', ['response']);
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

        $token = null;
        $errors = null;
        $success = false;

        $user = $this->Users->newEntity();
        $user = $this->Users->patchEntity($user, $this->request->data);
        $errors = $user->errors();
        if (!$errors) {
            if ($this->Users->save($user)) {
                $token = $this->ApiAuth->login($user->email_address, $this->request->data('password'));
                if ($token) {
                    $success = true;
                }
            }
        }

        $this->set(compact('token', 'errors', 'success'));
        $this->set('_serialize', ['token', 'errors', 'success']);
    }
}
