<?php
/**
 * Routes configuration
 *
 * In this file, you set up routes to your controllers and their actions.
 * Routes are very important mechanism that allows you to freely connect
 * different URLs to chosen controllers and their actions (functions).
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @license       http://www.opensource.org/licenses/mit-license.php MIT License
 */

use Cake\Core\Plugin;
use Cake\Routing\Router;

/**
 * The default class to use for all routes
 *
 * The following route classes are supplied with CakePHP and are appropriate
 * to set as the default:
 *
 * - Route
 * - InflectedRoute
 * - DashedRoute
 *
 * If no call is made to `Router::defaultRouteClass`, the class used is
 * `Route` (`Cake\Routing\Route\Route`)
 *
 * Note that `Route` does not do any inflections on URLs which will result in
 * inconsistently cased URLs when used with `:plugin`, `:controller` and
 * `:action` markers.
 *
 */
//Router::defaultRouteClass('Route');

Router::scope('/', function ($routes) {

    /**
     * These routes will probably be removed eventually.
     */
    /*$routes->connect('/', ['controller' => 'Pages', 'action' => 'display', 'home']);
    $routes->connect('/pages/*', ['controller' => 'Pages', 'action' => 'display']);

    $routes->fallbacks('InflectedRoute');*/
});

Router::scope('/api', function ($routes) {
    $routes->extensions(['json']);

    $routes->resources('Users', [
        'only' => ['register', 'login'],
        'map' => [
            'register' => [
                'action' => 'register',
                'method' => 'POST'
            ],
            'login' => [
                'action' => 'login',
                'method' => 'POST'
            ],
            'refresh_auth_token' => [
                'action' => 'refresh_auth_token',
                'method' => 'GET'
            ]
        ]
    ]);

    $routes->resources('Ratings', [
        'only' => ['index', 'add'],
        'map' => [
            'index' => [
                'action' => 'index',
                'method' => 'GET'
            ],
            'add' => [
                'action' => 'add',
                'method' => 'POST'
            ],
        ]
    ]);

    $routes->resources('Images', [
        'only' => ['index', 'get_previous'],
        'map' => [
            'index' => [
                'action' => 'index',
                'method' => 'GET'
            ],
            'get_previous' => [
                'action' => 'get_previous',
                'method' => 'GET'
            ]
        ]
    ]);
});

/**
 * Load all plugin routes.  See the Plugin documentation on
 * how to customize the loading of plugin routes.
 */
Plugin::routes();
