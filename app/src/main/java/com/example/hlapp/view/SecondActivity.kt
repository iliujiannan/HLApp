package com.example.hlapp.view

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.hlapp.R
import com.example.hlapp.api.DetectResponse
import com.example.hlapp.base.BaseActivity
import com.example.hlapp.util.ImageUtil
import com.example.hlapp.viewmodel.AiKsViewModel
import com.facebook.drawee.view.SimpleDraweeView

class SecondActivity : BaseActivity() {

    companion object {
        fun start(context: Context, bundle: Bundle) {
            context.startActivity(Intent(context, SecondActivity::class.java).apply {
                putExtra("data", bundle)
            })
        }
    }

    private val viewModel by lazy { ViewModelProviders.of(this)[AiKsViewModel::class.java] }

    private fun initComponent() {
        viewModel.detectResult.observe(this, Observer { handleDetectResult(it) })
        intent.getBundleExtra("data")?.apply {
            findViewById<TextView>(R.id.tips).text = resources.getString(R.string.hlapp_rec_result_tips, getString("path", ""))
            ImageUtil.loadLocalImage(findViewById<SimpleDraweeView>(R.id.photo), getString("path", ""))
            viewModel.doDetect(getString("path", ""))
        }
    }

    private fun handleDetectResult(result: DetectResponse) {
        findViewById<TextView>(R.id.detect_result).text = result.recommend
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seceond)
        initComponent()
        initData()
    }

    private fun initData() {

    }

}