# 2015-space-apps-challenge

## __Project Description__

This project is an Android application allowing users to visualize earth imagery taken from NASA's Global Imagery Browse Service (GIBS), in order to allow crowd voting of interesting images.

The goal was to make people help NASA sort thousands of satellite images every day.
We made an mobile application that allows users to "like" or "dislike" a satellite image with a possiblity of Fire event or any intersting event. 
Via an invitation to register and login, people are able to like any satellite image where it may have an interesting event. In this way satellite imagery can be filtered via crowdvoting their likes and dislikes. This data can be used find interesting new discoveries.


###Web

The Server side is handled by CakePHP 3.0, A PHP framework using RESTful web services.
The server requests for discovery objects from [Orchestrate](https://orchestrate.io/ "orchestrate.io"), and uses this data to query GIBS for image tiles. A query is also made to google-places in order to return the location of the images.

**FEATURES**
- *Hot/Not*
- *View list of likes*
- *Different-day discoveries*

Docs for the PHP server API are located in [Web/README.md](https://github.com/TheRealNox/2015-space-apps-challenge/blob/master/Web/README.md "README")



___



###Imagery Analyser

An imagery analyser that will process different layers from NASA's databases and push new discoveries onto our database:

Server made in C++ using Qt framework. Will be run daily to add new discoveries to our database. For now it only scan for Fire Hazard related discovery but later on, more plugins will be add to the system in order to embellish our panel of discovery.

*More technically:*
- We request, at very precise level, the layers coming from the EarthData database.
- We then process them using our custom search plugin(s) (Fire, Tornado, etc.).
And finally the analyser will create and push new discoveries to our server from those analysis.


___


###Android

![Splash Screen](https://github.com/TheRealNox/2015-space-apps-challenge/blob/master/Android/StellarViews/screenshots/device-2015-04-12-165449.png) ![Discovery Feed](https://github.com/TheRealNox/2015-space-apps-challenge/blob/master/Android/StellarViews/screenshots/device-2015-04-12-165521.png) ![My Discoveries](https://github.com/TheRealNox/2015-space-apps-challenge/blob/master/Android/StellarViews/screenshots/device-2015-04-12-165544.png)

