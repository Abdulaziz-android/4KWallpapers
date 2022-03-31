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
import com.abdulaziz.a4kfullwallpapers.adapters.PagingNewAdapter
import com.abdulaziz.a4kfullwallpapers.adapters.PagingRandomAdapter
import com.abdulaziz.a4kfullwallpapers.database.AppDatabase
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentCategoryBinding
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.abdulaziz.a4kfullwallpapers.models.newmodel.NewImageModelItem
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiClient
import com.abdulaziz.a4kfullwallpapers.viewmodels.ImageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable

private const val ARG_PARAM1 = "cate"

class CategoryFragment : Fragment() {

    private var imageType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageType = it.getString(ARG_PARAM1)
        }
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: AppDatabase
    private var listImage: List<ImageModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)

        database = AppDatabase.getInstance(requireContext())

        when (imageType) {
            "popular" -> {
                loadPopularData()
            }
            "random" -> {
                loadRandomData()
            }
            "like" -> {
                loadData()
            }
            "history" -> {
                loadHistoryData()
            }
        }

        return binding.root
    }

    private fun loadHistoryData() {
        listImage = if (database.imageDao().isExists()) {
            database.imageDao().getAllImages()
        } else arrayListOf()

        val adapter = ImageAdapter(listImage!!, object : ImageAdapter.OnItemClick {
            override fun OnItemClicked(position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("show", listImage as Serializable?)
                bundle.putInt("pos", position)
                findNavController().navigate(R.id.action_categoryFragment_to_showFragment, bundle)
            }
        })

        binding.rv.adapter = adapter

    }

    private var popularAdapter: PagingNewAdapter? = null

    private fun loadPopularData() {
        popularAdapter = PagingNewAdapter(object : PagingNewAdapter.OnClickListener {
            override fun OnImageClickListener(imageItem: NewImageModelItem, position: Int) {
                val listResult = arrayListOf<ImageModel>()
                popularAdapter!!.snapshot().forEach {
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

                findNavController().navigate(R.id.action_categoryFragment_to_showFragment, bundle)

            }
        })
        binding.rv.adapter = popularAdapter

        val imageViewModell =
            ViewModelProvider(requireActivity()).get("popular", ImageViewModel::class.java)

        imageViewModell.currentPage = "popular"
        imageViewModell.apiService = ApiClient.apiServiceSearch
        imageViewModell.liveDataPopular.observe(requireActivity()) {
            GlobalScope.launch(Dispatchers.Main) {
                popularAdapter!!.submitData(it)
            }
        }
    }

    private var adapter: PagingRandomAdapter? = null
    private fun loadRandomData() {
        adapter = PagingRandomAdapter(object : PagingRandomAdapter.OnClickListener {
            override fun OnImageClickListener(position: Int) {
                val listResult = arrayListOf<ImageModel>()
                adapter?.snapshot()?.forEach {
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
                findNavController().navigate(R.id.action_categoryFragment_to_showFragment, bundle)

            }
        })
        binding.rv.adapter = adapter

        val imageViewModel =
            ViewModelProvider(requireActivity()).get("random", ImageViewModel::class.java)

        imageViewModel.currentPage = "random"
        imageViewModel.liveDataRandom.observe(requireActivity()) {
            GlobalScope.launch(Dispatchers.Main) {
                adapter!!.submitData(it)
            }
        }

    }

    private fun loadData() {
        listImage = if (database.imageDao().isExists()) {
            database.imageDao().getLikeImages()
        } else arrayListOf()

        val adapter = ImageAdapter(listImage!!, object : ImageAdapter.OnItemClick {
            override fun OnItemClicked(position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("show", listImage as Serializable?)
                bundle.putInt("pos", position)
                findNavController().navigate(R.id.action_categoryFragment_to_showFragment, bundle)
            }
        })

        binding.rv.adapter = adapter

    }

}