<?php
namespace App\Model\Table;

use App\Model\Entity\Image;
use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * Images Model
 */
class ImagesTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        $this->table('images');
        $this->displayField('id');
        $this->primaryKey('id');
        $this->addBehavior('Timestamp');
        $this->hasMany('Ratings', [
            'foreignKey' => 'image_id'
        ]);
        $this->belongsTo('ImageDetails', [
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
            ->allowEmpty('id', 'create');

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
        $rules->add($rules->existsIn(['image_detail_id'], 'ImageDetails'));
        return $rules;
    }

    public function calculateInteresting($imageIds)
    {
        if (!is_array($imageIds)) {
            $imageIds = [$imageIds];
        }
        foreach ($imageIds as $imageId) {
            $ratings = $this->Ratings->find(
                'all', [
                    'fields' => [
                        'id'
                    ],
                    'conditions' => [
                        'image_id' => $imageId,
                        'is_interesting' => 1
                    ]
                ]
            );
            $sum = $ratings->count();

            $query = $this->query();
            $query->update()
                ->set(['interesting_count' => $sum])
                ->where(['id' => $imageId])
                ->execute();
        }
    }
}
