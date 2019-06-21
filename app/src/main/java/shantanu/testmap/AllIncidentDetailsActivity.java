package shantanu.testmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Util.GlobalVariable;
import model.IncidentModel;

/**
 * Created by Shantanu on 27-01-2017.
 */

public class AllIncidentDetailsActivity extends AppCompatActivity{
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

    Button mapButton;
    private static final String TAG = "DAY_INCIDENT_DETAILS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_incident_details);

        Log.e("CURRENT ACTIVITY",""+TAG);
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
                    Intent intent = new Intent(AllIncidentDetailsActivity.this,Test1.class);
                    intent.putExtra("incidentList",true);
                    startActivity(intent);
                }
            });
        }
    }

    private void init(){
        address_of_incident = (TextView) findViewById(R.id.textViewAllIncidentDetailsAddressOfIncident);
        attribute_1 = (TextView) findViewById(R.id.textViewAllIncidentDetailsAttribute1);
        attribute_2 = (TextView) findViewById(R.id.textViewAllIncidentDetailsAttribute2);
        attribute_3 = (TextView) findViewById(R.id.textViewAllIncidentDetailsAttribute3);
        cause_of_incident = (TextView) findViewById(R.id.textViewAllIncidentDetailsCauseOfIncident);
        city_id = (TextView) findViewById(R.id.textViewAllIncidentDetailsCitiId);
        date_time = (TextView) findViewById(R.id.textViewAllIncidentDetailsDate);
        description_of_incident = (TextView) findViewById(R.id.textViewAllIncidentDetailsDescriptionOfIncident);
        latitude = (TextView) findViewById(R.id.textViewAllIncidentDetailsLat);
        longitude = (TextView) findViewById(R.id.textViewAllIncidentDetailsLong);
        obj_part_of_incident = (TextView) findViewById(R.id.textViewAllIncidentDetailsObject);
        incident_id = (TextView) findViewById(R.id.textViewAllIncidentDetailsIncidentId);
        report_by = (TextView) findViewById(R.id.textViewAllIncidentDetailsReportBy);
        sap_notification = (TextView) findViewById(R.id.textViewAllIncidentDetailsSapNotification);
        gid = (TextView) findViewById(R.id.textViewAllIncidentDetailsGid);

        mapButton = (Button) findViewById(R.id.buttonIncidentDetailsOpenMap);
    }
}
