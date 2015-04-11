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

        $images = TableRegistry::get('Images');

        $imageCollections = TableRegistry::get('ImageCollections');
        $collections = $imageCollections->find('list')->toArray();

        foreach ($searchResult['results'] as $result) {
            $exists = $images->find('all', ['conditions' => ['unique_key' => $result['path']['key']]])->first();
            if ($exists) {
                continue;
            }

            $data = [
                'image_collection_id' => (int)array_search($result['value']['Category'], $collections, true),
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
