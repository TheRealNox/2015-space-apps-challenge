#ifndef STELLARVIEWSFINDER_H
#define STELLARVIEWSFINDER_H

#include <QtWidgets/QMainWindow>
#include "ui_StellarViewsFinder.h"

#include "Controller.h"

class							StellarViewsFinder : public QMainWindow
{
	Q_OBJECT

private:
	Ui::StellarViewsFinderClass _ui;


public:
	StellarViewsFinder(QWidget *parent = 0);
	~StellarViewsFinder();

public:
	void						plugController(Controller *);
};

#endif // STELLARVIEWSFINDER_H
