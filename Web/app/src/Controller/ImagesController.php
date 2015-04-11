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
            $conditions['id NOT IN'] = $excludeIds;
        }

        $images = $this->Images->find(
            'all', [
                'conditions' => $conditions,
                'limit' => $limit,
                'order' => 'id DESC'
            ]
        );

        foreach ($images as $image) {
            $date = new Time($image['date_taken']);
            $date = $date->i18nFormat('YYYY-MM-dd');
            $image->url = $this->generateUrl($date, $image['tile_x'], $image['tile_y']);
        }

        $success = true;

        $this->set(compact('images', 'success'));
        $this->set('_serialize', ['images', 'success']);
    }

    protected function generateUrl($dateTaken, $tileX, $tileY)
    {
        $url = 'https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=%s&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%%2Fjpeg&TileMatrix=8&TileCol=%d&TileRow=%d';
        return sprintf($url, $dateTaken, $tileY, $tileX);
    }
}
