package com.abdulaziz.a4kfullwallpapers.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.abdulaziz.a4kfullwallpapers.MainActivity
import com.abdulaziz.a4kfullwallpapers.R
import com.abdulaziz.a4kfullwallpapers.adapters.EffectAdapter
import com.abdulaziz.a4kfullwallpapers.databinding.FragmentEditBinding
import com.abdulaziz.a4kfullwallpapers.models.Effect
import com.google.android.material.slider.Slider
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import com.zomato.photofilters.SampleFilters
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val ARG_PARAM1 = "paramparam"
private const val ARG_PARAM2 = "link"

class EditFragment : Fragment() {

    private var param1: String? = null
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            link = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private var effectList: ArrayList<Effect>? = null
    private var currentPosition = 0
    private var isEffect = false
    private var currentBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditBinding.inflate(layoutInflater, container, false)

        Picasso.get().load(link).into(binding.imageView)

        when (param1) {
            "setter" -> {
                getSetter()
                isEffect = false
                binding.cardApply.visibility = View.GONE
            }
            "effect" -> {
                getBitmaps()
                binding.imageviewShowExit.setImageResource(R.drawable.ic_exit)
                isEffect = true
            }
        }

        binding.cardExit.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return binding.root
    }

    private fun getSetter() {
        binding.consSetter.visibility = View.VISIBLE
        binding.slider.visibility = View.GONE

        binding.apply {
            consSetter.visibility = View.VISIBLE

            cardHome.setOnClickListener {
                setWallpaper(WallpaperManager.FLAG_SYSTEM)
            }
            cardLock.setOnClickListener {
                setWallpaper(WallpaperManager.FLAG_LOCK)
            }

            cardAll.setOnClickListener {
                setWallpaper(WallpaperManager.FLAG_SYSTEM)
                setWallpaper(WallpaperManager.FLAG_LOCK)
            }
        }
    }


    private fun getBitmaps() {
        effectList = arrayListOf()
        binding.consSetter.visibility = View.GONE

        val limeStutterDrawable = binding.imageView.drawable as BitmapDrawable
        val limeStutterInputImage = limeStutterDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val limeStutterFilter = SampleFilters.getLimeStutterFilter()
        val limeSutterPutImage = limeStutterFilter.processFilter(limeStutterInputImage)
        effectList!!.add(Effect(limeStutterFilter, limeSutterPutImage, "LimeStutter"))

        val blueMessFilterDrawable = binding.imageView.drawable as BitmapDrawable
        val blueMessInputImage = blueMessFilterDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val blueMessFilter = SampleFilters.getBlueMessFilter()
        val blueMessPutImage = blueMessFilter.processFilter(blueMessInputImage)
        effectList!!.add(Effect(blueMessFilter, blueMessPutImage, "BlueMess"))

        val nightWhisperFilterDrawable = binding.imageView.drawable as BitmapDrawable
        val nightWhisperInputImage =
            nightWhisperFilterDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val nightWhisperFilter = SampleFilters.getNightWhisperFilter()
        val nightWhisperPutImage = nightWhisperFilter.processFilter(nightWhisperInputImage)
        effectList!!.add(Effect(nightWhisperFilter, nightWhisperPutImage, "NightWhisper"))

        val starLitFilterDrawable = binding.imageView.drawable as BitmapDrawable
        val starLitInputImage = starLitFilterDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val starLitFilter = SampleFilters.getStarLitFilter()
        val starLitPutImage = starLitFilter.processFilter(starLitInputImage)
        effectList!!.add(Effect(starLitFilter, starLitPutImage, "StarLit"))

        val aweStruckFilterDrawable = binding.imageView.drawable as BitmapDrawable
        val aweStruckInputImage = aweStruckFilterDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val aweStruckVibeFilter = SampleFilters.getAweStruckVibeFilter()
        val aweStruckVibePutImage = aweStruckVibeFilter.processFilter(aweStruckInputImage)
        effectList!!.add(Effect(aweStruckVibeFilter, aweStruckVibePutImage, "AweStruck"))

        currentBitmap = effectList!![0].bitmap
        binding.imageView.setImageBitmap(currentBitmap)

        val adapter =
            EffectAdapter(link!!, effectList!!, object : EffectAdapter.OnEffectClickListener {
                override fun onEffectClicked(position: Int) {
                    currentBitmap = effectList!![position].bitmap
                    currentPosition = position
                    binding.imageView.setImageBitmap(currentBitmap)
                    binding.slider.value = 0f
                }

            })
        binding.rv.adapter = adapter

        binding.slider.addOnChangeListener(object : Slider.OnChangeListener {
            @SuppressLint("RestrictedApi")
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                try {
                    val bitmap = effectList!![currentPosition].bitmap
                    val brightBitmap = brightBitmap(bitmap, value.toInt())
                    currentBitmap = brightBitmap
                    binding.imageView.setImageBitmap(brightBitmap)
                } catch (e: Exception) {
                    currentBitmap = effectList!![currentPosition].bitmap
                    binding.imageView.setImageBitmap(currentBitmap)
                }
            }

        })

        binding.cardApply.setOnClickListener {
            binding.rv.visibility = View.GONE
            binding.cardApply.visibility = View.GONE
            binding.imageviewShowExit.setImageResource(R.drawable.ic_back)
            getNextStep()
        }

    }

    private fun getNextStep() {
        binding.consDown.visibility = View.VISIBLE
        binding.slider.visibility = View.GONE
        binding.cardDownload.setOnClickListener {
            Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        saveImage(currentBitmap!!)
                      }
                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {}
                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {}
                })
        }
        binding.cardSet.setOnClickListener {
            binding.consDown.visibility = View.GONE
            getSetter()
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/${Environment.DIRECTORY_DOWNLOADS}")
        myDir.mkdirs()
        val fname = "Image-${System.currentTimeMillis()}.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(requireContext(), "Downloaded!", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun brightBitmap(bitmap: Bitmap, brightness: Int): Bitmap? {
        val colorTransform = floatArrayOf(1f,
            0f,
            0f,
            0f,
            brightness.toFloat(),
            0f,
            1f,
            0f,
            0f,
            brightness.toFloat(),
            0f,
            0f,
            1f,
            0f,
            brightness.toFloat(),
            0f,
            0f,
            0f,
            1f,
            0f)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        colorMatrix.set(colorTransform)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        val paint = Paint()
        paint.colorFilter = colorFilter
        val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
        return resultBitmap
    }

    private fun setWallpaper(flagSystem: Int) {
        Picasso.get().load(link).into(object : Target {
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                object : CountDownTimer(2000, 1) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.spinkitView.visibility = View.VISIBLE
                    }

                    override fun onFinish() {
                        var myBit = bitmap
                        if (isEffect) {
                            if (currentBitmap != null)
                                myBit = currentBitmap!!
                        }
                        val wallpaperManager = WallpaperManager.getInstance(context)
                        try {
                            wallpaperManager.setBitmap(myBit, null, true, flagSystem)
                        } catch (e: IOException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                            binding.spinkitView.visibility = View.GONE
                        }
                        binding.spinkitView.visibility = View.GONE
                        Toast.makeText(context, "Wallpaper installed!", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }

            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                Log.d("TAG", "FAILED")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                Log.d("TAG", "Prepare Load")
            }
        })
    }

}