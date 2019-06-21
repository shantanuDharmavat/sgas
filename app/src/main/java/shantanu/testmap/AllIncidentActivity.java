package shantanu.testmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Util.GlobalVariable;
import Util.NetworkCallHandler;
import Util.WebServiceClass;
import model.IncidentModel;

/**
 * Created by Abhijit on 04-01-2017.
 */

public class AllIncidentActivity extends AppCompatActivity {
    ListView incidentListView;
    List<String> test = new ArrayList<>();
    List<IncidentModel> incidentModelList = new ArrayList<>();
    String URL = WebServiceClass.ALLINCIDENTURL;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    ArrayList<String> final_pop_up_key;
    ArrayList<String> final_pop_up_value;
    ArrayAdapter<String> adapter;
    Map<String,String> attributeMap = new TreeMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_incident);
        new RetrieveIncidentTask().execute(URL);
        incidentListView = (ListView) findViewById(R.id.ListViewAllIncident);
//        adapter.notifyDataSetChanged();
        incidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(DayIncidentActivity.this, "selected item is: " + incidentListView.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AllIncidentActivity.this, DayIncidentDetailActivity.class);
                globalVariable.selectedIncidentModel = incidentModelList.get(position);
                startActivity(intent);
                Log.e("item click",""+incidentModelList.get(position));
            }
        });
    }

    private class RetrieveIncidentTask extends AsyncTask<String, Void, List<IncidentModel>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new ProgressDialog(AllIncidentActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected List<IncidentModel>doInBackground(String... params) {
            Log.e("json Object ", "Initiated");
            try {
                JSONArray arr = new NetworkCallHandler().allIncidentCall();
                System.out.println("json Object " + arr);

                IncidentModel incidentModel;
                JSONObject obj;
                String valueM;
                Set<String> keyM;

                if (arr != null)
                    for (int i = 0; i < arr.length(); i++) {
                        incidentModel = new IncidentModel();
                        obj = arr.getJSONObject(i);

                        final_pop_up_key = new ArrayList<>();
                        final_pop_up_value = new ArrayList<>();

                        for (Iterator<String> itr = obj.keys(); itr.hasNext();){
                            String key = itr.next();
                            String value = obj.get(key).toString();

                            key = key.toUpperCase().replace("_"," ");

                            attributeMap.put(key,value);
                        }

                        keyM = attributeMap.keySet();

                        for (String key : keyM){
                            final_pop_up_key.add(key);
                            valueM = attributeMap.get(key);
                            final_pop_up_value.add(valueM);
                        }

                        incidentModel.setIncident_id(obj.getString("inc_id"));
                        incidentModel.setDescription_of_incident(obj.getString("description_of_incident"));
                        incidentModel.setAddress_of_incident(obj.getString("Address_of_incident"));
                        incidentModel.setReport_by(obj.getString("report_by"));
                        incidentModel.setSap_notification(obj.getString("sap_notification"));
                        incidentModel.setCause_of_incident(obj.getString("cause_of_incident"));
                        incidentModel.setObj_part_of_incident(obj.getString("obj_part_of_incident"));
                        incidentModel.setAttribute_1(obj.getString("attribute_1"));
                        incidentModel.setAttribute_2(obj.getString("attribute_2"));
                        incidentModel.setAttribute_3(obj.getString("attribute_3"));
                        incidentModel.setCity_id(obj.getString("city_id"));

                        incidentModel.setLatitude(obj.getString("lat"));
                        incidentModel.setLongitude(obj.getString("lon"));
                        incidentModel.setDate_time(obj.getString("date_time"));
                        incidentModel.setGid(obj.getString("gid"));
                        incidentModel.setFinal_pop_up_key(final_pop_up_key);
                        incidentModel.setFinal_pop_up_value(final_pop_up_value);

                        Log.e("Model Data", "" + incidentModel.toString());
                        incidentModelList.add(incidentModel);
                    }
                else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return incidentModelList;
        }

        @Override
        protected void onPostExecute(final List<IncidentModel> incidentList) {
            super.onPostExecute(incidentList);
            if (dialog.isShowing())
                dialog.cancel();

            if (incidentList.size() > 0) {
                for (int i = 0; i < incidentList.size(); i++) {
                    test.add(incidentList.get(i).getIncident_id());
                }
                adapter = new ArrayAdapter(AllIncidentActivity.this, android.R.layout.simple_list_item_1, test);//android.R.id.text1,
                incidentListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
