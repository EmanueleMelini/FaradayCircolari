package org.experimentalplayers.faraday.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    var myContentView: Int = 0

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(myContentView)

    }

    override fun onResume() {
        super.onResume()

        mContext = this

    }


}