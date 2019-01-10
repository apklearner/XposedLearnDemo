package com.am.hskt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
                break;
        }
    }

    private String buildDeivceInfo() {
        StringBuffer buffer = new StringBuffer();
        String deviceId = DeviceUtils.getDeviceId(this);
        String uuid = DeviceUtils.randomDeviceUUID(this);
        buffer.append("devideId=").append(deviceId).append("\n")
                .append("android_id=").append(DeviceUtils.getAndroidId(this)).append("\n")
                .append("uuid=").append(uuid).append("\n")
                .append("model=").append(Build.MODEL).append("\n")
                .append("manufacture=").append(Build.MANUFACTURER).append("\n")
                .append("brand=").append(Build.BRAND).append("\n");
        return buffer.toString();

    }

}
