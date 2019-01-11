package com.hook.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;

public class DeviceUtils {

    /**
     * 获取手机 IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            if (PermissionUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return checkDeviceId(tm.getDeviceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机 IMSI
     *
     * @param context
     * @return
     */
    public static String getSubscriberId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取制造商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取安卓系统版本号
     *
     * @return
     */
    public static int getAndroidOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    private static String defaultUserAgent;

    public static String getDefaultUserAgent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                return WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            try {
                final Class<?> webSettingsClassicClass = Class.forName("android.webkit.WebSettingsClassic");
                final Class<?> webViewClassicClass = Class.forName("android.webkit.WebViewClassic");
                final Constructor<?> constructor = webSettingsClassicClass.getDeclaredConstructor(Context.class, webViewClassicClass);
                constructor.setAccessible(true);
                final Object object = constructor.newInstance(context, null);
                final Method method = webSettingsClassicClass.getMethod("getUserAgentString");
                return (String) method.invoke(object);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
                constructor.setAccessible(true);
                WebSettings settings = constructor.newInstance(context, null);
                return settings.getUserAgentString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            WebView webView = new WebView(context);
            WebSettings webSettings = webView.getSettings();
            return webSettings.getUserAgentString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.getProperty("http.agent");
    }

    /**
     * 获取系统默认UA
     *
     * @param context
     * @return
     */
    public static String getUserAgent(Context context) {
        if (defaultUserAgent == null) {
            synchronized (DeviceUtils.class) {
                if (defaultUserAgent == null) {
                    defaultUserAgent = getDefaultUserAgent(context);
                }
            }
        }
        return defaultUserAgent;
    }

    /**
     * 判断是否为wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getWifiMacAddress(Context context) {
        try {
            Context app = context.getApplicationContext();
            WifiManager wm = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
            if (wm.isWifiEnabled()) {
                WifiInfo info = wm.getConnectionInfo();
                return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getScreenWidth(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static String randomDeviceUUID(Context context) {
        UUID uuid;
        final String androidId = getAndroidId(context);
        try {
            if (androidId != null && !"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = getDeviceId(context);
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            uuid = UUID.randomUUID();
        }
        return uuid.toString();
    }

    public static String getIMEI(Context ctx) {
        return getDeviceId(ctx);
    }

    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float getScreenDensity(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static int getConnectionType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return 0; //not connected
            }
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;//wifi
            }
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int networkType = info.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        return 2;//2g
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        return 3;//3g
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        return 4;//4g
                    default:
                        return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getOperatorType(Context context) {
        String imsi = getSubscriberId(context);
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                //因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                //中国移动
                return 1;
            } else if (imsi.startsWith("46001")) {
                //中国联通
                return 3;
            } else if (imsi.startsWith("46003")) {
                //中国电信
                return 2;
            }
        }
        return 0;
    }

    public static int getDeviceType(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                return 1;//平板
            } else {
                return 2;//手机
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;//未知
    }

    public static String getSimSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String checkDeviceId(String deviceId) {
        if (!TextUtils.isEmpty(deviceId))
            return deviceId;
        try {
            return "35" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * ApiAd new Add
     */
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isTablet(Context context) {
        try {
            return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        } catch (Exception e) {
        }
        return false;
    }


    public static int getSceenType(Context ctx) {
        try {
            Configuration mConfiguration = ctx.getResources().getConfiguration();
            int ori = mConfiguration.orientation;
            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
                return 2;
            } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
                return 1;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public static String getIpAddress(Context context) {
        try {
            // 获取WiFi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 判断WiFi是否开启
            if (wifiManager.isWifiEnabled()) {
                // 已经开启了WiFi
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);
                return ip;
            } else {
                // 未开启WiFi
                return getIpAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String intToIp(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }


    /**
     * 获取本机IPv4地址
     *
     * @return 本机IPv4地址；null：无网络连接
     */
    private static String getIpAddress() {
        try {
            NetworkInterface networkInterface;
            InetAddress inetAddress;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 2G         2
     * 3G         3
     * 4G         4
     * 5G         5
     * WIFI       100
     * ETHERNET   101
     *
     * @return
     */
    private static int NET_TYPE_UNKNOWN = 0;
    private static int NET_CELL_UNKNOW = 1;
    private static int NET_TYPE_2G = 2;
    private static int NET_TYPE_3G = 3;
    private static int NET_TYPE_4G = 4;
    private static int NET_TYPE_5G = 5;
    private static int NET_TYPE_WIFI = 100;
    private static int NET_ETHERNET = 101;
    private static int NET_NEW_TYPE = 12;


    public static int getNetWorkType(Context ctx) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NET_TYPE_WIFI;
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NET_TYPE_2G;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NET_TYPE_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NET_TYPE_4G;
                    }
                }
            }
        } catch (Exception e) {
        }
        return NET_TYPE_UNKNOWN;
    }


    public static int getSIMProvider(Context ctx) {
        int provider = 0;
        try {
            // 返回唯一的用户ID;就是这张卡的编号神马的
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = tm.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                provider = 3;
            } else if (IMSI.startsWith("46001")) {
                provider = 1;
            } else if (IMSI.startsWith("46003")) {
                provider = 2;
            }
            return provider;
        } catch (Exception e) {
        }
        return provider;

    }

    private static String NULL = "null";

    /**
     * @param ctx
     * @return 0 lng  1 lat
     */
    public static String[] getLocation(Context ctx) {
        String lat = NULL;
        String lng = NULL;
        try {
            LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            if (ctx.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ctx.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationManager != null) {
                    lat = location.getLatitude() + "";
                    lng = location.getLongitude() + "";
                }
            }

        } catch (Exception e) {
        }
        return new String[]{lng, lat};
    }


    public static String getBssid(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        // 判断WiFi是否开启
        if (wifiManager.isWifiEnabled()) {
            // 已经开启了WiFi
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String bssid = wifiInfo.getBSSID();
            return bssid;
        }
        return NULL;
    }

    public static String getInstallPackageName(Context ctx) {
        try {
            return ctx.getPackageName();
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        return root;
    }

    public static boolean isSIMAviable(Context ctx) {
        try {
            TelephonyManager telMgr = (TelephonyManager)
                    ctx.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            boolean result = true;
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    result = false;
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static float getDenstity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

    public static String getLocale(Context ctx) {
        return ctx.getResources().getConfiguration().locale.getCountry();
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getCpuAbi() {
        return Build.CPU_ABI;
    }


//    /**
//     * 获取手机内存可用存储空间
//     *
//     * @return
//     */
//    public static long getAvailableExternalMemorySize() {
//        try {
//            File file = Environment.getExternalStorageDirectory();
//            StatFs statFs = new StatFs(file.getPath());
//            long availableBlocksLong = statFs.getAvailableBlocksLong();
//            long blockSizeLong = statFs.getBlockSizeLong();
//            return availableBlocksLong * blockSizeLong / 1000;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//
//    }

    public static boolean checkPackageInstalled(Context ctx, String packageName) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo != null;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }


    public static boolean isDeivceRooted() {

        return isTagDebug() || isSuperUser() || isXbinSuExist();
    }

    private static boolean isTagDebug() {
        String tag = Build.TAGS;
        return tag != null && tag.contains("test-keys");
    }

    private static boolean isSuperUser() {
        try {
            return new File("/system/app/Superuser.apk").exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isXbinSuExist() {
        try {
            ArrayList<String> arrays = new ArrayList<>();
            String command[] = new String[]{"/system/xbin/which", "su"};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String len;
            while ((len = br.readLine()) != null) {
                arrays.add(len);
            }
            return arrays != null && !arrays.isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}