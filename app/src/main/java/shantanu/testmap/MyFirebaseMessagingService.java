package shantanu.testmap;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Util.GlobalVariable;
import Util.MyNotificationManager;
import model.NotificationModel;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String Address_of_incident, attribute_1, attribute_2, attribute_3, cause_of_incident, city_id, date_time, description_of_incident, latitude, longitude, message, obj_part_of_incident, pro_uid, report_by, sap_notification, title;
    NotificationModel model;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    public ArrayList<String> final_pop_up_key = new ArrayList<>();
    public ArrayList<String> final_pop_up_value = new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String imageUrl = "null";

            for (Iterator<String> iter = data.keys(); iter.hasNext(); ) {
                String key = iter.next();
                String value = data.get(key).toString();

                final_pop_up_key.add(key);
                final_pop_up_value.add(value);
            }

            model = new NotificationModel();
            model.setAddress_of_incident(data.getString("Address_of_incident"));
            model.setAttribute_1(data.getString("attribute_1"));
            model.setAttribute_2(data.getString("attribute_2"));
            model.setAttribute_3(data.getString("attribute_3"));
            model.setCause_of_incident(data.getString("cause_of_incident"));
            model.setCity_id(data.getString("city_id"));
            model.setDate_time(data.getString("date_time"));
            model.setDescription_of_incident(data.getString("description_of_incident"));
            model.setLatitude(data.getString("latitude"));
            model.setLongitude(data.getString("longitude"));
            model.setMessage(data.getString("message"));
            model.setObj_part_of_incident(data.getString("obj_part_of_incident"));
            model.setPro_uid(data.getString("pro_uid"));
            model.setReport_by(data.getString("report_by"));
            model.setSap_notification(data.getString("sap_notification"));
            model.setTitle(data.getString("title"));

            model.setFinal_pop_up_value(final_pop_up_value);
            model.setFinal_pop_up_key(final_pop_up_key);

            title = data.getString("title");
            message = data.getString("message");

            /*if (globalVariable.dbHelper == null)
                globalVariable.dbHelper = new DataBaseHelper(this);*/

//            globalVariable.dbHelper.insertNotification(model);

            globalVariable.notificationModelList.add(model);
            globalVariable.setRecievedNotification(model);

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), Test1.class);
            intent.putExtra("incident", true);
            intent.putExtra("lat",model.getLatitude());
            intent.putExtra("lng",model.getLongitude());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            Intent newIntent = new Intent();
            newIntent.setAction("notification");
            newIntent.putExtra("incident",true);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(newIntent);

            //if there is no image
            if (imageUrl.equals("null")) {//"null"
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            } else {
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}