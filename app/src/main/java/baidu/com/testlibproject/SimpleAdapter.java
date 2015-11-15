package baidu.com.testlibproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleAdapter extends BaseAdapter {
    private Context mCxt;
    private String[] mStrArr;

    public SimpleAdapter(Context cxt) {
        mCxt = cxt;
    }

    public SimpleAdapter(Context cxt, String[] strArr) {
        mCxt = cxt;
        mStrArr = strArr;
    }

    public void setStrArr(String[] strArr) {
        mStrArr = strArr;
    }

    @Override
    public int getCount() {
        if (mStrArr != null) {
            return mStrArr.length;
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        if (mStrArr != null) {
            return mStrArr[position];
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCxt);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_activity_item, null);
        }
        if (mStrArr != null && position <= mStrArr.length) {
            ((TextView) convertView.findViewById(R.id.item_textview)).setText(mStrArr[position]);
        }
        convertView.setTag(position);
        return convertView;
    }
}
