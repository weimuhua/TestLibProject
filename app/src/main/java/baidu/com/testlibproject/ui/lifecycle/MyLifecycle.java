package baidu.com.testlibproject.ui.lifecycle;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.support.annotation.Nullable;

public interface MyLifecycle extends LifecycleObserver {

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}