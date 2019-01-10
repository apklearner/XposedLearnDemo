package com.am.hskt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class FileUtils {

    public static boolean writeFile(String pkgName, String string) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            if (!dir.exists() || !dir.isDirectory())
                dir.mkdirs();
            File dstFile = new File(dir, pkgName + ".txt");
            if (!dstFile.exists()) dstFile.createNewFile();


            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dstFile)));
            br.write(string);
            br.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

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

}
