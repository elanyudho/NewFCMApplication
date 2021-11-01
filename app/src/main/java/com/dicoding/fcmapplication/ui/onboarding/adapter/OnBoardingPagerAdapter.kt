package com.dicoding.fcmapplication.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.PlaceHolderOnBoardingBinding
import com.dicoding.fcmapplication.ui.onboarding.model.BoardingItem
import com.dicoding.fcmapplication.utils.extensions.glide

class OnBoardingPagerAdapter : BaseRecyclerViewAdapter<OnBoardingPagerAdapter.ViewPagerViewHolder>() {

    private var listData = mutableListOf<BoardingItem>()

    fun submitList(newList: List<BoardingItem>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> ViewPagerViewHolder
        get() = { layoutInflater, viewGroup, b ->
            ViewPagerViewHolder(PlaceHolderOnBoardingBinding.inflate(layoutInflater, viewGroup, b))
        }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewPagerViewHolder(itemView: PlaceHolderOnBoardingBinding) :
        BaseViewHolder<BoardingItem, PlaceHolderOnBoardingBinding>(itemView) {

        override fun bind(data: BoardingItem) {
            with(binding) {
                ivBoardingIllustration.glide(itemView, data.illustration)
                tvBoardingDesc.text = data.desc
            }
        }
    }
}