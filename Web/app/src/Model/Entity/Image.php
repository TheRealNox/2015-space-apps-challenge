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
        'image_detail_id' => true,
        'ratings' => true,
        'image_collections' => true,
        'image_details' => true,
        'unique_key' => true,
        'date_taken' => true,
        'created' => true,
        'modified' => true,
    ];
}
