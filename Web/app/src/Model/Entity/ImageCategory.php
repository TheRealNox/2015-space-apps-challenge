<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * ImageCategory Entity.
 */
class ImageCategory extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'title' => true,
        'sort_order' => true,
        'ratings' => true,
    ];
}
