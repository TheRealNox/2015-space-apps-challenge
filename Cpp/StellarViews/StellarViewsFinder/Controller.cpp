#include "Controller.h"

Controller::Controller()
{

}

Controller::~Controller()
{
	if (this->_managerThread.isRunning())
	{
		qDebug() << "Thread is still running";
		this->_taskManager->quit();
		this->_managerThread.quit();
		this->_waitCondition->wakeAll();
		this->_managerThread.wait();
		qDebug() << "Thread is dead now.";
	}

	if (this->_taskManager != NULL)
		delete this->_taskManager;
	if (this->_cmdsQueue != NULL)
		delete this->_cmdsQueue;
	if (this->_queueLocker != NULL)
		delete this->_queueLocker;
	if (this->_waitCondition != NULL)
		delete this->_waitCondition;
}

void						Controller::initializeAndStartTaskManager()
{
	this->_taskManager = new TaskManager();

	this->_cmdsQueue = new QList<BaseTask*>;
	this->_queueLocker = new QMutex;
	this->_waitCondition = new QWaitCondition;

	this->_taskManager->moveToThread(&this->_managerThread);

	this->_taskManager->setMutex(this->_queueLocker);
	this->_taskManager->setQueue(this->_cmdsQueue);
	this->_taskManager->setWaitCondition(this->_waitCondition);

	connect(&this->_managerThread, SIGNAL(started()), this->_taskManager, SLOT(run()));

	this->_managerThread.start();
}

void						Controller::sendTaskToModel(BaseTask * task)
{
		this->_queueLocker->lock();
		this->_cmdsQueue->push_back(task);
		this->_queueLocker->unlock();
		this->_waitCondition->wakeAll();
}

//
//Triggered from view
//

void						Controller::getFireTileRequest(GetFireTileRequest * toAdd)
{
	this->sendTaskToModel(new Task<GetFireTileRequest *>((RequestMask | FireTileRequest), toAdd));
}


//
//Triggered from Model
//
