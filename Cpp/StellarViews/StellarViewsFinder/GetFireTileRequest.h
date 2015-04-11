#ifndef __GETFIRETILEREQUEST_H__
#define __GETFIRETILEREQUEST_H__

#include <QImage>
#include <QPixmap>
#include <QObject>

#include "Request.h"

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

class						GetFireTileRequest : public Request
{
	Q_OBJECT

private:
	LatLonCoord				_topLeftCorner;
	LatLonCoord				_bottomRightCorner;
	Tile					_current;

public:
	GetFireTileRequest(QNetworkRequest * request = NULL);
	GetFireTileRequest(const GetFireTileRequest &request);
	~GetFireTileRequest();

public:
	void					run();
	
public:
	LatLonCoord				getTop() const;
	LatLonCoord				getBottom() const;
	Tile					getTile() const;

	void					setData(LatLonCoord top, LatLonCoord bottom, Tile current);

private:
	void					pushToServer();
};

Q_DECLARE_METATYPE(GetFireTileRequest);

#endif