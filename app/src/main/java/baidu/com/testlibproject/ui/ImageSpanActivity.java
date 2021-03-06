package baidu.com.testlibproject.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.R;

public class ImageSpanActivity extends AppCompatActivity {

    private static final String TAG = "ImageSpanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_span);
        initView();
    }

    private void initView() {
        EditText tv1 = findViewById(R.id.text_view1);
        TextView tv2 = findViewById(R.id.text_view2);
        TextView tv3 = findViewById(R.id.text_view3);
        String text1 = " 测试文字前面插入图片";
        String text2 = "测试文字 中间插入图片";
        String text3 = "测试文字后面插入图片 ";

        SpannableString span1 = new SpannableString(text1);
        MyImageSpan imageSpan1 = new MyImageSpan(this, R.mipmap.emoji_100, DynamicDrawableSpan.ALIGN_BOTTOM);
        MyImageSpan imageSpana = new MyImageSpan(this, R.mipmap.emoji_100, DynamicDrawableSpan.ALIGN_BOTTOM);
        imageSpan1.setEmoticonId(10001);
        span1.setSpan(imageSpan1, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span1.setSpan(imageSpana, span1.length() - 2, span1.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv1.setText(span1);

        Editable text = tv1.getText();
        MyImageSpan[] imageSpans = text.getSpans(0, span1.length() - 1, MyImageSpan.class);
        LogHelper.d(TAG, "imageSpans size = " + imageSpans.length);
        for (MyImageSpan span : imageSpans) {
            LogHelper.d(TAG, "getEmoticonId = " + span.getEmoticonId());
            LogHelper.d(TAG, "start = " + text.getSpanStart(span));
            LogHelper.d(TAG, "end = " + text.getSpanEnd(span));
        }
        LogHelper.d(TAG, "span1 = " + span1);

        SpannableString span2 = new SpannableString("测试文字 中间插入图片");
        ImageSpan imageSpan2 = new ImageSpan(this,
                R.mipmap.emoji_100, DynamicDrawableSpan.ALIGN_BOTTOM);
        span2.setSpan(imageSpan2, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv2.setText(span2);


        SpannableString span3 = new SpannableString(text3);
        ImageSpan imageSpan3 = new ImageSpan(this, R.mipmap.emoji_100, DynamicDrawableSpan.ALIGN_BOTTOM);
        span3.setSpan(imageSpan3, text3.length() - 1, text3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv3.setText(span3);
    }
}
