@file:Suppress("ConstantConditionIf", "DEPRECATION")

package baidu.com.testlibproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.audio.AudioRecordDemoActivity
import baidu.com.testlibproject.composeui.ComposeMainActivity
import baidu.com.testlibproject.coroutines.CoroutinesActivity
import baidu.com.testlibproject.coroutines.coroutinesExample4
import baidu.com.testlibproject.db.StationDbFactory
import baidu.com.testlibproject.intent.IntentTestActivity
import baidu.com.testlibproject.plugin.PluginActivity
import baidu.com.testlibproject.sensor.CameraActivity
import baidu.com.testlibproject.sensor.CompassActivity
import baidu.com.testlibproject.sensor.LocationMgrActivity
import baidu.com.testlibproject.service.*
import baidu.com.testlibproject.test.KotlinSingleton
import baidu.com.testlibproject.ui.UiTestActivity
import com.anggrayudi.storage.media.MediaStoreCompat
import com.anggrayudi.storage.media.MediaType
import com.example.nativelib.NativeLib
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import me.wayne.annotation.PluginCenterHolder
import kotlin.system.measureTimeMillis

@PluginCenterHolder
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var context: Context
    private lateinit var listView: ListView
    private val coroutinesScope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        KotlinSingleton.getInstance(this).gogogo()

        coroutinesExample4()

        initView()
        initData()
        testProvider()
        testMmkv()
        testLoadAudioFiles()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutinesScope.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123456 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            LogHelper.i(TAG, "selectedImageUri $selectedImageUri")
            if (selectedImageUri == null) {
                return
            }

            LogHelper.i(TAG, "selectedImageUri type ${contentResolver.getType(selectedImageUri)}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE) {
            if (hasPermission()) {
                coroutinesScope.launch {
                    loadAllAudioFilesByLib()
                }
            }
        }
    }

    private fun initView() {
        listView = findViewById(R.id.list_view)
    }

    private fun initData() {
        val adapter = SimpleAdapter(context).apply {
            setStrArr(resources.getStringArray(R.array.activity_item))
        }
        listView.let {
            it.adapter = adapter
            it.onItemClickListener = this
        }

        coroutinesScope.launch {
            val cost = measureTimeMillis {
                val nativeLib = NativeLib()
                val a = 66
                val b = 22
                LogHelper.i(TAG, "native str ${nativeLib.stringFromJNI()}")
                LogHelper.i(TAG, "addNative, $a + $b = ${nativeLib.addNative(a, b)}")
                LogHelper.i(TAG, "subtractNative, $a - $b = ${nativeLib.subtractNative(a, b)}")
            }
            LogHelper.i(TAG, "invoke native cost $cost")

            testService()
        }
    }

    private suspend fun testService() {
        if (DEBUG) {
            LogHelper.d(TAG, "This code runs in Coroutines, current thread = " + Thread.currentThread())
        }
        try {
            val result = MainServiceClient.getInstance(context).add(2, 3, true)
            if (DEBUG) LogHelper.d(TAG, "Service add, result : $result")

            val subBinderA = MainServiceClient.getInstance(context).getSubInterfaceA(true)
            val subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA)
            subInterfaceA.methodA1()
            subInterfaceA.methodB1()
            subInterfaceA.methodC1()
            delay(1000L)
            if (DEBUG) {
                LogHelper.d(TAG, "complete invoke SubInterfaceA!")
            }
        } catch (e: RemoteException) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        } catch (e: ServiceNotAvailable) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        }
    }

    @SuppressLint("Recycle")
    private fun testProvider() {
        val classLoader = classLoader
        if (DEBUG) {
            LogHelper.d(
                TAG,
                "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode()
            )
        }


        val resolver = context.contentResolver
        val url = Uri.withAppendedPath(
            Constants.DB_AUTHORITY_URI,
            StationDbFactory::class.java.name + "/" + "test"
        )
        resolver.query(url, null, null, null, null)
    }

    private fun testMmkv() {
        coroutinesScope.launch {
            MMKV.initialize(this@MainActivity)

            var time = System.currentTimeMillis()
            val sp = getSharedPreferences("legacy", MODE_PRIVATE)
            val editor = sp.edit()
            for (i in 0..1000) {
                editor.putString("index$i", i.toString())
            }
            editor.apply()
            LogHelper.d(TAG, "sp putString cost : ${System.currentTimeMillis() - time}")

            time = System.currentTimeMillis()
            val kv = MMKV.mmkvWithID("legacy_data")
            kv.importFromSharedPreferences(sp)
            editor.clear().commit()
            LogHelper.d(
                TAG,
                "importFromSharedPreferences cost : ${System.currentTimeMillis() - time}"
            )

            LogHelper.d(TAG, "testMmkv run")
            val kvInner = MMKV.mmkvWithID("inner")
            for (i in 0..1000) {
                kvInner.encode("index", i)
            }
            LogHelper.d(TAG, "cost: ${System.currentTimeMillis() - time}")

            time = System.currentTimeMillis()
            val kvIpc = MMKV.mmkvWithID("ipc", MMKV.MULTI_PROCESS_MODE)
            for (i in 0..1000) {
                kvIpc.encode("index", i)
            }
            LogHelper.d(TAG, "cost: ${System.currentTimeMillis() - time}")
        }
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            INTENT_TEST_UI_ACTIVITY -> startActivity(Intent(context, UiTestActivity::class.java))
            INTENT_TEST_INTENT_ACTIVITY -> startActivity(
                Intent(
                    context,
                    IntentTestActivity::class.java
                )
            )
            INTENT_TELEPHONY_MANAGER -> startActivity(
                Intent(
                    context,
                    TelephonyMgrActivity::class.java
                )
            )
            INTENT_SMS_MANAGER -> startActivity(Intent(context, SmsMgrActivity::class.java))
            INTENT_AUDIO_MANAGER -> startActivity(Intent(context, AudioMgrActivity::class.java))
            INTENT_VIBRATOR_ACTIVITY -> startActivity(Intent(context, VibratorActivity::class.java))
            INTENT_COMPASS_ACTIVITY -> startActivity(Intent(context, CompassActivity::class.java))
            INTENT_LOCATION_MANAGER -> startActivity(
                Intent(
                    context,
                    LocationMgrActivity::class.java
                )
            )
            INTENT_CAMERA_ACTIVITY -> startActivity(Intent(context, CameraActivity::class.java))
            INTENT_PLUGIN_ACTIVITY -> startActivity(Intent(context, PluginActivity::class.java))
            INTENT_COROUTINES_ACTIVITY -> startActivity(
                Intent(
                    context,
                    CoroutinesActivity::class.java
                )
            )
            INTENT_COMPOSE_UI_ACTIVITY -> startActivity(
                Intent(
                    context,
                    ComposeMainActivity::class.java
                )
            )
            INTENT_AUDIO_RECORD_DEMO_ACTIVITY -> startActivity(
                Intent(
                    context,
                    AudioRecordDemoActivity::class.java
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun testLoadAudioFiles() {
        if (hasPermission()) {
            coroutinesScope.launch {
                loadAllAudioFilesByLib()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_CODE)
        }
    }

    private fun loadAllAudioFilesByLib() {
        val list = MediaStoreCompat.fromMediaType(this, MediaType.AUDIO)
        val legalExtensionList = listOf("mp4", "MP4", "mp3", "MP3", "m4a")
        val newList = list.filter {
            legalExtensionList.contains(it.extension) && !it.absolutePath.contains("ringtone")
        }
        for (file in newList) {
            LogHelper.i(TAG, "audio file: ${file.baseName}, ${file.extension}, ${file.absolutePath}")
        }
    }

    private fun testPickAudioFile() {
        val requestAudioFile = 123456
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/*", "audio/*"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }
//        val intent = Intent(
//            Intent.ACTION_PICK,
//            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        )

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, requestAudioFile)
        } else {
            Toast.makeText(this, "cannot handle ACTION_GET_CONTENT", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val TAG = "MainActivityTAG"
        private const val DEBUG = FeatureConfig.DEBUG

        private const val REQ_CODE = 1001
        private const val INTENT_TEST_UI_ACTIVITY = 0
        private const val INTENT_TEST_INTENT_ACTIVITY = 1
        private const val INTENT_TELEPHONY_MANAGER = 2
        private const val INTENT_SMS_MANAGER = 3
        private const val INTENT_AUDIO_MANAGER = 4
        private const val INTENT_VIBRATOR_ACTIVITY = 5
        private const val INTENT_COMPASS_ACTIVITY = 6
        private const val INTENT_LOCATION_MANAGER = 7
        private const val INTENT_CAMERA_ACTIVITY = 8
        private const val INTENT_PLUGIN_ACTIVITY = 9
        private const val INTENT_COROUTINES_ACTIVITY = 10
        private const val INTENT_COMPOSE_UI_ACTIVITY = 11
        private const val INTENT_AUDIO_RECORD_DEMO_ACTIVITY = 12
    }
}
