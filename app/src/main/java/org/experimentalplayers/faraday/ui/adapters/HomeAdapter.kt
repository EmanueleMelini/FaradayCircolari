package org.experimentalplayers.faraday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.Document

class HomeAdapter(
    private var mContext: Context,
    private var list: List<Document>
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val item = list[position]

        holder.title.text = item.title

    }

    override fun getItemCount(): Int = list.size

}

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var title: TextView

    init {
        title = itemView.findViewById(R.id.home_item_text)
    }

}