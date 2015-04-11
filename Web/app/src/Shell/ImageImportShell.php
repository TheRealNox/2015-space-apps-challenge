<?php

namespace App\Shell;

use Cake\Console\Shell;
use Cake\Core\Configure;
use Cake\ORM\TableRegistry;

use SocalNick\Orchestrate\Client;
use SocalNick\Orchestrate\SearchOperation;

class ImageImportShell extends Shell
{
    public function main()
    {
        $client = new Client(Configure::read('Orchestrate.api_key'));

        $searchOp = new SearchOperation('discovery');
        $searchResult = $client->execute($searchOp)->getValue();

        $url = 'https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=%s&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%%2Fjpeg&TileMatrix=8&TileCol=%d&TileRow=%d';

        $images = TableRegistry::get('Images');

        foreach ($searchResult['results'] as $result) {
            $data = [
                //'image_collection_id' => '',
                'uuid' => $result['value']['CoordinateUUID'],
                'unique_key' => $result['path']['key'],
                'date_taken' => $result['value']['Time'],
                'tile_x' => $result['value']['TilePosition']['x'],
                'tile_y' => $result['value']['TilePosition']['y']
            ];

            $image = $images->newEntity($data);
            $images->save($image);
        }
    }
}
