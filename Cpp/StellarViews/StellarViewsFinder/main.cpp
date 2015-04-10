#include "StellarViewsFinder.h"
#include <QtWidgets/QApplication>

#include "Model.h"
#include "Controller.h"

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	StellarViewsFinder w;
	w.show();
	Model model;
	Controller controller;



	//Setting the MVC pattern
	w.plugController(&controller);
	controller.setModelAndConnectIt(&model);
	controller.initializeAndStartTaskManager();

	//Showing the main view
	w.show();

	//Qt Main Loop
	return a.exec();


}
