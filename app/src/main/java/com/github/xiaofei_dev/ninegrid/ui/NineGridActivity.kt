package com.github.xiaofei_dev.ninegrid.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.github.xiaofei_dev.ninegrid.R
import com.github.xiaofei_dev.ninegrid.extensions.getViewBitmap
import com.github.xiaofei_dev.ninegrid.extensions.saveImageToDir
import kotlinx.android.synthetic.main.activity_nine_grid.*
import kotlinx.android.synthetic.main.toolbar.*

class NineGridActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nine_grid)
        imagePath = intent.getStringExtra(IMAGE_PATH)
        initViews()
    }

    //activity 已加载完成
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //图片最终尺寸
        var width:Int
        var height:Int
        //待加载图像的实际尺寸
        val bmpWidth:Float
        val bmpHeight:Float
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath,options)
        bmpWidth = options.outWidth.toFloat()
        bmpHeight = options.outHeight.toFloat()

//        //如果待处理图像的长宽比太奇葩则拒绝服务
//        if (bmpWidth/bmpHeight > 2f || bmpWidth/bmpHeight < 0.5f){
//            toast(R.string.size_error)
//            finish()
//            return
//        }

        //这一堆判断看来挺乱，其实就是把待加载图片的最终尺寸设为充满容器且没有白边且适应屏幕
        if (bmpWidth == bmpHeight){
            width = imageContainer.measuredWidth
            height = width
        }else if(bmpWidth > bmpHeight){
            width = imageContainer.measuredWidth
            height = (width.toFloat()/bmpWidth * bmpHeight).toInt()
        }else{
            height = imageContainer.measuredHeight
            width = (height.toFloat()/bmpHeight * bmpWidth).toInt()
            if(width > imageContainer.measuredWidth){
                val oldWidth = width
                width = imageContainer.measuredWidth
                height = (width.toFloat()/oldWidth * height).toInt()
            }
        }
        Glide.with(this).load(imagePath).asBitmap().skipMemoryCache(true).override(width,height)/*.centerCrop()*//*.fitCenter()*/.into(nineGridImageView)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initViews(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_save.setOnClickListener {
            saveImageToDir(getViewBitmap(nineGridImageView),"NineGrid")
        }

        //seekBar 控制线条粗细
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nineGridImageView.setPaintWidth(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }


}
