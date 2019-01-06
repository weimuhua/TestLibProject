package baidu.com.testlibproject.dex;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

public class DexOptimizer {

    private static final String TAG = "DexOptimizer";

    private static final String DEX_SUFFIX = ".dex";
    private static final String ODEX_SUFFIX = ".odex";

    public interface ResultCallback {
        void onSuccess();

        void onFailed(Throwable thr);
    }

    public void optimizeDexByClassLoader(Context cxt, File apkFile, File optimizedDir) {
        Log.d(TAG, "begin optimizeDex");
        long time = System.currentTimeMillis();
        ClassLoader classLoader = new BaseDexClassLoader(apkFile.getAbsolutePath(),
                optimizedDir, null, cxt.getClassLoader());
        Log.d(TAG, "optimizeDex done, cost = " + (System.currentTimeMillis() - time));
    }

    public void optimizeDex(List<File> dexFiles, File optimizedDir) {
        Log.d(TAG, "begin optimizeDex");
        long time = System.currentTimeMillis();
        try {
            for (File dexFile : dexFiles) {
                DexFile.loadDex(dexFile.getParent(), optimizedPathFor(dexFile, optimizedDir), 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "interpretDex2Oat err!", e);
        }
        Log.d(TAG, "optimizeDex done, cost = " + (System.currentTimeMillis() - time));
    }

    public void optimizeDex(List<File> dexFiles, File optimizedDir, ResultCallback cb) {
        String targetISA = null;
        try {
            targetISA = getCurrentInstructionSet();
        } catch (Exception e) {
            Log.e(TAG, "get instruction err!", e);
        }
        Log.d(TAG, "optimizeDex, targetISA = " + targetISA);
        long time = System.currentTimeMillis();
        try {
            for (File dexFile : dexFiles) {
                interpretDex2Oat(dexFile.getAbsolutePath(),
                        optimizedPathFor(dexFile, optimizedDir), targetISA);
            }
        } catch (Exception e) {
            Log.e(TAG, "interpretDex2Oat err!", e);
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

    public static List<File> getDexFile(File apkFile) throws IOException {
        List<File> dexFiles = new ArrayList<>();
        ZipFile zipFile = new ZipFile(apkFile.getAbsolutePath());
        Enumeration em = zipFile.entries();
        while (em.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) em.nextElement();
            File file = new File(apkFile.getAbsolutePath() + File.separator + entry.getName());
            if (file.getName().endsWith(DEX_SUFFIX)) {
                dexFiles.add(file);
            }
        }
        zipFile.close();
        return dexFiles;
    }

    private static String optimizedPathFor(File path, File optimizedDirectory) throws Exception {
        if (Build.VERSION.SDK_INT > 25) {
            // dex_location = /foo/bar/baz.jar
            // odex_location = /foo/bar/oat/<isa>/baz.odex

            String currentInstructionSet = getCurrentInstructionSet();

            File parentFile = path.getParentFile();
            String fileName = path.getName();
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                fileName = fileName.substring(0, index);
            }

            return parentFile.getAbsolutePath() + "/oat/"
                    + currentInstructionSet + "/" + fileName + ODEX_SUFFIX;
        }

        String fileName = path.getName();
        if (!fileName.endsWith(DEX_SUFFIX)) {
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot < 0) {
                fileName += DEX_SUFFIX;
            } else {
                StringBuilder sb = new StringBuilder(lastDot + 4);
                sb.append(fileName, 0, lastDot);
                sb.append(DEX_SUFFIX);
                fileName = sb.toString();
            }
        }

        File result = new File(optimizedDirectory, fileName);
        return result.getPath();
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
