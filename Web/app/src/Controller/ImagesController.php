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

        foreach ($images as &$image) {
            $image['url'] = $this->generateUrl($image['date_taken'], $image['tile_x'], $image['tile_y']);
        }
        if (isset($image)) {
            unset($image);
        }

        $this->set(compact('images'));
        $this->set('_serialize', ['images']);
    }

    protected function generateUrl($dateTaken, $tileX, $tileY)
    {
        $url = 'https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=%s&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%%2Fjpeg&TileMatrix=8&TileCol=%d&TileRow=%d';
        return sprintf($url, $dateTaken, $tileY, $tileX);
    }
}
