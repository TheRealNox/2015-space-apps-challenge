#include "GetFireTileRequest.h"

GetFireTileRequest::GetFireTileRequest(QNetworkRequest * request) : Request(request)
{

}

GetFireTileRequest::~GetFireTileRequest()
{
}

GetFireTileRequest::GetFireTileRequest(const GetFireTileRequest &request)
{
	Request::setRequest(request.getRequest());
	this->setData(request.getTop(), request.getBottom(), request.getTile());
}

void				GetFireTileRequest::run()
{
	if (this->_nam == NULL)
		this->_nam = new QNetworkAccessManager;

	bool			useFull = false;

	QNetworkReply* reply = this->_nam->get(*this->_request);
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
		QImage image;
		QPixmap pixmap;
		pixmap.loadFromData(bytes);
		image = pixmap.toImage();


		QString path("D:\\work\\temp\\");
		QString name("");

		name.append(QString::number(this->_current.x));
		name.append("_");
		name.append(QString::number(this->_current.y));
		name.append(".png");

		path.append(name);

		qDebug() << " x1=" << this->_current.x << " x2=" << this->_current.y;

		for (int i = 0; i < image.byteCount(); i++)
		{
			if (image.constBits()[i] != '\0')
			{
				qDebug() << "Red with name:" << name;
				image.save(path, "PNG");
				this->pushToServer();
				i = image.byteCount();
			}
		}
	}
	// Some http error received
	else
	{
		qDebug() << "Request error: " << reply->errorString();
	}

	delete reply;
}

LatLonCoord			GetFireTileRequest::getTop() const
{
	return this->_topLeftCorner;
}

LatLonCoord			GetFireTileRequest::getBottom() const
{
	return this->_bottomRightCorner;
}

Tile				GetFireTileRequest::getTile() const
{
	return this->_current;
}

void				GetFireTileRequest::setData(LatLonCoord top, LatLonCoord bottom, Tile current)
{
	this->_topLeftCorner = top;
	this->_bottomRightCorner = bottom;
	this->_current = current;
}

void				GetFireTileRequest::pushToServer()
{
	QString key("7cbf17e4-fb32-4666-8a3e-a9ee8687f825:");

	QByteArray data = key.toLocal8Bit().toBase64();
	QString headerData = "Basic " + data;
	QNetworkRequest newRequest(QUrl("https://api.orchestrate.io/v0/discovery"));
	newRequest.setRawHeader("Authorization", headerData.toLocal8Bit());
	newRequest.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
	QJsonObject json;
	QJsonObject subjson1;
	QJsonObject subjson2;
	QString hash = QString::number(this->_topLeftCorner.lat);
	hash.append(QString::number(this->_topLeftCorner.lon));
	hash.append(QString::number(this->_bottomRightCorner.lat));
	hash.append(QString::number(this->_bottomRightCorner.lon));

	subjson1.insert("TopLatitude", this->_topLeftCorner.lat);
	subjson1.insert("TopLongitude", this->_topLeftCorner.lon);
	subjson1.insert("BottomLatitude", this->_bottomRightCorner.lat);
	subjson1.insert("BottomLongitude", this->_bottomRightCorner.lon);

	subjson2.insert("x", this->_current.x);
	subjson2.insert("y", this->_current.y);

	json.insert("CoordinateUUID", QString(QCryptographicHash::hash(hash.toLocal8Bit(), QCryptographicHash::Algorithm::Md5).toStdString().c_str()));
	json.insert("Time", QDate::currentDate().toString("yyyy-MM-dd"));
	json.insert("TileCoordinates", subjson1);
	json.insert("TilePosition", subjson2);
	json.insert("Category", "Fire All");

	QJsonDocument doc;
	doc.setObject(json);

	QNetworkReply * reply = this->_nam->post(newRequest, doc.toJson());
	QEventLoop eventLoop;
	QObject::connect(reply, SIGNAL(finished()), &eventLoop, SLOT(quit()));
	eventLoop.exec();

	if (reply->error() == QNetworkReply::NoError)
	{
		qDebug() << "Answer from server:" << reply->rawHeader("X-ORCHESTRATE-REQ-ID");
	}

}