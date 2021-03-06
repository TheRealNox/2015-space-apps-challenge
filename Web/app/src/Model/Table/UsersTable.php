<?php
namespace App\Model\Table;

use App\Model\Entity\User;
use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * Users Model
 */
class UsersTable extends Table
{
    const PASSWORD_HASH = 'whirlpool';

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        $this->table('users');
        $this->displayField('id');
        $this->primaryKey('id');
        $this->addBehavior('Timestamp');
        $this->hasMany('Ratings', [
            'foreignKey' => 'user_id'
        ]);
        $this->hasMany('UserAuthTokens', [
            'foreignKey' => 'user_id'
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
            ->add('email_address', 'validFormat', ['rule' => 'email'])
            ->requirePresence('email_address', true)
            ->requirePresence('password', true);


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
        $rules->add($rules->isUnique(['email_address']));
        return $rules;
    }

    public function beforeSave($event, $user, $options)
    {
        if (!$user->isNew() && $user->has('password') && empty($user->password)) {
            $user->unsetProperty('password');
        } else {
            $user->password = $this->hashPassword($user->password);
        }
    }

    public function hashPassword($password)
    {
        return hash(self::PASSWORD_HASH, $password);
    }
}
