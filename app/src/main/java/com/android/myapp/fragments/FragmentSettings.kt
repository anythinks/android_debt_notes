package com.android.myapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.android.myapp.R
import com.google.android.material.slider.Slider


class FragmentSettings : Fragment() {

    private lateinit var audioManager: AudioManager
    private lateinit var cameraID: String
    private lateinit var cameraManager: CameraManager
    private var isFlashlightOn = false
    private lateinit var imageVolume: ImageView
    private lateinit var flashLightContainer: CardView
    private lateinit var flashlightImage: ImageView
    val ThemeMode = "Theme_Mode"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val button = view.findViewById<Button>(R.id.button)
        val button2 = view.findViewById<Button>(R.id.button2)
        imageVolume = view.findViewById(R.id.imageVolume)
        val sliderVolume = view.findViewById<Slider>(R.id.sliderVolume)
        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val flashLight = view.findViewById<CardView>(R.id.cardView3)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        flashLightContainer = view.findViewById(R.id.cardView3)
        flashlightImage = view.findViewById(R.id.flashlightImage)

        sliderVolume.valueTo = maxVolume.toFloat()
        sliderVolume.value = currentVolume.toFloat()
        iconVolumeSet(currentVolume, maxVolume)

        flashLight.setOnClickListener {
            try {
                turnOnOffFlashlight()
            } catch (e: CameraAccessException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }

        button.setOnClickListener {
            openApp("com.v2ray.ang", "com.v2ray.ang.ui.MainActivity")
        }

        button2.setOnClickListener {
            openApp("app.revanced.android.youtube", "com.google.android.youtube.HomeActivity")
        }

        sliderVolume.addOnChangeListener { slider, value, fromUser ->
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), 0)
            iconVolumeSet(value.toInt(), maxVolume)
        }

        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            cameraManager =
                requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraID = try {
                cameraManager.cameraIdList[0]
            } catch (e: CameraAccessException) {
                throw RuntimeException(e)
            }
        } else {
            Toast.makeText(requireContext(), "Tidak Support", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    fun getAttrColor(colorPrimary: Int): TypedValue {
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(colorPrimary, typedValue, true)
        return typedValue
    }

    @Throws(CameraAccessException::class)
    fun turnOnOffFlashlight() {
        val colorPrimary = android.R.attr.colorPrimary
        val textColorPrimary = android.R.attr.textColorPrimary
        isFlashlightOn = if (isFlashlightOn) {
            cameraManager.setTorchMode(cameraID, false)
            flashLightContainer.setCardBackgroundColor(getAttrColor(colorPrimary).data)
            false
        } else {
            cameraManager.setTorchMode(cameraID, true)
            flashLightContainer.setCardBackgroundColor(getAttrColor(textColorPrimary).data)
            true
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openApp(packageName: String, activityName: String) {
        try {
            val intent = Intent().setClassName(packageName, activityName)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Apl tidak terinstal", Toast.LENGTH_SHORT).show()
        }
    }

    fun iconVolumeSet(currentProgress: Int, maxVolume: Int) {
        val maxVolumeBagi = (maxVolume / 2)
        if (currentProgress == 0) {
            imageVolume.setImageResource(R.drawable.volume_mute_24)
        } else if (currentProgress < maxVolumeBagi) {
            imageVolume.setImageResource(R.drawable.volume_down_24)
        } else if (currentProgress > maxVolumeBagi) {
            imageVolume.setImageResource(R.drawable.volume_up_24)
        }
    }
}