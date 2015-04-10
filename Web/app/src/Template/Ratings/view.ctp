<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('Edit Rating'), ['action' => 'edit', $rating->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Rating'), ['action' => 'delete', $rating->id], ['confirm' => __('Are you sure you want to delete # {0}?', $rating->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Ratings'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Rating'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Images'), ['controller' => 'Images', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image'), ['controller' => 'Images', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Image Categories'), ['controller' => 'ImageCategories', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image Category'), ['controller' => 'ImageCategories', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="ratings view large-10 medium-9 columns">
    <h2><?= h($rating->id) ?></h2>
    <div class="row">
        <div class="large-5 columns strings">
            <h6 class="subheader"><?= __('User') ?></h6>
            <p><?= $rating->has('user') ? $this->Html->link($rating->user->id, ['controller' => 'Users', 'action' => 'view', $rating->user->id]) : '' ?></p>
            <h6 class="subheader"><?= __('Image') ?></h6>
            <p><?= $rating->has('image') ? $this->Html->link($rating->image->id, ['controller' => 'Images', 'action' => 'view', $rating->image->id]) : '' ?></p>
            <h6 class="subheader"><?= __('Image Category') ?></h6>
            <p><?= $rating->has('image_category') ? $this->Html->link($rating->image_category->title, ['controller' => 'ImageCategories', 'action' => 'view', $rating->image_category->id]) : '' ?></p>
            <h6 class="subheader"><?= __('Note') ?></h6>
            <p><?= h($rating->note) ?></p>
        </div>
        <div class="large-2 columns numbers end">
            <h6 class="subheader"><?= __('Id') ?></h6>
            <p><?= $this->Number->format($rating->id) ?></p>
        </div>
        <div class="large-2 columns dates end">
            <h6 class="subheader"><?= __('Created') ?></h6>
            <p><?= h($rating->created) ?></p>
            <h6 class="subheader"><?= __('Updated') ?></h6>
            <p><?= h($rating->updated) ?></p>
        </div>
        <div class="large-2 columns booleans end">
            <h6 class="subheader"><?= __('Is Interesting') ?></h6>
            <p><?= $rating->is_interesting ? __('Yes') : __('No'); ?></p>
        </div>
    </div>
</div>
