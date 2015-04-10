#include "StellarViewsFinder.h"



StellarViewsFinder::StellarViewsFinder(QWidget *parent)
	: QMainWindow(parent), _nam(NULL)
{
	this->_ui.setupUi(this);

	this->_id = 0;
	this->_x1 = -90.f;
	this->_y1 = -180.f;
	this->_x2 = -90.f + STEP;
	this->_y2 = -180.f + STEP;

	connect(this->_ui.getTileButton, SIGNAL(clicked()), this, SLOT(buttonClicked()));

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

void				StellarViewsFinder::finishedSlot(QNetworkReply *reply)
{
	// Reading attributes of the reply
	//qDebug() << "Repply" << reply->attribute(QNetworkRequest::HttpStatusCodeAttribute) << reply->url();
	// e.g. the HTTP status code
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
		QImage image;
		QPixmap pixmap;
		pixmap.loadFromData(bytes);
		image = pixmap.toImage();


		QString path("D:\\work\\temp\\");
		QString name("");

		name.append(QString::number(this->_x1));
		name.append("_");
		name.append(QString::number(this->_y1));
		name.append("_");
		name.append(QString::number(this->_x2));
		name.append("_");
		name.append(QString::number(this->_y2));
		name.append(".png");

		path.append(name);

		//qDebug() << " x1=" << _x1 << " x2=" << _x2 << "Image invalid" << image.isNull();

		for (int i = 0; i < image.byteCount(); i++)
		{
			if (image.constBits()[i] != '\0')
			{
				qDebug() << "Red with name:" << name;
				image.save(path, "PNG");
				i = image.byteCount();
			}
		}
		qDebug() << "Tile number:" << this->_id;
		this->_id += 1;
		this->_x1 += STEP;
		this->_x2 += STEP;
		if (this->_x2 >= 90.f)
		{
			this->_x1 = -90.f;
			this->_x2 = -90.f + STEP;
			this->_y1 += STEP;
			this->_y2 = this->_y1 + STEP;
			if (this->_y2 >= 180.f)
				QCoreApplication::exit();
		}
	}
	// Some http error received
	else
	{
		// handle errors here
	}

	// We receive ownership of the reply object
	// and therefore need to handle deletion.
	delete reply;
}

void					StellarViewsFinder::buttonClicked()
{
	while (1)
	{
		if (this->_nam == NULL)
		{
			this->_nam = new QNetworkAccessManager();
			QObject::connect(this->_nam, SIGNAL(finished(QNetworkReply*)), this, SLOT(finishedSlot(QNetworkReply*)));
		}
		//MODIS_Terra_CorrectedReflectance_TrueColor
		//https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-04-08&Layer=MODIS_Fires_All&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.1.0&Format=image%2Fjpeg&TileMatrix=7&TileCol=0&TileRow=16
		//https://map1c.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=319&TileRow=16
		//https://map1a.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=-319&TileRow=-169
		//https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=159&TileRow=79
		//MODIS_Fires_All
		//https://map2b.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-02-11&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=149.625%2C-29.25%2C150.75%2C-28.125
		//https://map2c.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-02-11&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=173.25%2C-82.125%2C174.375%2C-81
		//https://map2b.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-01-19&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=189%2C90%2C198%2C99
		QString request("http://map2a.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-04-09&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=");
		request.append(QString::number(_x1));
		request.append("%2C");
		request.append(QString::number(_y1));
		request.append("%2C");
		request.append(QString::number(_x2));
		request.append("%2C");
		request.append(QString::number(_y2));
		QUrl url(request);
		QNetworkReply* reply = this->_nam->get(QNetworkRequest(url));
		if (this->_id > 2500)
			qDebug() << "Reaching the top bro";
		QCoreApplication::processEvents();
		_sleep(100);
		QCoreApplication::processEvents();
		//_sleep(100);
		//QCoreApplication::processEvents();
	}
}