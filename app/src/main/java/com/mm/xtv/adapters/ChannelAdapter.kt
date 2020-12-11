package com.mm.xtv.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.mm.xtv.Channels
import com.mm.xtv.R
import com.mm.xtv.player.exoplay
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.channel_row.view.*

class ChannelAdapter(private val context: Context, private val channels: List<Channels>) :
    RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {


    class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.channel_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val channelsList = channels[position]
        holder.itemView.channelTv.text = channelsList.channel_name
        Picasso.get().load(channelsList.channel_logo).into(holder.itemView.channelImg)
        holder.itemView.allView.setOnClickListener {
            val intent = Intent(context, exoplay::class.java)
            intent.putExtra("tv", channelsList.channel_url)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }
}
