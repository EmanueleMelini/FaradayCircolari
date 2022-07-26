package org.experimentalplayers.faraday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.ui.adapter.ScreenSlidePagerAdapter
import org.experimentalplayers.faraday.ui.fragments.*

class HomeActivity : BaseActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomBar: AnimatedBottomBar
    private lateinit var list: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        myContentView = R.layout.activity_home
        super.onCreate(savedInstanceState)

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

        mContext = this

        bottomBar.selectTabById(R.id.tab_home)

        bottomBar.setupWithViewPager2(viewPager2)

        //TODO: background service or calls -> decide
        //startService(Intent(applicationContext, FetchService::class.java))

    }

}