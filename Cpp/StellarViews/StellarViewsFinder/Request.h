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

class						Request : public QObject, QRunnable
{

	Q_OBJECT

protected:
	QNetworkAccessManager *	_nam;
	QNetworkRequest *		_request;

public:
	Request(QNetworkRequest * request = NULL);
	Request(const Request &);
	~Request();

public:
	virtual void			run();
	void					setRequest(QNetworkRequest *);
	QNetworkRequest *		getRequest() const;
};

Q_DECLARE_METATYPE(Request);

#endif