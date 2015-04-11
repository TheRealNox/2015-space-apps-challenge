<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * Images Controller
 *
 * @property \App\Model\Table\ImagesTable $Images
 */
class ImagesController extends AppController
{

    /**
     * Index method
     *
     * @return void
     */
    public function index()
    {
        $ratingIds = $this->Images->Ratings->getImageIdsByUserId($this->ApiAuth->user('id'));

        $conditions = [];
        if ($ratingIds) {
            $conditions['id NOT IN'] = $ratingIds;
        }

        $images = $this->Images->find('all', ['conditions' => $conditions]);

        $this->set(compact('images'));
        $this->set('_serialize', ['images']);
    }
}
