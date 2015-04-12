package nz.co.spaceapp.stellarviews;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Roberta on 12/04/2015.
 */
public class Ratings {

    protected static Type mArrayListType = new TypeToken<ArrayList<Ratings>>() {
    }.getType();

    @SerializedName("image")
    private Discovery mDiscovery;

    public static Type getArrayType() {
        return mArrayListType;
    }

    public Discovery getDiscovery() {
        return mDiscovery;
    }
    /*
     "id": 2,
            "user_id": 4,
            "image_id": 715,
            "is_interesting": true,
            "note": null,
            "created": "2015-04-12T02:03:34+0000",
            "modified": "2015-04-12T02:03:34+0000",
            "image": {
                "id": 715,
                "image_detail_id": 715,
                "unique_key": "095c6377442040dc",
                "date_taken": "2015-04-10T00:00:00+0000",
                "created": "2015-04-11T23:48:15+0000",
                "modified": "2015-04-11T23:48:15+0000",
                "image_detail": {
                    "id": 715,
                    "image_collection_id": 1,
                    "uuid": "38e86a24f3db99c0a372bc608819044b",
                    "lat_top": 27,
                    "lat_bottom": 28.125,
                    "long_top": 57.375,
                    "long_bottom": 58.5,
                    "tile_x": 104,
                    "tile_y": 211,
                    "created": "2015-04-11T23:48:15+0000",
                    "modified": "2015-04-11T23:48:15+0000"
                },
                "url": "https://map1b.vis.earthdata.nasa.gov/wmts-geo/wmts.cgi?TIME=2015-04-10&Layer=MODIS_Terra_CorrectedReflectance_TrueColor&TileMatrixSet=EPSG4326_250m&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fjpeg&TileMatrix=8&TileCol=211&TileRow=104"
            }
     */
}
