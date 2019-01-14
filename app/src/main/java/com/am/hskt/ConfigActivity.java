package com.am.hskt;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.am.hskt.view.AddPopWindow;
import com.am.hskt.view.EditInfoView;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener, AddPopWindow.OnItemSelect {

    private LinearLayout container;
    private Button sure;
    private Button add;
    private AddPopWindow popWindow;

    private static final String PkgName_TAG = "pkgName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initRes();
    }

    private void initRes() {
        container = findViewById(R.id.info_container);
        sure = findViewById(R.id.sure);
        add = findViewById(R.id.add);

        sure.setOnClickListener(this);
        add.setOnClickListener(this);

        popWindow = new AddPopWindow(this);
        popWindow.setSelectListener(this);

        addContentView(PkgName_TAG);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                boolean success = refreshConfig();
//                Toast.makeText(this, success ? "success" : "fail", Toast.LENGTH_SHORT).show();
                if (success) {
                    finish();
                }
                break;
            case R.id.add:
                popWindow.show(this);
                break;
        }
    }


    private boolean refreshConfig() {
//        String mPkgName = pkgName.getContent();
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("model").append(":").append(!TextUtils.isEmpty(model.getContent()) ? model.getContent() : Build.MODEL).append("|")
//                .append("deviceId").append(":").append(!TextUtils.isEmpty(deviceId.getContent()) ? deviceId.getContent() : DeviceUtils.getDeviceId(this)).append("|")
//                .append("manufacture").append(":").append(!TextUtils.isEmpty(manufacture.getContent()) ? manufacture.getContent() : Build.MANUFACTURER).append("|")
//                .append("brand").append(":").append(!TextUtils.isEmpty(brand.getContent()) ? brand.getContent() : Build.BRAND).append("|")
//                .append("android_id").append(":").append(!TextUtils.isEmpty(android_id.getContent()) ? android_id.getContent() : DeviceUtils.getAndroidId(this)).append("|")
//                .append("sdk_int").append(":").append(!TextUtils.isEmpty(sdk_int.getContent()) ? sdk_int.getContent() : Build.VERSION.SDK_INT).append("|")
//                .append("width").append(":").append(!TextUtils.isEmpty(_width.getContent()) ? _width.getContent() : getResources().getDisplayMetrics().widthPixels).append("|")
//                .append("height").append(":").append(!TextUtils.isEmpty(_height.getContent()) ? _height.getContent() : getResources().getDisplayMetrics().heightPixels);
//
//
//        return FileUtils.writeFile(mPkgName, buffer.toString());
        StringBuffer buffer = new StringBuffer();
        String mPkgName = null;
        if (container != null && container.getChildCount() >= 2) {
            for (int i = 0; i < container.getChildCount(); i++) {
                EditInfoView infoView = (EditInfoView) container.getChildAt(i);
                if (infoView.getTagData().equals(PkgName_TAG)) {
                    mPkgName = infoView.getContent();
                } else {
                    String content = infoView.getContent();
                    if (!TextUtils.isEmpty(content)) {
                        buffer.append(infoView.getTagData()).append(":").append(content).append("|");
                    } else {
                        Toast.makeText(this, "参数配置是空", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        } else {
            Toast.makeText(this, "需要配置参数", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (TextUtils.isEmpty(mPkgName)) {
            Toast.makeText(this, "包名为空", Toast.LENGTH_SHORT).show();
            return false;
        }


        Log.e("1234", "appData  " + mPkgName + "  " + buffer.toString());
        return FileUtils.writeFile(mPkgName, buffer.toString());
    }

    @Override
    public void onSelect(String title) {
        addContentView(title);
    }

    private void addContentView(String title) {
        if (checkAdd(title)) {
            EditInfoView infoView = new EditInfoView(this);
            if (PkgName_TAG.equals(title)) {
                infoView.setContent(getPackageName());
            }

            infoView.setTagData(title);
            container.addView(infoView);
        }
    }


    private boolean checkAdd(String title) {
        if (container.getChildCount() > 0) {
            for (int i = 0; i < container.getChildCount(); i++) {
                EditInfoView infoView = (EditInfoView) container.getChildAt(i);
                String infoTitle = infoView.getTagData();
                if (title.equals(infoTitle)) {
                    Toast.makeText(this, "已经添加了该配置", Toast.LENGTH_SHORT).show();
                    return false;

                }
            }
        }
        return true;
    }


}
