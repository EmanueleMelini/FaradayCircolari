package org.experimentalplayers.faraday.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.ui.HomeActivity
import org.experimentalplayers.faraday.ui.adapters.HomeAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import setGone
import simpleToast
import timber.log.Timber

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var topText: TextView
    private lateinit var listRecycler: RecyclerView
    private lateinit var listText: TextView
    private lateinit var arrowBack: ImageView

    private lateinit var mContext: Context
    private lateinit var homeActivity: HomeActivity

    private lateinit var homeAdapter: HomeAdapter
    private var siteDocuments: List<SiteDocument> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Timber.d("HomeFragment.onCreateView")

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        listRecycler = view.findViewById(R.id.list_recycler)
        listText = view.findViewById(R.id.list_text)
        topText = view.findViewById(R.id.top_text)
        arrowBack = view.findViewById(R.id.top_back)

        siteDocuments = emptyList()

        val itemAnimator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        listRecycler.itemAnimator = itemAnimator
        listRecycler.layoutManager = LinearLayoutManager(mContext)
        homeAdapter = HomeAdapter(mContext)
        listRecycler.adapter = homeAdapter
        homeAdapter.submitList(siteDocuments)

        arrowBack.setGone()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("HomeFragment.onCreate")

        mContext = requireContext()
        homeActivity = (mContext as HomeActivity)

    }

    override fun onResume() {
        super.onResume()

        Timber.d("HomeFragment.onResume")

        mContext = requireContext()

        homeActivity.canExit = true
        homeActivity.setOnBackPressedDispatcher {}

        topText.setText(R.string.home_top_text)
        listText.setText(R.string.home_list_text)

        refreshDocuments()

    }

    private fun refreshDocuments() {
        FirestoreHelper.firestoreInstance.getDocuments(10L) { docs ->
            if(docs != null) {

                siteDocuments = docs

                homeAdapter.submitList(siteDocuments)

                if(docs.isEmpty())
                    mContext.simpleToast(R.string.empty_docs)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }
    }

}