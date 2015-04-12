#include "TaskManager.h"

TaskManager::TaskManager() :
_cmdsQueue(NULL),
_queueLocker(NULL),
_waitCondition(NULL)
{
	this->_continue = true;
	this->_mainTaskDispatcher[RequestMask] = &TaskManager::runRequestCmds;
	this->_requestTaskDispatcher[FireTileRequest] = &TaskManager::getFireTileRequest;
}

TaskManager::~TaskManager()
{

}

void					TaskManager::setQueue(QList<BaseTask*>* list)
{
	this->_cmdsQueue = list;
}

void					TaskManager::setMutex(QMutex * mut)
{
	this->_queueLocker = mut;
}

void					TaskManager::setWaitCondition(QWaitCondition * con)
{
	this->_waitCondition = con;
}

void					TaskManager::run()
{
	qDebug() << "TaskManager is running";

	while (this->_continue)
	{
		this->_queueLocker->lock();
		if (this->_cmdsQueue->empty())
			this->_waitCondition->wait(this->_queueLocker);

		BaseTask * cmd = NULL;

		while (this->_cmdsQueue->empty() == false)
		{
			cmd = this->_cmdsQueue->front();
			this->_cmdsQueue->pop_front();
			this->_queueLocker->unlock();
			if (cmd != NULL)
				this->executeTask(cmd);
			this->_queueLocker->lock();
		}
		this->_queueLocker->unlock();
	}
}

void					TaskManager::quit()
{
	this->_continue = false;
}

void					TaskManager::executeTask(BaseTask *base)
{
	unsigned int	subtype = (base->getCommand() & SUBTYPE_MASK);


	if (this->_mainTaskDispatcher[subtype] != NULL)
		base = (this->*(_mainTaskDispatcher[subtype]))(base);

}

BaseTask *				TaskManager::runRequestCmds(BaseTask *base)
{
	unsigned int	cmdID = (base->getCommand() & CMD_MASK);

	if (this->_requestTaskDispatcher[cmdID] != NULL)
		base = (this->*(_requestTaskDispatcher[cmdID]))(base);

	return base;
}

BaseTask*				TaskManager::getFireTileRequest(BaseTask* base)
{
	Task<GetFireTileRequest*> * task = (Task<GetFireTileRequest*>*)base;
	GetFireTileRequest * request = task->getArg();
	QThreadPool::globalInstance()->start((QRunnable*)request);

	return base;
}