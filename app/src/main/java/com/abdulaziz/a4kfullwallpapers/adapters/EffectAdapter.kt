package com.abdulaziz.a4kfullwallpapers.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.databinding.ItemEffectBinding
import com.abdulaziz.a4kfullwallpapers.models.Effect
import com.squareup.picasso.Picasso

class EffectAdapter(
    private val link: String,
    private val list: List<Effect>,
    private val listener: OnEffectClickListener
) :
    RecyclerView.Adapter<EffectAdapter.EffectVH>() {

    var item: CardView? = null
    var lastPosition = 0
    var itemColor: Int? = null

    inner class EffectVH(private val itemBinding: ItemEffectBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(effect: Effect, position: Int) {
            if (position == 0) {
                item = itemBinding.card
                itemColor = ContextCompat.getColor(itemBinding.root.context, R.color.effect_card_color)
                itemBinding.card.setCardBackgroundColor(Color.WHITE)
            }
            Picasso.get().load(link).into(itemBinding.itemIv)
            itemBinding.itemIv.setImageBitmap(effect.bitmap)
            itemBinding.itemTv.text = effect.name
            itemBinding.root.setOnClickListener {
                listener.onEffectClicked(position)
                if (item != null) {
                    if (lastPosition != position) {
                        item!!.setCardBackgroundColor(itemColor!!)
                        item = itemBinding.card
                        lastPosition = position
                        itemBinding.card.setCardBackgroundColor(Color.WHITE)
                    } else
                        itemBinding.card.setCardBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectVH {
        return EffectVH(
            ItemEffectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EffectVH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnEffectClickListener {
        fun onEffectClicked(position: Int)
    }
}