#ifndef __TASKMANAGER_H__
#define __TASKMANAGER_H__

//Qt includes
#include <QDebug>
#include <QList>
#include <QObject>
#include <QThread>
#include <QThreadPool>
#include <QWaitCondition>

//Local includes
#include "GetFireTileRequest.h"
#include "Task.hpp"

class							TaskManager : public QObject
{
	Q_OBJECT

		//Here we typedef a function protopy to FUNCPTR in order to help readability
	typedef BaseTask *			(TaskManager::*FUNCPTR)(BaseTask * task);

	//Another typedef: the map containing the enum for key and the functor for value
	typedef std::map<unsigned int, FUNCPTR> DispatcherMap;

		// -- Attributs
private:
	QList<BaseTask*>*			_cmdsQueue;
	QMutex *					_queueLocker;
	QWaitCondition *			_waitCondition;
	DispatcherMap				_mainTaskDispatcher;
	DispatcherMap				_requestTaskDispatcher;
	bool						_continue;
	// --!Attributs

	// -- CTors & DTor
public:
	TaskManager();
	~TaskManager();

private:
	// --!CTors & DTor

	// -- Methods
public:
	void						setQueue(QList<BaseTask*>*);
	void						setMutex(QMutex *);
	void						setWaitCondition(QWaitCondition *);
	void						quit();

private:
	void						executeTask(BaseTask *);
	BaseTask*					runRequestCmds(BaseTask*);
	BaseTask*					getFireTileRequest(BaseTask*);


	// --!Methods

public slots :
		void						run();
};

#endif//__TASKMANAGER_H__

