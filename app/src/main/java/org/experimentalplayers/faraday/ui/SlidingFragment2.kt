package org.experimentalplayers.faraday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.experimentalplayers.faraday.R
class SlidingFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sliding_2, container, false)
    }

    companion object{
        fun newInstance() = SlidingFragment2()
    }

}