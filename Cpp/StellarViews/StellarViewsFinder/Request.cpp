#include "Request.h"

Request::Request(QNetworkRequest * request, QString category, QString key) : _nam(NULL), _request(request)
{
	this->_category = category;
	this->_apiKey = key;
	this->_readyToDraw = false;
	this->_downloadProgress = 0;
}

Request::~Request()
{
	if (this->_nam != NULL)
		delete this->_nam;
	if (this->_request != NULL)
		delete this->_request;
}

Request::Request(const Request &request) : _nam(NULL), _request(request.getRequest())
{

}

void						Request::run()
{

}

void						Request::setRequest(QNetworkRequest *request)
{
	this->_request = request;
}

QNetworkRequest *			Request::getRequest() const
{
	return this->_request;
}


LatLonCoord			Request::getTop() const
{
	return this->_topLeftCorner;
}

LatLonCoord			Request::getBottom() const
{
	return this->_bottomRightCorner;
}

Tile				Request::getTile() const
{
	return this->_current;
}

void				Request::setData(LatLonCoord top, LatLonCoord bottom, Tile current)
{
	this->_topLeftCorner = top;
	this->_bottomRightCorner = bottom;
	this->_current = current;
}

void				Request::pushToServer()
{
	QByteArray data = this->_apiKey.toLocal8Bit().toBase64();
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
	QString CoorUUID = QString(QCryptographicHash::hash(hash.toLocal8Bit(), QCryptographicHash::Algorithm::Md5).toHex());
	json.insert("CoordinateUUID", CoorUUID.toStdString().c_str());
	json.insert("Time", QDate::currentDate().addDays(-1).toString("yyyy-MM-dd"));
	json.insert("TileCoordinates", subjson1);
	json.insert("TilePosition", subjson2);
	json.insert("Category", this->_category.toStdString().c_str());

	QJsonDocument doc;
	doc.setObject(json);

	QNetworkReply * reply = this->_nam->post(newRequest, doc.toJson());
	QEventLoop eventLoop;
	QObject::connect(reply, SIGNAL(finished()), &eventLoop, SLOT(quit()));
	eventLoop.exec();

	if (reply->error() == QNetworkReply::NoError)
	{
		qDebug() << "Send value:" << this->_current.y << ":" << this->_current.x;
		qDebug() << "Answer from server:" << reply->rawHeader("X-ORCHESTRATE-REQ-ID");
	}
}

QRectF					Request::boundingRect() const
{
	return QRectF(0, 0, 512, 512);
}

void					Request::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
	if (this->_readyToDraw == true)
	{
		painter->setPen(Qt::NoPen);
		painter->drawPixmap(0, 0, this->_image);
	}
	else
	{
		painter->setBrush(QColor::fromRgb(0, 255, 0, 100));
		painter->drawRect(0, 0, 512, this->_downloadProgress);
		painter->setPen(Qt::NoPen);
		painter->setBrush(Qt::NoBrush);
		painter->setPen(Qt::darkGreen);
		painter->drawRect(0, 0, 512, 512);
	}

}