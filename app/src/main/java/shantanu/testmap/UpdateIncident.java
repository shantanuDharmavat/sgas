package shantanu.testmap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import Util.GlobalVariable;
import Util.NetworkCallHandler;
import model.IncidentModel;


/**
 * Created by Shantanu on 14-02-2017.
 */
public class UpdateIncident extends AppCompatActivity{
    EditText address_of_incident;
    EditText attribute_1;
    EditText attribute_2;
    EditText attribute_3;
    EditText cause_of_incident;
    EditText city_id;
    EditText date_time;
    EditText description_of_incident;
    EditText latitude;
    EditText longitude;
    EditText obj_part_of_incident;
    TextView incident_id;
    EditText report_by;
    TextView sap_notification;
    EditText gid;
    Button buttonSubmit;

    IncidentModel model;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_incident);
        
        init();

        if (globalVariable.selectedIncidentModel != null && globalVariable.selectedIncidentModel.getLongitude() != "") {
            model = globalVariable.selectedIncidentModel;

            address_of_incident.setText(model.getAddress_of_incident());
            attribute_1.setText(model.getAttribute_1());
            attribute_2.setText(model.getAttribute_2());
            attribute_3.setText(model.getAttribute_3());
            cause_of_incident.setText(model.getCause_of_incident());
            city_id.setText(model.getCity_id());
            description_of_incident.setText(model.getDescription_of_incident());
            obj_part_of_incident.setText(model.getObj_part_of_incident());
            incident_id.setText(model.getIncident_id());
            report_by.setText(model.getReport_by());
            sap_notification.setText(model.getSap_notification());

//            gid.setText(model.getGid());
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("pro_uid", incident_id.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("Address_of_incident",address_of_incident.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("report_by",report_by.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("sap_notification",sap_notification.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("cause_of_incident",cause_of_incident.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("obj_part_of_incident", obj_part_of_incident.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("attribute_1",attribute_1.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("attribute_2",attribute_2.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("attribute_3",attribute_3.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("city_id",city_id.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("description_of_incident",description_of_incident.getText().toString()));

                for (int i = 0; i < nameValuePairs.size(); i++){
                    Log.e("name value pair",""+nameValuePairs.get(i).getValue());
                }
                new updateIncidentTask().execute(nameValuePairs);
            }
        });
    }

    private void init() {
        address_of_incident = (EditText) findViewById(R.id.EditTextIncidentUpdateAddressOfIncident);
        attribute_1 = (EditText) findViewById(R.id.EditTextIncidentUpdateAttribute1);
        attribute_2 = (EditText) findViewById(R.id.EditTextIncidentUpdateAttribute2);
        attribute_3 = (EditText) findViewById(R.id.EditTextIncidentUpdateAttribute3);
        cause_of_incident = (EditText) findViewById(R.id.EditTextIncidentUpdateCauseOfIncident);
        city_id = (EditText) findViewById(R.id.EditTextIncidentUpdateCitiId);
//        date_time = (EditText) findViewById(R.id.EditTextIncidentUpdateDate);
        description_of_incident = (EditText) findViewById(R.id.EditTextIncidentUpdateDescriptionOfIncident);
//        latitude = (EditText) findViewById(R.id.EditTextIncidentUpdateLat);
//        longitude = (EditText) findViewById(R.id.EditTextIncidentUpdateLong);
        obj_part_of_incident = (EditText) findViewById(R.id.EditTextIncidentUpdateObject);
        incident_id = (TextView) findViewById(R.id.TextViewUpdateIncidentID);
        report_by = (EditText) findViewById(R.id.EditTextIncidentUpdateReportBy);
        sap_notification = (TextView) findViewById(R.id.EditTextIncidentUpdateSapNotification);
//        gid = (EditText) findViewById(R.id.EditTextIncidentUpdateGid);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitUpdate);
    }

    private class updateIncidentTask extends AsyncTask<List<NameValuePair>,Void,String>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpdateIncident.this);
            dialog.setTitle("Updating!");
            dialog.setMessage("Sending data");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(List<NameValuePair>... params) {
            String responeString = new NetworkCallHandler().updateIncident(params[0]);
            Log.e("update response string",""+responeString);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing())
                dialog.cancel();
        }
    }
}
