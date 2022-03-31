package com.abdulaziz.a4kfullwallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.databinding.ItemImageBinding
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.Result
import com.squareup.picasso.Picasso

class PagingAdapter(val listener: OnClickListener):PagingDataAdapter<Result, PagingAdapter.Vh>(MyDiffUtils()) {


    inner class Vh(val itemBinding: ItemImageBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun onBind(result: Result, position: Int){
            itemBinding.apply {
                Picasso.get().load(result.urls.small).placeholder(R.drawable.place_holder).into(imageView)
                root.setOnClickListener {
                    listener.OnImageClickListener(result, position)
                }
            }
        }
    }

     class MyDiffUtils:DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
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
        fun OnImageClickListener(result: Result, position: Int)
    }
}