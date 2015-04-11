#include "Request.h"

Request::Request(QNetworkRequest * request) : _nam(NULL), _request(request)
{

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