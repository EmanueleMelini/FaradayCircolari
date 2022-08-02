package org.experimentalplayers.faraday.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.Archive
import org.experimentalplayers.faraday.models.Type
import org.experimentalplayers.faraday.ui.adapters.DocumentsAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import simpleToast
import kotlin.streams.toList

class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    private var years: MutableList<Archive> = mutableListOf()
    private lateinit var mContext: Context

    private lateinit var yearsCircolariRecycler: RecyclerView
    private lateinit var yearsCircolariAdapter: DocumentsAdapter
    private var yearsCircolari: MutableList<Archive> = mutableListOf()

    private lateinit var yearsAvvisiRecycler: RecyclerView
    private lateinit var yearsAvvisiAdapter: DocumentsAdapter
    private var yearsAvvisi: MutableList<Archive> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_documents, container, false)

        yearsCircolariRecycler = view.findViewById(R.id.documents_recycler_circolari)
        yearsCircolariRecycler.setHasFixedSize(true)
        yearsCircolariRecycler.layoutManager = LinearLayoutManager(mContext)
        yearsCircolariAdapter = DocumentsAdapter(mContext, yearsCircolari)
        yearsCircolariRecycler.adapter = yearsCircolariAdapter

        yearsAvvisiRecycler = view.findViewById(R.id.documents_recycler_avvisi)
        yearsAvvisiRecycler.setHasFixedSize(true)
        yearsAvvisiRecycler.layoutManager = LinearLayoutManager(mContext)
        yearsAvvisiAdapter = DocumentsAdapter(mContext, yearsAvvisi)
        yearsAvvisiRecycler.adapter = yearsAvvisiAdapter


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

    }

    override fun onResume() {
        super.onResume()

        mContext = requireContext()

        years.clear()

        FirestoreHelper.firestoreInstance.getArchive() {
            if(it != null) {
                years.addAll(it)
                yearsCircolari.clear()
                yearsCircolari.addAll(years.stream().filter { document -> document.type == Type.CIRCOLARE }
                    .sorted { anno1, anno2 -> anno2.startYear.compareTo(anno1.startYear) }
                    .toList())
                yearsCircolariAdapter.notifyDataSetChanged()

                yearsAvvisi.clear()
                yearsAvvisi.addAll(years.stream().filter { document -> document.type == Type.AVVISO }
                    .sorted { anno1, anno2 -> anno2.startYear.compareTo(anno1.startYear) }
                    .toList())
                yearsAvvisiAdapter.notifyDataSetChanged()
            } else
                mContext.simpleToast("Errore nella chiamata, riprovare pià tardi")
        }

    }

}