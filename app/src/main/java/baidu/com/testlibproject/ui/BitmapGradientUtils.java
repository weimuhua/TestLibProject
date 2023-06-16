package baidu.com.testlibproject.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import androidx.annotation.NonNull;

public class BitmapGradientUtils {

    /**
     * 给originalBitmap着渐变色
     */
    public static void addGradient(@NonNull Bitmap originalBitmap, @NonNull int[] colors) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Canvas canvas = new Canvas(originalBitmap);//Canvas中Bitmap是用来保存像素，相当于画纸
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, width / 2, height / 2, colors, null,
                Shader.TileMode.CLAMP);//shader:着色器，线性着色器设置渐变从左上坐标到右下坐标
        paint.setShader(shader);//设置着色器
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));//设置图像混合模式
        canvas.drawRect(0, 0, width, height, paint);
    }

    public static Bitmap getGradientBitmap(Context context, int drawableId, int[] colors) {
        //android不允许直接修改res里面的图片，所以要用copy方法
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);
        addGradient(bitmap, colors);
        return bitmap;
    }
}
