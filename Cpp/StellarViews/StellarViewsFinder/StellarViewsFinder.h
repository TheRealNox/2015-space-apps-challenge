#ifndef STELLARVIEWSFINDER_H
#define STELLARVIEWSFINDER_H

#include <QtWidgets/QMainWindow>
#include "ui_StellarViewsFinder.h"

#include <QNetworkAccessManager>
#include <QUrl>
#include <QImage>
#include <QMessageBox>
#include <QNetworkRequest>
#include <QAuthenticator>
#include <QNetworkReply>
#include <QtCore/QCoreApplication>
#include <QDebug>
#include <QImageReader>
#include <QImage>
#include <QPixmap>
#include <QPainter>
#include <QColor>
#include <QNetworkAccessManager>
#include <QUrl>
#include <QBuffer>
#include <QNetworkRequest>
#include <QNetworkReply>

#include "Controller.h"

#define TILE_WIDTH 512.f
#define MIN_RES 0.002197265625
#define STEP ((TILE_WIDTH * MIN_RES) * 5.f)

class							StellarViewsFinder : public QMainWindow
{
	Q_OBJECT

	int						_id;
	float					_x1;
	float					_y1;
	float					_x2;
	float					_y2;

private:
	Ui::StellarViewsFinderClass _ui;
	QNetworkAccessManager *		_nam;

public:
	StellarViewsFinder(QWidget *parent = 0);
	~StellarViewsFinder();

public:
	void						plugController(Controller *);

public slots:
	void						finishedSlot(QNetworkReply *reply);
	void						buttonClicked();
};

#endif // STELLARVIEWSFINDER_H
