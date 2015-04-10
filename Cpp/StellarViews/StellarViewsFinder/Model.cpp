#include "Model.h"

Model::Model()
{
	this->_mainTaskDispatcher[InternalMask] = &Model::runInternalCmds;
	this->_mainTaskDispatcher[GeneralUIMask] = &Model::runGeneralUICmds;
	//this->_dispatchTaskMap[RenderMask] = &Model::runRenderCmds;
	//this->_dispatchTaskMap[OtherMask] = &Model::runOtherCmds;

	this->_internalTaskDispatcher[Undo] = &Model::handleUndo;
	this->_internalTaskDispatcher[Redo] = &Model::handleRedo;
	this->_internalTaskDispatcher[BeginGroupOfCmds] = &Model::handleGroupOfCmd;
	this->_internalTaskDispatcher[EndGroupOfCmds] = &Model::handleGroupOfCmd;
}

Model::~Model()
{
	while (!this->_undoQueue.empty())
	{
		delete this->_undoQueue.front();
		this->_undoQueue.pop_front();
	}

	while (!this->_redoQueue.empty())
	{
		delete this->_redoQueue.front();
		this->_redoQueue.pop_front();
	}
}

BaseTask*				Model::handleUndo(BaseTask* task)
{
	delete task;
	task = NULL;
	if (this->_undoQueue.empty())
		return NULL;


	BaseTask* cmd = this->_undoQueue.front();
	this->_undoQueue.pop_front();
	unsigned int cmdID = cmd->getCommand();

	if (cmdID == (InternalMask | EndGroupOfCmds))
	{
		this->runCommands(cmd, 1);

		while (this->_undoQueue.isEmpty() == false && this->_undoQueue.front()->getCommand() != (InternalMask | BeginGroupOfCmds))
		{
			cmd = this->_undoQueue.front();
			this->_undoQueue.pop_front();
			this->runCommands(cmd, 1);
		}

		if (this->_undoQueue.isEmpty() == false)
		{
			cmd = this->_undoQueue.front();
			this->_undoQueue.pop_front();
			this->runCommands(cmd, 1);
		}
	}
	else
	{
		this->runCommands(cmd, 1);
	}

	return task;
}

BaseTask*				Model::handleRedo(BaseTask*task)
{
	delete task;
	task = NULL;
	if (this->_redoQueue.empty())
		return NULL;

	BaseTask* cmd = this->_redoQueue.front();
	this->_redoQueue.pop_front();
	unsigned int cmdID = cmd->getCommand();

	if (cmdID == (InternalMask | BeginGroupOfCmds))
	{
		this->runCommands(cmd, 2);
		while (this->_redoQueue.front()->getCommand() != (InternalMask | EndGroupOfCmds))
		{
			cmd = this->_redoQueue.front();
			this->_redoQueue.pop_front();
			this->runCommands(cmd, 2);
		}

		cmd = this->_redoQueue.front();
		this->_redoQueue.pop_front();
		this->runCommands(cmd, 2);
	}
	else
	{
		this->runCommands(cmd, 2);
	}

	return task;
}

BaseTask *				Model::handleGroupOfCmd(BaseTask* task)
{
	return task;
}

void					Model::addToUndo(BaseTask* task)
{
	if (task == NULL)
		return;
	this->_undoQueue.push_front(task);
}

void					Model::addToRedo(BaseTask* task)
{
	if (task == NULL)
		return;
	this->_redoQueue.push_front(task);
}

void					Model::cleanRedo()
{
	while (this->_redoQueue.empty() == false)
	{
		delete (Task<void*>*)(this->_redoQueue.front());
		this->_redoQueue.pop_front();
	}
}

BaseTask *				Model::runInternalCmds(BaseTask * task, char unused)
{
	unsigned int	cmdID = (task->getCommand() & CMD_MASK);

	if (this->_internalTaskDispatcher[cmdID] != NULL)
		task = (this->*(_internalTaskDispatcher[cmdID]))(task);

	return task;
}

BaseTask *				Model::runGeneralUICmds(BaseTask * task, char unused)
{

	Q_UNUSED(unused);
	unsigned int	cmdID = (task->getCommand() & CMD_MASK);

	if (this->_generalUITaskDispatcher[cmdID] != NULL)
		task = (this->*(_generalUITaskDispatcher[cmdID]))(task);

	return task;
}

void					Model::runCommands(BaseTask* task, char fromUndoOrRedo)
{
	unsigned int	subtype = (task->getCommand() & SUBTYPE_MASK)/* >> 8*/;


	if (this->_mainTaskDispatcher[subtype] != NULL)
		task = (this->*(_mainTaskDispatcher[subtype]))(task, fromUndoOrRedo);

	if (fromUndoOrRedo != 1)
		this->addToUndo(task);
	else if (fromUndoOrRedo == 1)
		this->addToRedo(task);
	if (task != NULL && fromUndoOrRedo == 0)
		cleanRedo();
}