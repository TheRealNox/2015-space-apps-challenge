<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\ImageCategoriesTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\ImageCategoriesTable Test Case
 */
class ImageCategoriesTableTest extends TestCase
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
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::exists('ImageCategories') ? [] : ['className' => 'App\Model\Table\ImageCategoriesTable'];
        $this->ImageCategories = TableRegistry::get('ImageCategories', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->ImageCategories);

        parent::tearDown();
    }

    /**
     * Test initialize method
     *
     * @return void
     */
    public function testInitialize()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test validationDefault method
     *
     * @return void
     */
    public function testValidationDefault()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }
}
