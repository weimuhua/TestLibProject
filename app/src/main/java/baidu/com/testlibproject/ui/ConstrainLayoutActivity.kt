package baidu.com.testlibproject.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import baidu.com.testlibproject.R
import kotlinx.android.synthetic.main.activity_constrain_layout.*

class ConstrainLayoutActivity : Activity() {

    companion object {
        private val TEST_PROGRAM_ARRAY: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constrain_layout)
        spinner_tv.setOnClickListener { spinner.performClick() }
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, TEST_PROGRAM_ARRAY)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                spinner_tv.text = TEST_PROGRAM_ARRAY[position]
                frame_layout.removeAllViews()
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
                spinner_tv.text = ""
            }
        }
    }

    private fun addConstraintBasic() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_basic,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    private fun addConstraintGoneMargin() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_margin,
                frame_layout, false)
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
        frame_layout.addView(subView)
    }

    private fun addConstraintBias() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_bias,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    private fun addConstraintRatio() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_ratio,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    private fun addConstraintChains() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_chains,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    private fun addConstraintGuideLine() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_guideline,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    private fun addConstraintDimension() {
        val subView = LayoutInflater.from(this).inflate(R.layout.layout_constraint_dimension,
                frame_layout, false)
        frame_layout.addView(subView)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun addRelativeLayoutTest() {
        val testLayout = ConstraintLayoutTestLayout(this)
        testLayout.testRelativeLayout()
        frame_layout.addView(testLayout)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun addConstraintLayoutTest() {
        val testLayout = ConstraintLayoutTestLayout(this)
        testLayout.testConstraintLayout()
        frame_layout.addView(testLayout)
    }
}