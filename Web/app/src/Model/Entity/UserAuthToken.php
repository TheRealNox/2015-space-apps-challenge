<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * UserAuthToken Entity.
 */
class UserAuthToken extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * @var array
     */
    protected $_accessible = [
        'user_id' => true,
        'token' => true,
        'expires' => true,
        'user' => true,
    ];
}
