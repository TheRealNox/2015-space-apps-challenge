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

        if (isset($data['image_id'])) {
            $data = [$data];
        }

        $rows_total = count($data);
        $rows_saved = 0;
        $errors = [];

        foreach ($data as $rating) {
            $rating['user_id'] = $this->ApiAuth->user('id');
            $newRating = $this->Ratings->newEntity();
            $newRating = $this->Ratings->patchEntity($newRating, $rating);
            $rErrors = (array)$newRating->errors();
            if ($rErrors) {
                $errors[] = $rErrors;
            } else {
                if ($this->Ratings->save($rating)) {
                    $rows_saved++;
                }
            }
        }

        $success = ($rows_total === $rows_saved);

        $this->set(compact('errors', 'success', 'rows_total', 'rows_saved'));
        $this->set('_serialize', ['errors', 'success', 'rows_total', 'rows_saved']);
    }
}