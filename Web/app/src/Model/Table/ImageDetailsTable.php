<?php
namespace App\Model\Table;

use App\Model\Entity\ImageDetail;
use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * ImageDetails Model
 */
class ImageDetailsTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        $this->table('image_details');
        $this->displayField('id');
        $this->primaryKey('id');
        $this->addBehavior('Timestamp');
        $this->belongsTo('ImageCollections', [
            'foreignKey' => 'image_collection_id'
        ]);
        $this->hasMany('Images', [
            'foreignKey' => 'image_detail_id'
        ]);
    }

    /**
     * Default validation rules.
     *
     * @param \Cake\Validation\Validator $validator Validator instance.
     * @return \Cake\Validation\Validator
     */
    public function validationDefault(Validator $validator)
    {
        $validator
            ->add('id', 'valid', ['rule' => 'numeric'])
            ->allowEmpty('id', 'create')
            ->add('uuid', 'valid', ['rule' => 'numeric'])
            ->allowEmpty('uuid')
            ->add('lat', 'valid', ['rule' => 'decimal'])
            ->allowEmpty('lat')
            ->add('long', 'valid', ['rule' => 'decimal'])
            ->allowEmpty('long')
            ->add('tile_x', 'valid', ['rule' => 'numeric'])
            ->allowEmpty('tile_x')
            ->add('tile_y', 'valid', ['rule' => 'numeric'])
            ->allowEmpty('tile_y');

        return $validator;
    }

    /**
     * Returns a rules checker object that will be used for validating
     * application integrity.
     *
     * @param \Cake\ORM\RulesChecker $rules The rules object to be modified.
     * @return \Cake\ORM\RulesChecker
     */
    public function buildRules(RulesChecker $rules)
    {
        $rules->add($rules->existsIn(['image_collection_id'], 'ImageCollections'));
        return $rules;
    }
}
