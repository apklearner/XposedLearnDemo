package com.am.hskt;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.am.hskt.hooks.HookDevice;
import com.am.hskt.hooks.HookLocation;
import com.am.hskt.hooks.HookNet;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
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

        HookDevice.hook(context, loadPackageParam.packageName, loadPackageParam.classLoader);
        HookLocation.hook(context, loadPackageParam.packageName, loadPackageParam.classLoader);
        HookNet.hook(context, loadPackageParam.packageName, loadPackageParam.classLoader);

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
//
        //隐藏xposed类
//        findAndHookMethod("java.lang.Class", loadPackageParam.classLoader, "forName", String.class, boolean.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                String className = (String) param.args[0];
//                if (className != null && (className.equals("de.robv.android.xposed.XposedBridge") || className.equals("de.robv.android.xposed.XC_MethodReplacement"))) {
//                    param.setThrowable(new ClassNotFoundException());
//                }
//            }
//        });


        findAndHookMethod("java.io.File", loadPackageParam.classLoader, "exists", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                File file = (File) param.thisObject;
                String path = file.getPath();
                if (!TextUtils.isEmpty(path)) {
                    if (path.contains("system/bin/su") || path.contains("system/xbin/su") || path.contains("system/app/Superuser.apk")) {
                        String root = getFileString(loadPackageParam.packageName, FieldEnums.root.value);
                        if (!TextUtils.isEmpty(root))
                            param.setResult(false);

                    }
                }
            }
        });


//        findAndHookMethod("java.lang.Runtime", loadPackageParam.classLoader, "exec", String[].class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                try {
//                    String[] value = (String[]) param.args[0];
//                    if (value.length == 2) {
//                        if (value[0].contains("system/xbin/which") && value[1].contains("su")) {
//                            String root = getFileString(loadPackageParam.packageName, "root");
//                            if (!TextUtils.isEmpty(root))
//                                param.setResult(null);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


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


    public static void setStaticObjectField(Class mClass, String fieldName, Object replaceValue, Object defaultValue) {
        if (replaceValue instanceof String) {
            XposedHelpers.setStaticObjectField(mClass, fieldName, replaceValue == null ? defaultValue : replaceValue);
        } else if (replaceValue instanceof Integer) {
            if (defaultValue instanceof String) {
                XposedHelpers.setStaticObjectField(mClass, fieldName, (int) replaceValue > 0 ? (int) replaceValue + "" : defaultValue);
            } else {
                XposedHelpers.setStaticObjectField(mClass, fieldName, (int) replaceValue > 0 ? (int) replaceValue : (int) defaultValue);
            }
        } else if (replaceValue instanceof Long) {
            XposedHelpers.setStaticObjectField(mClass, fieldName, (long) replaceValue > 0 ? (long) replaceValue : (long) defaultValue);

        }

    }

    public static String getFileString(String pkgName, String string) {
//        try {
//            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
//            File dstFile = new File(dir, pkgName + ".txt");
//
//            StringBuffer buffer = new StringBuffer();
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dstFile)));
//            String append;
//            while ((append = br.readLine()) != null) {
//                buffer.append(append);
//            }
//
////            Log.e("1234", "getFileString infos  " + buffer.toString());
//
//            String[] results = buffer.toString().split("[|]");
//            if (results != null && results.length > 0) {
//                for (String item : results) {
//                    if (!TextUtils.isEmpty(item) && item.contains(":")) {
//                        String[] childItem = item.split(":");
//                        String key = childItem[0];
//                        String value = childItem[1];
//
//                        if (string.equals(key)) {
//                            return value;
//                        }
//                    }
//                }
//
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return FileUtils.getFileString(pkgName, string);

    }


    private static boolean checkHook(String pkgName) {
//        try {
//            File dir = new File(Environment.getExternalStorageDirectory(), "/myFile/" + formatDate(System.currentTimeMillis()));
//            File dstFile = new File(dir, pkgName + ".txt");
//            return dstFile.exists();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
        return FileUtils.checkHook(pkgName);
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

//    private static void writeToDes(File from, File to) {
//        try {
//
//            FileInputStream fis = new FileInputStream(from);
//            FileOutputStream fos = new FileOutputStream(to);
//            byte[] data = new byte[1024];
//            while (fis.read(data) != -1) {
//                fos.write(data, 0, data.length);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private static String formatDate(long nowTime) {
//        String format = "yyyy-MM-dd";
//        return new SimpleDateFormat(format).format(nowTime);
//
//    }


}
