#ifndef __CONTROLLER_H__
#define __CONTROLLER_H__

//Qt includes
#include <QList>
#include <QThread>
#include <QWaitCondition>

//Local includes
#include "TaskManager.h"
#include "GetFireTileRequest.h"

class									Controller : public QObject
{
	Q_OBJECT

		// -- Attributs
private:
	QThread								_managerThread;
	TaskManager *						_taskManager;

	QList<BaseTask*>*					_cmdsQueue;

	QMutex *							_queueLocker;
	QWaitCondition *					_waitCondition;
	// --!Attributs

	// -- CTors & DTor
public:
	Controller();
	~Controller();

private:
	// --!CTors & DTor

	// -- Methods
public:
	void								initializeAndStartTaskManager();

private:
	void								sendTaskToModel(BaseTask*);
	// --!Methods

	// -- Slots
public slots:
	//--------------------
	//Triggered from View:
	//--------------------
	//------INTERNAL CMDS

	//------GENERAL CMDS
	void								getFireTileRequest(GetFireTileRequest*);

	//--------------------
	//Triggered from Model:
	//--------------------

	// --!Slots

	// -- Signals
signals:
	// --!Signals


};

#endif//__CONTROLLER_H__

