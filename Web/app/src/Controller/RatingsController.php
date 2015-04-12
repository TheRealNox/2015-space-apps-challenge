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
        $is_interesting = (string)$this->request->query('is_interesting');

        $conditions = [
            'user_id' => $this->ApiAuth->user('id')
        ];
        if (strlen($is_interesting) > 0) {
            $conditions['is_interesting'] = (int)(bool)$is_interesting;
        }

        $ratings = $this->Ratings->find('all', [
            'conditions' => $conditions,
            'contain' => [
                'Images' => [
                    'ImageDetails'
                ]
            ]
        ]);

        foreach ($ratings as $rating) {
            $this->Ratings->Images->addExtra($rating->image);
        }

        $success = true;

        $this->set(compact('ratings', 'success'));
        $this->set('_serialize', ['ratings', 'success']);
    }

    /**
     * Add method
     *
     * @return void
     */
    public function add()
    {
        if (isset($this->request->data['ratings'])) {
            $data = $this->request->data['ratings'];
        } else {
            $data = [
                [
                    'image_id' => $this->request->data('image_id'),
                    'is_interesting' => $this->request->data('is_interesting'),
                    'note' => $this->request->data('note'),
                ]
            ];
        }

        if (!is_array($data)) {
            $data = json_decode($data);
        }
        $rows_saved = 0;
        $errors = [];
        $imageIds = [];
        $rows_total = count($data);

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
                $exists = $this->Ratings->find('all', ['conditions' => ['user_id' => $rating['user_id'], 'image_id' => $rating['user_id']]])->first();
                if ($exists) {
                    $errors[] = sprintf('Image %d has already been rated', $rating['image_id']);
                    continue;
                }
                $newRating = $this->Ratings->newEntity($rating);
                $rErrors = (array)$newRating->errors();
                if ($rErrors) {
                    $errors[] = $rErrors;
                } else {
                    if ($this->Ratings->save($newRating)) {
                        $rows_saved++;
                    } else {
                        $errors[] = (array)$newRating->errors();
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
