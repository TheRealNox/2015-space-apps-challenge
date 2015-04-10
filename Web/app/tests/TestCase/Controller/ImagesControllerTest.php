<?php
namespace App\Test\TestCase\Controller;

use App\Controller\ImagesController;
use Cake\TestSuite\IntegrationTestCase;

/**
 * App\Controller\ImagesController Test Case
 */
class ImagesControllerTest extends IntegrationTestCase
{

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'Images' => 'app.images',
        'Ratings' => 'app.ratings',
        'Users' => 'app.users',
        'UserAuthTokens' => 'app.user_auth_tokens',
        'ImageCategories' => 'app.image_categories'
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
