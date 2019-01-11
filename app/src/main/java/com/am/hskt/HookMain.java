package com.am.hskt;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ly on 2018/4/28.
 */

public class HookMain implements IXposedHookLoadPackage {

    private static final String TAG = HookMain.class.getSimpleName();

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        Log.e(TAG, "handleLoadPackage  " + loadPackageParam.packageName + "    " + checkHook(loadPackageParam.packageName));

        if (checkHook(loadPackageParam.packageName))
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


    private void hookKG(final XC_LoadPackage.LoadPackageParam loadPackageParam, final Context context) {

//        findAndHookConstructor(DexClassLoader.class, String.class, String.class, String.class, ClassLoader.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                Log.w(TAG, param.args[0] + " |  " + param.args[1] + "|" + param.args[2]);
//                File dir = new File(Environment.getExternalStorageDirectory(), "/myDex/" + formatDate(System.currentTimeMillis()));
//                if (!dir.exists() || !dir.isDirectory())
//                    dir.mkdirs();
//
//                String name = (String) param.args[0];
//                String dexname = name.substring(name.lastIndexOf("/") + 1);
//                File destFile = new File(dir, dexname);
//                writeToDes(new File((String) param.args[0]), destFile);
//
//            }
//        });

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

        findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String replaceDeviceId = getFileString(loadPackageParam.packageName, "deviceId");
                Logger.e(TAG, "afterHookedMethod  deviceId ->>> " + replaceDeviceId);
                if (replaceDeviceId != null) {
                    param.setResult(replaceDeviceId);
                }

            }
        });


//        findAndHookMethod("android.os.Build", param.classLoader, "getString", String.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//            }
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                Log.e("1234", "afterHookedMethod Build " + param.args[0]);
//                Log.e("1234", "afterHookedMethod Build " + param.args[1]);
//
//
//            }
//        });

        /**
         * MODEL
         * MANUFACTURER
         * BRAND
         */
        findAndHookMethod("android.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

//                Log.e("1234", "beforeHookedMethod  Build ");

                String model = getFileString(loadPackageParam.packageName, "model");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "MODEL", model == null ? Build.MODEL : model);

                String manufacture = getFileString(loadPackageParam.packageName, "manufacture");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "MANUFACTURER", manufacture == null ? Build.MANUFACTURER : manufacture);

                String brand = getFileString(loadPackageParam.packageName, "brand");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "BRAND", brand == null ? Build.BRAND : brand);

                String sdkIntString = getFileString(loadPackageParam.packageName, "sdk_int");
                int sdkInt = sdkIntString == null ? Build.VERSION.SDK_INT : Integer.parseInt(sdkIntString);
                XposedHelpers.setStaticObjectField(android.os.Build.VERSION.class, "SDK_INT", sdkInt > 0 ? sdkInt : Build.VERSION.SDK_INT);

                String sdkReleaseStr = getFileString(loadPackageParam.packageName, "sdk_release");
                XposedHelpers.setStaticObjectField(android.os.Build.VERSION.class, "RELEASE", sdkReleaseStr == null ? Build.VERSION.RELEASE : sdkReleaseStr);

                //board
                String boardStr = getFileString(loadPackageParam.packageName, "board");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "BOARD", boardStr == null ? Build.BOARD : boardStr);

                //cpu_abi
                String cpu_abiStr = getFileString(loadPackageParam.packageName, "cpu_abi");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "CPU_ABI", cpu_abiStr == null ? Build.CPU_ABI : cpu_abiStr);

                //product
                String productStr = getFileString(loadPackageParam.packageName, "product");
                XposedHelpers.setStaticObjectField(android.os.Build.class, "PRODUCT", productStr == null ? Build.PRODUCT : productStr);

                //TODO
                String witdhStr = getFileString(loadPackageParam.packageName, "width");
                String heightStr = getFileString(loadPackageParam.packageName, "height");
                String densityStr = getFileString(loadPackageParam.packageName, "density");

                int width = witdhStr == null ? context.getResources().getDisplayMetrics().widthPixels : Integer.parseInt(witdhStr);
                int height = heightStr == null ? context.getResources().getDisplayMetrics().heightPixels : Integer.parseInt(heightStr);
                float density = densityStr == null ? context.getResources().getDisplayMetrics().density : Float.parseFloat(densityStr);

                XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "widthPixels", width);
                XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "heightPixels", height);
                XposedHelpers.setFloatField(context.getResources().getDisplayMetrics(), "density", density);

            }

        });

        /**
         * android_id
         */
        findAndHookMethod("android.provider.Settings.Secure", loadPackageParam.classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                Log.e("1234", "afterHookedMethod  Secure ");

                String params2 = (String) param.args[1];
                if ("android_id".equals(params2)) {
                    String androidId = getFileString(loadPackageParam.packageName, "android_id");
                    param.setResult(androidId == null ? DeviceUtils.getAndroidId(context) : androidId);
                }
            }
        });


        /***
         *  subscriberId
         */
        findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String subscriberId = getFileString(loadPackageParam.packageName, "subscriberId");
                if (!TextUtils.isEmpty(subscriberId)) {
                    param.setResult(subscriberId);
                }
            }
        });


        findAndHookMethod("android.webkit.WebSettings", loadPackageParam.classLoader, "getUserAgentString", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String userAgent = getFileString(loadPackageParam.packageName, "userAgent");
                if (!TextUtils.isEmpty(userAgent)) {
                    param.setResult(userAgent);
                }
            }
        });


//        /***
//         *
//         */
//        findAndHookMethod("android.content.res.ResourcesImpl", loadPackageParam.classLoader, "getDisplayMetrics", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//                String witdhStr = getFileString(loadPackageParam.packageName, "width");
//                String heightStr = getFileString(loadPackageParam.packageName, "height");
//                int width = witdhStr == null ? sw : Integer.parseInt(witdhStr);
//                int height = heightStr == null ? sh : Integer.parseInt(heightStr);
////
//                XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "widthPixels", width);
//                XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "heightPixels", height);
//
//
//            }
//        });

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


    private static boolean checkHook(String pkgName) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
            File dstFile = new File(dir, pkgName + ".txt");
            return dstFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    private static String getKeyString(Context context, String pkgName, String checkKey) {
//        try {
//            SharedPreferences preferences = context.getSharedPreferences(pkgName, Context.MODE_PRIVATE);
//            boolean isExists = preferences.getBoolean("ok", false);
//            Log.e("1234", "getKeyString exists " + isExists);
//            if (isExists) {
//                String info = preferences.getString("infos", null);
//                Log.e("1234", "getKeyString infos  " + info);
//                String[] results = info.split("[|]");
//                if (results != null && results.length > 0) {
//                    for (String item : results) {
//                        if (!TextUtils.isEmpty(item) && item.contains(":")) {
//                            String[] childItem = item.split(":");
//                            String key = childItem[0];
//                            String value = childItem[1];
//                            if (checkKey.equals(key)) {
//                                return value;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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
