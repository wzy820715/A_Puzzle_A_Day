package com.demo.apuzzleaday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.apuzzleaday.databinding.ActivityPlayBinding

class PuzzlePlayActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}