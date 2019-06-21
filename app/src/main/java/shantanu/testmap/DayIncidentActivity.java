package shantanu.testmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Adapter.MyRecyclerViewAdapter;
import Util.DividerItemDecoration;
import Util.GlobalVariable;
import Util.NetworkCallHandler;
import Util.OnItemClickListener;
import Util.Utilities;
import Util.WebServiceClass;
import model.IncidentModel;

/**
 * Created by Shantanu on 27-12-2016.
 */

public class DayIncidentActivity extends AppCompatActivity {
    //    ListView incidentListView;
    List<String> test = new ArrayList<>();
    static List<IncidentModel> incidentModelList;
    String URL = WebServiceClass.ALLINCIDENTURL;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    ArrayList<String> final_pop_up_key;
    ArrayList<String> final_pop_up_value;
    //    ArrayAdapter<String> adapter;
    ImageButton buttonCalender;
    EditText editTextDate;
    static String currentDate;
    Map<String, String> attributeMap = new TreeMap<>();
    Button buttonAllIncident;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner spinnerFlag;
    private String flag = "1";
    private static int number;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_incident);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        buttonCalender = (ImageButton) findViewById(R.id.buttonCalender);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        buttonAllIncident = (Button) findViewById(R.id.buttonAllIncident);
        spinnerFlag = (Spinner) findViewById(R.id.spinnerFlag);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.flag_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFlag.setAdapter(adapter);

        currentDate = new Utilities().dateUtil();
        editTextDate.setText(currentDate);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        buttonCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities utils = new Utilities();
                utils.datePopUp(DayIncidentActivity.this, editTextDate);
                taskCaller();
            }
        });

        buttonAllIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveIncidentTask(true).execute();
            }
        });

        spinnerFlag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    flag = "1";
                else if (position == 1)
                    flag = "4";
                else if (position == 2)
                    flag = "2";
                else
                    flag = "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e("DATE", "" + currentDate);
        new RetrieveIncidentTask(false).execute(currentDate);
    }

    private void taskCaller() {
        Log.e("list ", "" + incidentModelList.size());


        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new RetrieveIncidentTask(false).execute(editTextDate.getText().toString());
            }
        });
    }

    private class RetrieveIncidentTask extends AsyncTask<String, Void, List<IncidentModel>> {
        ProgressDialog dialog;
        boolean allIncident = false;

        RetrieveIncidentTask(boolean test) {
            if (test)
                allIncident = true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new ProgressDialog(DayIncidentActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected List<IncidentModel> doInBackground(String... params) {
            Log.e("json Object ", "Initiated");
            try {
                JSONArray arr;
                if (allIncident)
                    arr = new NetworkCallHandler().allIncidentCall();
                else
                    arr = new NetworkCallHandler().singleIncidentCall(params[0]);

//                System.out.println("json Object " + arr);
                incidentModelList = new ArrayList<>();

                IncidentModel incidentModel;
                JSONObject obj;
                String valueM;
                Set<String> keyM;
                if (arr != null) {
//                    Log.e("json array", "" + arr.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        incidentModel = new IncidentModel();
                        obj = arr.getJSONObject(i);
                        String sub;

                        final_pop_up_key = new ArrayList<>();
                        final_pop_up_value = new ArrayList<>();

                        for (Iterator<String> itr = obj.keys(); itr.hasNext(); ) {
                            String key = itr.next();
                            String value = obj.get(key).toString();

                            key = key.replace("_", " ").toUpperCase();

                            attributeMap.put(key, value);
                        }

                        keyM = attributeMap.keySet();

                        for (String key : keyM) {
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
                        incidentModel.setCity_name("city_name");
                        if (obj.getString("flag").endsWith("-08")) {
                            sub = obj.getString("flag").substring(0, ((obj.getString("flag").length()) - 3));
                        } else
                            sub = obj.getString("flag");
                        incidentModel.setFlag(sub);
                        incidentModel.setFinal_pop_up_key(final_pop_up_key);
                        incidentModel.setFinal_pop_up_value(final_pop_up_value);

//                        Log.e("Model Data", "" + incidentModel.toString());
                        incidentModelList.add(incidentModel);
                    }
                } else {
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

            List<IncidentModel> secondaryList = new ArrayList<>();

            if (flag.equalsIgnoreCase("0")) {
                secondaryList.addAll(incidentList);
            } else if (flag.equalsIgnoreCase("2")) {
                for (int i = 0; i < incidentList.size(); i++) {
//                    Log.e("incident flag", "" + incidentList.get(i).getFlag());
                    if (incidentList.get(i).getFlag().equalsIgnoreCase(flag) || incidentList.get(i).getFlag().equalsIgnoreCase("3"))
                        secondaryList.add(incidentList.get(i));
                }
            } else {
//                Log.e("Flag", "" + flag);
                for (int i = 0; i < incidentList.size(); i++) {
//                    Log.e("incident flag", "" + incidentList.get(i).getFlag());
                    if (incidentList.get(i).getFlag().equalsIgnoreCase(flag))
                        secondaryList.add(incidentList.get(i));
                }
            }
//            Log.e("SecondaryList", "" + secondaryList.size());
//            Log.e("PrimaryList", "" + incidentList.size());

            /*Collections.sort(secondaryList, new Comparator<IncidentModel>() {
                @Override
                public int compare(IncidentModel lhs, IncidentModel rhs) {
                    try {
                        Calendar lhsCalender = new GregorianCalendar();
                        String lhsString = lhs.getDate_time();
                        Date lhsDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(lhsString);
                        lhsCalender.setTime(lhsDate);

                        Calendar rhsCalender = new GregorianCalendar();
                        String mystring = rhs.getDate_time();
                        Date rhsDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(mystring);
                        rhsCalender.setTime(rhsDate);

                        if (lhsCalender.after(rhsDate)) {
                            Log.e("LHS",""+lhsCalender.toString());
                            Log.e("RHS",""+rhsCalender.toString());
                            return -1;
                        } else {
                            Log.e("LHS",""+lhsCalender.toString());
                            Log.e("RHS",""+rhsCalender.toString());
                            return 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });*/

            /*for (int i = 0; i < secondaryList.size(); i++) {
                Log.e("Unsorted list", "" + secondaryList.get(i).getDate_time());
            }*/

            /*for (int j = 0; j < secondaryList.size(); j++) {
                for (int k = j + 1; k < secondaryList.size(); k++) {
                    try {
                        Calendar lhsCalender = new GregorianCalendar();
                        String lhsString = secondaryList.get(j).getDate_time();
                        Date lhsDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(lhsString);
                        lhsCalender.setTime(lhsDate);

                        Calendar rhsCalender = new GregorianCalendar();
                        String mystring = secondaryList.get(k).getDate_time();
                        Date rhsDate = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(mystring);
                        rhsCalender.setTime(rhsDate);

//                        Log.e("lhs Calender", "" + lhsCalender.getTime().toString());
//                        Log.e("rhs Calender", "" + rhsCalender.getTime().toString());

                        if (rhsCalender.after(lhsCalender)) {
                            Log.e("sorted", "asg");
                            IncidentModel temp = secondaryList.get(k);
                            secondaryList.set(k, secondaryList.get(j));
                            secondaryList.set(j, temp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exception thrown",""+e.getMessage());
                    }
                }
            }*/

            Collections.sort(secondaryList, new Comparator<IncidentModel>() {
                @Override
                public int compare(IncidentModel lhs, IncidentModel rhs) {
                    try {
                        Calendar lhsCalender = new GregorianCalendar();
                        String lhsString = lhs.getDate_time();//secondaryList.get(j).getDate_time();
                        Date lhsDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(lhsString);
                        lhsCalender.setTime(lhsDate);

                        Calendar rhsCalender = new GregorianCalendar();
                        String mystring = rhs.getDate_time();//secondaryList.get(k).getDate_time();
                        Date rhsDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(mystring);
                        rhsCalender.setTime(rhsDate);

//                        Log.e("RHS", "" + rhsCalender.toString());

                        if (rhsCalender.after(lhsCalender))
                            return 1;
                        if (lhsCalender.after(rhsCalender))
                            return -1;
                        else return 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });

            /*for (int i = 0; i < secondaryList.size(); i++) {
                Log.e("Sorted list", "" + secondaryList.get(i).getDate_time());
            }*/


            mAdapter = new MyRecyclerViewAdapter(secondaryList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(IncidentModel item) {
//                    Log.e("on item click", "" + item.getIncident_id());
//                    Log.e("on item click", "" + item.getAddress_of_incident());
//                    Log.e("on item click", "" + item.getDate_time());

                    Intent intent = new Intent(DayIncidentActivity.this, DayIncidentDetailActivity.class);
                    globalVariable.selectedIncidentModel = item;
                    startActivity(intent);
                }
            });
            mAdapter.notifyDataSetChanged();
        }
    }
}