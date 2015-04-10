#include "StellarViewsFinder.h"
#include <QtWidgets/QApplication>
#include <QImage>
#include "Controller.h"

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	StellarViewsFinder w;
	Controller controller;

	//Setting the MVC pattern
	w.plugController(&controller);
	controller.initializeAndStartTaskManager();

	//Showing the main view
	w.show();

	//Qt Main Loop
	return a.exec();


}
