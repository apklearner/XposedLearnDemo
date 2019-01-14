package com.am.hskt.hooks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.am.hskt.FieldEnums;
import com.am.hskt.FileUtils;
import com.am.hskt.HookMain;

import org.w3c.dom.Text;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;


public class HookDevice {

    public static void hook(final Context context, final String packageName, ClassLoader classLoader) {

        /**
         * MODEL
         * MANUFACTURER
         * BRAND
         */
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", classLoader, "get", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                hookBuild(context, packageName);
            }

        });


        /**
         * android_id
         */
        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", classLoader, "getString", ContentResolver.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                Log.e("1234", "afterHookedMethod  Secure ");

                String params2 = (String) param.args[1];
                if (FieldEnums.android_id.value.equals(params2)) {
                    String androidId = FileUtils.getFileString(packageName, FieldEnums.android_id.value);
                    if (!TextUtils.isEmpty(androidId))
                        param.setResult(androidId);
                }
            }
        });


        /***
         *  subscriberId
         */
        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String subscriberId = FileUtils.getFileString(packageName, FieldEnums.subscriberId.value);
                if (!TextUtils.isEmpty(subscriberId)) {
                    param.setResult(subscriberId);
                }
            }
        });

        /**
         * devideId
         */
        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String replaceDeviceId = FileUtils.getFileString(packageName, FieldEnums.deviceId.value);
//                Logger.e(TAG, "afterHookedMethod  deviceId ->>> " + replaceDeviceId);
                if (replaceDeviceId != null) {
                    param.setResult(replaceDeviceId);
                }
            }
        });


        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getNetworkOperator", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String netOperator = FileUtils.getFileString(packageName, FieldEnums.netOperator.value);
                if (!TextUtils.isEmpty(netOperator)) param.setResult(netOperator);
            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSimOperator", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String simOperator = FileUtils.getFileString(packageName, FieldEnums.simOperator.value);
                if (!TextUtils.isEmpty(simOperator)) param.setResult(simOperator);
            }
        });


        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSimOperator", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String simOperator = FileUtils.getFileString(packageName, FieldEnums.simOperator.value);
                if (!TextUtils.isEmpty(simOperator)) param.setResult(simOperator);
            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSimState", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String simState = FileUtils.getFileString(packageName, FieldEnums.simState.value);
                if (!TextUtils.isEmpty(simState)) param.setResult(Integer.parseInt(simState));
            }
        });


