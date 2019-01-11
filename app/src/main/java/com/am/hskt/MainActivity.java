package com.am.hskt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_config, btn_msg;
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
        tv_msg = findViewById(R.id.tv_msg);

        btn_config.setOnClickListener(this);
        btn_msg.setOnClickListener(this);

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
        }
    }

    private String buildDeivceInfo() {
        StringBuffer buffer = new StringBuffer();
        String deviceId = DeviceUtils.getDeviceId(this);
        String uuid = DeviceUtils.randomDeviceUUID(this);
        String subscriberId = DeviceUtils.getSubscriberId(this);
        buffer.append("devideId=").append(deviceId).append("\n")
                .append("android_id=").append(DeviceUtils.getAndroidId(this)).append("\n")
                .append("uuid=").append(uuid).append("\n")
                .append("model=").append(Build.MODEL).append("\n")
                .append("manufacture=").append(Build.MANUFACTURER).append("\n")
                .append("brand=").append(Build.BRAND).append("\n")
                .append("sdk_int=").append(Build.VERSION.SDK_INT).append("\n")
                .append("sdk_release=").append(Build.VERSION.RELEASE).append("\n")
                .append("width=").append(getResources().getDisplayMetrics().widthPixels).append("\n")
                .append("height=").append(getResources().getDisplayMetrics().heightPixels).append("\n")
                .append("density=").append(getResources().getDisplayMetrics().density).append("\n")
                .append("subscriberId=").append(subscriberId).append("\n")
                .append("board=").append(Build.BOARD).append("\n")
                .append("cup_abi=").append(Build.CPU_ABI).append("\n")
                .append("product=").append(Build.PRODUCT).append("\n")
                .append("rooted=").append(DeviceUtils.isRoot()).append("\n")
                .append("sdk=").append(Build.VERSION.SDK).append("\n");

//                .append("userAgent=").append(new WebView(this).getSettings().getUserAgentString());
        return buffer.toString();

    }

}
