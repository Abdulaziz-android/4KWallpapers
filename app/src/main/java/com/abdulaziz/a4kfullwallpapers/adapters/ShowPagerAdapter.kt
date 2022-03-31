package com.abdulaziz.a4kfullwallpapers.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abdulaziz.a4kfullwallpapers.fragments.InnerShowFragment
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.Result


class ShowPagerAdapter(fm: FragmentManager,val images: List<ImageModel>,val currentPosition: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return InnerShowFragment.newInstance(images, position)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis()
    }
}
