<?php
namespace App\Controller;

use App\Controller\AppController;

use Cake\I18n\Time;

/**
 * Error Controller
 */
class ErrorController extends AppController
{

    /**
     * Index method
     *
     * @return void
     */
    public function index()
    {
    }

    public function render($view = NULL, $layout = NULL)
    {
        $this->response->body(null);
        return $this->response;
    }
}
