package org.experimentalplayers.faraday.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import go
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.ArchiveEntry
import org.experimentalplayers.faraday.models.SiteDocument
import org.experimentalplayers.faraday.models.Type
import org.experimentalplayers.faraday.ui.adapters.ArchiveAdapter
import org.experimentalplayers.faraday.ui.adapters.DocumentsAdapter
import org.experimentalplayers.faraday.utils.FirestoreHelper
import setGone
import setVisible
import simpleToast
import timber.log.Timber
import kotlin.streams.toList

class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    private lateinit var mContext: Context

    private lateinit var topText: TextView
    private lateinit var yearsRecycler: RecyclerView
    private lateinit var yearsAdapter: DocumentsAdapter
    private var archiveEntries: List<ArchiveEntry> = emptyList()

    private lateinit var listView: ConstraintLayout
    private lateinit var listRecycler: RecyclerView
    private lateinit var listAdapter: ArchiveAdapter
    private var siteDocuments: List<SiteDocument> = emptyList()
    private lateinit var listText: TextView

    private var isArchive: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_documents, container, false)

        yearsRecycler = view.findViewById(R.id.documents_recycler_archive)
        //yearsRecycler.setHasFixedSize(false)

        val itemAnimator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        yearsRecycler.itemAnimator = itemAnimator

        archiveEntries = emptyList()
        yearsRecycler.layoutManager = LinearLayoutManager(mContext)
        yearsAdapter = DocumentsAdapter(mContext, this)
        yearsRecycler.adapter = yearsAdapter

        yearsAdapter.submitList(archiveEntries)


        listRecycler = view.findViewById(R.id.list_recycler)

        val itemAnimator2: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        listRecycler.itemAnimator = itemAnimator2

        siteDocuments = emptyList()
        listRecycler.layoutManager = LinearLayoutManager(mContext)
        listAdapter = ArchiveAdapter()
        listRecycler.adapter = listAdapter
        listAdapter.submitList(siteDocuments)

        listText = view.findViewById(R.id.list_text)

        listView = view.findViewById(R.id.document_list)

        topText = view.findViewById(R.id.top_text)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback {
            if(!isArchive) {
                isArchive = true
                yearsRecycler.setVisible()
                listView.setGone()
            } else
                requireActivity().onBackPressed()
        }
    }

    fun seeDocuments(schoolYear: String, type: Type) {
        isArchive = false

        yearsRecycler.setGone()
        listView.setVisible()

        listText.text = schoolYear

        FirestoreHelper.firestoreInstance.getDocuments() {
            if(it != null) {

                siteDocuments = it.stream()
                    //.filter { doc -> doc.type == type && doc.schoolYear == schoolYear }
                    .toList()

                listAdapter.submitList(siteDocuments)

                if(it.isEmpty())
                    mContext.simpleToast(R.string.empty_docs)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

    }

    override fun onResume() {
        super.onResume()

        if(isArchive) {
            yearsRecycler.setVisible()
            listView.setGone()
        }

        topText.text = "Archivio"

        mContext = requireContext()

        FirestoreHelper.firestoreInstance.getArchive() {
            if(it != null) {

                Timber.d("ARCHIVE_DOCS-${it.toString()}")

                archiveEntries = it.stream()
                    .sorted { anno1, anno2 -> anno2.startYear.compareTo(anno1.startYear) }
                    .toList()

                yearsAdapter.submitList(archiveEntries)

                if(it.isEmpty())
                    mContext.simpleToast(R.string.empty_archive)

            } else
                mContext.simpleToast(R.string.conn_error_text)
        }

    }

}