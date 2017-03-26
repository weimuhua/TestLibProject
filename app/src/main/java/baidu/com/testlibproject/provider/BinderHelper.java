package baidu.com.testlibproject.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import baidu.com.commontools.utils.ProcessUtils;
import baidu.com.testlibproject.App;
import baidu.com.testlibproject.BuildConfig;
import baidu.com.testlibproject.Constants;
import baidu.com.testlibproject.LogHelper;

/**
 * Created by Wayne on 2017/3/26.
 */

public class BinderHelper {

    private static final String TAG = "BinderHelper";
    private static final boolean DEBUG = BuildConfig.DEBUG_LOG;

    private static final String PROCESS_NAME_UI = "";
    private static final String PROCESS_NAME_BKG = ":bkg";

    private static final int PROCESS_TYPE_UNKNOWN = 0;
    private static final int PROCESS_TYPE_UI = 1;
    private static final int PROCESS_TYPE_BKG = 2;

    public static final String BINDER_NAME_A = "binder_a";
    public static final String BINDER_NAME_B = "binder_b";
    public static final String BINDER_NAME_C = "binder_c";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PROCESS_TYPE_UNKNOWN, PROCESS_TYPE_BKG, PROCESS_TYPE_UI,})
    @interface ProcessType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PROCESS_NAME_BKG, PROCESS_NAME_UI,})
    @interface ProcessName {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({BINDER_NAME_A, BINDER_NAME_B, BINDER_NAME_C,})
    public @interface BinderName {
    }

    private static final Map<String, IBinder> sBinderMap = new HashMap<>();

    @ProcessType
    private static int getProcessType() {
        String processName = ProcessUtils.getProcessName(App.getContext());
        if (Constants.PACKAGE_NAME.equals(processName)) {
            return PROCESS_TYPE_UI;
        } else if (Constants.BKG_PROCESS_NAME.equals(processName)) {
            return PROCESS_TYPE_BKG;
        } else {
            return PROCESS_TYPE_UNKNOWN;
        }
    }

    public static IBinder getBinder(Context cxt, @BinderName final String name) {
        if (DEBUG) {
            LogHelper.d(TAG, "getBinder : " + name);
        }
        int processType = getProcessType();
        switch (processType) {
            case PROCESS_TYPE_BKG:
                return BinderManager.getBinder(name);
            case PROCESS_TYPE_UI:
            case PROCESS_TYPE_UNKNOWN:
            default:
                synchronized (sBinderMap) {
                    IBinder iBinder = sBinderMap.get(name);
                    if (iBinder == null || !iBinder.isBinderAlive()) {
                        try {
                            ContentResolver resolver = cxt.getContentResolver();
                            Bundle bundle = resolver.call(Constants.BINDER_AUTHORITY_URI,
                                    BinderProvider.GET_BINDER_METHOD, name, null);
                            if (bundle != null) {
                                if (DEBUG) {
                                    LogHelper.d(TAG, "bundle != null");
                                }
                                bundle.setClassLoader(BinderHelper.class.getClassLoader());
                                BinderParcelable bp = bundle.getParcelable(BinderProvider.BINDER_KEY);
                                if (bp != null && bp.iBinder != null) {
                                    if (DEBUG) {
                                        LogHelper.d(TAG, "BinderParcelable != null");
                                    }
                                    iBinder = bp.iBinder;
                                    sBinderMap.put(name, iBinder);
                                    iBinder.linkToDeath(new IBinder.DeathRecipient() {
                                        @Override
                                        public void binderDied() {
                                            sBinderMap.remove(name);
                                        }
                                    }, 0);
                                }
                            }
                        } catch (Throwable e) {
                            if (DEBUG) {
                                throw new RuntimeException("getService service=" + name
                                        + ", processType=" + processType, e);
                            }
                        }
                    }
                    return iBinder;
                }
        }
    }
}
