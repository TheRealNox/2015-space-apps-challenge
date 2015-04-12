# 2015-space-apps-challenge

## __Project Description__
This project is an Android application allowing users to visualize earth imagery taken from NASA's Global Imagery Browse Service (GIBS), in order to allow crowd voting of interesting images.

__**Android**__

___

**_Web_**

The Server side is handled by CakePHP 3.0, A PHP framework using RESTful web services.
The server requests for discovery objects from [Orchestrate](https://orchestrate.io/ "orchestrate.io"), and uses this data to query GIBS for image tiles. A query is also made to google-places in order to return the location of the images.

**FEATURES**
- *Hot/Not*
- *View list of likes*
- *Different-day discoveries*

Docs for the PHP server API are located in [Web/README.md](https://github.com/TheRealNox/2015-space-apps-challenge/blob/master/Web/README.md "README")
