package com.abdulaziz.a4kfullwallpapers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.abdulaziz.a4kfullwallpapers.MainActivity
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.adapters.ViewAdapter
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentHomeBinding
import com.abdulaziz.a4kfullwallpapers.databinding.ItemTabBinding
import com.abdulaziz.a4kfullwallpapers.util.ImageData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: ViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        val tabsList = ImageData.getTabsList()

        viewAdapter = ViewAdapter(requireActivity().supportFragmentManager, tabsList)
        binding.viewpager.adapter = viewAdapter
        binding.tablayout.setupWithViewPager(binding.viewpager)


        setTabs(container)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).showMyView()
    }

    private fun setTabs(container: ViewGroup?) {
        val tabsList = ImageData.getTabsList()

        val count: Int = binding.tablayout.tabCount
        for (i in 0 until count) {
            val tabView: View = ItemTabBinding.inflate(layoutInflater, container, false).root
            val textView = tabView.findViewById<TextView>(R.id.tab_title)
            textView.text = tabsList[i]
            val imageView = tabView.findViewById<ImageView>(R.id.tab_icon)
            if (i == 0) imageView.visibility = View.VISIBLE
            binding.tablayout.getTabAt(i)?.customView = tabView
        }
        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val imageView = tabView!!.findViewById<ImageView>(R.id.tab_icon)
                imageView.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val imageView = tabView!!.findViewById<ImageView>(R.id.tab_icon)
                imageView.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }



}