package com.am.hskt;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class FileUtils {

    public static boolean writeFile(String pkgName, String string, boolean append) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            if (!dir.exists() || !dir.isDirectory())
                dir.mkdirs();
            File dstFile = new File(dir, pkgName + ".txt");
            if (!dstFile.exists()) dstFile.createNewFile();


            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dstFile, append)));
            br.write(string);
            br.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public static boolean writeFile(String pkgName, String string) {
        return writeFile(pkgName, string, false);

    }


    private static String formatDate(long nowTime) {
        String format = "yyyy-MM-dd";
        return new SimpleDateFormat(format).format(nowTime);
    }

    public static boolean putSpData(Context context, String pkgName, String data) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(pkgName, Context.MODE_PRIVATE);
            preferences.edit().putBoolean("ok", true)
                    .putString("infos", data).commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getFileString(String pkgName, String string) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            File dstFile = new File(dir, pkgName + ".txt");

            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dstFile)));
            String append;
            while ((append = br.readLine()) != null) {
                buffer.append(append);
            }

//            Log.e("1234", "getFileString infos  " + buffer.toString());

            String[] results = buffer.toString().split("[|]");
            if (results != null && results.length > 0) {
                for (String item : results) {
                    if (!TextUtils.isEmpty(item) && item.contains(":")) {
                        String[] childItem = item.split(":");
                        String key = childItem[0];
                        String value = childItem[1];

                        if (string.equals(key)) {
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkHook(String pkgName) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            File dstFile = new File(dir, pkgName + ".txt");
            return dstFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String createConfigs(HashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (String key : map.keySet()) {
                String value = map.get(key);
                if (!TextUtils.isEmpty(value)) {
                    buffer.append(key).append(":").append(value).append("|");
                }
            }
            return buffer.toString();
        }
        return null;
    }


    public static void addDefaultConfig() {
        HashMap<String, String> map = new HashMap<>();
//        map.put(FieldEnums.lat.value, "32.890124");
//        map.put(FieldEnums.lng.value, "115.814205");

        map.put(FieldEnums.lat.value, "39.92");
        map.put(FieldEnums.lng.value, "116.52");

        map.put(FieldEnums.deviceId.value,"");

        map.put(FieldEnums.android_id.value, "9c33128ba7cd8644");
        map.put(FieldEnums.model.value, "VKY-AL00");
        map.put(FieldEnums.manufacture.value, "HUAWEI");
//        map.put(FieldEnums.brand.value, "HUAWEI");
//        map.put(FieldEnums.sdk_int.value, "24");
//        map.put(FieldEnums.sdk_release.value, "6.0");
//        map.put(FieldEnums.width.value, "1080");
//        map.put(FieldEnums.height.value, "1800");
//        map.put(FieldEnums.density.value, "3");
        map.put(FieldEnums.board.value, "VKY");
        map.put(FieldEnums.cpu_abi.value, "arm64-v8a");
        map.put(FieldEnums.product.value, "VKY-AL00");
        map.put(FieldEnums.wifiEnable.value, "false");
//        map.put(FieldEnums.lineNumber.value,"");
//        map.put(FieldEnums.simserialNumber.value,"");

//        map.put(FieldEnums.netType.value, ConnectivityManager.TYPE_MOBILE_MMS + "");
//        map.put(FieldEnums.telNetType.value, TelephonyManager.NETWORK_TYPE_LTE + "");
//        map.put(FieldEnums.netSubType.value, TelephonyManager.NETWORK_TYPE_LTE + "");
        map.put(FieldEnums.netOperator.value, "460001");
        map.put(FieldEnums.simOperator.value, "460001");
        map.put(FieldEnums.simState.value, TelephonyManager.SIM_STATE_READY + "");
        map.put(FieldEnums.mac.value, "00-00-00-00-00-00-00-00");
        map.put(FieldEnums.bssid.value, "00-00-00-00-00-00-00-00");
        map.put(FieldEnums.ssid.value, "wifi_abc");
//        map.put(FieldEnums.ipAdress.value,"123456789");

        writeFile("com.am.hskt", createConfigs(map));

        map.put(FieldEnums.hookGaoDeMap.value, "true");
        writeFile("com.alibaba.android.rimet", createConfigs(map));


    }

}
