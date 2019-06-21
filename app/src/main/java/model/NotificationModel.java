package model;

import java.util.ArrayList;

/**
 * Created by Abhijit on 29-12-2016.
 */

public class NotificationModel {
    String Address_of_incident, attribute_1, attribute_2, attribute_3,cause_of_incident,city_id,date_time,description_of_incident,latitude,longitude,message,obj_part_of_incident,pro_uid,report_by,sap_notification,title;

    public ArrayList<String> final_pop_up_key = new ArrayList<>();
    public ArrayList<String> final_pop_up_value = new ArrayList<>();

    public ArrayList<String> getFinal_pop_up_value() {
        return final_pop_up_value;
    }

    public void setFinal_pop_up_value(ArrayList<String> final_pop_up_value) {
        this.final_pop_up_value = final_pop_up_value;
    }

    public ArrayList<String> getFinal_pop_up_key() {
        return final_pop_up_key;
    }

    public void setFinal_pop_up_key(ArrayList<String> final_pop_up_key) {
        this.final_pop_up_key = final_pop_up_key;
    }

    public String getAddress_of_incident() {
        return Address_of_incident;
    }

    public void setAddress_of_incident(String address_of_incident) {
        Address_of_incident = address_of_incident;
    }

    public String getAttribute_1() {
        return attribute_1;
    }

    public void setAttribute_1(String attribute_1) {
        this.attribute_1 = attribute_1;
    }

    public String getAttribute_2() {
        return attribute_2;
    }

    public void setAttribute_2(String attribute_2) {
        this.attribute_2 = attribute_2;
    }

    public String getAttribute_3() {
        return attribute_3;
    }

    public void setAttribute_3(String attribute_3) {
        this.attribute_3 = attribute_3;
    }

    public String getCause_of_incident() {
        return cause_of_incident;
    }

    public void setCause_of_incident(String cause_of_incident) {
        this.cause_of_incident = cause_of_incident;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDescription_of_incident() {
        return description_of_incident;
    }

    public void setDescription_of_incident(String description_of_incident) {
        this.description_of_incident = description_of_incident;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObj_part_of_incident() {
        return obj_part_of_incident;
    }

    public void setObj_part_of_incident(String obj_part_of_incident) {
        this.obj_part_of_incident = obj_part_of_incident;
    }

    public String getPro_uid() {
        return pro_uid;
    }

    public void setPro_uid(String pro_uid) {
        this.pro_uid = pro_uid;
    }

    public String getReport_by() {
        return report_by;
    }

    public void setReport_by(String report_by) {
        this.report_by = report_by;
    }

    public String getSap_notification() {
        return sap_notification;
    }

    public void setSap_notification(String sap_notification) {
        this.sap_notification = sap_notification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
