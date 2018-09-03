package com.github.xiaofei_dev.ninegrid.ui

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseActivity : AppCompatActivity() {
    companion object{
        val IMAGE_PATH = "IMAGE_PATH"
        val TAG = "BaseActivity"
    }
    var imagePath: String = ""

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
