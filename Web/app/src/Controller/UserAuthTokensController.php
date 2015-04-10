<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * UserAuthTokens Controller
 *
 * @property \App\Model\Table\UserAuthTokensTable $UserAuthTokens
 */
class UserAuthTokensController extends AppController
{

    /**
     * Index method
     *
     * @return void
     */
    public function index()
    {
        $this->paginate = [
            'contain' => ['Users']
        ];
        $this->set('userAuthTokens', $this->paginate($this->UserAuthTokens));
        $this->set('_serialize', ['userAuthTokens']);
    }

    /**
     * View method
     *
     * @param string|null $id User Auth Token id.
     * @return void
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function view($id = null)
    {
        $userAuthToken = $this->UserAuthTokens->get($id, [
            'contain' => ['Users']
        ]);
        $this->set('userAuthToken', $userAuthToken);
        $this->set('_serialize', ['userAuthToken']);
    }

    /**
     * Add method
     *
     * @return void Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $userAuthToken = $this->UserAuthTokens->newEntity();
        if ($this->request->is('post')) {
            $userAuthToken = $this->UserAuthTokens->patchEntity($userAuthToken, $this->request->data);
            if ($this->UserAuthTokens->save($userAuthToken)) {
                $this->Flash->success('The user auth token has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The user auth token could not be saved. Please, try again.');
            }
        }
        $users = $this->UserAuthTokens->Users->find('list', ['limit' => 200]);
        $this->set(compact('userAuthToken', 'users'));
        $this->set('_serialize', ['userAuthToken']);
    }

    /**
     * Edit method
     *
     * @param string|null $id User Auth Token id.
     * @return void Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $userAuthToken = $this->UserAuthTokens->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $userAuthToken = $this->UserAuthTokens->patchEntity($userAuthToken, $this->request->data);
            if ($this->UserAuthTokens->save($userAuthToken)) {
                $this->Flash->success('The user auth token has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The user auth token could not be saved. Please, try again.');
            }
        }
        $users = $this->UserAuthTokens->Users->find('list', ['limit' => 200]);
        $this->set(compact('userAuthToken', 'users'));
        $this->set('_serialize', ['userAuthToken']);
    }

    /**
     * Delete method
     *
     * @param string|null $id User Auth Token id.
     * @return void Redirects to index.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $userAuthToken = $this->UserAuthTokens->get($id);
        if ($this->UserAuthTokens->delete($userAuthToken)) {
            $this->Flash->success('The user auth token has been deleted.');
        } else {
            $this->Flash->error('The user auth token could not be deleted. Please, try again.');
        }
        return $this->redirect(['action' => 'index']);
    }
}
