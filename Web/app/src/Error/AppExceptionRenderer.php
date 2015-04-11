<?php

namespace App\Error;

use App\Controller\ErrorController;
use Cake\Error\ExceptionRenderer;

class AppExceptionRenderer extends ExceptionRenderer
{
    protected function _getController()
    {
        return new ErrorController();
    }
}
