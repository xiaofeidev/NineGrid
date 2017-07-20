package com.github.xiaofei_dev.ninegrid.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.view.View
import com.github.xiaofei_dev.ninegrid.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception



/**
 * Created by xiaofei on 2017/6/22.
 */

//给 Activity 扩展的方法，dp 转 px
fun Activity.dp2px(dp: Float): Int {
    val px = Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources
            .displayMetrics))
    return px
}

//给 Activity 扩展的方法，保存图片到 SD 卡指定名称的文件夹
fun Activity.saveImageToDir(bmp: Bitmap,dir:String){
    var save:Boolean = false
    val appDir: File = File(Environment.getExternalStorageDirectory(), dir)
    if(!appDir.exists()){
        appDir.mkdir()
    }
    val fileName:String = System.currentTimeMillis().toString() + ".jpg"
    val file: File = File(appDir, fileName)
    try {
        val fos: FileOutputStream = FileOutputStream(file)
        save = bmp.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.flush()
        fos.close()
    }catch (e: Exception){
        save = false
        e.printStackTrace()
    }

    if(save){
        ToastUtil.showToast(applicationContext,"图片已成功保存至：$appDir")
        //通知图库更新
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.path)))
    }else{
        ToastUtil.showToast(applicationContext,R.string.save_failure)
    }
}

//给 Activity 扩展的方法，获取普通 View 截图
fun Activity.shotView(view: View):Bitmap{
    // 设置控件允许绘制缓存
    view.setDrawingCacheEnabled(true)
    // 获取控件的绘制缓存（快照）
    val bitmap = view.drawingCache
    view.setDrawingCacheEnabled(false)
//    view.destroyDrawingCache()
    return bitmap
}

fun Activity.getViewBitmap(v: View): Bitmap {
    v.isDrawingCacheEnabled = true
    v.buildDrawingCache()
//    if (Build.VERSION.SDK_INT >= 11) {
//        v.measure(View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(v.height, View.MeasureSpec.EXACTLY))
//        v.layout(v.x.toInt(), v.y.toInt(), v.x.toInt() + v.measuredWidth, v.y.toInt() + v.measuredHeight)
//    }
    val bitmap = Bitmap.createBitmap(v.drawingCache, 0, 0, v.measuredWidth, v.measuredHeight)
    v.isDrawingCacheEnabled = false
    v.destroyDrawingCache()
    return bitmap
}

