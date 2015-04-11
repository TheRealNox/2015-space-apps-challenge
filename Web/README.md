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

Registers a new user.

**Parameters:**
- email_address
- password

### users/login
``POST /api/users/login``

Logs a user in.

**Parameters:**
- email_address
- password

### ratings
``GET /api/users/login``

Shows all ratings for the logged in user.

**Parameters:**
- (none)
