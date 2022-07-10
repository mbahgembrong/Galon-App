package com.example.galonapps.ui.pelanggan.transaksi

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.config.Constant
import com.example.galonapps.databinding.ActivityBayarBinding
import com.example.galonapps.model.TransaksiRequest
import com.google.common.util.concurrent.ListenableFuture
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.squareup.picasso.Picasso
import timber.log.Timber
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BayarActivity : AppCompatActivity() {
    companion object {
        const val ACTIVITY_PEMBAYARAN = "Activity Pembayaran"
    }

    lateinit var binding: ActivityBayarBinding
    lateinit var viewModel: TransaksiViewModel
    lateinit var transaksi: Bundle
    var imstr = ""
    var fileUri = Uri.parse("")

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaksi = intent.getBundleExtra(ACTIVITY_PEMBAYARAN)!!
        initView()
        Timber.d("transaksi: $transaksi")
        observer()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        kameraInput()
        binding.previewCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }
        binding.imageBuktiPembayaran.setOnClickListener {
            if (fileUri.toString().isNotEmpty()) {
                startCamera()
                binding.imageBuktiPembayaran.visibility = View.INVISIBLE
            }
        }
        binding.buttonTakeCamera.setOnClickListener {
            takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                animateFlash()
            }
        }
        binding.buttonBayar.setOnClickListener {
            App.alertDialog(this) {
                viewModel.bayar(
                    TransaksiRequest(
                        transaksi.getString("idTransaksi"), null, null, null, imstr,
                        transaksi.getInt("status")
                    )
                )
            }
        }
    }

    private fun observer() {
        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        viewModel.isUpdateTransaksi.observe(this) {
            if (it) {
                toast("Upload Bukti Pembayaran Pembayaran", MotionToast.TOAST_SUCCESS)
                finish()
            } else {
                toast("Upload Bukti Pembayaran Gagal", MotionToast.TOAST_ERROR)
            }
        }
    }

    private fun initView() {
        binding = ActivityBayarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textKeteranganPembayaran.text = Constant.KETERANGAN_PEMBAYARAN
        binding.textGrandTotal.text = App.currencyFormat(transaksi.getInt("grandTotal"))

    }

    fun kameraInput() =
        runWithPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA) {
            startCamera()
        }

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            this, message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )
    }

    private fun startCamera() {
        binding.apply {
            previewCamera.visibility = View.VISIBLE
            buttonTakeCamera.visibility = View.VISIBLE
            buttonBayar.visibility = View.INVISIBLE
            imageBuktiPembayaran.visibility = View.INVISIBLE
        }
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.previewCamera.createSurfaceProvider())
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    fileUri = output.savedUri
                    Picasso.get().load(fileUri).into(binding.imageBuktiPembayaran)
                    binding.apply {
                        previewCamera.visibility = View.INVISIBLE
                        buttonTakeCamera.visibility = View.INVISIBLE
                        buttonBayar.visibility = View.VISIBLE
                        imageBuktiPembayaran.visibility = View.VISIBLE
                    }
                    imstr = encodeImageToString(fileUri)
                }
            }
        )
    }

    fun encodeImageToString(uri: Uri): String {
        val imageStream: InputStream? = contentResolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val outputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

}
