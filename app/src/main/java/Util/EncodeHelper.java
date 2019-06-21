package Util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Shantanu on 11-01-2017.
 */

public class EncodeHelper {
    static String cipherText;

    public static final String md5(final String s) {
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

        randomGenerated = random.nextInt(2);
        if (randomGenerated == 0) {
            Log.e("RANDOM GENERATED", "" + randomGenerated);
            for (int i = 0; i < plainText.length(); i++) {
                if (plainTextList.get(i).equals(""))
                    plainTextList.remove(i);
                cipherText = cipherText.concat(plainTextList.get(i).concat(String.valueOf(random.nextInt(9))));
//                Log.e("GENERATION even",""+cipherText);
            }
            cipherText = cipherText.concat("0");
        }
        else {
            Log.e("RANDOM GENERATED", "" + randomGenerated);
            for (int i = 0; i < plainText.length(); i++) {
                if (plainTextList.get(i).equals(""))
                    plainTextList.remove(i);
                cipherText = cipherText.concat(random.nextInt(9) + plainTextList.get(i));
//                Log.e("GENERATION odd",""+cipherText);
            }
            cipherText = cipherText.concat("1");
        }
//        cipherText = cipherText.concat("0");
        Log.e("CIPHERTEXT", "" + cipherText);
        return cipherText;
    }
}
