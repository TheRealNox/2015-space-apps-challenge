<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('New Image Category'), ['action' => 'add']) ?></li>
        <li><?= $this->Html->link(__('List Ratings'), ['controller' => 'Ratings', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Rating'), ['controller' => 'Ratings', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="imageCategories index large-10 medium-9 columns">
    <table cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th><?= $this->Paginator->sort('id') ?></th>
            <th><?= $this->Paginator->sort('title') ?></th>
            <th><?= $this->Paginator->sort('sort_order') ?></th>
            <th class="actions"><?= __('Actions') ?></th>
        </tr>
    </thead>
    <tbody>
    <?php foreach ($imageCategories as $imageCategory): ?>
        <tr>
            <td><?= $this->Number->format($imageCategory->id) ?></td>
            <td><?= $this->Number->format($imageCategory->title) ?></td>
            <td><?= $this->Number->format($imageCategory->sort_order) ?></td>
            <td class="actions">
                <?= $this->Html->link(__('View'), ['action' => 'view', $imageCategory->id]) ?>
                <?= $this->Html->link(__('Edit'), ['action' => 'edit', $imageCategory->id]) ?>
                <?= $this->Form->postLink(__('Delete'), ['action' => 'delete', $imageCategory->id], ['confirm' => __('Are you sure you want to delete # {0}?', $imageCategory->id)]) ?>
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
