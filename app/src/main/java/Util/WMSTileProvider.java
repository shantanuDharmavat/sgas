package Util;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.URLEncoder;

/**
 * Created by Abhijit on 13-12-2016.
 */


public abstract class WMSTileProvider extends UrlTileProvider {

    // Web Mercator n/w corner of the map.
    private static final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
    //array indexes for that data
    private static final int ORIG_X = 0;
    private static final int ORIG_Y = 1; // "

    // Size of square world map in meters, using WebMerc projection.
    private static final double MAP_SIZE = 20037508.34789244 * 2;

    // array indexes for array to hold bounding boxes.
    public static final int MINX = 0;
    public static final int MAXX = 1;
    public static final int MINY = 2;
    public static final int MAXY = 3;

    // cql filters
    private String cqlString = "";
    public static double[] bbox = new double[4];

    // Construct with tile size in pixels, normally 256, see parent class.
    public WMSTileProvider(int x, int y) {
        super(x, y);
    }

    protected String getCql() {
        return URLEncoder.encode(cqlString);
    }

    public void setCql(String c) {
        cqlString = c;
    }

    // Return a web Mercator bounding box given tile x/y indexes and a zoom
    // level.
    protected double[] getBoundingBox(int x, int y, int zoom) {
        double tileSize = MAP_SIZE / Math.pow(2, zoom);
        double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
        double maxx = TILE_ORIGIN[ORIG_X] + (x+1) * tileSize;
        double miny = TILE_ORIGIN[ORIG_Y] - (y+1) * tileSize;
        double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

        bbox = new double[4];
        bbox[MINX] = minx;
        bbox[MINY] = miny;
        bbox[MAXX] = maxx;
        bbox[MAXY] = maxy;


        //Log.e("bbox[MINX]=", "" + bbox[MINX]);
        //Log.e("bbox[MINY]=", "" + bbox[MINY]);
        //Log.e("bbox[MAXX]=", "" + bbox[MAXX]);
        //Log.e("bbox[MAXY]=", "" + bbox[MAXY]);

        return bbox;
    }

}