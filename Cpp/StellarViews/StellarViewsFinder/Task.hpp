#ifndef __TASK_H__
#define __TASK_H__

#include <QDebug>

#include "TaskEnum.h"

//
//Base Task class, ready to be templated
//
class						BaseTask
{
protected:
	unsigned int			_cmd;

private:
	BaseTask();

public:
	BaseTask(unsigned int cmd)
	{
		this->_cmd = cmd;
	}

public:
	const unsigned int&		getCommand() const
	{
		return this->_cmd;
	}

	void					setCommand(unsigned int cmd)
	{
		this->_cmd = cmd;
	}
};

//
//Templated Class inheriting of BaseTask
//
template<typename T>
class				Task : public BaseTask
{
private:
	T				_arg;
private:
	Task();

public:
	Task(unsigned int cmd, T arg = NULL) : BaseTask(cmd)
	{
		this->_arg = arg;
	}

	~Task() {}

public:
	T				getArg() const
	{
		return (this->_arg);
	}

	void			setArg(T arg)
	{
		this->_arg = arg;
	}
};

#endif//__TASK_H