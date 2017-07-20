package com.github.xiaofei_dev.ninegrid.ui

import android.os.Bundle
import com.github.xiaofei_dev.ninegrid.R
import com.github.xiaofei_dev.ninegrid.extensions.OpenUtil
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemOpenSource.setOnClickListener {
            val url = getString(R.string.openSourceLink)
            OpenUtil.openLink(this, null, url, false)
        }
        itemScoreAndFeedback.setOnClickListener {
            OpenUtil.openApplicationMarket(packageName, "com.coolapk.market",
                    this)
        }
        itemDonate.setOnClickListener {
            OpenUtil.alipayDonate(this)
        }
    }
}
