package baidu.com.testlibproject;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.os.Process;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.util.List;

import baidu.com.testlibproject.service.MainService;
import baidu.com.testlibproject.service.MainServiceClient;

public class App extends Application {

    private static String TAG = "ProjectApp";
    private static boolean DEBUG = FeatureConfig.DEBUG;

    private static final int PROCESS_UNKNOWN = 0;
    private static final int PROCESS_UI = 1;
    private static final int PROCESS_BKG = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) LogHelper.d(TAG, "Application onCreate, process : " + getProcessName());

        int processType = getProcessType();
        switch (processType) {
            case PROCESS_UI:
                MainServiceClient.getInstance(this).tryToConnect();
                break;
            case PROCESS_BKG:
                Intent intent = new Intent(this, MainService.class);
                startService(intent);
                break;
        }
    }

    private int getProcessType() {
        String processName = getProcessName();
        if (Constants.PACKAGE_NAME.equals(processName)) {
            return PROCESS_UI;
        } else if (Constants.BKG_PROCESS_NAME.equals(processName)) {
            return PROCESS_BKG;
        } else {
            return PROCESS_UNKNOWN;
        }
    }

    private String getProcessName() {
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (RunningAppProcessInfo info : runningApps) {
                if (info.pid == pid) return info.processName;
            }
        }

        return null;
    }
}
