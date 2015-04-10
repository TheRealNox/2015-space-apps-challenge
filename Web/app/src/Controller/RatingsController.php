<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * Ratings Controller
 *
 * @property \App\Model\Table\RatingsTable $Ratings
 */
class RatingsController extends AppController
{

    /**
     * Index method
     *
     * @return void
     */
    public function index()
    {
        $this->paginate = [
            'contain' => ['Users', 'Images', 'ImageCategories']
        ];
        $this->set('ratings', $this->paginate($this->Ratings));
        $this->set('_serialize', ['ratings']);
    }

    /**
     * View method
     *
     * @param string|null $id Rating id.
     * @return void
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function view($id = null)
    {
        $rating = $this->Ratings->get($id, [
            'contain' => ['Users', 'Images', 'ImageCategories']
        ]);
        $this->set('rating', $rating);
        $this->set('_serialize', ['rating']);
    }

    /**
     * Add method
     *
     * @return void Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $rating = $this->Ratings->newEntity();
        if ($this->request->is('post')) {
            $rating = $this->Ratings->patchEntity($rating, $this->request->data);
            if ($this->Ratings->save($rating)) {
                $this->Flash->success('The rating has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The rating could not be saved. Please, try again.');
            }
        }
        $users = $this->Ratings->Users->find('list', ['limit' => 200]);
        $images = $this->Ratings->Images->find('list', ['limit' => 200]);
        $imageCategories = $this->Ratings->ImageCategories->find('list', ['limit' => 200]);
        $this->set(compact('rating', 'users', 'images', 'imageCategories'));
        $this->set('_serialize', ['rating']);
    }

    /**
     * Edit method
     *
     * @param string|null $id Rating id.
     * @return void Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $rating = $this->Ratings->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $rating = $this->Ratings->patchEntity($rating, $this->request->data);
            if ($this->Ratings->save($rating)) {
                $this->Flash->success('The rating has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The rating could not be saved. Please, try again.');
            }
        }
        $users = $this->Ratings->Users->find('list', ['limit' => 200]);
        $images = $this->Ratings->Images->find('list', ['limit' => 200]);
        $imageCategories = $this->Ratings->ImageCategories->find('list', ['limit' => 200]);
        $this->set(compact('rating', 'users', 'images', 'imageCategories'));
        $this->set('_serialize', ['rating']);
    }

    /**
     * Delete method
     *
     * @param string|null $id Rating id.
     * @return void Redirects to index.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $rating = $this->Ratings->get($id);
        if ($this->Ratings->delete($rating)) {
            $this->Flash->success('The rating has been deleted.');
        } else {
            $this->Flash->error('The rating could not be deleted. Please, try again.');
        }
        return $this->redirect(['action' => 'index']);
    }
}
