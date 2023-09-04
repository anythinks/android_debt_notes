package com.android.myapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class FragmentMusic : Fragment() {

    lateinit var audioManager: AudioManager
    private lateinit var cameraID: String
    private lateinit var cameraManager: CameraManager
    private var isFlashlightOn = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)
        val button = view.findViewById<Button>(R.id.button)
        val button2 = view.findViewById<Button>(R.id.button2)
        val volumeText = view.findViewById<TextView>(R.id.textVolume)
        val seekBarVolume = view.findViewById<SeekBar>(R.id.seekBarVolume)
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val flashLight = view.findViewById<CardView>(R.id.cardView3)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeText.text = "Volume : $currentVolume"
        seekBarVolume.max = maxVolume
        seekBarVolume.progress = currentVolume

        flashLight.setOnClickListener {
            try {
                turnOnOffFlashlight()
            } catch (e: CameraAccessException) {
                throw RuntimeException(e)
            }
        }

        button.setOnClickListener{
            openApp("com.v2ray.ang", "com.v2ray.ang.ui.MainActivity")
        }

        button2.setOnClickListener{
            openApp("app.revanced.android.youtube", "com.google.android.youtube.HomeActivity")
        }

        seekBarVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                volumeText.text = "Volume : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraID = try {
                cameraManager.cameraIdList[0]
            } catch (e: CameraAccessException) {
                throw RuntimeException(e)
            }
        } else {
            Toast.makeText(requireContext(),"Tidak Support", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    @Throws(CameraAccessException::class)
    fun turnOnOffFlashlight() {
        isFlashlightOn = if (isFlashlightOn) {
            cameraManager.setTorchMode(cameraID, false)
            false
        } else {
            cameraManager.setTorchMode(cameraID, true)
            true
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openApp(packageName:String, activityName:String){
        val intent = Intent().setClassName(packageName, activityName)
        startActivity(intent)
    }
}

