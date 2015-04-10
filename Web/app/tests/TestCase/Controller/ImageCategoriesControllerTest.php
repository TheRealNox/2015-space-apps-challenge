<?php
namespace App\Test\TestCase\Controller;

use App\Controller\ImageCategoriesController;
use Cake\TestSuite\IntegrationTestCase;

/**
 * App\Controller\ImageCategoriesController Test Case
 */
class ImageCategoriesControllerTest extends IntegrationTestCase
{

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'ImageCategories' => 'app.image_categories',
        'Ratings' => 'app.ratings',
        'Users' => 'app.users',
        'UserAuthTokens' => 'app.user_auth_tokens',
        'Images' => 'app.images'
    ];

    /**
     * Test index method
     *
     * @return void
     */
    public function testIndex()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test view method
     *
     * @return void
     */
    public function testView()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test add method
     *
     * @return void
     */
    public function testAdd()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test edit method
     *
     * @return void
     */
    public function testEdit()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test delete method
     *
     * @return void
     */
    public function testDelete()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }
}
