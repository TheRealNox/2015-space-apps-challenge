#ifndef __CONTROLLER_H__
#define __CONTROLLER_H__

//Qt includes
#include <QList>
#include <QThread>
#include <QWaitCondition>

//Local includes
#include "TaskManager.h"

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
		void								undoTriggered();
		void								redoTriggered();
		void								groupOfCommandsTriggered(bool beginning);

//------GENERAL UI CMDS
		void								shotToBeAddedToShotList(QString, bool toCheck);

//--------------------
//Triggered from Model:
//--------------------

		// --!Slots

		// -- Signals
	signals:
		// --!Signals


	};

#endif//__CONTROLLER_H__

