package baidu.com.testlibproject.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import baidu.com.testlibproject.R
import baidu.com.testlibproject.SimpleAdapter
import baidu.com.testlibproject.service.NotificationActivity
import baidu.com.testlibproject.ui.lifecycle.MyLifecycleComponent
import kotlin.system.exitProcess

class UiTestActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_test_layout)
        initView()
        initData()

        lifecycle.addObserver(MyLifecycleComponent())
    }

    private fun initView() {
        mListView = findViewById(R.id.list_view)
    }

    private fun initData() {
        val strArr = resources.getStringArray(R.array.ui_test_item)
        val adapter = SimpleAdapter(this, strArr)
        mListView!!.adapter = adapter
        mListView!!.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            INTENT_AUTO_COMPLETE_TEXTVIEW -> {
                startActivity(Intent(this, TestAutoCompleteTvActivity::class.java))
                view.postDelayed({
                    exitProcess(-1)
                }, 10 * 1000L)
            }
            INTENT_TEST_GRID_VIEW_ACTIVITY -> {
                startActivity(Intent(this, TestGridViewActivity::class.java))
            }
            INTENT_TEST_SPINNER_ACTIVITY -> {
                startActivity(Intent(this, TestSpinnerActivity::class.java))
            }
            INTENT_TEST_TOAST_ACTIVITY -> {
                startActivity(Intent(this, TestToastActivity::class.java))
            }
            INTENT_TEST_DATE_TIME_PICKET -> {
                startActivity(Intent(this, DateTimePickerActivity::class.java))
            }
            INTENT_SEARCH_VIEW_ACTIVITY -> {
                startActivity(Intent(this, SearchViewActivity::class.java))
            }
            INTENT_NOTIFICATION_TEST -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
            INTENT_POPUP_WINDOW_ACTIVITY -> {
                startActivity(Intent(this, PopupWindowActivity::class.java))
            }
            INTENT_ACTION_BAR_ACTIVITY -> {
                startActivity(Intent(this, TestActionBarActivity::class.java))
            }
            INTENT_CONFIGURATION_TEST -> {
                startActivity(Intent(this, ConfigurationTest::class.java))
            }
            INTENT_CONSTRAIN_LAYOUT_ACTIVITY -> {
                startActivity(Intent(this, ConstrainLayoutActivity::class.java))}
            INTENT_TEST_WEBVIEW_ACTIVITY -> {
                startActivity(Intent(this, WebViewActivity::class.java))
            }
            INTENT_TEST_VIDEO_VIEW_ACTIVITY -> {
                startActivity(Intent(this, VideoViewActivity::class.java))
            }
            INTENT_TEST_SURFACE_VIEW_ACTIVITY -> {
                startActivity(Intent(this, SurfaceViewActivity::class.java))
            }
            INTENT_TEST_IMAGESPAN_ACTIVITY -> {
                startActivity(Intent(this, ImageSpanActivity::class.java))
            }
            INTENT_TEST_LOCAL_WEBVIEW_ACTIVITY -> {
                startActivity(Intent(this, LocalWebViewActivity::class.java))
            }
            INTENT_TEST_KOTLIN_CODE_ACTIVITY -> {
                startActivity(Intent(this, KotlinActivity::class.java))
            }
            INTENT_TEST_ROUND_PROGRESSBAR_ACTIVITY -> {
                startActivity(Intent(this, ProgressBarActivity::class.java))
            }
            INTENT_TEST_COMMIT_FRAGMENT_ACTIVITY -> {
                startActivity(Intent(this, MyFragmentActivity::class.java))
            }
        }
    }

    companion object {
        private const val INTENT_AUTO_COMPLETE_TEXTVIEW = 0
        private const val INTENT_TEST_GRID_VIEW_ACTIVITY = 1
        private const val INTENT_TEST_SPINNER_ACTIVITY = 2
        private const val INTENT_TEST_TOAST_ACTIVITY = 3
        private const val INTENT_TEST_DATE_TIME_PICKET = 4
        private const val INTENT_SEARCH_VIEW_ACTIVITY = 5
        private const val INTENT_NOTIFICATION_TEST = 6
        private const val INTENT_POPUP_WINDOW_ACTIVITY = 7
        private const val INTENT_ACTION_BAR_ACTIVITY = 8
        private const val INTENT_CONFIGURATION_TEST = 9
        private const val INTENT_CONSTRAIN_LAYOUT_ACTIVITY = 10
        private const val INTENT_TEST_WEBVIEW_ACTIVITY = 11
        private const val INTENT_TEST_VIDEO_VIEW_ACTIVITY = 12
        private const val INTENT_TEST_SURFACE_VIEW_ACTIVITY = 13
        private const val INTENT_TEST_IMAGESPAN_ACTIVITY = 14
        private const val INTENT_TEST_LOCAL_WEBVIEW_ACTIVITY = 15
        private const val INTENT_TEST_KOTLIN_CODE_ACTIVITY = 16
        private const val INTENT_TEST_ROUND_PROGRESSBAR_ACTIVITY = 17
        private const val INTENT_TEST_COMMIT_FRAGMENT_ACTIVITY = 18;
    }
}
