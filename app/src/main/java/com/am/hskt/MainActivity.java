package com.am.hskt;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_config, btn_msg, btn_default;
    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRes();
    }

    private void initRes() {
        btn_config = findViewById(R.id.btn_config);
        btn_msg = findViewById(R.id.btn_msg);
        btn_default = findViewById(R.id.btn_default);
        tv_msg = findViewById(R.id.tv_msg);

        btn_config.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        btn_default.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_config:
                startActivity(new Intent(this, ConfigActivity.class));
                break;
            case R.id.btn_msg:
                tv_msg.setText(buildDeivceInfo());
//                Log.e("1234", " tags  "+Build.TAGS);
                break;
            case R.id.btn_default:
                FileUtils.addDefaultConfig();
                break;
        }
    }

    private String buildDeivceInfo() {
        StringBuffer buffer = new StringBuffer();
        String deviceId = DeviceUtils.getDeviceId(this);
        String uuid = DeviceUtils.randomDeviceUUID(this);
        String subscriberId = DeviceUtils.getSubscriberId(this);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String[] loc = getLocationData();
        buffer.append("devideId = ").append(deviceId).append("\n")
                .append("android_id = ").append(DeviceUtils.getAndroidId(this)).append("\n")
                .append("uuid = ").append(uuid).append("\n")
                .append("tags = ").append(Build.TAGS).append("\n")
                .append("id = ").append(Build.ID).append("\n")
                .append("serial = ").append(Build.USER).append("\n")
                .append("model = ").append(Build.MODEL).append("\n")
                .append("time = ").append(Build.TIME).append("\n")
//                .append("support_abi=").append(Build.SUPPORTED_ABIS)
                .append("type = ").append(Build.TYPE).append("\n")
                .append("hardware = ").append(Build.HARDWARE).append("\n")
                .append("bootloader = ").append(Build.BOOTLOADER).append("\n")
                .append("fingerprint = ").append(Build.FINGERPRINT).append("\n")
                .append("user = ").append(Build.USER).append("\n")
                .append("host = ").append(Build.HOST).append("\n")
                .append("device = ").append(Build.DEVICE).append("\n")
                .append("display = ").append(Build.DISPLAY).append("\n")
                .append("manufacture = ").append(Build.MANUFACTURER).append("\n")
                .append("brand = ").append(Build.BRAND).append("\n")
                .append("sdk_int = ").append(Build.VERSION.SDK_INT).append("\n")
                .append("sdk_release = ").append(Build.VERSION.RELEASE).append("\n")
                .append("width = ").append(getResources().getDisplayMetrics().widthPixels).append("\n")
                .append("height = ").append(getResources().getDisplayMetrics().heightPixels).append("\n")
                .append("density = ").append(getResources().getDisplayMetrics().density).append("\n")
                .append("subscriberId = ").append(subscriberId).append("\n")
                .append("board = ").append(Build.BOARD).append("\n")
                .append("cup_abi = ").append(Build.CPU_ABI).append("\n")
                .append("cup_abi2 = ").append(Build.CPU_ABI2).append("\n")
                .append("product = ").append(Build.PRODUCT).append("\n")
                .append("rooted = ").append(DeviceUtils.isRoot()).append("\n")
                .append("sdk = ").append(Build.VERSION.SDK).append("\n")
                .append("lat = ").append(loc[0]).append("\n")
                .append("lng = ").append(loc[1]).append("\n")
//                .append("wifiEnable=").append(DeviceUtils.isWifi(this)).append("\n")
                .append("mac = ").append(DeviceUtils.getWifiMacAddress(this)).append("\n")
                .append("bssid = ").append(DeviceUtils.getBssid(this)).append("\n")
                .append("ssid = ").append(DeviceUtils.getssid(this)).append("\n")
                .append("netOperator = ").append(tm.getNetworkOperator()).append("\n")
                .append("simoperator = ").append(tm.getSimOperator()).append("\n")
                .append("linNum = ").append(DeviceUtils.getLineNumber(this)).append("\n")
                .append("simSerial = ").append(DeviceUtils.getSimSerialNumber(this)).append("\n");

//                .append("userAgent=").append(new WebView(this).getSettings().getUserAgentString());
        return buffer.toString();

    }


    private String[] getLocationData() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = lm.getProviders(true);
            if (providers != null && providers.size() > 0) {
                String mprovider = null;
                if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    mprovider = LocationManager.NETWORK_PROVIDER;
                } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    mprovider = LocationManager.GPS_PROVIDER;
                }

                Log.e("1234", "provider " + mprovider);
                if (mprovider != null) {
                    Location location = lm.getLastKnownLocation(mprovider);
                    return new String[]{location.getLatitude() + "", location.getLongitude() + ""};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[]{"0", "0"};
    }

}
