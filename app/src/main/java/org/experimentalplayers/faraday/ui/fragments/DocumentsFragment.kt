package org.experimentalplayers.faraday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.experimentalplayers.faraday.R

class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documents, container, false)
    }

}