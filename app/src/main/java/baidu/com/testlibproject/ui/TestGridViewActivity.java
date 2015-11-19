package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.testlibproject.R;

public class TestGridViewActivity extends Activity {

    private static final int MSG_LOAD_ALL_APP_DONE = 1;

    private Context mCxt;
    private GridView mGridView;
    private GridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gridview_layout);
        mCxt = this;
        initView();
        initData();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.grid_view);
    }

    private void initData() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = mCxt.getPackageManager();
                List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                List<PackageInfo> adapterDataList = new ArrayList<>();
                List<String> appNameList = new ArrayList<>();
                List<Drawable> appIconList = new ArrayList<>();
                for (PackageInfo info : infoList) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        appNameList.add(info.applicationInfo.loadLabel(pm).toString());
                        appIconList.add(info.applicationInfo.loadIcon(pm));
                        adapterDataList.add(info);
                    }
                }
                mAdapter = new GridViewAdapter(mCxt, adapterDataList, appNameList, appIconList);
                mHandler.sendEmptyMessage(MSG_LOAD_ALL_APP_DONE);
            }
        });
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_ALL_APP_DONE:
                    mGridView.setAdapter(mAdapter);
                    break;
            }
            return false;
        }
    });

    private class GridViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mAppNameList;
        private List<Drawable> mAppIconList;
        private List<PackageInfo> mInfoList;

        public GridViewAdapter(Context cxt, List<PackageInfo> infoList, List<String> appNameArr,
                List<Drawable> appIconArr) {
            mContext = cxt;
            mInfoList = infoList;
            mAppNameList = appNameArr;
            mAppIconList = appIconArr;
        }

        @Override
        public int getCount() {
            return mInfoList.size();
        }

        @Override
        public PackageInfo getItem(int position) {
            return mInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.grid_view_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.app_name);
            if (position <= mAppNameList.size()) {
                tv.setText(mAppNameList.get(position));
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.app_icon);
            if (position <= mAppIconList.size()) {
                imageView.setImageDrawable(mAppIconList.get(position));
            }
            return convertView;
        }
    }
}
