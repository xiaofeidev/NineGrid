package com.github.xiaofei_dev.ninegrid.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by xiaofei on 2017/7/19.
 */
object ToastUtil {

    private var toast: Toast? = null

    fun showToast(context: Context, content: String) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_LONG)
        } else {
            toast!!.setText(content)
        }
        toast!!.show()
    }

    fun showToast(context: Context, content: Int) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_LONG)
        } else {
            toast!!.setText(content)
        }
        toast!!.show()
    }
}