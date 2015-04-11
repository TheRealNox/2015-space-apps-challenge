<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\ImageCollectionsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\ImageCollectionsTable Test Case
 */
class ImageCollectionsTableTest extends TestCase
{

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'ImageCollections' => 'app.image_collections',
        'Images' => 'app.images',
        'Ratings' => 'app.ratings',
        'Users' => 'app.users',
        'UserAuthTokens' => 'app.user_auth_tokens'
    ];

    /**
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::exists('ImageCollections') ? [] : ['className' => 'App\Model\Table\ImageCollectionsTable'];
        $this->ImageCollections = TableRegistry::get('ImageCollections', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->ImageCollections);

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
