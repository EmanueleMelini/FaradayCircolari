package org.experimentalplayers.faraday.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import go
import goNoTransaction
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.service.FetchService

class HomeActivity : BaseActivity() {

    private lateinit var quote: TextView
    private lateinit var bottomBar: AnimatedBottomBar

    //inner class

    override fun onCreate(savedInstanceState: Bundle?) {
        myContentView = R.layout.activity_home
        super.onCreate(savedInstanceState)

        quote = findViewById(R.id.home_quote)
        bottomBar = findViewById(R.id.home_bottom_bar)

    }

    override fun onResume() {
        super.onResume()

        bottomBar.selectTabById(R.id.tab_home)

        bottomBar.setOnTabSelectListener(object: AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newIndex) {
                    1 -> goNoTransaction(TestActivity::class.java)
                    0 -> go(SlidingActivity::class.java)
                    else -> Toast.makeText(mContext, "TEST", Toast.LENGTH_LONG).show()
                }
            }
        })

        mContext = this

        val quotes = listOf("Porcodio", "Porcamadonna", "Zio pera", "Scaccia frocio", "Accendiamo i cazzi")

        quote.text = quotes[(quotes.indices).random()]

        startService(Intent(applicationContext, FetchService::class.java))

    }

}