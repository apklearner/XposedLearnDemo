package com.am.hskt.hooks;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import com.am.hskt.FieldEnums;
import com.am.hskt.FileUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class HookLocation {

    public static void hook(Context context, final String packageName, ClassLoader classLoader) {

        XposedHelpers.findAndHookMethod("android.location.Location", classLoader, "getLatitude", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                String latStr = FileUtils.getFileString(packageName, FieldEnums.lat.value);
//                String lngStr = FileUtils.getFileString(packageName,FieldEnums.lng.value);
                if (!TextUtils.isEmpty(latStr)) {
                    double latDou = Double.parseDouble(latStr);
                    param.setResult(latDou);
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.location.Location", classLoader, "getLongitude", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String lngStr = FileUtils.getFileString(packageName, FieldEnums.lng.value);
                if (!TextUtils.isEmpty(lngStr)) {
                    double lngDou = Double.parseDouble(lngStr);
                    param.setResult(lngDou);
                }
            }
        });


        XposedHelpers.findAndHookMethod("android.location.LocationManager", classLoader, "getLastKnownLocation", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Location l = new Location(LocationManager.GPS_PROVIDER);
                l.setAccuracy(100f);
                l.setTime(0);
                param.setResult(l);
            }
        });


//        XposedHelpers.findAndHookMethod("android.location.LocationManager", classLoader, "getLastLocation", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Location l = new Location(LocationManager.GPS_PROVIDER);
//                l.setAccuracy(100f);
//                l.setTime(0);
//                param.setResult(l);
//            }
//        });


        String hookGaode = FileUtils.getFileString(packageName, FieldEnums.hookGaoDeMap.value);

        if (!TextUtils.isEmpty(hookGaode)) {

            XposedHelpers.findAndHookMethod("com.amap.api.location.AMapLocation", classLoader, "getLatitude", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String latStr = FileUtils.getFileString(packageName, FieldEnums.lat.value);
//                String lngStr = FileUtils.getFileString(packageName,FieldEnums.lng.value);
                    if (!TextUtils.isEmpty(latStr)) {
                        double latDou = Double.parseDouble(latStr);
                        param.setResult(latDou);
                    }
                }
            });

            XposedHelpers.findAndHookMethod("com.amap.api.location.AMapLocation", classLoader, "getLongitude", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String lngStr = FileUtils.getFileString(packageName, FieldEnums.lng.value);
                    if (!TextUtils.isEmpty(lngStr)) {
                        double lngDou = Double.parseDouble(lngStr);
                        param.setResult(lngDou);
                    }
                }
            });
        }

//        XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", classLoader, "isWifiEnabled", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                param.setResult(false);
//            }
//        });
//
//        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getBSSID", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                param.setResult("00-00-00-00-00-00-00-00");
//            }
//        });
//        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo", classLoader, "getMacAddress", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                param.setResult("00-00-00-00-00-00-00-00");
//            }
//        });

    }

}
