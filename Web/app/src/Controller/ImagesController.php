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
        $limit = (int)$this->request->param('limit');
        if (!$limit || $limit > 100 || $limit < 1) {
            $limit = 20;
        }
        $exclude = explode(',', (string)$this->request->param('exclude'));
        if (!is_array($exclude)) {
            $exclude = [];
        }

        $excludeIds = $this->Images->Ratings->getImageIdsByUserId($this->ApiAuth->user('id'));

        foreach ($exclude as $id) {
            $id = intval($id);
            if ($id > 0 && !in_array($id, $excludeIds)) {
                $excludeIds[] = $id;
            }
        }

        $conditions = [];
        if ($excludeIds) {
            $conditions['Images.id NOT IN'] = $excludeIds;
        }

        $images = $this->Images->find(
            'all', [
                'conditions' => $conditions,
                'limit' => $limit,
                'order' => 'Images.id DESC',
                'contain' => ['ImageDetails']
            ]
        );

        $this->Images->addExtras($images);

        $success = true;

        $this->set(compact('images', 'success'));
        $this->set('_serialize', ['images', 'success']);
    }
}
