package org.experimentalplayers.faraday.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.Attachment
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.Type.AVVISO
import org.experimentalplayers.faraday.models.Type.CIRCOLARE
import org.experimentalplayers.faraday.ui.adapters.HomeAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import simpleToast
import timber.log.Timber
import kotlin.streams.toList

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var mContext: Context

    private lateinit var topText: TextView
    private lateinit var homeRecycler: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private var siteDocuments: List<SiteDocument> = emptyList()
    private var attachments: MutableList<Attachment> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeRecycler = view.findViewById(R.id.home_recycler_circolari)

        val itemAnimator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        homeRecycler.itemAnimator = itemAnimator

        //homeRecycler.itemAnimator = null
        //circolariRecycler.setHasFixedSize(true)
        siteDocuments = emptyList()
        homeRecycler.layoutManager = LinearLayoutManager(mContext)
        homeAdapter = HomeAdapter(mContext)
        //homeAdapter.setHasStableIds(true)
        homeRecycler.adapter = homeAdapter
        homeAdapter.submitList(siteDocuments)

        topText = view.findViewById(R.id.top_text)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

    }

    override fun onResume() {
        super.onResume()

        topText.text = "Bacheca"

        mContext = requireContext()

        FirestoreHelper.firestoreInstance.getDocuments() { docs ->
            if(docs != null) {

                siteDocuments = docs.toList()

                homeAdapter.submitList(siteDocuments)

                if(docs.isEmpty())
                    mContext.simpleToast(R.string.empty_docs)
            } else
                mContext.simpleToast(R.string.conn_error_text)
        }

    }

}