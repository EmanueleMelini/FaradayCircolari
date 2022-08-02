package org.experimentalplayers.faraday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.models.SiteDocument
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(
    private var mContext: Context
) : ListAdapter<SiteDocument, HomeAdapter.HomeViewHolder>(HomeDiffCallback()) {

    class HomeDiffCallback : DiffUtil.ItemCallback<SiteDocument>() {
        override fun areItemsTheSame(oldItem: SiteDocument, newItem: SiteDocument): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SiteDocument, newItem: SiteDocument): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val item = getItem(position)

        holder.title.text = item.title

        item.publishDate?.let {
            val sfd = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            holder.date.text = sfd.format(Date(it.seconds * 1000))
        }

        item.attachments?.let {
            val str = "${it.size}"
            holder.count.text = str
        }

    }

    override fun getItemCount(): Int = currentList.size

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var date: TextView
        var count: TextView

        init {
            title = itemView.findViewById(R.id.home_item_text)
            date = itemView.findViewById(R.id.home_item_date)
            count = itemView.findViewById(R.id.home_item_count)
        }

    }
}