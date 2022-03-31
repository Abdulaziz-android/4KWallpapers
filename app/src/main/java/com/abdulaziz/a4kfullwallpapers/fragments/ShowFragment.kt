package com.abdulaziz.a4kfullwallpapers.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.abdulaziz.a4kfullwallpapers.MainActivity
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.adapters.ShowPagerAdapter
import com.abdulaziz.a4kfullwallpapers.database.AppDatabase
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentShowBinding
import com.abdulaziz.a4kfullwallpapers.databinding.ItemBottomshetBinding
import com.abdulaziz.a4kfullwallpapers.models.ImageModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

private const val ARG_PARAM1 = "show"
private const val ARG_PARAM2 = "pos"

class ShowFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var list: List<ImageModel>? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getSerializable(ARG_PARAM1) as List<ImageModel>
            position = it.getInt(ARG_PARAM2)
        }
        (activity as MainActivity).hideMyView()
    }

    private var _binding: FragmentShowBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: ShowPagerAdapter
    private lateinit var database: AppDatabase
    private var currentImage: ImageModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShowBinding.inflate(layoutInflater, container, false)

        database = AppDatabase.getInstance(binding.root.context)
        database.imageDao().insertImage(list!![position!!])
        if (database.imageDao().isExists()) {
            if (database.imageDao().getImageById(list!![position!!].id) != null)
                currentImage = database.imageDao().getImageById(list!![position!!].id)
            else currentImage = list!![position!!]
        } else currentImage = list!![position!!]

        pagerAdapter =
            ShowPagerAdapter(requireActivity().supportFragmentManager, list!!, position!!)

        binding.viewpagerShow.adapter = pagerAdapter
        binding.viewpagerShow.currentItem = position!!

        binding.viewpagerShow.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {

                if (database.imageDao().getImageById(list!![position].id) != null)
                    currentImage = database.imageDao().getImageById(list!![position].id)
                else {
                    currentImage = list!![position]
                    database.imageDao().insertImage(currentImage!!)
                }


                if (currentImage!!.like) {
                    binding.imageLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_full,
                        resources.newTheme()))
                } else {
                    binding.imageLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_emty,
                        resources.newTheme()))
                }
            }
            override fun onPageScrollStateChanged(state: Int) {} })

        onClickListener()

        return binding.root
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun onClickListener() {
        binding.apply {
            cardExit.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }

            cardShare.setOnClickListener {
                val myText = "link: ${list!![binding.viewpagerShow.currentItem].url_regular}"
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, myText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            cardInfo.setOnClickListener {
                val bottomSheetDialog =
                    BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
                val dialogBinding = ItemBottomshetBinding.inflate(layoutInflater)
                val image = list?.get(binding.viewpagerShow.currentItem)!!
                dialogBinding.apply {
                    webSiteTv.text = "Website: ${image.website}"
                    authorTv.text = "Author: ${image.author}"
                    linkTv.text = "Download: ${image.link_download}"
                    sizeTv.text = "Size: ${image.width} x ${image.height}"
                }


                setBlur(dialogBinding.blurviewSheet)
                bottomSheetDialog.setContentView(dialogBinding.root)
                bottomSheetDialog.setOnShowListener { dialog: DialogInterface? ->
                    binding.apply {
                        imageviewShowExit.setImageResource(R.drawable.ic_exit)
                        cardInfo.visibility = View.GONE
                        cardShare.visibility = View.GONE
                    }
                }

                bottomSheetDialog.setOnDismissListener { dialog: DialogInterface? ->
                    imageviewShowExit.setImageResource(R.drawable.ic_back)
                    cardInfo.visibility = View.VISIBLE
                    cardShare.visibility = View.VISIBLE
                }

                bottomSheetDialog.show()
            }

            cardDownload.setOnClickListener {
                Dexter.withContext(binding.root.context)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            download(list?.get(binding.viewpagerShow.currentItem)!!)
                        }
                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {}
                        override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {}
                    }).check()
            }

            cardSetter.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("paramparam", "setter")
                bundle.putString("link", list?.get(binding.viewpagerShow.currentItem)!!.url_regular)
                findNavController().navigate(R.id.action_showFragment_to_editFragment, bundle)
            }
            cardEffect.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("paramparam", "effect")
                bundle.putString("link", list?.get(binding.viewpagerShow.currentItem)!!.url_regular)
                findNavController().navigate(R.id.action_showFragment_to_editFragment, bundle)
            }

        }

        if (currentImage!!.like) {
            binding.imageLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_full,
                resources.newTheme()))
        }

        binding.cardLike.setOnClickListener {
            if (currentImage!!.like) {
                currentImage!!.like = false
                database.imageDao().insertImage(currentImage!!)
                binding.imageLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_emty,
                    resources.newTheme()))
            } else {
                currentImage!!.like = true
                database.imageDao().insertImage(currentImage!!)
                binding.imageLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_full,
                    resources.newTheme()))
            }
        }

    }

    private fun download(imageResult: ImageModel) {
        try {
            val downloadManager = activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
            val uri: Uri = Uri.parse(imageResult.link_download)
            val request = DownloadManager.Request(uri)
            request.setTitle(imageResult.id)
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "${imageResult.id}.jpg")
            downloadManager!!.enqueue(request)
            Toast.makeText(requireContext(), "Downloading started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBlur(blurView: BlurView) {
        val radius = 10f
        val decorView = (activity as MainActivity).window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(context))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showMyView()
    }
}