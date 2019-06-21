package Util;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Abhijit on 13-12-2016.
 */
/*link for webservice = http://203.129.224.73:8080/geoserver*/
/*lat long values = 72.39440918, 23.59419433*/
public class TileProviderFactory {

    public static WMSTileProvider getOsgeoWmsTileProvider(String STR_WMS_LAYER_NAME) {


        //This is configured for:
        // http://beta.sedac.ciesin.columbia.edu/maps/services
        // if it doesn't, find another one that supports EPSG:900913
        final String WMS_FORMAT_STRING =
                "http://203.129.224.73:8080/geoserver/wms" +
                        "?service=WMS" +
                        "&version=1.1.1" +
                        "&request=GetMap" +
                        "&layers=" + STR_WMS_LAYER_NAME +
                        "&bbox=%f,%f,%f,%f" +
                        "&width=256" +
                        "&height=256" +
                        "&srs=EPSG:900913" +
                        "&format=image/png" +
                        "&transparent=true";


        Log.i("service link :",""+WMS_FORMAT_STRING);


        WMSTileProvider tileProvider = new WMSTileProvider(256,256)
        {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                String s = String.format(Locale.US, WMS_FORMAT_STRING, bbox[MINX],bbox[MINY], bbox[MAXX], bbox[MAXY]);
                Log.d("WMSDEMO", s);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }
}
