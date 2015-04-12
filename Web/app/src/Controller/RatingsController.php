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
    public function index()
    {
        $ratings = $this->Ratings->find('all', [
            'conditions' => [
                'user_id' => $this->ApiAuth->user('id')
            ]
        ]);

        $this->set(compact('ratings'));
        $this->set('_serialize', ['ratings']);
    }

    /**
     * Add method
     *
     * @return void
     */
    public function add()
    {
        $data = $this->request->data;

        if (isset($data['ratings'])) {
            $data = $data['ratings'];
        } else {
            $data = [
                [
                    'image_id' => $data['image_id'],
                    'is_interesting' => $data['is_interesting'],
                ]
            ];
        }

        $rows_total = count($data);
        $rows_saved = 0;
        $errors = [];
        $imageIds = [];

        if (!is_array($data)) {
            $data = json_decode($data);
        }

        if (is_array($data)) {
            foreach ($data as $rating) {
                $rating = (array)$rating;
                if (!isset($rating['image_id'], $rating['is_interesting'])) {
                    continue;
                }
                if (!in_array($rating['image_id'], $imageIds) && $rating['is_interesting']) {
                    $imageIds[] = $rating['image_id'];
                }
                $rating['user_id'] = $this->ApiAuth->user('id');
                $newRating = $this->Ratings->newEntity();
                $newRating = $this->Ratings->patchEntity($newRating, $rating);
                var_dump($newRating);
                if (!is_object($newRating)) {
                    die();
                }
                $rErrors = (array)$newRating->errors();
                if ($rErrors) {
                    var_dump($rErrors);
                    $errors[] = $rErrors;
                } else {
                    echo '__noerror__';
                    if ($this->Ratings->save($rating)) {
                        $rows_saved++;
                    }
                }
            }
        }

        $success = ($rows_total === $rows_saved);

        $this->Ratings->Images->calculateInteresting($imageIds);

        $this->set(compact('errors', 'success', 'rows_total', 'rows_saved'));
        $this->set('_serialize', ['errors', 'success', 'rows_total', 'rows_saved']);
    }
}
