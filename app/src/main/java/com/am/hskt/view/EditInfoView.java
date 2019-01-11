package com.am.hskt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.am.hskt.R;

public class EditInfoView extends RelativeLayout {

    private String TAG;
    private TextView tvTitle;
    private EditText etContent;


    public EditInfoView(Context context) {
        this(context, null);
    }

    public EditInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(attrs);
    }

    private void initRes(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_add_item, this, true);
        tvTitle = findViewById(R.id.tv_title);
        etContent = findViewById(R.id.et_item);
        if (attrs != null) {
            TypedArray typedValue = getContext().obtainStyledAttributes(attrs, R.styleable.add_items);
            String title = typedValue.getString(R.styleable.add_items_title);
            String content = typedValue.getString(R.styleable.add_items_content);
            if (!TextUtils.isEmpty(title)) {
                TAG = title;
                tvTitle.setText(title);
            }

            if (!TextUtils.isEmpty(content)) {
                etContent.setText(content);
            }
        }
    }

    public void setTagData(String mTag) {
        this.TAG = mTag;
        tvTitle.setText(TAG);
    }


    public String getTagData() {
        return TAG;
    }

    public String getContent() {
        return etContent.getText().toString();
    }

    public void setContent(String title){
        etContent.setText(title);
    }

}
