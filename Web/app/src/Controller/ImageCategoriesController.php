<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * ImageCategories Controller
 *
 * @property \App\Model\Table\ImageCategoriesTable $ImageCategories
 */
class ImageCategoriesController extends AppController
{

    /**
     * Index method
     *
     * @return void
     */
    public function index()
    {
        $this->set('imageCategories', $this->paginate($this->ImageCategories));
        $this->set('_serialize', ['imageCategories']);
    }

    /**
     * View method
     *
     * @param string|null $id Image Category id.
     * @return void
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function view($id = null)
    {
        $imageCategory = $this->ImageCategories->get($id, [
            'contain' => ['Ratings']
        ]);
        $this->set('imageCategory', $imageCategory);
        $this->set('_serialize', ['imageCategory']);
    }

    /**
     * Add method
     *
     * @return void Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $imageCategory = $this->ImageCategories->newEntity();
        if ($this->request->is('post')) {
            $imageCategory = $this->ImageCategories->patchEntity($imageCategory, $this->request->data);
            if ($this->ImageCategories->save($imageCategory)) {
                $this->Flash->success('The image category has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The image category could not be saved. Please, try again.');
            }
        }
        $this->set(compact('imageCategory'));
        $this->set('_serialize', ['imageCategory']);
    }

    /**
     * Edit method
     *
     * @param string|null $id Image Category id.
     * @return void Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $imageCategory = $this->ImageCategories->get($id, [
            'contain' => []
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $imageCategory = $this->ImageCategories->patchEntity($imageCategory, $this->request->data);
            if ($this->ImageCategories->save($imageCategory)) {
                $this->Flash->success('The image category has been saved.');
                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error('The image category could not be saved. Please, try again.');
            }
        }
        $this->set(compact('imageCategory'));
        $this->set('_serialize', ['imageCategory']);
    }

    /**
     * Delete method
     *
     * @param string|null $id Image Category id.
     * @return void Redirects to index.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $imageCategory = $this->ImageCategories->get($id);
        if ($this->ImageCategories->delete($imageCategory)) {
            $this->Flash->success('The image category has been deleted.');
        } else {
            $this->Flash->error('The image category could not be deleted. Please, try again.');
        }
        return $this->redirect(['action' => 'index']);
    }
}
