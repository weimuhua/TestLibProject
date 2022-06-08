package baidu.com.testlibproject.ui

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import baidu.com.commontools.utils.MobileInfo


class BottomDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                MobileInfo.dp2px(requireContext(), 200f).toInt()
            )
            val contentView = FrameLayout(requireContext()).apply {
                background = ColorDrawable(Color.GREEN)
            }
            setContentView(contentView, params)
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_NoTitleBar_Fullscreen)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes.gravity = Gravity.BOTTOM
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "BottomDialogFragment_TAG"

        fun showDialog(fragmentManager: FragmentManager): BottomDialogFragment? {
            val fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
            fragment?.let {
                fragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
            }
            return try {
                val dialog = BottomDialogFragment()
                dialog.show(fragmentManager, FRAGMENT_TAG)
                dialog
            } catch (e: IllegalStateException) {
                null
            }
        }
    }
}