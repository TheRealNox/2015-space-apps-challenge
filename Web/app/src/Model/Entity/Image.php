<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Image Entity.
 */
class Image extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'image_collection_id' => true,
        'ratings' => true,
        'image_collections' => true,
        'uuid' => true,
        'unique_key' => true,
        'date_taken' => true,
        'tile_x' => true,
        'tile_y' => true,
        'created' => true,
        'modified' => true,
    ];
}
