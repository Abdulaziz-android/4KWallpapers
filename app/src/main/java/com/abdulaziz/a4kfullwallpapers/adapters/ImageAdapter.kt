package com.abdulaziz.a4kfullwallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.databinding.ItemImageBinding
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.squareup.picasso.Picasso

class ImageAdapter(val list: List<ImageModel>, val listener: OnItemClick) :
    RecyclerView.Adapter<ImageAdapter.ImageVH>() {

    inner class ImageVH(val itemBinding: ItemImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(imageModel: ImageModel, position: Int) {
            Picasso.get().load(imageModel.url_small).placeholder(R.drawable.place_holder).into(itemBinding.imageView)
            itemBinding.root.setOnClickListener {
                listener.OnItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
        return ImageVH(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageVH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClick {
        fun OnItemClicked(position: Int)
    }
}