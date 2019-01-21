package baidu.com.testlibproject.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

public class MyImageSpan extends ImageSpan {

    private int mEmoticonId;

    public MyImageSpan(Bitmap b) {
        super(b);
    }

    public MyImageSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public MyImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public MyImageSpan(Context context, Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
    }

    public MyImageSpan(Drawable drawable) {
        super(drawable);
    }

    public MyImageSpan(Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public MyImageSpan(Drawable drawable, String source) {
        super(drawable, source);
    }

    public MyImageSpan(Drawable drawable, String source, int verticalAlignment) {
        super(drawable, source, verticalAlignment);
    }

    public MyImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public MyImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public MyImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public MyImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public void setEmoticonId(int id) {
        mEmoticonId = id;
    }

    public int getEmoticonId() {
        return mEmoticonId;
    }
}
