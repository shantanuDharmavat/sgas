package shantanu.testmap;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Util.GlobalVariable;
import Util.SharedPrefrence;
import Util.WebServiceClass;

import static Util.EncodeHelper.md5;

/**
 * Created by Shantanu on 16-12-2016.
 */

public class FireBaseIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    static String cipherText;
    String url = WebServiceClass.tokenRegisterUrl;
    GlobalVariable globalVariable = GlobalVariable.getInstance();


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        new MakeServiceAllTask().execute(token);
    }

    private class MakeServiceAllTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return TokenHandshake(url,
                        md5(globalVariable.IMEI)
                        , params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response ", "" + s);
        }
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefrence.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    public String TokenHandshake(String url, String IMEI, String token) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();//AndroidHttpClient.newInstance("Android");
        HttpPost httppost = new HttpPost(url);
        String object = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobileimeino", IMEI));
            nameValuePairs.add(new BasicNameValuePair("token", token));
            nameValuePairs.add(new BasicNameValuePair("controller", "register"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            object = EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    /*public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            Log.e("IMEI PlainText : ", "" + s);
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return encodeMD5(hexString.toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String encodeMD5(String plainText) {
        cipherText = "";
        Random random = new Random();
        int randomGenerated;
        Log.e("IMEI MD5", "" + plainText);
        plainText = plainText.trim();
        String[] plainTextArray = plainText.split("");
//        plainTextArray[0].
        List<String> plainTextList = new ArrayList<String>(Arrays.asList(plainTextArray));

        randomGenerated = random.nextInt(1);
        if (randomGenerated == 0) {
            Log.e("RANDOM GENERATED", "" + randomGenerated);
            for (int i = 0; i < plainText.length(); i++) {
                if (plainTextList.get(i).equals(""))
                    plainTextList.remove(i);
                cipherText = cipherText.concat(plainTextList.get(i).concat(String.valueOf(random.nextInt(9))));
            }
            cipherText = cipherText.concat("0");
        }
        else {
            Log.e("RANDOM GENERATED", "" + randomGenerated);
            for (int i = 0; i < plainText.length(); i++) {
                if (plainTextList.get(i).equals(""))
                    plainTextList.remove(i);
                cipherText = cipherText.concat(random.nextInt(9) + plainTextList.get(i));
            }
            cipherText = cipherText.concat("1");
        }
//        cipherText = cipherText.concat("0");
        Log.e("CIPHERTEXT", "" + cipherText);
        return cipherText;
    }*/
}
