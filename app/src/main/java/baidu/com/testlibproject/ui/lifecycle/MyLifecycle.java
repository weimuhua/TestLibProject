package baidu.com.testlibproject.ui.lifecycle;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

public interface MyLifecycle extends LifecycleObserver {

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}