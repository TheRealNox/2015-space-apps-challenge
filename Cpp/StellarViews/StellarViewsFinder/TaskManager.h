#ifndef __TASKMANAGER_H__
#define __TASKMANAGER_H__

//Qt includes
#include <QDebug>
#include <QList>
#include <QObject>
#include <QThread>
#include <QWaitCondition>

//Local includes
#include "Model.h"
#include "Task.hpp"

class							TaskManager : public QObject
{
	Q_OBJECT

		// -- Attributs
private:
	QList<BaseTask*>*			_cmdsQueue;
	QMutex *					_queueLocker;
	QWaitCondition *			_waitCondition;
	Model *						_model;
	bool						_continue;
	// --!Attributs

	// -- CTors & DTor
public:
	TaskManager(Model*);
	~TaskManager();

private:
	TaskManager();
	// --!CTors & DTor

	// -- Methods
public:
	void						setQueue(QList<BaseTask*>*);
	void						setMutex(QMutex *);
	void						setWaitCondition(QWaitCondition *);
	void						quit();

private:
	// --!Methods

	public slots :
		void						run();
};

#endif//__TASKMANAGER_H__
