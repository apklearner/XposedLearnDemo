package com.am.hskt.hooks;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.am.hskt.FieldEnums;
import com.am.hskt.FileUtils;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class HookNet {
    public static void hook(Context context, final String packageName, ClassLoader classLoader) {

        XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", classLoader, "isWifiEnabled", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String wifiEnableStr = FileUtils.getFileString(packageName, FieldEnums.wifiEnable.value);
                if (!TextUtils.isEmpty(wifiEnableStr)) {
                    param.setResult(Boolean.parseBoolean(wifiEnableStr));
                }
            }
        });


//        XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", classLoader, "isWifiEnabled", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String wifiEnableStr = FileUtils.getFileString(packageName, FieldEnums.wifiEnable.value);
//                if (!TextUtils.isEmpty(wifiEnableStr)) {
//                    param.setResult(Boolean.parseBoolean(wifiEnableStr));
//                }
//            }
//        });


        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getBSSID", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String bssid = FileUtils.getFileString(packageName, FieldEnums.bssid.value);
                if (!TextUtils.isEmpty(bssid)) {
                    param.setResult(bssid);
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getMacAddress", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String mac = FileUtils.getFileString(packageName, FieldEnums.mac.value);
                if (!TextUtils.isEmpty(mac)) {
                    param.setResult(mac);
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getSSID", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String ssid = FileUtils.getFileString(packageName, FieldEnums.ssid.value);
                if (!TextUtils.isEmpty(ssid)) {
                    param.setResult(ssid);
                }
            }
        });


        XposedHelpers.findAndHookMethod("android.net.NetworkInfo", classLoader, "getType", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String netType = FileUtils.getFileString(packageName, FieldEnums.netType.value);
                if (!TextUtils.isEmpty(netType)) {
                    param.setResult(Integer.parseInt(netType));
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.net.NetworkInfo", classLoader, "getSubtype", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String netSubType = FileUtils.getFileString(packageName, FieldEnums.netSubType.value);
                if (!TextUtils.isEmpty(netSubType)) {
                    param.setResult(Integer.parseInt(netSubType));
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getIpAddress", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String ipAdress = FileUtils.getFileString(packageName, FieldEnums.ipAdress.value);
                if (!TextUtils.isEmpty(ipAdress)) {
                    param.setResult(Integer.parseInt(ipAdress));
                }
            }
        });


        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getNetworkType", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String telNetType = FileUtils.getFileString(packageName, FieldEnums.telNetType.value);
                if (!TextUtils.isEmpty(telNetType)) param.setResult(telNetType);
            }
        });




//        TelephonyManager     telephonyManager;
//        telephonyManager.getNetworkType()

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


    }
}
