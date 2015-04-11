<?php
namespace App\Test\TestCase\Controller;

use App\Controller\UserAuthTokensController;
use Cake\TestSuite\IntegrationTestCase;

/**
 * App\Controller\UserAuthTokensController Test Case
 */
class UserAuthTokensControllerTest extends IntegrationTestCase
{

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'UserAuthTokens' => 'app.user_auth_tokens',
        'Users' => 'app.users',
        'Ratings' => 'app.ratings',
        'Images' => 'app.images',
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
