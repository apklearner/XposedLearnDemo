package com.am.hskt;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.am.hskt.view.EditInfoView;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private EditInfoView model, deviceId, manufacture, brand, pkgName, android_id;
    private Button sure;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initRes();
    }

    private void initRes() {
        pkgName = findViewById(R.id.pkgName);
        sure = findViewById(R.id.sure);
        model = findViewById(R.id.model);
        deviceId = findViewById(R.id.deviceId);
        manufacture = findViewById(R.id.manufacture);
        brand = findViewById(R.id.brand);
        android_id = findViewById(R.id.android_id);

        sure.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                boolean success = refreshConfig();
                Toast.makeText(this, success ? "success" : "fail", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private boolean refreshConfig() {
        String mPkgName = pkgName.getContent();
        StringBuffer buffer = new StringBuffer();
        buffer.append("model").append(":").append(!TextUtils.isEmpty(model.getContent()) ? model.getContent() : Build.MODEL).append("|")
                .append("deviceId").append(":").append(!TextUtils.isEmpty(deviceId.getContent()) ? deviceId.getContent() : DeviceUtils.getDeviceId(this)).append("|")
                .append("manufacture").append(":").append(!TextUtils.isEmpty(manufacture.getContent()) ? manufacture.getContent() : Build.MANUFACTURER).append("|")
                .append("brand").append(":").append(!TextUtils.isEmpty(brand.getContent()) ? brand.getContent() : Build.BRAND).append("|")
                .append("android_id").append(":").append(!TextUtils.isEmpty(android_id.getContent()) ? android_id.getContent() : DeviceUtils.getAndroidId(this));


        return FileUtils.writeFile(mPkgName, buffer.toString());

    }


}
