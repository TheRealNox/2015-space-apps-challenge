# ----------------------------------------------------
# This file is generated by the Qt Visual Studio Add-in.
# ------------------------------------------------------

TEMPLATE = app
TARGET = StellarViewsFinder
DESTDIR = ../Win32/Debug
QT += core network xml widgets gui location concurrent
CONFIG += debug
DEFINES += WIN64 QT_DLL QT_LOCATION_LIB QT_NETWORK_LIB QT_WIDGETS_LIB QT_XML_LIB QT_CONCURRENT_LIB
INCLUDEPATH += ./GeneratedFiles \
    . \
    ./GeneratedFiles/Debug
DEPENDPATH += .
MOC_DIR += ./GeneratedFiles/debug
OBJECTS_DIR += debug
UI_DIR += ./GeneratedFiles
RCC_DIR += ./GeneratedFiles
include(StellarViewsFinder.pri)
