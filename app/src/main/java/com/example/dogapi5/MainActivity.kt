package com.example.dogapi2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.dogapi5.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    private lateinit var dogImageView: ImageView
    private lateinit var regenerateButton: Button
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dogImageView = findViewById(R.id.dogImageView)
        regenerateButton = findViewById(R.id.regenerateButton)
        regenerateButton.setOnClickListener {
            fetchRandomDogImage()
        }

        fetchRandomDogImage()
    }

    private fun fetchRandomDogImage() {
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitHelper.getInstance().create(DogApiService::class.java).getRandomDogImage()
                }
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.message
                    Log.d("MainActivity", "Image URL: $imageUrl")
                    imageUrl?.let {
                        Picasso.get().load(it).into(dogImageView)
                    }
                } else {
                    Log.e("MainActivity", "Failed to load image, Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching image: ${e.message}", e)
            }
        }
    }
}