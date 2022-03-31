package com.abdulaziz.a4kfullwallpapers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.adapters.ImageAdapter
import com.abdulaziz.a4kfullwallpapers.adapters.PagingAdapter
import com.abdulaziz.a4kfullwallpapers.adapters.PagingNewAdapter
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentPagerBinding
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.abdulaziz.a4kfullwallpapers.models.newmodel.NewImageModelItem
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.Result
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiClient
import com.abdulaziz.a4kfullwallpapers.util.NetworkHelper
import com.abdulaziz.a4kfullwallpapers.viewmodels.AllViewModel
import com.abdulaziz.a4kfullwallpapers.viewmodels.ImageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PagerFragment : Fragment() {

    private var list: ArrayList<String>? = null
    private var param2: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getStringArrayList(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    private var _binding: FragmentPagerBinding? = null
    private val binding get() = _binding!!
    private var adapter: PagingAdapter? = null
    private lateinit var networkHelper: NetworkHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPagerBinding.inflate(layoutInflater, container, false)
        networkHelper = NetworkHelper(binding.root.context)

        if (networkHelper.isNetworkConnected()) {
            when (param2) {
                0 -> {
                    loadAll()
                }
                1 -> {
                    loadNewPictures()
                }
                else -> {
                    loadAny()
                }
            }
        }

        return binding.root
    }

    private var allViewModel:AllViewModel?=null
    private var allAdapter:ImageAdapter?=null

    private fun loadAll() {
        allViewModel = ViewModelProvider(requireActivity()).get(AllViewModel::class.java)
        allViewModel!!.getAllImages().observe(requireActivity()) {
            val list = arrayListOf<ImageModel>()
            it.forEach { result ->
                list.add(
                    ImageModel(
                        result.id,
                        result.width,
                        result.height,
                        result.urls.small,
                        result.urls.regular,
                        result.links.download,
                        result.likes,
                        result.links.html,
                        result.user.name,
                        false
                    )
                )
            }
            allAdapter = ImageAdapter(list, object : ImageAdapter.OnItemClick {
                override fun OnItemClicked(position: Int) {
                    val bundle = Bundle()
                    bundle.putSerializable("show", list as Serializable?)
                    bundle.putInt("pos", position)
                    findNavController().navigate(R.id.action_homeFragment_to_showFragment, bundle)

                }

            })
            binding.rv.adapter = allAdapter

        }


    }

    private fun loadAny() {
        adapter = PagingAdapter(object : PagingAdapter.OnClickListener {
            override fun OnImageClickListener(result: Result, position: Int) {
                val listResult = arrayListOf<ImageModel>()
                adapter!!.snapshot().forEach {
                    if (it != null) {
                        listResult.add(ImageModel(it.id,
                            it.width,
                            it.height,
                            it.urls.small,
                            it.urls.regular,
                            it.links.download,
                            it.likes,
                            it.links.html,
                            it.user.name,
                            false))
                    }
                }

                val bundle = Bundle()
                bundle.putSerializable("show", listResult as Serializable?)
                bundle.putInt("pos", position)
                findNavController().navigate(R.id.action_homeFragment_to_showFragment, bundle)

            }
        })
        binding.rv.adapter = adapter

        val s = list!![param2!!]


        val imageViewModel = ViewModelProvider(requireActivity()).get(s, ImageViewModel::class.java)

        imageViewModel.currentPage = s
        imageViewModel.apiService = ApiClient.apiServiceSearch
        imageViewModel.liveData.observe(requireActivity()) {
            GlobalScope.launch(Dispatchers.Main) {
                adapter!!.submitData(it)
            }
        }
    }

    var newAdapter: PagingNewAdapter? = null

    private fun loadNewPictures() {
        newAdapter = PagingNewAdapter(object : PagingNewAdapter.OnClickListener {
            override fun OnImageClickListener(imageItem: NewImageModelItem, position: Int) {
                val listResult = arrayListOf<ImageModel>()
                newAdapter!!.snapshot().forEach {
                    if (it != null) {
                        listResult.add(ImageModel(it.id,
                            it.width,
                            it.height,
                            it.urls.small,
                            it.urls.regular,
                            it.links.download,
                            it.likes,
                            it.links.html,
                            it.user.name,
                            false))
                    }
                }
                val bundle = Bundle()
                bundle.putSerializable("show", listResult as Serializable?)
                bundle.putInt("pos", position)

                findNavController().navigate(R.id.action_homeFragment_to_showFragment, bundle)

            }
        })
        binding.rv.adapter = newAdapter

        val s = list!![param2!!]

        val imageViewModel = ViewModelProvider(requireActivity()).get(s, ImageViewModel::class.java)
        imageViewModel.currentPage = s
        imageViewModel.apiService = ApiClient.apiServiceNew
        imageViewModel.liveDataNew.observe(requireActivity()) {
            GlobalScope.launch(Dispatchers.Main) {
                newAdapter!!.submitData(it)
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: ArrayList<String>, param2: Int) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }


}