package baidu.com.testlibproject.test;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.BuildConfig;
import dalvik.system.DexClassLoader;

public class Test {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "test";

    private static final String EMOJI_REGEX = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";

    private void testMatchEmoji() {
//        ((TextView) findViewById(R.id.textview)).setText(String.valueOf(Character.toChars(Integer.parseInt("1F601", 16))));

        int i0 = 0x1f60a;
        int i1 = 0x1f60c;
        int i2 = 0x1f61a;
        int i3 = 0x1f613;
        int i4 = 0x1f630;
        int i5 = 0x1f61d;
        int i6 = 0x1f601;
        int i7 = 0x1f61c;
        int i8 = 0x263a;
        int i9 = 0x1f609;
        int[] intArr = new int[10];
        intArr[0] = i0;
        intArr[1] = i1;
        intArr[2] = i2;
        intArr[3] = i3;
        intArr[4] = i4;
        intArr[5] = i5;
        intArr[6] = i6;
        intArr[7] = i7;
        intArr[8] = i8;
        intArr[9] = i9;
        StringBuilder sb = new StringBuilder();
        for (int anIntArr : intArr) {
            sb.append("嘿").append(String.valueOf(Character.toChars(anIntArr)));
        }
        LogHelper.d(TAG, "sb " + sb.toString());

        long time = System.currentTimeMillis();
        Pattern pattern = Pattern.compile(EMOJI_REGEX, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sb);
        LogHelper.d(TAG, "cost " + (System.currentTimeMillis() - time));

        while (matcher.find()) {
            LogHelper.d(TAG, "find " + matcher.group());
        }
        LogHelper.d(TAG, "cost " + (System.currentTimeMillis() - time));
    }

    private void testClassLoader(Context cxt) {
        ClassLoader classLoader = cxt.getClassLoader();
        if (classLoader != null) {
            Log.i(TAG, "[onCreate] classLoader " + " : " + classLoader.toString());
            while (classLoader.getParent() != null) {
                classLoader = classLoader.getParent();
                Log.i(TAG, "[onCreate] classLoader " + " : " + classLoader.toString());
            }
        }
    }

    private void testRuntimeMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        LogHelper.d(TAG, "totalMemory=" + getValue(totalMemory));
        LogHelper.d(TAG, "freeMemory=" + getValue(freeMemory));
        LogHelper.d(TAG, "maxMemory=" + getValue(maxMemory));
    }

    private String getValue(long value) {
        return Long.toString(value / (1024));
    }


    private void testLoadClass(Context cxt) {
        if (DEBUG) {
            LogHelper.d(TAG, "testLoadClass");
        }
        long time = System.currentTimeMillis();
        String apkPath = "/data/local/tmp/app-debug.apk";
        DexClassLoader classLoader = new DexClassLoader(apkPath,
                cxt.getCacheDir().getAbsolutePath(), null, cxt.getClassLoader());
        if (DEBUG) {
            LogHelper.d(TAG, "new DexClassLoader done; cost = " + (System.currentTimeMillis() - time));
        }

        try {
            if (DEBUG) {
                LogHelper.d(TAG, "begin to loadClass");
            }
            time = System.currentTimeMillis();
            Class<?> loadedClass = classLoader.loadClass("com.tencent.od.base.apk.DiffInfo");
            Constructor constructor = loadedClass.getConstructor(JSONObject.class);
            JSONObject json = new JSONObject();
            json.put("name", "ClassLoader");
            json.put("compressionMethod", 10);
            json.put("diffType", 101);
            json.put("modTime", System.currentTimeMillis());
            Object obj = constructor.newInstance(json);
            if (DEBUG) {
                LogHelper.d(TAG, "newInstance done, cost = " + (System.currentTimeMillis() - time));
            }

            Method m = loadedClass.getMethod("jsonObject");
            JSONObject result = (JSONObject) m.invoke(obj);
            if (DEBUG) {
                LogHelper.d(TAG, "result = " + result.toString());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
