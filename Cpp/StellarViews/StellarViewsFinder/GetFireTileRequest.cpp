#include "GetFireTileRequest.h"

GetFireTileRequest::GetFireTileRequest(QNetworkRequest * request, QString key) : Request(request, "Fire All", key)
{

}

GetFireTileRequest::~GetFireTileRequest()
{
}

GetFireTileRequest::GetFireTileRequest(const GetFireTileRequest &request)
{
	Request::setRequest(request.getRequest());
	Request::setData(request.getTop(), request.getBottom(), request.getTile());
}

void				GetFireTileRequest::run()
{
	if (this->_nam == NULL)
		this->_nam = new QNetworkAccessManager;

	//QObject::connect(_nam, SIGNAL(finished(QNetworkReply *)), this, SLOT(handleReply(QNetworkReply*)));
	QEventLoop daLoop;
	QTimer autoKill;
	autoKill.setSingleShot(true);
	QObject::connect(&autoKill, SIGNAL(timeout()), &daLoop, SLOT(quit()));
	QNetworkReply* reply = this->_nam->get(*this->_request);
	//autoKill.start(5000);
	QObject::connect(reply, SIGNAL(downloadProgress(qint64, qint64)), this, SLOT(downloadProgress(qint64, qint64)));
	QObject::connect(reply, SIGNAL(finished()), &daLoop, SLOT(quit()));
	daLoop.exec();
	this->handleReply(reply);
}

void					GetFireTileRequest::downloadProgress(qint64 read, qint64 total)
{
	this->_downloadProgress = ((100 * read) / total) *(512/100);
}

void					GetFireTileRequest::handleReply(QNetworkReply * reply)
{
	// no error received?
	if (reply->error() == QNetworkReply::NoError)
	{
		// read data from QNetworkReply here
		QByteArray bytes = reply->readAll();  // bytes
		QImage image;
		this->_image.loadFromData(bytes);
		image = this->_image.toImage();
		//this->_readyToDraw = true;
		this->update();

		//QString path("D:\\work\\temp\\");
		//QString name("");

		//name.append(QString::number(this->_current.x));
		//name.append("_");
		//name.append(QString::number(this->_current.y));
		//name.append(".png");

		//path.append(name);

		qDebug() << " x1=" << this->_current.x << " x2=" << this->_current.y;
		int colourWeight = 0;
		for (int i = 0; i < image.byteCount(); i++)
		{
			if (image.constBits()[i] != '\0')
				colourWeight += image.constBits()[i];
		}

		if (colourWeight >= COLOUR_WEIGHT)
		{
			//qDebug() << "Red with name:" << name;
			//image.save(path, "PNG");
			this->pushToServer();
		}

	}
	// Some http error received
	else
	{
		qDebug() << "Request error: " << reply->errorString();
	}

	reply->deleteLater();
}