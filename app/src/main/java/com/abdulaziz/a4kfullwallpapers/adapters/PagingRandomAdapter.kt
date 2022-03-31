package com.abdulaziz.a4kfullwallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.databinding.ItemImageBinding
import com.abdulaziz.a4kfullwallpapers.models.randommodel.RandomImageModelItem
import com.squareup.picasso.Picasso

class PagingRandomAdapter(val listener: OnClickListener):PagingDataAdapter<RandomImageModelItem, PagingRandomAdapter.Vh>(MyDiffUtils()) {

    inner class Vh(val itemBinding: ItemImageBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun onBind(imageItem: RandomImageModelItem, position: Int){
            itemBinding.apply {
                Picasso.get().load(imageItem.urls.small).placeholder(R.drawable.place_holder).into(imageView)
                root.setOnClickListener {
                    listener.OnImageClickListener(position)
                }
            }
        }
    }

     class MyDiffUtils:DiffUtil.ItemCallback<RandomImageModelItem>(){
        override fun areItemsTheSame(oldItem: RandomImageModelItem, newItem: RandomImageModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RandomImageModelItem, newItem: RandomImageModelItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position)!!, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    interface OnClickListener{
        fun OnImageClickListener(position: Int)
    }
}