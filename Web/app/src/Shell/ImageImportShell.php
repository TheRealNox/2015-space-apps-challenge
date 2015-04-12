<?php

namespace App\Shell;

use Cake\Console\Shell;
use Cake\Core\Configure;
use Cake\Datasource\ConnectionManager;
use Cake\ORM\TableRegistry;

use SocalNick\Orchestrate\Client;
use SocalNick\Orchestrate\SearchOperation;

class ImageImportShell extends Shell
{
    const BATCH_SIZE = 100;
    const LOG_QUERIES = false;
    const GOOGLE_MAX_PER_SECOND = 10;

    /**
     * @var array
     */
    protected $googleRateLimiting = [];

    public function main()
    {
        $client = new Client(Configure::read('Orchestrate.api_key'));
        $images = TableRegistry::get('Images');

        if (self::LOG_QUERIES) {
            $conn = ConnectionManager::get('default');
            $conn->logQueries(true);
        }

        for ($n = 0; true; $n += self::BATCH_SIZE) {
            $searchOp = new SearchOperation('discovery', '*', self::BATCH_SIZE, $n);
            $searchResult = $client->execute($searchOp);

            if ($searchResult->count() === 0) {
                break;
            }

            $imageCollections = TableRegistry::get('ImageCollections');
            $collections = $imageCollections->find('list')->toArray();

            foreach ($searchResult->getValue()['results'] as $result) {
                $exists = $images->find('all', ['conditions' => ['unique_key' => $result['path']['key']]])->first();
                if ($exists) {
                    continue;
                }

                $data = [
                    'image_collection_id' => (int)array_search($result['value']['Category'], $collections, true),
                    'uuid' => $result['value']['CoordinateUUID'],
                    'tile_x' => $result['value']['TilePosition']['x'],
                    'tile_y' => $result['value']['TilePosition']['y'],
                    'lat_top' => $result['value']['TileCoordinates']['TopLatitude'],
                    'lat_bottom' => $result['value']['TileCoordinates']['BottomLatitude'],
                    'location_name' => $locationName,
                    'long_top' => $result['value']['TileCoordinates']['TopLongitude'],
                    'long_bottom' => $result['value']['TileCoordinates']['BottomLongitude'],
                ];
                $imageDetailId = $this->getImageDetailId($data);

                $data = [
                    'image_detail_id' => $imageDetailId,
                    'unique_key' => $result['path']['key'],
                    'date_taken' => $result['value']['Time']
                ];

                $image = $images->newEntity($data);
                $images->save($image);
            }
        }
    }

    protected function getImageDetailId($data)
    {
        $imageDetails = TableRegistry::get('ImageDetails');

        $exists = $imageDetails->find('all', ['conditions' => ['uuid' => $data['uuid']]])->first();
        if ($exists) {
            return $exists->id;
        }

        // Rate limiting start
        $idx = time();
        if (!isset($this->googleRateLimiting[$idx])) {
            $this->googleRateLimiting[$idx] = 1;
        } else {
            $this->googleRateLimiting[$idx]++;
        }
        if ($this->googleRateLimiting[$idx] >= self::GOOGLE_MAX_PER_SECOND) {
            $this->googleRateLimiting[$idx+1] = 1;
            sleep(1);
        }
        // End rate limiting

        $geocodeUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s";
        $lat = $data['lat_top'];
        $long = $data['long_top'];

        $ch = curl_init(sprintf($geocodeUrl, $lat, $long));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $geocode = @json_decode(curl_exec($ch));
        curl_close($ch);
        $locationName = null;
        if ($geocode->results) {
            $location = array_shift($geocode->results);
            $data['location_name'] = $location->formatted_address;
        }

        $imageDetail = $imageDetails->newEntity($data);
        $result = $imageDetails->save($imageDetail);
        return $result->id;
    }
}
