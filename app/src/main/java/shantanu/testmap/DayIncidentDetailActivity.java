package shantanu.testmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Util.GlobalVariable;
import Util.NetworkCallHandler;
import model.IncidentModel;

/**
 * Created by Abhijit on 27-12-2016.
 */

public class DayIncidentDetailActivity extends AppCompatActivity {
    IncidentModel model;
    GlobalVariable globalVariable = GlobalVariable.getInstance();

    TextView address_of_incident;
    TextView attribute_1;
    TextView attribute_2;
    TextView attribute_3;
    TextView cause_of_incident;
    TextView city_id;
    TextView date_time;
    TextView description_of_incident;
    TextView latitude;
    TextView longitude;
    TextView obj_part_of_incident;
    TextView incident_id;
    TextView report_by;
    TextView sap_notification;
    TextView gid;

    Button mapButton, updateButton, photoButton, closeButton;
    private static final String TAG = "DAY_INCIDENT_DETAILS";
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_incident_detail);

        Log.e("CURRENT ACTIVITY", "" + TAG);
        init();
        if (globalVariable.selectedIncidentModel != null && globalVariable.selectedIncidentModel.getLongitude() != "") {
            model = globalVariable.selectedIncidentModel;

            address_of_incident.setText(model.getAddress_of_incident());
            attribute_1.setText(model.getAttribute_1());
            attribute_2.setText(model.getAttribute_2());
            attribute_3.setText(model.getAttribute_3());
            cause_of_incident.setText(model.getCause_of_incident());
            city_id.setText(model.getCity_id());
            date_time.setText(model.getDate_time());
            description_of_incident.setText(model.getDescription_of_incident());
            latitude.setText(model.getLatitude());
            longitude.setText(model.getLongitude());
            obj_part_of_incident.setText(model.getObj_part_of_incident());
            incident_id.setText(model.getIncident_id());
            report_by.setText(model.getReport_by());
            sap_notification.setText(model.getSap_notification());
            gid.setText(model.getGid());

            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(DayIncidentDetailActivity.this, Test1.class);
                    intent.putExtra("incidentList", true);
                    startActivity(intent);
                }
            });

            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(DayIncidentDetailActivity.this, IncidentPhotoActivity.class);
                    intent.putExtra("id", incident_id.getText());
                    startActivity(intent);
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(DayIncidentDetailActivity.this, UpdateIncident.class);
                    startActivity(intent);
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DayIncidentDetailActivity.this);
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("ARE YOU SURE YOU WANT TO CLOSE THIS CASE");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("incident id", "" + model.getIncident_id());
                            new closeCaseTask().execute(model.getIncident_id());
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    private void init() {
        address_of_incident = (TextView) findViewById(R.id.textViewIncidentDetailsAddressOfIncident);
        attribute_1 = (TextView) findViewById(R.id.textViewIncidentDetailsAttribute1);
        attribute_2 = (TextView) findViewById(R.id.textViewIncidentDetailsAttribute2);
        attribute_3 = (TextView) findViewById(R.id.textViewIncidentDetailsAttribute3);
        cause_of_incident = (TextView) findViewById(R.id.textViewIncidentDetailsCauseOfIncident);
        city_id = (TextView) findViewById(R.id.textViewIncidentDetailsCitiId);
        date_time = (TextView) findViewById(R.id.textViewIncidentDetailsDate);
        description_of_incident = (TextView) findViewById(R.id.textViewIncidentDetailsDescriptionOfIncident);
        latitude = (TextView) findViewById(R.id.textViewIncidentDetailsLat);
        longitude = (TextView) findViewById(R.id.textViewIncidentDetailsLong);
        obj_part_of_incident = (TextView) findViewById(R.id.textViewIncidentDetailsObject);
        incident_id = (TextView) findViewById(R.id.textViewIncidentDetailsIncidentId);
        report_by = (TextView) findViewById(R.id.textViewIncidentDetailsReportBy);
        sap_notification = (TextView) findViewById(R.id.textViewIncidentDetailsSapNotification);
        gid = (TextView) findViewById(R.id.textViewIncidentDetailsGid);

        mapButton = (Button) findViewById(R.id.buttonIncidentDetailsOpenMap);
        photoButton = (Button) findViewById(R.id.buttonIncidentDetailsTakePhoto);
        updateButton = (Button) findViewById(R.id.buttonIncidentDetailsUpdate);
        closeButton = (Button) findViewById(R.id.buttonIncidentDetailsClose);
    }

    private class closeCaseTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String responseString = new NetworkCallHandler().closeIncident(params[0]);
            Log.e("response String", "a" + responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AlertDialog.Builder dialog = new AlertDialog.Builder(DayIncidentDetailActivity.this);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setMessage("Incident successfully closed");
            dialog.setTitle("Success");
            dialog.setIcon(R.drawable.tick);
            dialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.e("inside onSaveInstance", "true");
        outState.putStringArrayList("key", globalVariable.selectedIncidentModel.getFinal_pop_up_key());
        outState.putStringArrayList("value", globalVariable.selectedIncidentModel.getFinal_pop_up_value());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("onRestoreInstance", "" + savedInstanceState.getBoolean("fromIncidentList"));

        IncidentModel model = new IncidentModel();

        model.setFinal_pop_up_key(savedInstanceState.getStringArrayList("key"));
        model.setFinal_pop_up_value(savedInstanceState.getStringArrayList("value"));

        for (int i = 0; i < model.getFinal_pop_up_key().size(); i++) {
            Log.e("key index " + i + " ", "" + model.getFinal_pop_up_key().get(i));
        }

        Log.e("flag index", "" + model.getFinal_pop_up_key().indexOf("FLAG"));
        model.setFlag(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("FLAG")));
        model.setLongitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("LON")));
        model.setLatitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("LAT")));
        model.setAddress_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ADDRESS OF INCIDENT")));
        model.setAttribute_1(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 1")));
        model.setAttribute_2(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 2")));
        model.setAttribute_3(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 3")));
        model.setCause_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("CAUSE OF INCIDENT")));
        model.setCity_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("CITY ID")));
        model.setDate_time(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("DATE TIME")));
        model.setDescription_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("DESCRIPTION OF INCIDENT")));
        model.setGid(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("GID")));
        model.setObj_part_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("OBJ PART OF INCIDENT")));
        model.setSap_notification(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("SAP NOTIFICATION")));
        model.setIncident_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("INC ID")));
        model.setReport_by(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("REPORT BY")));
        globalVariable.selectedIncidentModel = model;

    }
}
