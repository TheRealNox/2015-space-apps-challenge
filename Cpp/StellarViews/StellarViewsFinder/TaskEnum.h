#ifndef __TASKENUM_H__
#define __TASKENUM_H__

#define SUBTYPE_MASK						0xFFFF0000
#define CMD_MASK							0x0000FFFF

typedef									enum
{
	FireTileRequest = 0x0001
}										GeneralUISubCmds;

typedef									enum
{
	InternalMask = 0x00000000,
	RequestMask = 0x000F0000,
	OtherMask = 0xFFFF0000
}										CommandMask;

#endif//__TASKENUM_H__

