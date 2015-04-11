<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * ImageDetail Entity.
 */
class ImageDetail extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'image_collection_id' => true,
        'uuid' => true,
        'lat_top' => true,
        'lat_bottom' => true,
        'long_top' => true,
        'long_bottom' => true,
        'tile_x' => true,
        'tile_y' => true,
        'image_collection' => true,
        'images' => true,
    ];
}
