<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('New Rating'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Users'), ['controller' => 'Users', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New User'), ['controller' => 'Users', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Images'), ['controller' => 'Images', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image'), ['controller' => 'Images', 'action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Image Categories'), ['controller' => 'ImageCategories', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image Category'), ['controller' => 'ImageCategories', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="ratings index large-10 medium-9 columns">
    <table cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th><?= $this->Paginator->sort('id') ?></th>
            <th><?= $this->Paginator->sort('user_id') ?></th>
            <th><?= $this->Paginator->sort('image_id') ?></th>
            <th><?= $this->Paginator->sort('is_interesting') ?></th>
            <th><?= $this->Paginator->sort('image_category_id') ?></th>
            <th><?= $this->Paginator->sort('note') ?></th>
            <th><?= $this->Paginator->sort('created') ?></th>
            <th class="actions"><?= __('Actions') ?></th>
        </tr>
    </thead>
    <tbody>
    <?php foreach ($ratings as $rating): ?>
        <tr>
            <td><?= $this->Number->format($rating->id) ?></td>
            <td>
                <?= $rating->has('user') ? $this->Html->link($rating->user->id, ['controller' => 'Users', 'action' => 'view', $rating->user->id]) : '' ?>
            </td>
            <td>
                <?= $rating->has('image') ? $this->Html->link($rating->image->id, ['controller' => 'Images', 'action' => 'view', $rating->image->id]) : '' ?>
            </td>
            <td><?= h($rating->is_interesting) ?></td>
            <td>
                <?= $rating->has('image_category') ? $this->Html->link($rating->image_category->title, ['controller' => 'ImageCategories', 'action' => 'view', $rating->image_category->id]) : '' ?>
            </td>
            <td><?= h($rating->note) ?></td>
            <td><?= h($rating->created) ?></td>
            <td class="actions">
                <?= $this->Html->link(__('View'), ['action' => 'view', $rating->id]) ?>
                <?= $this->Html->link(__('Edit'), ['action' => 'edit', $rating->id]) ?>
                <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $rating->id], ['confirm' => __('Are you sure you want to delete # {0}?', $rating->id)]) ?>
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
