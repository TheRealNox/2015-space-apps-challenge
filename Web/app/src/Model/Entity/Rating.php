<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Rating Entity.
 */
class Rating extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'user_id' => true,
        'image_id' => true,
        'is_interesting' => true,
        'note' => true,
        'user' => true,
        'image' => true,
    ];
}
