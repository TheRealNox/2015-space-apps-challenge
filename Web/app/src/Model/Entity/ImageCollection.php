<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * ImageCollection Entity.
 */
class ImageCollection extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'title' => true,
        'sort_order' => true,
        'image_details' => true,
    ];
}
