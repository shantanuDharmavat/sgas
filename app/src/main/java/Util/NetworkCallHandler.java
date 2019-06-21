package Util;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shantanu on 04-01-2017.
 */

public class NetworkCallHandler {

    static String URL = WebServiceClass.ALLINCIDENTURL;
    static  String encodedIMEI = EncodeHelper.md5(GlobalVariable.getInstance().IMEI);

    public JSONArray allIncidentCall() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        JSONArray array;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedIMEI));
            nameValuePairs.add(new BasicNameValuePair("controller","acomplist"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            /*Log.e("URL", httpclient.toString());
            Log.e("HTTP POST URL", "");*/
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
//            Log.e("json response",""+EntityUtils.toString(entity));
//            largeLog("json response",EntityUtils.toString(entity));
            array = new JSONArray(EntityUtils.toString(entity));
//            Log.e("json array","a"+array.toString());

            return array;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray singleIncidentCall(String date) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        JSONArray array;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedIMEI));
            nameValuePairs.add(new BasicNameValuePair("controller","day_incident"));
            nameValuePairs.add(new BasicNameValuePair("date_inc",date));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
//            Log.e("URL", httpclient.toString());
//            Log.e("HTTP POST URL", "");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
//            Log.e("json response",""+EntityUtils.toString(entity));
//            largeLog("json response",EntityUtils.toString(entity));

            array = new JSONArray(EntityUtils.toString(entity));

            return array;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String locationUpdate(String id, String lat,String lon, String encodedImei, String cityId){
        String responseString = "";
        try {
            String URL = WebServiceClass.ALLINCIDENTURL;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedImei));
            nameValuePairs.add(new BasicNameValuePair("controller","update_incident_marker"));
            nameValuePairs.add(new BasicNameValuePair("lat",""+lat));
            nameValuePairs.add(new BasicNameValuePair("lon",""+lon));
            nameValuePairs.add(new BasicNameValuePair("city_id",""+cityId));
            nameValuePairs.add(new BasicNameValuePair("date_time",""+new Utilities().dateUtil()));
            nameValuePairs.add(new BasicNameValuePair("flag","2"));
            nameValuePairs.add(new BasicNameValuePair("pro_uid",""+id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
//            Log.e("URL", httpclient.toString());
//            Log.e("HTTP POST URL", "");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            responseString = EntityUtils.toString(entity);

            Log.e("response String",""+responseString);

            return responseString;
        }catch (Exception e){e.printStackTrace();}
        return "";
    }

    public static String imageSender(String imageString,String id,Context context){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        String responseString;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            Log.e("image string",""+imageString);

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedIMEI));
            nameValuePairs.add(new BasicNameValuePair("controller","update_incident_image"));
            nameValuePairs.add(new BasicNameValuePair("pro_uid",id));
            nameValuePairs.add(new BasicNameValuePair("image_src",imageString));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            responseString = EntityUtils.toString(entity);

            return responseString;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String closeIncident(String id){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        String responseString;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedIMEI));
            nameValuePairs.add(new BasicNameValuePair("controller","close_incident"));
            nameValuePairs.add(new BasicNameValuePair("pro_uid",id));
            nameValuePairs.add(new BasicNameValuePair("flag","4"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            responseString = EntityUtils.toString(entity);

            return responseString;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String updateIncident(List<NameValuePair> nameValuePairs){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        String responseString;
        try {
            nameValuePairs.add(new BasicNameValuePair("controller","update_incident_param"));
            nameValuePairs.add(new BasicNameValuePair("mobileimeino", encodedIMEI));
            nameValuePairs.add(new BasicNameValuePair("flag","3"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            responseString = EntityUtils.toString(entity);

            return responseString;
        }catch (Exception e){e.printStackTrace();}
        return "";
    }

    public static String getDistance(String url){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseString;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            responseString = EntityUtils.toString(entity);

            return responseString;
        }catch (Exception e){e.printStackTrace();}
        return "";
    }

    /*
    public String testCall(String url,String cipherImei) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String object = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", cipherImei));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            Log.e("URL", httpclient.toString());
            Log.e("HTTP POST URL", "");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            object = entity.toString();
            Log.e("RESPONSE",""+object);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }*/
}


//            nameValuePairs.add(new BasicNameValuePair("mob_imei_no", "cdbe80827a2a9f57bc97264a5f2c7c0d"));
