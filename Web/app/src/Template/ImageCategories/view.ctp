<div class="actions columns large-2 medium-3">
    <h3><?= __('Actions') ?></h3>
    <ul class="side-nav">
        <li><?= $this->Html->link(__('Edit Image Category'), ['action' => 'edit', $imageCategory->id]) ?> </li>
        <li><?= $this->Form->postLink(__('Delete Image Category'), ['action' => 'delete', $imageCategory->id], ['confirm' => __('Are you sure you want to delete # {0}?', $imageCategory->id)]) ?> </li>
        <li><?= $this->Html->link(__('List Image Categories'), ['action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Image Category'), ['action' => 'add']) ?> </li>
        <li><?= $this->Html->link(__('List Ratings'), ['controller' => 'Ratings', 'action' => 'index']) ?> </li>
        <li><?= $this->Html->link(__('New Rating'), ['controller' => 'Ratings', 'action' => 'add']) ?> </li>
    </ul>
</div>
<div class="imageCategories view large-10 medium-9 columns">
    <h2><?= h($imageCategory->title) ?></h2>
    <div class="row">
        <div class="large-2 columns numbers end">
            <h6 class="subheader"><?= __('Id') ?></h6>
            <p><?= $this->Number->format($imageCategory->id) ?></p>
            <h6 class="subheader"><?= __('Title') ?></h6>
            <p><?= $this->Number->format($imageCategory->title) ?></p>
            <h6 class="subheader"><?= __('Sort Order') ?></h6>
            <p><?= $this->Number->format($imageCategory->sort_order) ?></p>
        </div>
    </div>
</div>
<div class="related row">
    <div class="column large-12">
    <h4 class="subheader"><?= __('Related Ratings') ?></h4>
    <?php if (!empty($imageCategory->ratings)): ?>
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
        <?php foreach ($imageCategory->ratings as $ratings): ?>
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
