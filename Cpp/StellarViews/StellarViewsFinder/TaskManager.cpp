#include "TaskManager.h"

TaskManager::TaskManager(Model * model) :
_cmdsQueue(NULL),
_queueLocker(NULL),
_waitCondition(NULL),
_model(model)
{
	this->_continue = true;
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
				this->_model->runCommands(cmd);
			this->_queueLocker->lock();
		}
		this->_queueLocker->unlock();
	}
}

void					TaskManager::quit()
{
	this->_continue = false;
}

