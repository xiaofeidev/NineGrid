package com.github.xiaofei_dev.ninegrid.ui


import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.github.xiaofei_dev.ninegrid.R
import com.github.xiaofei_dev.ninegrid.extensions.getViewBitmap
import com.github.xiaofei_dev.ninegrid.extensions.saveImageToDir
import kotlinx.android.synthetic.main.activity_clip_we_chat.*
import kotlinx.android.synthetic.main.toolbar.*

class ClipWeChatActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_we_chat)
        imagePaths = intent.getStringArrayListExtra(IMAGE_PATH)
        initView()
    }


    private fun initView(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //待加载图像的实际尺寸
        var bmpWidth:Float
        var bmpHeight:Float

        //获取屏幕尺寸,这里主要使用宽度
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        //设置九宫格容器的宽高都为屏幕宽度
        val layoutParams: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(width, width)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        container.layoutParams = layoutParams

        Glide.with(this).load(imagePaths[0]).asBitmap().skipMemoryCache(true).into(object:SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>?) {
                bmpWidth = resource.width.toFloat()
                bmpHeight = resource.height.toFloat()

//                if(bmpWidth != bmpHeight){
//                    toast("图像宽高不一致")
//                    finish()
//                    return
//                }
                //需要裁剪的长度
                val clipStep = (bmpWidth * 0.3269).toInt()
                //中间边距的二分之一
                val marginStep = bmpWidth * 0.00455
                //图片宽度的三分之一
                val bmpWidthThird = bmpWidth/3
                //图片宽度的三分之二
                val bmpWidthThirdTow = bmpWidthThird * 2

                imageView1.setImageBitmap(Bitmap
                        .createBitmap(resource,0,0,clipStep,clipStep))

                imageView2.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThird + marginStep).toInt(),0,clipStep
                                ,clipStep))

                imageView3.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThirdTow + marginStep).toInt(),0
                                ,clipStep,clipStep))

                imageView4.setImageBitmap(Bitmap
                        .createBitmap(resource,0,(bmpWidthThird + marginStep).toInt(),clipStep
                                ,clipStep))

                imageView5.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThird + marginStep).toInt(),(bmpWidthThird + marginStep).toInt()
                                ,clipStep,clipStep))

                imageView6.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThirdTow + marginStep).toInt(),(bmpWidthThird + marginStep).toInt()
                                ,clipStep,clipStep))

                imageView7.setImageBitmap(Bitmap
                        .createBitmap(resource,0,(bmpWidthThirdTow + marginStep).toInt()
                                ,clipStep,clipStep))

                imageView8.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThird + marginStep).toInt(),(bmpWidthThirdTow + marginStep).toInt()
                                ,clipStep,clipStep))

                imageView9.setImageBitmap(Bitmap
                        .createBitmap(resource,(bmpWidthThirdTow + marginStep).toInt(),(bmpWidthThirdTow + marginStep).toInt()
                                ,clipStep,clipStep))

//                resource.recycle()
            }
        })

        btn_save.setOnClickListener {
            //保存裁剪的所有 9 张图片，此处可以进一步优化，比如保存成功的 Toast 提示
            saveImageToDir(getViewBitmap(imageView9),"NineGrid")
            saveImageToDir(getViewBitmap(imageView8),"NineGrid")
            saveImageToDir(getViewBitmap(imageView7),"NineGrid")
            saveImageToDir(getViewBitmap(imageView6),"NineGrid")
            saveImageToDir(getViewBitmap(imageView5),"NineGrid")
            saveImageToDir(getViewBitmap(imageView4),"NineGrid")
            saveImageToDir(getViewBitmap(imageView3),"NineGrid")
            saveImageToDir(getViewBitmap(imageView2),"NineGrid")
            saveImageToDir(getViewBitmap(imageView1),"NineGrid")
        }
    }
}
