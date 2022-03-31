package com.abdulaziz.a4kfullwallpapers

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import com.abdulaziz.a4kfullwallpapers.databinding.ActivityMainBinding
import com.abdulaziz.a4kfullwallpapers.databinding.ItemAlertDialogBinding
import com.abdulaziz.a4kfullwallpapers.databinding.ItemDialogBinding
import com.abdulaziz.a4kfullwallpapers.databinding.ItemTabMainBinding
import com.abdulaziz.a4kfullwallpapers.util.ConnectionLiveData
import com.abdulaziz.a4kfullwallpapers.util.NetworkHelper
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import eightbitlab.com.blurview.RenderScriptBlur


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkHelper:NetworkHelper

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkHelper = NetworkHelper(binding.root.context)
        binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)

        getBlur()

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setTabs()

        binding.navView.itemIconTintList = null
        binding.navView.setNavigationItemSelectedListener(this)

        setConnectionChecker()

    }

    private fun setConnectionChecker() {
        val alertDialog = AlertDialog.Builder(binding.root.context, R.style.AlertDialogTheme).create()
        val itemDialog = ItemAlertDialogBinding.inflate(layoutInflater)
        alertDialog.setView(itemDialog.root)
        alertDialog.setCancelable(false)

        val connectionLiveData = ConnectionLiveData(application)
        connectionLiveData.observe(this) { isConnect ->
            if (isConnect) {
                alertDialog.dismiss()
            } else {
                alertDialog.show()
            }
        }

        if (!networkHelper.isNetworkConnected()){
            alertDialog.show()
        }
    }

    private fun getBlur() {
        val radius = 4f
        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        binding.blurview.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }


    private fun setTabs() {
        val imageList = ArrayList<Int>()
        imageList.add(R.drawable.ic_white_home)
        imageList.add(R.drawable.ic_white_popular)
        imageList.add(R.drawable.ic_white_refresh)
        imageList.add(R.drawable.ic_white_like)
        for (i in imageList.indices) {
            binding.tablayoutMain.addTab(binding.tablayoutMain.newTab())
            val tabView: View = ItemTabMainBinding.inflate(layoutInflater,
                findViewById<ViewGroup>(android.R.id.content), false).root
            val image = tabView.findViewById<ImageView>(R.id.tab_main_iv)
            val imageView = tabView.findViewById<ImageView>(R.id.tab_main_icon)
            image.setImageResource(imageList[i])
            if (i == 0) imageView.visibility = View.VISIBLE
            binding.tablayoutMain.getTabAt(i)?.customView = tabView
        }
        binding.tablayoutMain.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val bundle = Bundle()
                when (binding.tablayoutMain.selectedTabPosition) {
                    0 -> {
                        binding.toolbar.title = "Home"
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .popBackStack()
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .navigate(R.id.homeFragment)
                    }
                    1 -> {
                        binding.toolbar.title = "Popular"
                        bundle.putString("cate", "popular")
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .popBackStack()
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .navigate(R.id.categoryFragment, bundle)
                    }
                    2 -> {
                        binding.toolbar.title = "Random"
                        bundle.putString("cate", "random")
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .popBackStack()
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .navigate(R.id.categoryFragment, bundle)
                    }
                    3 -> {
                        binding.toolbar.title = "Like"
                        bundle.putString("cate", "like")
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .popBackStack()
                        Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                            .navigate(R.id.categoryFragment, bundle)
                    }
                }
                val tabView = tab.customView
                val imageView = tabView!!.findViewById<ImageView>(R.id.tab_main_icon)
                imageView.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView = tab.customView
                val imageView = tabView!!.findViewById<ImageView>(R.id.tab_main_icon)
                imageView.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val bundle = Bundle()
        when (item.itemId) {
            R.id.home -> {
                binding.tablayoutMain.selectTab(binding.tablayoutMain.getTabAt(0))
            }
            R.id.popular -> {
                binding.tablayoutMain.selectTab(binding.tablayoutMain.getTabAt(1))
            }
            R.id.random -> {
                binding.tablayoutMain.selectTab(binding.tablayoutMain.getTabAt(2))

            }
            R.id.liked -> {
                binding.tablayoutMain.selectTab(binding.tablayoutMain.getTabAt(3))
            }
            R.id.history -> {
                binding.toolbar.title = "History"
                binding.tablayoutMain.selectTab(null)
                bundle.putString("cate", "history")
                Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                    .popBackStack()
                Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
                    .navigate(R.id.categoryFragment, bundle)
            }
            R.id.about -> {
                val view = findViewById<View>(android.R.id.content) as ViewGroup
                val alertDialog = AlertDialog.Builder(binding.root.context, R.style.SheetDialog)
                val itemDialog = ItemDialogBinding.inflate(layoutInflater, view, false)
                alertDialog.setView(itemDialog.root)
                alertDialog.show()
               }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun hideMyView() {
        binding.toolbar.visibility = View.GONE
        binding.cardview.visibility = View.GONE
    }

    fun showMyView() {
        binding.toolbar.visibility = View.VISIBLE
        binding.cardview.visibility = View.VISIBLE
    }
}