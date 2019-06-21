package Util;

import android.location.Location;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.IncidentModel;
import model.NotificationModel;

/**
 * Created by Abhijit on 13-12-2016.
 */

public class GlobalVariable {
    private static GlobalVariable globalVariable = new GlobalVariable();
    public GlobalVariable()
    {
        // don't allow the class to be instantiated
    }

    public static GlobalVariable getInstance(){
        return globalVariable;
    }
    public static DataBaseHelper dbHelper;
    public static File imageFile;

    public static File getImageFile() {
        return imageFile;
    }

    public static void setImageFile(File imageFile) {
        GlobalVariable.imageFile = imageFile;
    }

    public TelephonyManager telephonyManager;
    public String IMEI;
    public IncidentModel selectedIncidentModel = new IncidentModel();

    public List<NotificationModel> notificationModelList = new ArrayList<>();
    public NotificationModel recievedNotification = new NotificationModel();

    public NotificationModel getRecievedNotification() {
        return recievedNotification;
    }

    public void setRecievedNotification(NotificationModel recievedNotification) {
        this.recievedNotification = recievedNotification;
    }

    public ArrayList<String> final_pop_up_key = new ArrayList<>();
    public ArrayList<String> final_pop_up_value = new ArrayList<>();
    public List<IncidentModel> incidentModelList = new ArrayList<>();
    //********************************************************************
    //Essential of the app must be set when app starts
    //*********************************************************************
    // build version of app
    public static String selected_heading="";
    public static String selected_sub_heading="";
    public static String selected_again_heading="";
    public static int BUILD_VERSION=0;
    // screen size
    public static float DP_HEIGHT ;
    public static float DP_WIDTH ;
    // user details
    public static int USER_ID=0;
    public static int USER_ROLE=0;
    public static int USER_STATE=0;
    public static String USER_NAME="";
    // date details
    public static String TODAYS_DATE=""; // in format yyy/mm/dd
    public static Integer TODAY_DATE=0;  // just date
    public static Integer TODAY_MONTH=0; // just month
    public static Integer TODAY_YEAR=0; // just year
    public static String APP_DIR_PATH="";

    // common geojson variables
    public static String str_arr_cat_name [];
    //

    public static String SELECTED_LANGUAGE="el"; //  http://www.w3schools.com/tags/ref_language_codes.asp
    public static Integer MAP_ID= 0;
    public static String  GEOJSON_FLOW_1_DIR_NAME ="geojson_flow_1";
    public static String  GEOJSON_FLOW_1_SELECTED_FILE_NAME= "";
    public static Integer GEOJSON_FLOW_1_SELECTED_ID= 0;
    public static String GEOJSON_FLOW_1_WEB_VIEW= "";

    public static String  GEOJSON_FLOW_2_DIR_NAME ="geojson_flow_2";
    public static String  GEOJSON_FLOW_2_SELECTED_FILE_NAME= "geojson_flow_2.geojson";
    public static Integer GEOJSON_FLOW_2_SELECTED_ID= 0;
    public static String GEOJSON_FLOW_2_SELECTED_CITY= "";

    // Default Lat Long values
    public static Double DEFAULTLAT = 23.59419433;
    public static Double DEFAULTLONG = 72.39440918;


    public static String  GEOJSON_FLOW_4_DIR_NAME ="geojson_flow_4";
    public static String  GEOJSON_FLOW_4_SELECTED_FILE_NAME= "geojson_flow_4.geojson";
    public static Integer GEOJSON_FLOW_4_SELECTED_ID= 0;

    public static String WEBVIEW_URL="";
    public static String WEBVIEW_PDF="http://drive.google.com/viewerng/viewer?embedded=true&url=";

    public static Integer getMonthFromDate(String date)
    {
        String temp[] = date.split("-");
        temp[0]=  temp[0].replaceAll("[^0-9]", "");
        temp[1]= temp[1].replaceAll("[^0-9]", "");
        temp[2]= temp[2].replaceAll("[^0-9]", "");
        Integer temp1 =Integer.valueOf(temp[1]);
        return  temp1;
    }
    public static Integer getYearFromDate(String date)
    {
        String temp[] = date.split("-");
        temp[0]=  temp[0].replaceAll("[^0-9]", "");
        temp[1]= temp[1].replaceAll("[^0-9]", "");
        temp[2]= temp[2].replaceAll("[^0-9]", "");
        Integer temp2 =Integer.valueOf(temp[0]);
        return  temp2;
    }
    // Layer list ===============================
    public static String[]  cp_lay1_key =   {"gps_voula","pe_gps_voula","oria_td_voula","oria_eniaiou_dhmou"};
    public static String[]  cp_lay1_value = {"gps_voula","pe_gps_voula","oria_td_voula","oria_eniaiou_dhmou"};
    public static boolean[] cp_lay1_bol =   {false        ,true         ,false         ,true           };

