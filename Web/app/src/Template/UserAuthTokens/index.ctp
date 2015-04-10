<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('New User Auth Token'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="userAuthTokens index large-10 medium-9 columns">
    <table cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th><?= $this->Paginator->sort('id') ?></th>
            <th><?= $this->Paginator->sort('user_id') ?></th>
            <th><?= $this->Paginator->sort('token') ?></th>
            <th><?= $this->Paginator->sort('created') ?></th>
            <th><?= $this->Paginator->sort('expires') ?></th>
            <th class="actions"><?= __('Actions') ?></th>
        </tr>
    </thead>
    <tbody>
    <?php foreach ($userAuthTokens as $userAuthToken): ?>
        <tr>
            <td><?= $this->Number->format($userAuthToken->id) ?></td>
            <td>
                <?= $userAuthToken->has('user') ? $this->Html->link($userAuthToken->user->id, ['controller' => 'Users', 'action' => 'view', $userAuthToken->user->id]) : '' ?>
            </td>
            <td><?= h($userAuthToken->token) ?></td>
            <td><?= h($userAuthToken->created) ?></td>
            <td><?= h($userAuthToken->expires) ?></td>
            <td class="actions">
                <?= $this->Html->link(__('View'), ['action' => 'view', $userAuthToken->id]) ?>
                <?= $this->Html->link(__('Edit'), ['action' => 'edit', $userAuthToken->id]) ?>
                <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $userAuthToken->id], ['confirm' => __('Are you sure you want to delete # {0}?', $userAuthToken->id)]) ?>
            </td>
        </tr>

    <?php endforeach; ?>
    </tbody>
    </table>
    <div class="paginator">
        <ul class="pagination">
            <?= $this->Paginator->prev('< ' . __('previous')) ?>
            <?= $this->Paginator->numbers() ?>
            <?= $this->Paginator->next(__('next') . ' >') ?>
        </ul>
        <p><?= $this->Paginator->counter() ?></p>
    </div>
</div>
