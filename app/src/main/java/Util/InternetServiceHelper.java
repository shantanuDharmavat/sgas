package Util;

import java.net.InetAddress;

/**
 * Created by Shantanu on 22-02-2017.
 */

public class InternetServiceHelper {
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