//        TelephonyManager telephonyManager;
//        telephonyManager.getLine1Number();  telephonyManager.getSimSerialNumber()
        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getLine1Number", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String lineNum = FileUtils.getFileString(packageName, FieldEnums.lineNumber.value);
                if (!TextUtils.isEmpty(lineNum)) param.setResult(lineNum);
            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getLine1Number", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String lineNum = FileUtils.getFileString(packageName, FieldEnums.lineNumber.value);
                if (!TextUtils.isEmpty(lineNum)) param.setResult(lineNum);
            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSimSerialNumber", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String simserNum = FileUtils.getFileString(packageName, FieldEnums.simserialNumber.value);
                if (!TextUtils.isEmpty(simserNum)) param.setResult(simserNum);
            }
        });

    }

    private static void hookBuild(Context context, String packageName) {

        //tag
//        String tags = FileUtils.getFileString(packageName, FieldEnums.tags.value);
//        HookMain.setStaticObjectField(android.os.Build.class, "TAGS", tags, Build.TAGS);

        //id
        String id = FileUtils.getFileString(packageName, FieldEnums.id.value);
        HookMain.setStaticObjectField(android.os.Build.class, "ID", id, Build.ID);

        //serial
        String serial = FileUtils.getFileString(packageName, FieldEnums.serial.value);
        HookMain.setStaticObjectField(android.os.Build.class, "SERIAL", serial, Build.SERIAL);

        //model
        String model = FileUtils.getFileString(packageName, FieldEnums.model.value);
        HookMain.setStaticObjectField(android.os.Build.class, "MODEL", model, Build.MODEL);

        //time
        String time = FileUtils.getFileString(packageName, FieldEnums.time.value);
        HookMain.setStaticObjectField(android.os.Build.class, "TIME", time, Build.TIME);

//        support_abi
//        String support_abi = FileUtils.getFileString(packageName,FieldEnums.support_abi.value);
//        HookMain.setStaticObjectField(android.os.Build.class, "TIME", support_abi, Build.SUPPORTED_ABIS);

        //type
        String type = FileUtils.getFileString(packageName, FieldEnums.type.value);
        HookMain.setStaticObjectField(android.os.Build.class, "TYPE", type, Build.TYPE);

        //hardware
        String hardware = FileUtils.getFileString(packageName, FieldEnums.hardward.value);
        HookMain.setStaticObjectField(android.os.Build.class, "HARDWARE", hardware, Build.HARDWARE);

        //bootloader
        String bootloader = FileUtils.getFileString(packageName,FieldEnums.bootloader.value);
        HookMain.setStaticObjectField(android.os.Build.class, "BOOTLOADER", bootloader, Build.BOOTLOADER);

        //fingerprint
        String fingerprint = FileUtils.getFileString(packageName,FieldEnums.fingerprint.value);
        HookMain.setStaticObjectField(android.os.Build.class, "FINGERPRINT", fingerprint, Build.FINGERPRINT);

        //user
        String user = FileUtils.getFileString(packageName,FieldEnums.user.value);
        HookMain.setStaticObjectField(android.os.Build.class, "USER", user, Build.USER);

        //host
        String host = FileUtils.getFileString(packageName,FieldEnums.host.value);
        HookMain.setStaticObjectField(android.os.Build.class, "HOST", host, Build.HOST);

        //device
        String device = FileUtils.getFileString(packageName,FieldEnums.device.value);
        HookMain.setStaticObjectField(android.os.Build.class, "DEVICE", device, Build.DEVICE);

        //display
        String display = FileUtils.getFileString(packageName,FieldEnums.display.value);
        HookMain.setStaticObjectField(android.os.Build.class, "DISPLAY", display, Build.DISPLAY);


        //manufacture
        String manufacture = FileUtils.getFileString(packageName, FieldEnums.manufacture.value);
        HookMain.setStaticObjectField(android.os.Build.class, "MANUFACTURER", manufacture, Build.MANUFACTURER);

        //brand
        String brand = FileUtils.getFileString(packageName, FieldEnums.brand.value);
        HookMain.setStaticObjectField(android.os.Build.class, "BRAND", brand, Build.BRAND);

        //sdk_int
        String sdkIntString = FileUtils.getFileString(packageName, FieldEnums.sdk_int.value);
        int sdkInt = sdkIntString == null ? Build.VERSION.SDK_INT : Integer.parseInt(sdkIntString);
        HookMain.setStaticObjectField(android.os.Build.VERSION.class, "SDK_INT", sdkInt, Build.VERSION.SDK_INT);
        HookMain.setStaticObjectField(android.os.Build.VERSION.class, "SDK", sdkInt, Build.VERSION.SDK);

        //sdk_release
        String sdkReleaseStr = FileUtils.getFileString(packageName, FieldEnums.sdk_release.value);
        HookMain.setStaticObjectField(android.os.Build.VERSION.class, "RELEASE", sdkReleaseStr, Build.VERSION.RELEASE);

        //board
        String boardStr = FileUtils.getFileString(packageName, FieldEnums.board.value);
        HookMain.setStaticObjectField(android.os.Build.class, "BOARD", boardStr, Build.BOARD);

        //cpu_abi
        String cpu_abiStr = FileUtils.getFileString(packageName, FieldEnums.cpu_abi.value);
        HookMain.setStaticObjectField(android.os.Build.class, "CPU_ABI", cpu_abiStr, Build.CPU_ABI);

        //cpu_abi_2
        String cpu_abiStr_2 = FileUtils.getFileString(packageName, FieldEnums.cpu_abi2.value);
        HookMain.setStaticObjectField(android.os.Build.class, "CPU_ABI2", cpu_abiStr_2, Build.CPU_ABI2);

        //product
        String productStr = FileUtils.getFileString(packageName, FieldEnums.product.value);
        HookMain.setStaticObjectField(android.os.Build.class, "PRODUCT", productStr, Build.PRODUCT);


        //TODO
        String witdhStr = FileUtils.getFileString(packageName, FieldEnums.width.value);
        String heightStr = FileUtils.getFileString(packageName, FieldEnums.height.value);
        String densityStr = FileUtils.getFileString(packageName, FieldEnums.density.value);

        int width = witdhStr == null ? context.getResources().getDisplayMetrics().widthPixels : Integer.parseInt(witdhStr);
        int height = heightStr == null ? context.getResources().getDisplayMetrics().heightPixels : Integer.parseInt(heightStr);
        float density = densityStr == null ? context.getResources().getDisplayMetrics().density : Float.parseFloat(densityStr);

        XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "widthPixels", width);
        XposedHelpers.setIntField(context.getResources().getDisplayMetrics(), "heightPixels", height);
        XposedHelpers.setFloatField(context.getResources().getDisplayMetrics(), "density", density);
    }

}
