package baidu.com.testlibproject.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import baidu.com.testlibproject.R

class ConstrainLayoutActivity : Activity() {

    companion object {
        private val TEST_PROGRAM_ARRAY: Array<String> = 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            arrayOf(
                    "相对位置demo",
                    "Margin demo",
                    "bias demo",
                    "ratio demo",
                    "chains demo",
                    "guideline demo",
                    "尺寸约束",
                    "RL性能测试",
                    "CL性能测试"
            )
        } else {
            arrayOf(
                    "相对位置demo",
                    "Margin demo",
                    "bias demo",
                    "ratio demo",
                    "尺寸约束",
                    "chains demo",
                    "guideline demo")
        }
    }

    private lateinit var spinner: Spinner
    private lateinit var spinnerTv: TextView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constrain_layout)
        spinnerTv = findViewById(R.id.spinner_tv)
        spinner = findViewById(R.id.spinner)
        frameLayout = findViewById(R.id.frame_layout)

        spinnerTv.setOnClickListener { spinner.performClick() }
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, TEST_PROGRAM_ARRAY)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                spinnerTv.text = TEST_PROGRAM_ARRAY[position]
                frameLayout.removeAllViews()
                when (id) {
                    0L -> {
                        addConstraintBasic()
                    }
                    1L -> {
                        addConstraintGoneMargin()
                    }
                    2L -> {
                        addConstraintBias()
                    }
                    3L -> {
                        addConstraintRatio()
                    }
                    4L -> {
                        addConstraintChains()
                    }
                    5L -> {
                        addConstraintGuideLine()
                    }
                    6L -> {
                        addConstraintDimension()
                    }
                    7L -> {
                        addRelativeLayoutTest()
                    }
                    8L -> {
                        addConstraintLayoutTest()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerTv.text = ""
            }
        }
    }

    private fun addConstraintBasic() {
        LayoutInflater.from(this).inflate(
            R.layout.layout_constraint_basic, frameLayout, true)
    }

    private fun addConstraintGoneMargin() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_margin,
                frameLayout, false)
        val normalMarginBtn = subView.findViewById<Button>(R.id.normal_margin_btn)
        val normalTvB = subView.findViewById<TextView>(R.id.normal_tv_b)
        normalMarginBtn.setOnClickListener {
            if (normalTvB.visibility == View.VISIBLE) {
                normalTvB.visibility = View.GONE
            } else {
                normalTvB.visibility = View.VISIBLE
            }
        }
        val goneMarginBtn = subView.findViewById<Button>(R.id.gone_margin_btn)
        val goneTvB = subView.findViewById<TextView>(R.id.gone_tv_b)
        goneMarginBtn.setOnClickListener {
            if (goneTvB.visibility == View.VISIBLE) {
                goneTvB.visibility = View.GONE
            } else {
                goneTvB.visibility = View.VISIBLE
            }
        }
        frameLayout.addView(subView)
    }

    private fun addConstraintBias() {
        LayoutInflater.from(this).inflate(
            R.layout.layout_constraint_bias, frameLayout, true)
    }

    private fun addConstraintRatio() {
        LayoutInflater.from(this).inflate(
            R.layout.layout_constraint_ratio, frameLayout, true)
    }

    private fun addConstraintChains() {
        LayoutInflater.from(this).inflate(
            R.layout.layout_constraint_chains, frameLayout, true)
    }

    private fun addConstraintGuideLine() {
        LayoutInflater.from(this).inflate(
            R.layout.layout_constraint_guideline, frameLayout, true)
    }

    private fun addConstraintDimension() {
        LayoutInflater.from(this).inflate(R.layout.layout_constraint_dimension,
                frameLayout, true)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun addRelativeLayoutTest() {
        val testLayout = ConstraintLayoutTestLayout(this)
        testLayout.testRelativeLayout()
        frameLayout.addView(testLayout)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun addConstraintLayoutTest() {
        val testLayout = ConstraintLayoutTestLayout(this)
        testLayout.testConstraintLayout()
        frameLayout.addView(testLayout)
    }
}