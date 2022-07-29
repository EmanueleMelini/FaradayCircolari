package org.experimentalplayers.faraday.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.Attachment
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.SiteDocument.DocumentType.AVVISO
import org.experimentalplayers.faraday.models.SiteDocument.DocumentType.CIRCOLARE
import org.experimentalplayers.faraday.ui.adapters.HomeAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import simpleToast
import kotlin.streams.toList

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var siteDocuments: MutableList<SiteDocument> = mutableListOf()
    private var attachments: MutableList<Attachment> = mutableListOf()
    private lateinit var mContext: Context

    private lateinit var circolariRecycler: RecyclerView
    private lateinit var circolariAdapter: HomeAdapter
    private var circolari: MutableList<SiteDocument> = mutableListOf()

    private lateinit var avvisiRecycler: RecyclerView
    private lateinit var avvisiAdapter: HomeAdapter
    private var avvisi: MutableList<SiteDocument> = mutableListOf()

    private lateinit var root: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        circolariRecycler = view.findViewById(R.id.home_recycler_circolari)
        circolariRecycler.setHasFixedSize(true)
        circolariRecycler.layoutManager = LinearLayoutManager(mContext)
        circolariAdapter = HomeAdapter(mContext, circolari)
        circolariRecycler.adapter = circolariAdapter

        avvisiRecycler = view.findViewById(R.id.home_recycler_avvisi)
        avvisiRecycler.setHasFixedSize(true)
        avvisiRecycler.layoutManager = LinearLayoutManager(mContext)
        avvisiAdapter = HomeAdapter(mContext, avvisi)
        avvisiRecycler.adapter = avvisiAdapter

        root = view.findViewById(R.id.home_root)

        //circolariRecycler.layoutManager = GridLayoutManager(mContext, 1)

        //avvisiRecycler.layoutManager = GridLayoutManager(mContext, 1)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

    }

    override fun onResume() {
        super.onResume()

        mContext = requireContext()

        siteDocuments.clear()

        FirestoreHelper.firestoreInstance.getDocuments() {
            if(it != null) {
                siteDocuments.addAll(it)
                circolari.clear()
                circolari.addAll(siteDocuments.stream().filter { document -> document.type === CIRCOLARE }
                    .sorted(Comparator.comparing(SiteDocument::publishDate))
                    .limit(10)
                    .toList())
                circolariAdapter.notifyDataSetChanged()

                avvisi.clear()
                avvisi.addAll(siteDocuments.stream().filter { document -> document.type === AVVISO }
                    .sorted(Comparator.comparing(SiteDocument::publishDate))
                    .limit(10)
                    .toList())
                avvisiAdapter.notifyDataSetChanged()
            } else
                mContext.simpleToast("Errore nella chiamata, riprovare pià tardi")
        }

    }

}