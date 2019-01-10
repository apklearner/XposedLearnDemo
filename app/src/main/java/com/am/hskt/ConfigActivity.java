package com.am.hskt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.am.hskt.view.EditInfoView;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private EditInfoView uuid, deviceId;
    private Button sure;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initRes();
    }

    private void initRes() {
        sure = findViewById(R.id.sure);
        uuid = findViewById(R.id.uuid);
        deviceId = findViewById(R.id.deviceId);

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
        StringBuffer buffer = new StringBuffer();
        buffer.append("uuid").append(":").append("null").append("|")
                .append("deviceId").append(":").append(!TextUtils.isEmpty(deviceId.getContent()) ? deviceId.getContent() : "null");

        return FileUtils.writeFile(buffer.toString());

    }


}
