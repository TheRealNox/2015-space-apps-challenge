#include "StellarViewsFinder.h"

StellarViewsFinder::StellarViewsFinder(QWidget *parent)
	: QMainWindow(parent)
{
	this->_ui.setupUi(this);
}

StellarViewsFinder::~StellarViewsFinder()
{

}

void				StellarViewsFinder::plugController(Controller * control)
{
	//this->_ui.actionUndo->setShortcutContext(Qt::ApplicationShortcut);
	//this->_ui.actionRedo->setShortcutContext(Qt::ApplicationShortcut);

	//this->addAction(this->_ui.actionUndo);
	//this->addAction(this->_ui.actionRedo);

	//connect(this->_ui.actionUndo, SIGNAL(triggered()), control, SLOT(undoTriggered()));
	//connect(this->_ui.actionRedo, SIGNAL(triggered()), control, SLOT(redoTriggered()));
}