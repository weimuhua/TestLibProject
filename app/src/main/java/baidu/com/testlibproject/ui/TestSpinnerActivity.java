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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.commontools.utils.MobileInfo;
import baidu.com.testlibproject.R;

public class TestSpinnerActivity extends Activity implements Handler.Callback, AdapterView.OnItemSelectedListener {

    private static final int MSG_LOAD_GALLERY_DRAWABLE_DONE = 1;

    private Handler mHandler;
    private Context mContext;
    private Gallery mGallery;
    private ImageView mImageView;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_layout);
        mContext = this;
        mHandler = new Handler(this);
        initView();
        initData();
    }

    private void initView() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] strArr = new String[]{
                "Baidu", "AliBaBa", "Tencent", "JingDong"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_activity_item, strArr);
        spinner.setAdapter(adapter);

        mGallery = (Gallery) findViewById(R.id.gallery);
        mImageView = (ImageView) findViewById(R.id.gallery_select_img);
    }

    private void initData() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                PackageManager pm = mContext.getPackageManager();
                List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                List<Drawable> drawableList = new ArrayList<>();
                for (PackageInfo info : infoList) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        drawableList.add(info.applicationInfo.loadIcon(pm));
                    }
                }
                mAdapter = new GalleryAdapter(drawableList);
                mHandler.sendEmptyMessage(MSG_LOAD_GALLERY_DRAWABLE_DONE);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_GALLERY_DRAWABLE_DONE:
                mGallery.setAdapter(mAdapter);
                mGallery.setOnItemSelectedListener(this);
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mImageView.setImageDrawable(mAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class GalleryAdapter extends BaseAdapter {

        private List<Drawable> mDrawableList;

        public GalleryAdapter(List<Drawable> drawableList) {
            mDrawableList = drawableList;
        }

        @Override
        public int getCount() {
            return mDrawableList.size();
        }

        @Override
        public Drawable getItem(int position) {
            return mDrawableList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageDrawable(mDrawableList.get(position));
            imageView.setLayoutParams(new Gallery.LayoutParams((int) MobileInfo.dp2px(mContext, 60),
                    (int) MobileInfo.dp2px(mContext, 60)));
            return imageView;
        }
    }
}