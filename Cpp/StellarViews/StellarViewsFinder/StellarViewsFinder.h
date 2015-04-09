#ifndef STELLARVIEWSFINDER_H
#define STELLARVIEWSFINDER_H

#include <QtWidgets/QMainWindow>
#include "ui_StellarViewsFinder.h"

class StellarViewsFinder : public QMainWindow
{
	Q_OBJECT

public:
	StellarViewsFinder(QWidget *parent = 0);
	~StellarViewsFinder();

private:
	Ui::StellarViewsFinderClass ui;
};

#endif // STELLARVIEWSFINDER_H
