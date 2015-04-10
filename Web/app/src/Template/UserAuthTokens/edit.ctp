<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Form->postLink(
                __('Delete'),
                ['action' => 'delete', $userAuthToken->id],
                ['confirm' => __('Are you sure you want to delete # {0}?', $userAuthToken->id)]
            )
        ?></li>
        <li><?= $this->Html->link(__('List User Auth Tokens'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="userAuthTokens form large-10 medium-9 columns">
    <?= $this->Form->create($userAuthToken); ?>
    <fieldset>
        <legend><?= __('Edit User Auth Token') ?></legend>
        <?php
            echo $this->Form->input('user_id', ['options' => $users, 'empty' => true]);
            echo $this->Form->input('token');
            echo $this->Form->input('expires');
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
