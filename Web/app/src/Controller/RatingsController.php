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

    public function index()
    {

    }




}
