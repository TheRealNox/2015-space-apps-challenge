<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('Edit User'), ['action' => 'edit', $user->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete User'), ['action' => 'delete', $user->id], ['confirm' => __('Are you sure you want to delete # {0}?', $user->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Users'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Ratings'), ['controller' => 'Ratings', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Rating'), ['controller' => 'Ratings', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List User Auth Tokens'), ['controller' => 'UserAuthTokens', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User Auth Token'), ['controller' => 'UserAuthTokens', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="users view large-10 medium-9 columns">
    <h2><?= h($user->id) ?></h2>
    <div class="row">
        <div class="large-5 columns strings">
            <h6 class="subheader"><?= __('Username') ?></h6>
            <p><?= h($user->username) ?></p>
            <h6 class="subheader"><?= __('Password') ?></h6>
            <p><?= h($user->password) ?></p>
        </div>
        <div class="large-2 columns numbers end">
            <h6 class="subheader"><?= __('Id') ?></h6>
            <p><?= $this->Number->format($user->id) ?></p>
        </div>
        <div class="large-2 columns dates end">
            <h6 class="subheader"><?= __('Created') ?></h6>
            <p><?= h($user->created) ?></p>
            <h6 class="subheader"><?= __('Updated') ?></h6>
            <p><?= h($user->updated) ?></p>
        </div>
    </div>
</div>
<div class="related row">
    <div class="column large-12">
    <h4 class="subheader"><?= __('Related Ratings') ?></h4>
    <?php if (!empty($user->ratings)): ?>
    <table cellpadding="0" cellspacing="0">
        <tr>
            <th><?= __('Id') ?></th>
            <th><?= __('User Id') ?></th>
            <th><?= __('Image Id') ?></th>
            <th><?= __('Is Interesting') ?></th>
            <th><?= __('Image Category Id') ?></th>
            <th><?= __('Note') ?></th>
            <th><?= __('Created') ?></th>
            <th><?= __('Updated') ?></th>
            <th class="actions"><?= __('Actions') ?></th>
        </tr>
        <?php foreach ($user->ratings as $ratings): ?>
        <tr>
            <td><?= h($ratings->id) ?></td>
            <td><?= h($ratings->user_id) ?></td>
            <td><?= h($ratings->image_id) ?></td>
            <td><?= h($ratings->is_interesting) ?></td>
            <td><?= h($ratings->image_category_id) ?></td>
            <td><?= h($ratings->note) ?></td>
            <td><?= h($ratings->created) ?></td>
            <td><?= h($ratings->updated) ?></td>

            <td class="actions">
                <?= $this->Html->link(__('View'), ['controller' => 'Ratings', 'action' => 'view', $ratings->id]) ?>

                <?= $this->Html->link(__('Edit'), ['controller' => 'Ratings', 'action' => 'edit', $ratings->id]) ?>

                <?= $this->Form->postLink(__('Delete'), ['controller' => 'Ratings', 'action' => 'delete', $ratings->id], ['confirm' => __('Are you sure you want to delete # {0}?', $ratings->id)]) ?>

            </td>
        </tr>

        <?php endforeach; ?>
    </table>
    <?php endif; ?>
    </div>
</div>
<div class="related row">
    <div class="column large-12">
    <h4 class="subheader"><?= __('Related UserAuthTokens') ?></h4>
    <?php if (!empty($user->user_auth_tokens)): ?>
    <table cellpadding="0" cellspacing="0">
        <tr>
            <th><?= __('Id') ?></th>
            <th><?= __('User Id') ?></th>
            <th><?= __('Token') ?></th>
            <th><?= __('Created') ?></th>
            <th><?= __('Expires') ?></th>
            <th class="actions"><?= __('Actions') ?></th>
        </tr>
        <?php foreach ($user->user_auth_tokens as $userAuthTokens): ?>
        <tr>
            <td><?= h($userAuthTokens->id) ?></td>
            <td><?= h($userAuthTokens->user_id) ?></td>
            <td><?= h($userAuthTokens->token) ?></td>
            <td><?= h($userAuthTokens->created) ?></td>
            <td><?= h($userAuthTokens->expires) ?></td>

            <td class="actions">
                <?= $this->Html->link(__('View'), ['controller' => 'UserAuthTokens', 'action' => 'view', $userAuthTokens->id]) ?>

                <?= $this->Html->link(__('Edit'), ['controller' => 'UserAuthTokens', 'action' => 'edit', $userAuthTokens->id]) ?>

                <?= $this->Form->postLink(__('Delete'), ['controller' => 'UserAuthTokens', 'action' => 'delete', $userAuthTokens->id], ['confirm' => __('Are you sure you want to delete # {0}?', $userAuthTokens->id)]) ?>

            </td>
        </tr>

        <?php endforeach; ?>
    </table>
    <?php endif; ?>
    </div>
</div>
