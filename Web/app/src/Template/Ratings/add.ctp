<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('List Ratings'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Images'), ['controller' => 'Images', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image'), ['controller' => 'Images', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Image Categories'), ['controller' => 'ImageCategories', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image Category'), ['controller' => 'ImageCategories', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="ratings form large-10 medium-9 columns">
    <?= $this->Form->create($rating); ?>
    <fieldset>
        <legend><?= __('Add Rating') ?></legend>
        <?php
            echo $this->Form->input('user_id', ['options' => $users, 'empty' => true]);
            echo $this->Form->input('image_id', ['options' => $images, 'empty' => true]);
            echo $this->Form->input('is_interesting');
            echo $this->Form->input('image_category_id', ['options' => $imageCategories, 'empty' => true]);
            echo $this->Form->input('note');
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
