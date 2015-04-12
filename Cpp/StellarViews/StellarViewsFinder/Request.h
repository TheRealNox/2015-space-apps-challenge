#ifndef __REQUEST_H__
#define __REQUEST_H__

#include <QCoreApplication>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QObject>
#include <QThread>
#include <QRunnable>
#include <QJsonObject>
#include <QJsonDocument>
#include <Qtimer>
#include <QGraphicsPixmapItem>
#include <QPainter>

struct			LatLonCoord
{
	float		lat;
	float		lon;
};

struct			Tile
{
	int			x;
	int			y;
};

class						Request : public QObject, public QRunnable, public QGraphicsItem
{

	Q_OBJECT
	Q_INTERFACES(QGraphicsItem)

protected:
	QString					_apiKey;
	QString					_category;
	QNetworkAccessManager *	_nam;
	QNetworkRequest *		_request;
	LatLonCoord				_topLeftCorner;
	LatLonCoord				_bottomRightCorner;
	Tile					_current;
	bool					_readyToDraw;
	QPixmap					_image;
	int						_downloadProgress;

public:
	Request(QNetworkRequest * request = NULL, QString = 0, QString = 0);
	Request(const Request &);
	~Request();

public:
	virtual void			run();
	QRectF					boundingRect() const;
	void					paint(QPainter *, const QStyleOptionGraphicsItem *, QWidget *);
	void					pushToServer();
	virtual LatLonCoord		getTop() const;
	virtual LatLonCoord		getBottom() const;
	virtual Tile			getTile() const;

	virtual void			setData(LatLonCoord top, LatLonCoord bottom, Tile current);

	void					setRequest(QNetworkRequest *);
	QNetworkRequest *		getRequest() const;
};

Q_DECLARE_METATYPE(Request);

#endif