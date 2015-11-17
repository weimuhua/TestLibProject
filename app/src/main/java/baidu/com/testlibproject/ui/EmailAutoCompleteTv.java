package baidu.com.testlibproject.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;

public class EmailAutoCompleteTv extends AutoCompleteTextView implements View.OnFocusChangeListener {

    private static final String TAG = "EmailAutoCompleteTv";
    private static boolean DEBUG = FeatureConfig.DEBUG;

    public EmailAutoCompleteTv(Context cxt) {
        super(cxt);
        init();
    }

    public EmailAutoCompleteTv(Context cxt, AttributeSet set) {
        super(cxt, set);
        init();
    }

    private void init() {
        setAdapter(new EmailAutoCompleteAdapter(getContext(), R.layout.main_activity_item,
                getResources().getStringArray(R.array.auto_complete_arr)));
        setThreshold(1);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            String text = getText().toString();
            if (!"".equals(text)) {
                performFiltering(text, 0);
            }
        }
    }

    @Override
    protected void replaceText(CharSequence text) {
        if (DEBUG) LogHelper.d(TAG, "replaceText, text : " + text);
        String t = getText().toString();
        int index = t.indexOf("@");
        if (index != -1) {
            t = t.substring(0, index) + text;
        } else {
            t += text;
        }
        super.replaceText(t);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String t = text.toString();
        if (DEBUG) LogHelper.d(TAG, "performFiltering, text : " + t);

        //因为用户输入邮箱时，都是以字母，数字开始，而我们的adapter中只会提供以类似于"@163.com"
        //的邮箱后缀，因此在调用super.performFiltering时，传入的一定是以"@"开头的字符串
        int index = t.indexOf("@");
        if (index == -1) {
            if (t.matches("^[a-zA-Z0-9_]+$")) {
                super.performFiltering("@", keyCode);
            } else {
                //当用户中途输入非法字符时，关闭下拉提示框
                dismissDropDown();
            }
        } else {
            super.performFiltering(t.substring(index), keyCode);
        }
    }

    private class EmailAutoCompleteAdapter extends ArrayAdapter<String> {

        public EmailAutoCompleteAdapter(Context cxt, int resId, String[] strArr) {
            super(cxt, resId, strArr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.main_activity_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.item_textview);
            String text = EmailAutoCompleteTv.this.getText().toString();
            int index = text.indexOf("@");
            if (index != -1) {
                text = text.substring(0, index);
            }
            text = text + getItem(position);
            tv.setText(text);
            return convertView;
        }
    }
}
