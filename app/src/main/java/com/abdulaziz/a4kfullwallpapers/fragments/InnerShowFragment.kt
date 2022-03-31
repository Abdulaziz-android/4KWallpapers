package com.abdulaziz.a4kfullwallpapers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentInnerShowBinding
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.squareup.picasso.Picasso
import java.io.Serializable

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InnerShowFragment : Fragment() {

    private var list: List<ImageModel>? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getSerializable(ARG_PARAM1) as List<ImageModel>
            position = it.getInt(ARG_PARAM2)
        }
    }

    private var _binding:FragmentInnerShowBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentInnerShowBinding.inflate(layoutInflater, container, false)

        val imageUrl = list?.get(position!!)?.url_regular

        Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder).into(binding.imageView)


        return binding.root
       }

    companion object {

        fun newInstance(param1: List<ImageModel>, param2: Int) =
            InnerShowFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1 as Serializable)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}