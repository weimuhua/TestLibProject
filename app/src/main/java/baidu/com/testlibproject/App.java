package baidu.com.testlibproject;

import android.app.Application;
import android.content.Intent;

import baidu.com.commontools.utils.ProcessUtils;
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
        if (DEBUG)
            LogHelper.d(TAG, "Application onCreate, process : " + ProcessUtils.getProcessName(this));

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
        String processName = ProcessUtils.getProcessName(this);
        if (Constants.PACKAGE_NAME.equals(processName)) {
            return PROCESS_UI;
        } else if (Constants.BKG_PROCESS_NAME.equals(processName)) {
            return PROCESS_BKG;
        } else {
            return PROCESS_UNKNOWN;
        }
    }
}
