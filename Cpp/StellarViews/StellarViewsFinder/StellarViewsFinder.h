#ifndef STELLARVIEWSFINDER_H
#define STELLARVIEWSFINDER_H

#include <QtWidgets/QMainWindow>
#include "ui_StellarViewsFinder.h"

#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QNetworkRequest>
#include <QUrl>
#include <QImage>
#include <QPixmap>
#include <QMessageBox>
#include <QAuthenticator>
#include <QtCore/QCoreApplication>
#include <QDebug>
#include <QImageReader>
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
#define STEP ((TILE_WIDTH * MIN_RES) * 1.f)

class							StellarViewsFinder : public QMainWindow
{
	Q_OBJECT

	int						_id;
	LatLonCoord				_top;
	LatLonCoord				_bottom;
	Tile					_tile;

private:
	Ui::StellarViewsFinderClass _ui;

public:
	StellarViewsFinder(QWidget *parent = 0);
	~StellarViewsFinder();

public:
	void						plugController(Controller *);

public slots:
	void						buttonClicked();

public:
signals :
	void						newGetFireTileRequestReady(GetFireTileRequest*);
};

#endif // STELLARVIEWSFINDER_H
