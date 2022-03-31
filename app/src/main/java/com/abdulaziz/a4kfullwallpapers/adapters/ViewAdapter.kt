package com.abdulaziz.a4kfullwallpapers.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abdulaziz.a4kfullwallpapers.fragments.PagerFragment

class ViewAdapter(fm: FragmentManager,val pagesList: List<String>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return PagerFragment.newInstance(pagesList as ArrayList<String>, position)
    }

    override fun getCount(): Int {
        return pagesList.size
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis()
    }

}
