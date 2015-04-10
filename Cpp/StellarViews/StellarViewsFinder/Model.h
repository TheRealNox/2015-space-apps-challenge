#ifndef __MODEL_H__
#define __MODEL_H__

//STD includes
#include <utility>

//Qt includes
#include <QDebug>
#include <QHash>
#include <QList>
#include <QObject>
#include <QThread>

//local includes
#include "Task.hpp"

class									Model : public QObject
{
	Q_OBJECT

		//Here we typedef a function protopy to FUNCPTR in order to help readability
		typedef BaseTask *					(Model::*FUNCPTR)(BaseTask * task);
	typedef BaseTask *					(Model::*FUNCPTRWITHCHAR)(BaseTask * task, char fromUndoOrRedo);

	//Another typedef: the map containing the enum for key and the functor for value
	typedef std::map<unsigned int, FUNCPTR> DispatcherMap;
	typedef std::map<unsigned int, FUNCPTRWITHCHAR> DispatcherMapWithChar;

	// -- Attributs
private:
	DispatcherMapWithChar				_mainTaskDispatcher;
	DispatcherMap						_internalTaskDispatcher;
	DispatcherMap						_generalUITaskDispatcher;
	DispatcherMapWithChar				_clipSettingsTaskDispatcher;

	QList<BaseTask*>					_undoQueue;
	QList<BaseTask*>					_redoQueue;
	// --!Attributs

	// -- CTors & DTor
public:
	Model();
	~Model();

private:
	// --!CTors & DTor

	// -- Methods
public:

private:

	void							addToUndo(BaseTask*);
	void							addToRedo(BaseTask*);
	void							cleanRedo();

	BaseTask*						runInternalCmds(BaseTask*, char);
	BaseTask*						handleUndo(BaseTask*);
	BaseTask*						handleRedo(BaseTask*);
	BaseTask*						handleGroupOfCmd(BaseTask*);

	BaseTask*						runGeneralUICmds(BaseTask*, char);
	// --!Methods

	// -- Slots & Signals
	public slots:
	void							runCommands(BaseTask* cmd, char = 0);

signals:
	// --!Slots & Signals
};

#endif//__MODEL_H__

