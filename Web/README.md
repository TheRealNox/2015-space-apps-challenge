Volcanoes, Icebergs and Cats from space

## Overview
This is a REST web API built in cakePHP.

## Usage
You can set what output format you would like by appending the a file extension to the URL, e.g. /api/users/login**.json**.

Currently the only supported output format is JSON.

## Reference

All methods require **auth_token** to be sent along with the request, except for users/register and users/login. The server will supply an **auth_token** when you log in or register a user.

The currently implemented methods are below:

### users/register
``POST /api/users/register``

Registers a new user. Supplies an auth_token which can be used in subsequent requests.

**Parameters:**
- email_address
- password

**Returns:**
``
[
  errors: [],
  auth_token: "blah blah blah",
  success: true
]
``

### users/login
``POST /api/users/login``

Logs a user in. Supplies an auth_token which can be used in subsequent requests.

**Parameters:**
- email_address
- password

**Returns:**
``
[
  auth_token: "blah blah blah",
  success: true
]
``

### ratings
``GET /api/ratings/index``

Shows all ratings for the logged in user.

**Parameters:**
- (none)

**Returns:**
``
[
  [
    id: 123,
    user_id: 123,
    image_id: 123,
    is_interesting: 1,
    image_category_id: 0,
    note: "space ftw",
    created: "2015-04-11 00:39:54",
    updated: "2015-04-11 00:39:54"
  ],
  ...
]
``

### ratings/add

``POST /api/ratings/add``

Recieves auth_token and Image ID to update number of likes

**Parameters:**
- auth_token
- ratings

**Returns:**

``
{
    "errors": [],
    "success": false,
    "rows_total": 1,
    "rows_saved": 1
}
``

### images/index
``GET /api/images/index``

Returns Images from data supplied by Orchestrate, Amount is determined by available data.

**Parameters:**
- (none)

**Returns:**

``
{
    "images": [
        {
            "id": 4,
            "image_collection_id": 1,
            "uuid": "12321321321321554641651511651",
            "unique_key": "095b43c112204fee",
            "date_taken": "2015-03-10T00:00:00+0000",
            "tile_x": 26,
            "tile_y": 125,
            "created": "2015-04-11T06:06:24+0000",
            "modified": "2015-04-11T06:06:24+0000",
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        }
    ]
}
``

### images/get_previous
``GET /api/images/get_previous?auth_token={auth-token}``

Returns Images from data supplied by Orchestrate, Amount is determined by available data.

**Parameters:**
- image_id
- limit

**Returns:**

``
{
    "images": [
        {
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        },
        {
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        },
        {
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        },
        {
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        },
        {
            "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-03-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=125&TileRow=26"
        }
    ],
    "success": true
}
``
