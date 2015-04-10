<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('Edit User Auth Token'), ['action' => 'edit', $userAuthToken->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete User Auth Token'), ['action' => 'delete', $userAuthToken->id], ['confirm' => __('Are you sure you want to delete # {0}?', $userAuthToken->id)]) ?> </li>
        <li><?= $this->Html->link(__('List User Auth Tokens'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User Auth Token'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="userAuthTokens view large-10 medium-9 columns">
    <h2><?= h($userAuthToken->id) ?></h2>
    <div class="row">
        <div class="large-5 columns strings">
            <h6 class="subheader"><?= __('User') ?></h6>
            <p><?= $userAuthToken->has('user') ? $this->Html->link($userAuthToken->user->id, ['controller' => 'Users', 'action' => 'view', $userAuthToken->user->id]) : '' ?></p>
            <h6 class="subheader"><?= __('Token') ?></h6>
            <p><?= h($userAuthToken->token) ?></p>
        </div>
        <div class="large-2 columns numbers end">
            <h6 class="subheader"><?= __('Id') ?></h6>
            <p><?= $this->Number->format($userAuthToken->id) ?></p>
        </div>
        <div class="large-2 columns dates end">
            <h6 class="subheader"><?= __('Created') ?></h6>
            <p><?= h($userAuthToken->created) ?></p>
            <h6 class="subheader"><?= __('Expires') ?></h6>
            <p><?= h($userAuthToken->expires) ?></p>
        </div>
    </div>
</div>
