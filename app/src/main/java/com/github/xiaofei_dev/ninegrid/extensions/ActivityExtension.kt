package com.github.xiaofei_dev.ninegrid.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.TypedValue
import android.view.View
import com.github.xiaofei_dev.ninegrid.R
import org.jetbrains.anko.toast
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
    var save:Boolean
    val appDir = File(Environment.getExternalStorageDirectory(), dir)
    if(!appDir.exists()){
        appDir.mkdir()
    }
    val fileName:String = System.currentTimeMillis().toString() + ".jpg"
    val file: File = File(appDir, fileName)
    try {
        val fos: FileOutputStream = FileOutputStream(file)
        save = bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
        fos.flush()
        fos.close()
    }catch (e: Exception){
        save = false
        toast("${e.message}")
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

////删除指定文件或整个文件夹的所有文件
//fun Activity.RecursionDeleteFile(file: File) {
//    if (file.isFile) {
//        file.delete()
//        return
//    }
//    if (file.isDirectory) {
//        val childFile = file.listFiles()
//        if (childFile == null || childFile.size == 0) {
//            file.delete()
//            return
//        }
//        for (f in childFile) {
//            RecursionDeleteFile(f)
//        }
//        file.delete()
//    }
//}
//删除缓存文件夹中的缓存图片
fun Activity.deleteFiles(file: File) {
    if (file.isDirectory) {
        val childFile = file.listFiles()
        if (childFile == null || childFile.size == 0) {
            file.delete()
            return
        }
        for (f in childFile) {
            if(f.isFile && f.path.endsWith(".png")){
                f.delete()
            }
        }
    }
}

//请求权限
fun Activity.requestPermission(permissions: Array<String>,requestCode: Int){
    ActivityCompat.requestPermissions(this, permissions, requestCode)
}

