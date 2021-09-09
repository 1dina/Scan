package com.example.scantest

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraDevice
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.mlplugin.card.bcr.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.log10


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        detect.isEnabled = false

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        } else {
            buttonClickListener()
        }


    }
    private val callback: MLBcrCapture.Callback = object : MLBcrCapture.Callback {
        override fun onSuccess(bankCardResult: MLBcrCaptureResult) {
            edit.setText(bankCardResult.number)
 }

        override fun onCanceled() {


        }

        override fun onFailure(retCode: Int, bitmap: Bitmap) {
            Toast.makeText(this@MainActivity, "TRY AGAIN LATER!", Toast.LENGTH_SHORT).show()
        }

        override fun onDenied() {

        }
    }

    private fun startCaptureActivity(callback: MLBcrCapture.Callback) {
        val config = MLBcrCaptureConfig.Factory()
                .setResultType(MLBcrCaptureConfig.RESULT_NUM_ONLY)
                .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
                .create()
        val bankCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config)
        bankCapture.captureFrame(this, callback)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            buttonClickListener()
        }
    }

    private fun buttonClickListener() {
        detect.isEnabled = true
        detect.setOnClickListener {
            startCaptureActivity(callback)
        }
    }

}











