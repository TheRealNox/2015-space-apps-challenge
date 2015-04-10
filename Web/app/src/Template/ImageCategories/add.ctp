<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('List Image Categories'), ['action' => 'index']) ?></li>
        <li><?= $this->Html->link(__('List Ratings'), ['controller' => 'Ratings', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Rating'), ['controller' => 'Ratings', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="imageCategories form large-10 medium-9 columns">
    <?= $this->Form->create($imageCategory); ?>
    <fieldset>
        <legend><?= __('Add Image Category') ?></legend>
        <?php
            echo $this->Form->input('title');
            echo $this->Form->input('sort_order');
        ?>
    </fieldset>
    <?= $this->Form->button(__('Submit')) ?>
    <?= $this->Form->end() ?>
</div>
