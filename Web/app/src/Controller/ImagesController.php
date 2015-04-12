<?php
namespace App\Controller;

use App\Controller\AppController;

use Cake\I18n\Time;

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
     * @!param int $image_detail_id
     * @!param int $limit
     * @!param string $exclude
     * @!param bool $exclude_rated
     * @return void
     */
    public function index()
    {
        $imageDetailId = (int)$this->request->query('image_detail_id');
        $limit = (int)$this->request->query('limit');
        if (!$limit || $limit > 100 || $limit < 1) {
            $limit = 20;
        }
        $exclude = explode(',', (string)$this->request->query('exclude'));
        if (!is_array($exclude)) {
            $exclude = [];
        }
        $exclude_rated = (string)$this->request->query('exclude_rated');
        if (strlen($exclude_rated) > 0 && !$exclude_rated) {
            $exclude_rated = false;
        } else {
            $exclude_rated = true;
        }

        if ($exclude_rated) {
            $excludeIds = $this->Images->Ratings->getImageIdsByUserId($this->ApiAuth->user('id'));
        }

        foreach ($exclude as $id) {
            $id = intval($id);
            if ($id > 0 && !in_array($id, $excludeIds)) {
                $excludeIds[] = $id;
            }
        }

        $conditions = [];
        if ($imageDetailId) {
            $conditions['Images.image_detail_id'] = $imageDetailId;
        }
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

    public function get_previous()
    {
        $limit = (int)$this->request->query('limit');
        if (!$limit || $limit > 20 || $limit < 1) {
            $limit = 5;
        }
        $image_id = (int)$this->request->query('image_id');

        $image = $this->Images
            ->find(
                'all', [
                    'conditions' => [
                        'Images.id' => $image_id
                    ],
                    'contain' => ['ImageDetails']
                ]
            )
            ->first();

        $images = [];
        $success = false;

        if ($image) {
            $date = new Time($image->date_taken);
            $tileX = $image->image_detail->tile_x;
            $tileY = $image->image_detail->tile_y;

            for ($i = 0; $i < $limit; $i++) {
                $date->modify('-1 day');
                $dateTaken = $date->i18nFormat('YYYY-MM-dd');

                $imgObj = new \stdClass();
                $imgObj->url = $this->Images->generateUrl($dateTaken, $tileX, $tileY);
                $imgObj->date_taken = (string)$dateTaken;

                $images[] = $imgObj;
            }

            $success = true;
        }

        $this->set(compact('images', 'success'));
        $this->set('_serialize', ['images', 'success']);
    }
}
