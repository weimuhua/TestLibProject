package baidu.com.testlibproject


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SimpleAdapter : BaseAdapter {
    private var mCxt: Context? = null
    private var mStrArr: Array<String>? = null

    constructor(cxt: Context) {
        mCxt = cxt
    }

    constructor(cxt: Context, strArr: Array<String>) {
        mCxt = cxt
        mStrArr = strArr
    }

    fun setStrArr(strArr: Array<String>) {
        mStrArr = strArr
    }

    override fun getCount(): Int {
        return if (mStrArr != null) {
            mStrArr!!.size
        } else {
            0
        }
    }

    override fun getItem(position: Int): String? {
        return if (mStrArr != null) {
            mStrArr!![position]
        } else {
            null
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = LayoutInflater.from(mCxt)
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_activity_item, parent, false)
        }
        if (mStrArr != null && position <= mStrArr!!.size) {
            (convertView!!.findViewById<View>(R.id.item_textview) as TextView).text = mStrArr!![position]
        }
        convertView!!.tag = position
        return convertView
    }
}