    public static String[]  cp_lay2_key =   {"sd_voula","xrhseis_voula", "zwnh_ymhttou","zwnh_aktwn_sarwnikou"};
    public static String[]  cp_lay2_value = {"sd_voula","xrhseis_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou"};
    public static boolean[] cp_lay2_bol =   {false     ,true           ,false          ,true          };

    public static String[]  sua_lay1_key =   {"sd_voula","xrhseis_voula", "zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static String[]  sua_lay1_value = {"sd_voula","xrhseis_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static boolean[] sua_lay1_bol =   {false     ,true           ,false          ,true                  ,true                  ,false     };

    public static String[]  sua_lay2_key =   {"aaa_voula","sao_voula", "zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static String[]  sua_lay2_value = {"aaa_voula","sao_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static boolean[] sua_lay2_bol =   {false     ,true           ,false          ,true                  ,true                  ,false     };

    public static String[]  sua_lay3_key =   {"aaa_voula","sao_voula", "zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static String[]  sua_lay3_value = {"aaa_voula","sao_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static boolean[] sua_lay3_bol =   {false     ,true           ,false          ,true                  ,true                  ,false     };



    public static String[]  vvv_lay1_key =   {"layer_pe_gps_voula","oria_td_voula", "oria_eniaiou_dhmou"};
    public static String[]  vvv_lay1_value = {"layer_pe_gps_voula","oria_td_voula", "oria_eniaiou_dhmou"};
    public static boolean[] vvv_lay1_bol =   {true                 ,true           ,true            };

//    public static String[]  vvv_lay2_key =   {"oikopeda_voula","artiothta_voula", "zwnh_ymhttou","kalypsi_voula", "sd_voula"};
//    public static String[]  vvv_lay2_value = {"oikopeda_voula","artiothta_voula", "zwnh_ymhttou","kalypsi_voula", "sd_voula"};
//    public static boolean[] vvv_lay2_bol =   {false,true ,false ,true,true};

    public static String[]  vvv_lay2_key =   {"oikopeda_voula","artiothta_voula", "zwnh_ymhttou","kalypsi_voula", "sd_voula", "xrhseis_voula","ypomnhma_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou"};
    public static String[]  vvv_lay2_value = {"oikopeda_voula","artiothta_voula", "zwnh_ymhttou","kalypsi_voula", "sd_voula","xrhseis_voula","ypomnhma_voula","zwnh_ymhttou" ,"zwnh_aktwn_sarwnikou"};
    public static boolean[] vvv_lay2_bol =   {false            ,true              ,false          ,true             ,true    ,false              ,false               ,false         , false };

    public static String[]  vvv_lay3_key =   {"kadoi_apor_voula","kadoi_anak_voula","agwgoi_lymatwn_voula","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static String[]  vvv_lay3_value = {"kadoi_apor_voula","kadoi_anak_voula","agwgoi_lymatwn_voula","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static boolean[] vvv_lay3_bol =   {false             ,true              ,false                 ,true                   ,true                  };

    public static String[]  vvv_lay4_key =   {"aaa_voula","sao_voula", "zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static String[]  vvv_lay4_value = {"aaa_voula","sao_voula","zwnh_ymhttou","zwnh_aktwn_sarwnikou","freatia_omvriwn_voula","agwgoi_omvriwn_voula"};
    public static boolean[] vvv_lay4_bol =   {false     ,true           ,false          ,true                  ,true                  ,false     };


    public static String[]  vvv_lay5_key =   {"aaa_voula","seo_voula", "sao_voula"};
    public static String[]  vvv_lay5_value = {"aaa_voula","seo_voula", "sao_voula"};
    public static boolean[] vvv_lay5_bol =   {false     ,true           ,false    };


    public static String[] selected_key= cp_lay1_key.clone();
    public static String[] selected_value=cp_lay1_value.clone();
    public static boolean[] set_visibility=cp_lay1_bol.clone();

    //layer list end ==============================

    //----------------------------------------------------------------------------------------------------
    //GPS starts here
    //----------------------------------------------------------------------------------------------------
    public static Boolean mRequestingLocationUpdates=false;

    //----------------------------------------------------------------------------------------------------
    //GPS ends here
    //----------------------------------------------------------------------------------------------------

    public Location mGlobalCurrentLocation;

    public Location getmGlobalCurrentLocation() {
        return mGlobalCurrentLocation;
    }

    public void setmGlobalCurrentLocation(Location mGlobalCurrentLocation) {
        this.mGlobalCurrentLocation = mGlobalCurrentLocation;
    }


}

