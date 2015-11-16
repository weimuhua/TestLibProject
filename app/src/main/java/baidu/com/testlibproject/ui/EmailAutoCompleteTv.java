package baidu.com.testlibproject.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.R;

public class EmailAutoCompleteTv extends AutoCompleteTextView implements View.OnFocusChangeListener {

    private static final String TAG = "EmailAutoCompleteTv";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    public EmailAutoCompleteTv(Context cxt) {
        super(cxt);
        init();
    }

    private void init() {
        setAdapter(new EmailAutoCompleteAdapter(getContext(), R.layout.main_activity_item,
                getResources().getStringArray(R.array.auto_complete_arr)));
        setThreshold(1);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

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
            String text = tv.getText().toString();
            int index = text.indexOf("@");
            if (index != -1) {
                text = text.substring(0, index) + getItem(position);
            }
            tv.setText(text);
            return convertView;
        }
    }
}
