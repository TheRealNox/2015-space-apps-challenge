#include "StellarViewsFinder.h"



StellarViewsFinder::StellarViewsFinder(QWidget *parent)
	: QMainWindow(parent)
{
	this->_ui.setupUi(this);

	this->_id = 0;
	this->_top.lat = 90.f;
	this->_top.lon = -180.f;
	this->_bottom.lat = 90.f - STEP;
	this->_bottom.lon = -180.f + STEP;
	this->_tile.x = 0;
	this->_tile.y = 0;
	this->_ui.zoomSlider->setMinimum(0);
	this->_ui.zoomSlider->setMaximum(500);
	this->_ui.zoomSlider->setValue(250);
	connect(this->_ui.getTileButton, SIGNAL(clicked()), this, SLOT(buttonClicked()));
	connect(this->_ui.zoomSlider, SIGNAL(valueChanged(int)), this, SLOT(sliderChanged(int)));

}

StellarViewsFinder::~StellarViewsFinder()
{

}

void				StellarViewsFinder::plugController(Controller * control)
{
	connect(this, SIGNAL(newGetFireTileRequestReady(GetFireTileRequest*)), control, SLOT(getFireTileRequest(GetFireTileRequest *)));
}

void					StellarViewsFinder::buttonClicked()
{
	QGraphicsScene *scene = new QGraphicsScene(0, 0, 320 * 512, 160 * 512, this);
	this->_ui.graphicsView->setScene(scene);
	this->_ui.graphicsView->setSceneRect(0, 0, 320 * 512, 160 * 512);

	bool				run = true;
	while (run)
	{
		QString request("http://map2a.vis.earthdata.nasa.gov/wms/wms.php?TIME=");
		QDate current = QDate::currentDate();
		current = current.addDays(-1);
		request.append(current.toString("yyyy-MM-dd"));
		request.append("&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=");
		request.append(QString::number(this->_top.lat));
		request.append("%2C");
		request.append(QString::number(this->_top.lon));
		request.append("%2C");
		request.append(QString::number(this->_bottom.lat));
		request.append("%2C");
		request.append(QString::number(this->_bottom.lon));
		QUrl url(request);
		QNetworkRequest * nRequest = new QNetworkRequest(url);
		GetFireTileRequest *fireRequest = new GetFireTileRequest(nRequest, this->_ui.apiKey->text());
		((Request*)fireRequest)->setData(this->_top, this->_bottom, this->_tile);
		emit this->newGetFireTileRequestReady(fireRequest);
//		fireRequest->setPos(this->_tile.y * 512, this->_tile.x * 512);
		fireRequest->setAutoDelete(true);
//		scene->addItem(fireRequest);
		QCoreApplication::processEvents();
		this->_id += 1;
		this->_tile.x += 1;
		this->_top.lat -= STEP;
		this->_bottom.lat -= STEP;
		if (this->_tile.x >= 160)
		{
			this->_tile.x = 0;
			this->_tile.y += 1;
			this->_top.lat = 90.f;
			this->_bottom.lat = 90.f - STEP;
			this->_top.lon += STEP;
			this->_bottom.lon = this->_top.lon + STEP;
			if (this->_tile.y >= 320)
				run = false;
		}
	}
	this->updateView();
}

void					StellarViewsFinder::sliderChanged(int value)
{
	qreal scale = qPow(qreal(2), (value - 500) / qreal(50));
	if (scale < 0.00333333f)
		scale = 0.003333333f;

	QMatrix matrix;
	matrix.scale(scale, scale);
	this->_ui.graphicsView->setMatrix(matrix);
}

void					StellarViewsFinder::updateView()
{
	QGraphicsScene *scene = new QGraphicsScene(0, 0, 320 * 512, 160 * 512, this);
	this->_ui.graphicsView->setScene(scene);
	this->_ui.graphicsView->setSceneRect(0, 0, 320 * 512, 160 * 512);

		// Populate scene
	int nitems = 0;
	for (int i = 0; i < 320; i += 1)
	{
		for (int j = 0; j < 160; j += 1)
		{
			QString request("https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=");
			QDate current = QDate::currentDate();
			current = current.addDays(-1);
			request.append(current.toString("yyyy-MM-dd"));
			request.append("&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=");
			request.append(QString::number(i));
			request.append("&TileRow=");
			request.append(QString::number(j));
			QUrl url(request);
			QNetworkRequest nRequest(url);
			QNetworkAccessManager nam;
			bool			useFull = false;

			QNetworkReply* reply = nam.get(nRequest);
			QEventLoop eventLoop;
			QObject::connect(reply, SIGNAL(finished()), &eventLoop, SLOT(quit()));
			eventLoop.exec();
			QVariant statusCodeV =
				reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);

			// Or the target URL if it was a redirect:
			QVariant redirectionTargetUrl =
				reply->attribute(QNetworkRequest::RedirectionTargetAttribute);

			// no error received?
			if (reply->error() == QNetworkReply::NoError)
			{
				// read data from QNetworkReply here
				QByteArray bytes = reply->readAll();  // bytes
				QPixmap * pixmap = new QPixmap;
				pixmap->loadFromData(bytes);
				QGraphicsPixmapItem  *item = new QGraphicsPixmapItem(*pixmap);
				item->setPos(i * 512, j * 512);
				scene->addItem(item);
				delete pixmap;
				delete reply;
			}
			// Some http error received
			else
			{
				qDebug() << "Request error: " << reply->errorString();
			}
			++nitems;
		}
	}
	delete scene;
}