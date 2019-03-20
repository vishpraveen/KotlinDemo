package com.example.magneto.kotlindemo.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.NavigationAdapter
import com.example.magneto.kotlindemo.bean.NavModel
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecyclerOnClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThirdNew : AppCompatActivity(),RecyclerOnClick  {

    private var drawerLayout:DrawerLayout?=null
    var bar:BottomAppBar?=null
    private var recyclerNavMenu:RecyclerView?=null
    var layoutManager:LinearLayoutManager?=null
    private var navAdapter:NavigationAdapter?=null
    private lateinit var mContext:Context
    private var listener:RecyclerOnClick?=null
    private var nav_header : ConstraintLayout?=null
    private lateinit var iv_image : ImageView
    private lateinit var rlBase64 : RelativeLayout
    private lateinit var rlCompressed : RelativeLayout
    private lateinit var ivBase64Image : ImageView
    private lateinit var ivCompressedImage : ImageView
    private var navList=ArrayList<NavModel>()
    var fab:FloatingActionButton?=null
    private val REQUEST_IMAGE_GET : Int =10
    private val REQUEST_IMAGE_CAPTURE = 20
    private val REQUEST_IMAGE_CAPTURE_FULL = 30
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_new)

        mContext=this
        listener=this

        initUI()
        onClickListener()

        animation_view.playAnimation()
        animation_view1.playAnimation()

    }

    private fun onClickListener() {
        iv_image.setOnClickListener {
            drawerLayout?.closeDrawer(GravityCompat.START)
            Toast.makeText(mContext, "Clicked On Image", Toast.LENGTH_SHORT).show()
            showImageDialog();
        }

        bar?.setNavigationOnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }
        fab?.setOnClickListener {
//            val intent=Intent(mContext,SplashActivity::class.java)
            val intent=Intent(mContext,TestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showImageDialog() {
        var builder : AlertDialog.Builder = MaterialAlertDialogBuilder(mContext)
//        var layoutInflater =this.layoutInflater
        var view : View = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom,null)
        builder.setView(view)
        builder.setCancelable(true)
        var llGallery : LinearLayout= view.findViewById(R.id.llGallery)
        var llCamera : LinearLayout= view.findViewById(R.id.llCamera)
        var llCameraFullImage : LinearLayout= view.findViewById(R.id.llCameraFullImage)

        var alertDialog = builder.create()
        alertDialog.show()

        llGallery.setOnClickListener {
            alertDialog.dismiss()
            /*
            * checkPermission and open Gallary
            * */
            if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_IMAGE_GET)
            }
            else{
                openGallery()
            }
        }

        llCamera.setOnClickListener {
            alertDialog.dismiss()
            /*
            * checkPermission and open Camera
            * */
            if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_IMAGE_CAPTURE)
            }
            else{
                openCamera(REQUEST_IMAGE_CAPTURE)
            }
        }

        llCameraFullImage.setOnClickListener {
            alertDialog.dismiss()
            /*
            * checkPermission and open Camera
            * */
            if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_IMAGE_CAPTURE_FULL)
            }
            else{
                openCamera(REQUEST_IMAGE_CAPTURE_FULL)
            }
        }
    }

    private fun openGallery() {
        msg("Gallery")
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type="image/*"
        }
        if (intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,REQUEST_IMAGE_GET)
        }
    }

    private fun openCamera(requestFrom : Int) {
        /*
        * Camera code for Thumbnail Image
        * */
        if (requestFrom == REQUEST_IMAGE_CAPTURE) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
        /*
        * Camera code for getting the image path.
        * */
        if (requestFrom == REQUEST_IMAGE_CAPTURE_FULL) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
//                Continue only if the File was successfully created
                    photoFile!!.also {
                        val photoUri: Uri = FileProvider.getUriForFile(
                                this,
//                            "com.example.magneto.kotlindemo.fileprovider",
                                mContext.packageName,
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_FULL)
                    }
                }
            }
        }
    }

    /*
    * This method is used to create Temporary image file with absolute file path
    * */
    private fun createImageFile() : File{
//        Create an image from file
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}",/*prefix*/
                ".jpg",/*suffix*/
                    storageDir/* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun msg(s: String) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            when(requestCode){
                REQUEST_IMAGE_GET -> {
                    if (data!=null) {
                        val fullPhotoUri: Uri = data!!.data
//                        Log.e("FullPhotoUri: ", fullPhotoUri.toString())
                        val imageStream : InputStream = contentResolver.openInputStream(fullPhotoUri)
                        val bitmap : Bitmap = BitmapFactory.decodeStream(imageStream)
//                        Log.e("Original_Size: ",""+bitmap.byteCount)
                        val compressedBitmap = compressBitmapSize(bitmap)
//                        Log.e("Compressed_Size: ",""+compressedBitmap.byteCount)
                        val base64Image: String = convertToBase64(compressedBitmap)
//                        Log.e("Base64Image: ", base64Image.trim())
                        convertBase64ToBitmap(base64Image)
                        Glide.with(mContext)
                                .asBitmap()
                                .load(fullPhotoUri)
                                .into(iv_image)

                        Glide.with(mContext)
                                .asBitmap()
                                .load(compressedBitmap)
                                .into(ivCompressedImage)

                        rlCompressed.visibility= View.VISIBLE

//                        iv_image.setImageResource(fullPhotoUri)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
//                    uncomment below code to get thumbnail image from camera intent
                    val imageBitmap = data!!.extras.get("data") as Bitmap
                    Log.e("ImageBitMap: ",imageBitmap.toString())
                    iv_image.setImageBitmap(imageBitmap)
                    /*
                    * Code for image compression and converting to Base64
                    * */
                    compressBitmap_and_convertToBase64(imageBitmap)



                }
                REQUEST_IMAGE_CAPTURE_FULL -> {
//                    for full size image
                    Log.e("Actual_file",currentPhotoPath)
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentPhotoPath)
                            .into(iv_image)
                    val file : File= File(currentPhotoPath)
                    val bitmapOptions : BitmapFactory.Options = BitmapFactory.Options()
                    var imageBitmap : Bitmap = BitmapFactory.decodeFile(file.absolutePath,bitmapOptions)
                    /*
                    * Code for image compression and converting to Base64
                    * */
                    compressBitmap_and_convertToBase64(imageBitmap)
                }
                else -> {
                    msg("Sorry Something went wrong.")
                }
            }
        }
    }

    private fun compressBitmap_and_convertToBase64(imageBitmap: Bitmap) {
        val compressedBitmap = compressBitmapSize(imageBitmap)
//                        Log.e("Compressed_Size: ",""+compressedBitmap.byteCount)
        val base64Image: String = convertToBase64(compressedBitmap)
//                        Log.e("Base64Image: ", base64Image.trim())
        convertBase64ToBitmap(base64Image)
        Glide.with(mContext)
                .asBitmap()
                .load(imageBitmap)
                .into(iv_image)

        Glide.with(mContext)
                .asBitmap()
                .load(compressedBitmap)
                .into(ivCompressedImage)

        rlCompressed.visibility= View.VISIBLE
    }

    private fun convertBase64ToBitmap(base64Image: String) {
        val decodedString = Base64.decode(base64Image,Base64.DEFAULT)
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.size)

        Glide.with(mContext)
                .asBitmap()
                .load(bitmap)
                .into(ivBase64Image)

        rlBase64.visibility= View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_IMAGE_GET -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }
            }
        }
    }

   public fun compressBitmapSize(image : Bitmap) :Bitmap{
       /*val IMAGE_MAX_SIZE = 900
       var width =image.width
       var height = image.height

       val bitmapRatio : Float = (width/height).toFloat()
       if (bitmapRatio>1){
           width=IMAGE_MAX_SIZE
           height = (width/bitmapRatio).toInt()
       }
       else{
           height = IMAGE_MAX_SIZE
//           width= (height*bitmapRatio).toInt()
       }*/
       return Bitmap.createScaledBitmap(image,600,400,true)
   }

    private fun convertToBase64(bitmap: Bitmap) : String{
        val baos : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        val b =baos.toByteArray()
        return Base64.encodeToString(b,Base64.DEFAULT)
    }

    private fun initUI() {
        bar=findViewById(R.id.bar)
        drawerLayout=findViewById(R.id.drawer_layout)
        nav_header=findViewById(R.id.nav_header)
        iv_image=nav_header!!.findViewById(R.id.iv_image)
        recyclerNavMenu=findViewById(R.id.recycler_nav_menu)
        layoutManager= LinearLayoutManager(mContext)
        rlBase64 = findViewById(R.id.rlBase64)
        rlCompressed = findViewById(R.id.rlCompressed)
        ivBase64Image = findViewById(R.id.ivBase64Image)
        ivCompressedImage = findViewById(R.id.ivCompressedImage)
        recyclerNavMenu?.layoutManager=layoutManager
        navAdapter=NavigationAdapter(mContext,navList,listener)
        recyclerNavMenu?.adapter= navAdapter

        fab=findViewById(R.id.fab)
        addNavMenuItems()
    }

    private fun addNavMenuItems() {
//        navList= ArrayList<NavModel>()
        val model1= NavModel()
        model1.image=R.drawable.ic_home
        model1.name=getString(R.string.nav_home)

        val model2= NavModel()
        model2.image=R.drawable.ic_favorite
        model2.name=getString(R.string.nav_profile)

        val model3= NavModel()
        model3.image=R.drawable.ic_contact_phone
        model3.name=getString(R.string.nav_contact)

        val model4= NavModel()
        model4.image=R.drawable.ic_contact_mail
        model4.name=getString(R.string.nav_movie)

        val model5= NavModel()
        model5.image=R.drawable.ic_room
        model5.name=getString(R.string.nav_logout)

        navList.add(model1)
        navList.add(model2)
        navList.add(model3)
        navList.add(model4)
        navList.add(model5)

        navAdapter!!.notifyDataSetChanged()

    }

    override fun onClick(where: EnumClicks, view: View, objects: Objects?, position: Int) {
        val model= navList[position]
//        Toast.makeText(mContext,"Clicked on: "+model.name+" \n Position is: "+position,Toast.LENGTH_SHORT).show()

        if (position==0){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,MainActivity::class.java)
            startActivity(intent)
        }
        if (position==1){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,SwipeActivity::class.java)
            startActivity(intent)
        }
        if (position==2){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,AnimationActivity::class.java)
            startActivity(intent)
        }
        if (position==3){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,MovieDetails::class.java)
            startActivity(intent)
        }
        if (position== 4){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent = Intent(mContext,ProductActivity::class.java)
            startActivity(intent)
        }
    }
}
