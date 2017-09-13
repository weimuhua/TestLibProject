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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

    private static final int PAGE_SIZE = 20;

    private Context mCxt;
    private ViewPager mViewPager;
    private MAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gridview_layout);
        mCxt = this;
        initView();
        initData();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
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

                List<View> views = new ArrayList<>();
                List<PackageInfo> tempList = new ArrayList<>(adapterDataList);
                for (int i = 0; i < tempList.size(); i++) {
                    if ((i != 0 && i % (PAGE_SIZE - 1) == 0) || i == tempList.size() - 1) {
                        int curIndex = views.size() * PAGE_SIZE;

                        View view = LayoutInflater.from(mCxt).inflate(R.layout.layout_gridview, null);
                        GridView gridView = view.findViewById(R.id.grid_view);
                        gridView.setAdapter(new GridViewAdapter(mCxt,
                                adapterDataList.subList(curIndex, i),
                                appNameList.subList(curIndex, i),
                                appIconList.subList(curIndex, i)));
                        views.add(view);
                    }
                }
                mAdapter = new MAdapter(views);
                mHandler.sendEmptyMessage(MSG_LOAD_ALL_APP_DONE);
            }
        });
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_ALL_APP_DONE:
                    mViewPager.setAdapter(mAdapter);
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
            TextView tv = convertView.findViewById(R.id.app_name);
            if (position <= mAppNameList.size()) {
                tv.setText(mAppNameList.get(position));
            }
            ImageView imageView = convertView.findViewById(R.id.app_icon);
            if (position <= mAppIconList.size()) {
                imageView.setImageDrawable(mAppIconList.get(position));
            }
            return convertView;
        }
    }

    private class MAdapter extends PagerAdapter {

        private List<View> mViews;

        public MAdapter(List<View> views) {
            mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
