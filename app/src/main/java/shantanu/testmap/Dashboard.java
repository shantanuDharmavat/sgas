package shantanu.testmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Random;

import Util.GlobalVariable;

public class Dashboard extends Activity implements View.OnClickListener {
    Button buttonMap, buttonDayIncident,ButtonAllIncident;
    private static Boolean showSplash = true;
    Intent intent;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    static String cipherText = "";
    private static String TAG = "DASHBOARD";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        Random random = new Random();
        Log.e("Random = ",""+random.nextInt(2));
        Log.e("TOKEN",""+ Util.SharedPrefrence.getInstance(this).getDeviceToken());
        globalVariable.telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        /*int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.READ_PHONE_STATE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)){

        }*/

        globalVariable.IMEI = globalVariable.telephonyManager.getDeviceId();
        filePathCreator();
        init();
        MultiDex.install(this);
        String app = BuildConfig.APPLICATION_ID;
        Log.e("package name", "name " + app);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init() {
        buttonMap = (Button) findViewById(R.id.buttonMap);
        buttonDayIncident = (Button) findViewById(R.id.buttonIncidentDay);
        ButtonAllIncident = (Button) findViewById(R.id.buttonAllIncident);

        setClickListeners();
    }

    private void setClickListeners() {
        buttonMap.setOnClickListener(this);
        buttonDayIncident.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonIncidentDay:
                Log.e(TAG,"OnClick DayIncident");
                intent = new Intent(Dashboard.this, DayIncidentActivity.class);
                break;
            case R.id.buttonMap:
                Log.e(TAG,"OnClick Map");
                intent = new Intent(Dashboard.this, Test1.class);
                break;
            case R.id.buttonIncidentAll:
                Log.e(TAG,"OnClick allIncident");
                intent = new Intent(Dashboard.this,AllIncidentActivity.class);
                break;
        }
        startActivity(intent);
    }

    private void filePathCreator(){
        try {

            String fileString = "/Gas/Incident_img";
            File rootFile = new File(Environment.getExternalStorageDirectory(), fileString);
            if (!rootFile.exists()) {
                Log.e("file test",""+rootFile.mkdirs());
            }

            Log.e("file Exists",""+rootFile.exists());
            Log.e("file is file",""+rootFile.isFile());

            Log.e("image file ", ""+rootFile.toString());

            globalVariable.setImageFile(rootFile);
        }catch (Exception e){e.printStackTrace();}
    }
}