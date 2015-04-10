#ifndef __TASKENUM_H__
#define __TASKENUM_H__

#define SUBTYPE_MASK						0xFFFF0000
#define CMD_MASK							0x0000FFFF

typedef									enum
{
	Undo = 0x0001,
	Redo = 0x0002,
	BeginGroupOfCmds = 0x0003,
	EndGroupOfCmds = 0x0004
}										InternalSubCmds;

typedef									enum
{
	PushAssetToShotListWithoutCheck = 0x0001,
	PushAssetToShotListWithCheck = 0x0002,
	RemoveAssetFromShotList = 0x0003,
	ShotSelectedForPlayback = 0x0004,
	InsertClipToShotList = 0x0005,
	MoveClipWithinShotList = 0x0006
}										GeneralUISubCmds;

typedef									enum
{
	SourceTypeChanged = 0x0001,
	ExposureChanged = 0x0002,
	KelvinChanged = 0x0003,
	StockChanged = 0x0004,
	StockAmountChanged = 0x0005,
	StockCurveAmountChanged = 0x0006,
	GrainSizeChanged = 0x0007,
	GrainAmountChanged = 0x0008,
	LowWheelChanged = 0x0009,
	MidWheelChanged = 0x000A,
	HighWheelChanged = 0x000B,
	LiftAmountChanged = 0x000C,
	GammaAmountChanged = 0x000D,
	GainAmountChanged = 0x000E,
	SaturationAmountChanged = 0x000F,
	HistogramSliderChanged = 0x0010,
	ShowOriginalChanged = 0x0011
}										ClipSettingsSubCmds;

typedef									enum
{
	InternalMask = 0x00000000,
	GeneralUIMask = 0x000F0000,
	ClipSettingsMask = 0x00FF0000,
	RenderMask = 0x0FFF0000,
	OtherMask = 0xFFFF0000
}										CommandMask;

#endif//__TASKENUM_H__

