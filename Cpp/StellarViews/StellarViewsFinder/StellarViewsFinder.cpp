#include "StellarViewsFinder.h"



StellarViewsFinder::StellarViewsFinder(QWidget *parent)
	: QMainWindow(parent)
{
	this->_ui.setupUi(this);

	this->_id = 0;
	this->_top.lat = -90.f;
	this->_top.lon = -180.f;
	this->_bottom.lat = -90.f + STEP;
	this->_bottom.lon = -180.f + STEP;
	this->_tile.x = 0;
	this->_tile.y = 0;
	connect(this->_ui.getTileButton, SIGNAL(clicked()), this, SLOT(buttonClicked()));

}

StellarViewsFinder::~StellarViewsFinder()
{

}

void				StellarViewsFinder::plugController(Controller * control)
{
	connect(this, SIGNAL(newGetFireTileRequestReady(GetFireTileRequest*)), control, SLOT(getFireTileRequest(GetFireTileRequest *)));
	//this->_ui.actionUndo->setShortcutContext(Qt::ApplicationShortcut);
	//this->_ui.actionRedo->setShortcutContext(Qt::ApplicationShortcut);

	//this->addAction(this->_ui.actionUndo);
	//this->addAction(this->_ui.actionRedo);

	//connect(this->_ui.actionUndo, SIGNAL(triggered()), control, SLOT(undoTriggered()));
	//connect(this->_ui.actionRedo, SIGNAL(triggered()), control, SLOT(redoTriggered()));
}

void					StellarViewsFinder::buttonClicked()
{
	bool				run = true;
	while (run)
	{
		//MODIS_Terra_CorrectedReflectance_TrueColor
		//https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-04-08&Layer=MODIS_Fires_All&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.1.0&Format=image%2Fjpeg&TileMatrix=7&TileCol=0&TileRow=16
		//https://map1c.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=319&TileRow=16
		//https://map1a.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=-319&TileRow=-169
		//https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-02-08&Layer=MODIS_Aqua_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=159&TileRow=79
		//MODIS_Fires_All
		//https://map2b.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-02-11&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=149.625%2C-29.25%2C150.75%2C-28.125
		//https://map2c.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-02-11&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=173.25%2C-82.125%2C174.375%2C-81
		//https://map2b.vis.earthdata.nasa.gov/wms/wms.php?TIME=2015-01-19&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MODIS_Fires_All&WIDTH=512&HEIGHT=512&SRS=EPSG%3A4326&STYLES=&BBOX=189%2C90%2C198%2C99

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
		GetFireTileRequest *fireRequest = new GetFireTileRequest(nRequest);
		fireRequest->setData(this->_top, this->_bottom, this->_tile);
		emit this->newGetFireTileRequestReady(fireRequest);
		QCoreApplication::processEvents();
		this->_id += 1;
		this->_tile.x += 1;
		this->_top.lat += STEP;
		this->_bottom.lat += STEP;
		if (this->_tile.x >= 160)
		{
			this->_tile.x = 0;
			this->_tile.y += 1;
			this->_top.lat = -90.f;
			this->_bottom.lat = -90.f + STEP;
			this->_top.lon += STEP;
			this->_bottom.lon = this->_top.lon + STEP;
			if (this->_tile.y >= 320)
				run = false;
		}
	}
}