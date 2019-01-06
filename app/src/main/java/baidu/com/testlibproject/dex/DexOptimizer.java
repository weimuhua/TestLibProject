package baidu.com.testlibproject.dex;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dalvik.system.BaseDexClassLoader;

public class DexOptimizer {

    private static final String TAG = "DexOptimizer";

    public interface ResultCallback {
        void onSuccess();

        void onFailed(Throwable thr);
    }

    public void optimizeDex(Context cxt, File apkFile, File optimizedDir) {
        Log.d(TAG, "begin optimizeDex");
        long time = System.currentTimeMillis();
        ClassLoader classLoader = new BaseDexClassLoader(apkFile.getAbsolutePath(),
                optimizedDir, null, cxt.getClassLoader());
        Log.d(TAG, "optimizeDex done, cost = " + (System.currentTimeMillis() - time));
    }

    public void optimizeDex(List<File> dexFiles, File optimizedDir, ResultCallback cb) {
        String targetISA = null;
        try {
            targetISA = getCurrentInstructionSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "optimizeDex, targetISA = " + targetISA);
        long time = System.currentTimeMillis();
        try {
            for (File dexFile : dexFiles) {
                interpretDex2Oat(dexFile.getAbsolutePath(), optimizedDir.getAbsolutePath(), targetISA);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (cb != null) {
                cb.onFailed(e);
            }
        }

        if (cb != null) {
            cb.onSuccess();
        }
        Log.d(TAG, "optimizeDex done, cost = " + (System.currentTimeMillis() - time));
    }

    private static String getCurrentInstructionSet() throws Exception {
        Class<?> clazz = Class.forName("dalvik.system.VMRuntime");
        Method currentGet = clazz.getDeclaredMethod("getCurrentInstructionSet");
        return (String) currentGet.invoke(null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void interpretDex2Oat(String dexFilePath, String oatFilePath,
            String targetISA) throws IOException {
        final File oatFile = new File(oatFilePath);
        if (!oatFile.exists()) {
            oatFile.getParentFile().mkdirs();
        }

        final List<String> commandAndParams = new ArrayList<>();
        commandAndParams.add("dex2oat");

        // for 7.1.1, duplicate class fix
        if (Build.VERSION.SDK_INT >= 24) {
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("-classpath");
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("&");
        }
        commandAndParams.add("--dex-file=" + dexFilePath);
        commandAndParams.add("--oat-file=" + oatFilePath);
        commandAndParams.add("--instruction-set=" + targetISA);

        if (Build.VERSION.SDK_INT > 25) {
            commandAndParams.add("--compiler-filter=quicken");
        } else {
            commandAndParams.add("--compiler-filter=interpret-only");
        }

        final ProcessBuilder pb = new ProcessBuilder(commandAndParams);
        pb.redirectErrorStream(true);

        final Process dex2oatProcess = pb.start();

        StreamConsumer.consumeInputStream(dex2oatProcess.getInputStream());
        StreamConsumer.consumeInputStream(dex2oatProcess.getErrorStream());

        try {
            final int ret = dex2oatProcess.waitFor();
            if (ret != 0) {
                throw new IOException("dex2oat works unsuccessfully, exit code: " + ret);
            }
        } catch (InterruptedException e) {
            throw new IOException("dex2oat is interrupted, msg: " + e.getMessage(), e);
        }
    }

    private static class StreamConsumer {
        static final Executor STREAM_CONSUMER = Executors.newSingleThreadExecutor();

        static void consumeInputStream(final InputStream is) {
            STREAM_CONSUMER.execute(new Runnable() {
                @Override
                public void run() {

                    if (is == null) {
                        return;
                    }

                    final byte[] buffer = new byte[256];
                    try {
                        while ((is.read(buffer)) > 0) {
                            // To satisfy checkstyle rules.
                        }
                    } catch (IOException ignored) {
                        // Ignored.
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ignored) {
                            // Ignored.
                        }
                    }
                }
            });
        }
    }
}
