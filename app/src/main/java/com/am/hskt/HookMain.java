package com.am.hskt;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Observer;
import java.util.UUID;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ly on 2018/4/28.
 */

public class HookMain implements IXposedHookLoadPackage {

    private static final String TAG = HookMain.class.getSimpleName();

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        Log.e(TAG, "handleLoadPackage  " + loadPackageParam.packageName);

        if ("com.am.hskt".equals(loadPackageParam.packageName))
            XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Logger.addLogAdapter(new AndroidLogAdapter());
                    Logger.addLogAdapter(new DiskLogAdapter());
                    Context app = (Application) param.thisObject;
                    hookKG(loadPackageParam, app);
                }
            });

    }


    private void hookKG(XC_LoadPackage.LoadPackageParam param, final Context context) throws ClassNotFoundException {

        findAndHookConstructor(DexClassLoader.class, String.class, String.class, String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.w(TAG, param.args[0] + " |  " + param.args[1] + "|" + param.args[2]);
                File dir = new File(Environment.getExternalStorageDirectory(), "/myDex/" + formatDate(System.currentTimeMillis()));
                if (!dir.exists() || !dir.isDirectory())
                    dir.mkdirs();

                String name = (String) param.args[0];
                String dexname = name.substring(name.lastIndexOf("/") + 1);
                File destFile = new File(dir, dexname);
                writeToDes(new File((String) param.args[0]), destFile);

            }
        });

//        findAndHookMethod("java.util.UUID", param.classLoader, "randomUUID", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
////                Logger.e(TAG,"beforeHookedMethod "  + param.getResult());
//            }
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                Logger.e(TAG, "afterHookedMethod " + param.getResult());
//
//                try {
//                    Constructor<?> constructor = Class.forName("java.util.UUID").getConstructor(Byte[].class);
//                    constructor.setAccessible(true);
//                    Object object = constructor.newInstance("aufjsofjfnshfujf".getBytes());
//                    param.setResult(object);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

        findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String replaceDeviceId = getFileString("deviceId");
                Logger.e(TAG, "afterHookedMethod  deviceId ->>> " + replaceDeviceId);
                if (replaceDeviceId != null) {
                    param.setResult(replaceDeviceId);
                }

            }
        });


    }


    public static String getFileString(String string) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            File dstFile = new File(dir, "infos.txt");

            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dstFile)));
            String append;
            while ((append = br.readLine()) != null) {
                buffer.append(append);
            }


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

    private static void writeToDes(File from, File to) {
        try {

            FileInputStream fis = new FileInputStream(from);
            FileOutputStream fos = new FileOutputStream(to);
            byte[] data = new byte[1024];
            while (fis.read(data) != -1) {
                fos.write(data, 0, data.length);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String formatDate(long nowTime) {
        String format = "yyyy-MM-dd";
        return new SimpleDateFormat(format).format(nowTime);

    }


}
