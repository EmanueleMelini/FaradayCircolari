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
import org.experimentalplayers.faraday.models.Document
import org.experimentalplayers.faraday.models.DocumentType
import org.experimentalplayers.faraday.ui.adapters.HomeAdapter
import java.time.LocalDateTime
import kotlin.streams.toList

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var documents: List<Document>
    private lateinit var mContext: Context

    private lateinit var circolariRecycler: RecyclerView
    private lateinit var avvisiRecycler: RecyclerView
    private lateinit var root: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val circolari = documents.stream().filter { document -> document.type === DocumentType.CIRCOLARE }
            .sorted(Comparator.comparing(Document::date))
            .limit(10)
            .toList()

        val avvisi = documents.stream().filter { document -> document.type === DocumentType.AVVISO }
            .sorted(Comparator.comparing(Document::date))
            .limit(10)
            .toList()

        circolariRecycler = view.findViewById(R.id.home_recycler_circolari)
        avvisiRecycler = view.findViewById(R.id.home_recycler_avvisi)
        root = view.findViewById(R.id.home_root)

        circolariRecycler.setHasFixedSize(true)
        circolariRecycler.layoutManager = LinearLayoutManager(mContext)
        //circolariRecycler.layoutManager = GridLayoutManager(mContext, 1)
        circolariRecycler.adapter = HomeAdapter(mContext, circolari)

        avvisiRecycler.setHasFixedSize(true)
        avvisiRecycler.layoutManager = LinearLayoutManager(mContext)
        //avvisiRecycler.layoutManager = GridLayoutManager(mContext, 1)
        avvisiRecycler.adapter = HomeAdapter(mContext, avvisi)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()

        documents = listOf(
            Document(
                1,
                "Primo documento",
                "Primo documento di test",
                "https://github.com/EmanueleMelini",
                LocalDateTime.now(),
                DocumentType.CIRCOLARE
            ),
            Document(
                2,
                "Secondo documento",
                "Secondo documento di test",
                "https://github.com/devExcale",
                LocalDateTime.now(),
                DocumentType.CIRCOLARE
            ),
            Document(
                3,
                "Terzo documento",
                "Terzo documento di test",
                "https://github.com",
                LocalDateTime.now(),
                DocumentType.AVVISO
            ),
            Document(
                4,
                "Quarto documento",
                "Quarto documento di test",
                "https://github.com",
                LocalDateTime.now(),
                DocumentType.AVVISO
            )
        )

    }

    override fun onResume() {
        super.onResume()

        mContext = requireContext()

    }

}