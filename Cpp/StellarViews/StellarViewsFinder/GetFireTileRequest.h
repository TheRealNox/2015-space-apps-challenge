#ifndef __GETFIRETILEREQUEST_H__
#define __GETFIRETILEREQUEST_H__

#include <QImage>
#include <QPixmap>
#include <QObject>

#include "Request.h"

#define COLOUR_WEIGHT (150*250)

class						GetFireTileRequest : public Request
{
	Q_OBJECT

private:


public:
	GetFireTileRequest(QNetworkRequest * request = NULL, QString = 0);
	GetFireTileRequest(const GetFireTileRequest &request);
	~GetFireTileRequest();

public:
	void					run();

private:

public slots :
	void					downloadProgress(qint64 read, qint64 total);
	void					handleReply(QNetworkReply * reply);

};

Q_DECLARE_METATYPE(GetFireTileRequest);

#endif