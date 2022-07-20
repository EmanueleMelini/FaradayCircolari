package org.experimentalplayers.faraday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.experimentalplayers.faraday.R
class SlidingFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sliding_1, container, false)
    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(context, "TEST", Toast.LENGTH_LONG).show()
    }

    companion object{
        fun newInstance() = SlidingFragment1()
    }

}