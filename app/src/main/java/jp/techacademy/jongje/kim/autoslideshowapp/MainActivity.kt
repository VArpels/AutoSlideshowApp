package jp.techacademy.jongje.kim.autoslideshowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE =100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

                getContentsInfo()

            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSIONS_REQUEST_CODE)
            }
        }else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSIONS_REQUEST_CODE ->
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("ANDROID", "許可された")
                } else {
                    Log.d("ANDROID", "許可されなかった")
                }
        }

    }
    private fun getContentsInfo() {

        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if(cursor!=null){
            while (cursor.moveToNext()) {

                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                Log.d("ANDROID", "URI:" + imageUri.toString())

            }
        }


        pause_button.setOnClickListener{
            if (cursor!!.moveToFirst()) {
                do {

                        val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id = cursor.getLong(fieldIndex)
                        val imageUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)

                        Log.d("ANDROID", "URI : " + imageUri.toString())
                        //Handler(Looper.getMainLooper()).postDelayed({
                        imageView.setImageURI(imageUri)
                    //}, 200)

                } while (cursor.moveToNext())
            }

        }

        back_button.setOnClickListener{
            if (cursor!!.moveToPrevious()) {
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    Log.d("ANDROID", "URI:" + imageUri.toString())
                imageView.setImageURI(imageUri)
                }
        }


        play_button.setOnClickListener{
            if (cursor!!.moveToNext()) {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                Log.d("ANDROID", "URI:" + imageUri.toString())
                imageView.setImageURI(imageUri)
            }
        }
    }
}
