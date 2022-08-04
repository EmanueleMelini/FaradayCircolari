package org.experimentalplayers.faraday.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import doAfter
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.ui.adapters.ScreenSlidePagerAdapter
import org.experimentalplayers.faraday.ui.fragments.*
import simpleToast
import timber.log.Timber

class HomeActivity : BaseActivity() {

    private lateinit var viewPager2: ViewPager2
    lateinit var bottomBar: AnimatedBottomBar

    private lateinit var list: ArrayList<Fragment>
    lateinit var onBackPressedFragment: () -> Unit
    var canExit: Boolean = true
    var close: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        myContentView = R.layout.activity_home
        super.onCreate(savedInstanceState)

        Timber.d("HomeActivity.onCreate")

        bottomBar = findViewById(R.id.home_bottom_bar)
        viewPager2 = findViewById(R.id.home_pager)

        list = arrayListOf(
            HomeFragment.newInstance(),
            DocumentsFragment.newInstance(),
            HistoryFragment.newInstance(),
            SettingsFragment.newInstance(),
            InfoFragment.newInstance()
        )

        viewPager2.adapter = ScreenSlidePagerAdapter(this, list)

    }

    override fun onResume() {
        super.onResume()

        Timber.d("HomeActivity.onResume")

        mContext = this

        bottomBar.setupWithViewPager2(viewPager2)

    }

    private fun cancelClose() {
        close = true
        simpleToast(R.string.close_app_message)
        val backFun = { close = false }
        backFun.doAfter(3000L)
    }

    fun setOnBackPressedDispatcher(doOnBackPressed: () -> Unit) {
        this.onBackPressedDispatcher.addCallback {
            Timber.d("HomeActivity BACK PRESSED")
            if(canExit) {
                if(close)
                    onBackPressed()
                else
                    cancelClose()
            } else
                doOnBackPressed()
        }
    }

}