package com.github.xiaofei_dev.ninegrid.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.github.xiaofei_dev.ninegrid.R
import com.github.xiaofei_dev.ninegrid.extensions.deleteFiles
import com.github.xiaofei_dev.ninegrid.extensions.getViewBitmap
import com.github.xiaofei_dev.ninegrid.extensions.requestPermission
import com.github.xiaofei_dev.ninegrid.extensions.saveImageToDir
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File


//未继承 BaseActivity
class MainActivity : AppCompatActivity() {
    companion object {
        val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
        val REQUEST_SELECT_PICTURE = 0x01
    }

    private var mUri:Uri = Uri.parse("")
    private var photo:String = ""
    //待加载图像的实际尺寸
    private var bmpWidth:Float = 0f
    private var bmpHeight:Float = 0f

    private var mPressedTime: Long = 0

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        //删除缓存图片文件
        deleteFiles(cacheDir)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        if (resultCode == android.app.Activity.RESULT_OK && requestCode == REQUEST_SELECT_PICTURE) {
            if (data != null) {
                mUri = data.data
                val path = getRealPathFromURI(mUri)
                photo = path

                Log.d("MainActivity","${data.data.toString()}\n${photo}")

                mainPhotoView.visibility = View.VISIBLE
//                mainPhotoView.postDelayed(Runnable {  Glide.with(this).load(photo).asBitmap().into(mainPhotoView) },2000)

                Glide.with(this).load(photo).asBitmap().into(mainPhotoView)

                //用节省开销的优雅方式测量图片尺寸
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path,options)
                bmpWidth = options.outWidth.toFloat()
                bmpHeight = options.outHeight.toFloat()

                mainBottomBar.visibility = View.VISIBLE
            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            val uri:Uri? = UCrop.getOutput(data!!)
            if (uri != null){
                mUri = uri
                val path = getRealPathFromURI(uri)
                photo = path
                Log.d("MainActivity","${uri}\n${photo}")

//                val pho = BitmapFactory.decodeFile(photo)
//                val stream = ByteArrayOutputStream()
//                pho.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                Glide.with(this).load(stream.toByteArray()).asBitmap().skipMemoryCache(true).into(mainPhotoView)
//                mainPhotoView.postDelayed(Runnable {
//                    Glide.with(this).load(/*photo*/stream.toByteArray()).asBitmap().skipMemoryCache(true).into(mainPhotoView)
//                    //用节省开销的优雅方式测量图片尺寸
//                    val options: BitmapFactory.Options = BitmapFactory.Options()
//                    options.inJustDecodeBounds = true
//                    BitmapFactory.decodeFile(path,options)
//                    bmpWidth = options.outWidth.toFloat()
//                    bmpHeight = options.outHeight.toFloat()
//                },1000)

                Glide.with(this).load(photo).asBitmap().skipMemoryCache(true).into(mainPhotoView)

                //用节省开销的优雅方式测量图片尺寸
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path,options)
                bmpWidth = options.outWidth.toFloat()
                bmpHeight = options.outHeight.toFloat()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tool_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save ->{
                saveImageToDir(getViewBitmap(mainPhotoView),"NineGrid")
            }
            R.id.about ->{
                startActivity<AboutActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val mNowTime = System.currentTimeMillis()//记录本次按键时刻
        if (mNowTime - mPressedTime > 1000) {//比较两次按键时间差
            toast("再按一次退出应用")
            mPressedTime = mNowTime
        } else {
            //退出程序
            super.onBackPressed()
        }
    }

    private fun initView(){
        toolBarMain.title = ""
        setSupportActionBar(toolBarMain)
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            btn_save.visibility = View.GONE
            start.setOnClickListener {
            pickFromGallery()
        }

        btn_action_crop.setOnClickListener {

            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.PNG)
            //设置裁剪图片的图片质量
            options.setCompressionQuality(90)
            //设置你想要指定的可操作的手势
            options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
            //是否能调整裁剪框
            options.setFreeStyleCropEnabled(true)
            //设置 toolbar 颜色
            options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            //状态栏颜色
            options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//        options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.bottom_bar_bk));
//        options.setCropFrameColor(ContextCompat.getColor(this, R.color.bottom_bar_bk));
            options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.active))
//            val uCrop = UCrop.of(mUri, Uri.fromFile(File(Environment.getExternalStorageDirectory(), "NineGrid/${System.currentTimeMillis()}.png")))
            val uCrop = UCrop.of(mUri, Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}.png")))
                    .withOptions(options)
            uCrop.start(this@MainActivity)
        }

        btn_action_qq.setOnClickListener {
            if(bmpWidth != bmpHeight){
                toast("图像宽高不一致")
//                return@setOnClickListener
            }else{
                startActivity<ClipQQActivity>(BaseActivity.IMAGE_PATH to photo)
            }
        }

        btn_action_wechat.setOnClickListener {
            if(bmpWidth != bmpHeight){
                toast("图像宽高不一致")
//                return@setOnClickListener
            }else{
                startActivity<ClipWeChatActivity>(BaseActivity.IMAGE_PATH to photo)
            }

        }

        btn_action_nine.setOnClickListener {
            //如果待处理图像的长宽比太奇葩则拒绝服务
            if (bmpWidth/bmpHeight > 2f || bmpWidth/bmpHeight < 0.5f){
                toast(R.string.size_error)
//                return@setOnClickListener
            }else{
                startActivity<NineGridActivity>(BaseActivity.IMAGE_PATH to photo)
            }

        }

        btn_open_new.setOnClickListener {
//            PhotoPicker.builder()
//                    .setPhotoCount(1)
//                    .setShowCamera(true)
//                    .setShowGif(true)
//                    .setPreviewEnabled(true)
//                    .start(this, PhotoPicker.REQUEST_CODE)
            pickFromGallery()
        }
    }

    //权限请求回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE_READ_ACCESS_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    //选择图片
    private fun pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        } else {
//            val intent = Intent()
//            intent.type = "image/*"
//            intent.action = Intent.ACTION_GET_CONTENT
//            intent.addCategory(Intent.CATEGORY_OPENABLE)

            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            //这样会有一个自定义选择器
//            startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image_hint)), REQUEST_SELECT_PICTURE);
            startActivityForResult(intent, REQUEST_SELECT_PICTURE)
        }
    }

    //获取图片的真实路径,很重要
    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
//        return File(result).absolutePath
        return result
    }
}
