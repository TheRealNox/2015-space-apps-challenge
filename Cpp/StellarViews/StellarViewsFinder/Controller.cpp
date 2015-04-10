#include "Controller.h"

Controller::Controller() : _model(NULL)
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

void						Controller::setModelAndConnectIt(Model* model)
{
	this->_model = model;

	//From Model To View
}

void						Controller::initializeAndStartTaskManager()
{
	this->_taskManager = new TaskManager(this->_model);

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
	if (this->_model != NULL)
	{
		this->_queueLocker->lock();
		this->_cmdsQueue->push_back(task);
		this->_queueLocker->unlock();
		this->_waitCondition->wakeAll();
	}
}

//
//Triggered from view
//
void						Controller::undoTriggered()
{
	this->sendTaskToModel(new BaseTask(InternalMask | Undo));
}

void						Controller::redoTriggered()
{
	this->sendTaskToModel(new BaseTask(InternalMask | Redo));
}

void						Controller::groupOfCommandsTriggered(bool beginning)
{
	this->sendTaskToModel(new BaseTask(InternalMask | (beginning ? BeginGroupOfCmds : EndGroupOfCmds)));
}

void						Controller::shotToBeAddedToShotList(QString toAdd, bool toCheck)
{
	this->sendTaskToModel(new Task<QString>((toCheck ? (GeneralUIMask | PushAssetToShotListWithCheck) : (GeneralUIMask | PushAssetToShotListWithoutCheck)), toAdd));
}


//
//Triggered from Model
//
