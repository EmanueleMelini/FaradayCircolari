package org.experimentalplayers.faraday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.experimentalplayers.faraday.R

class SlidingActivity : FragmentActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomBar: AnimatedBottomBar
    private lateinit var list: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sliding)

        viewPager2 = findViewById(R.id.pager)

        list = arrayListOf(
            SlidingFragment1.newInstance(),
            SlidingFragment2.newInstance(),
            SlidingFragment3.newInstance(),
            SlidingFragment4.newInstance(),
            SlidingFragment5.newInstance()
        )

        viewPager2.adapter = ScreenSlidePagerAdapter(this, list)

        bottomBar = findViewById(R.id.sliding_bottom_bar)

    }

    override fun onResume() {
        super.onResume()

        bottomBar.selectTabById(R.id.tab_home)

        bottomBar.setupWithViewPager2(viewPager2)

    }

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        private val list: ArrayList<Fragment>
    ) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = list.size

        override fun createFragment(position: Int): Fragment = list[position]

    }


}