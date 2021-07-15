package com.demo.apuzzleaday

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.demo.apuzzleaday.databinding.ActivityPuzzleMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

class PuzzleMainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPuzzleMainBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPuzzleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED -> binding.bgDim.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.bgDim.visibility = View.GONE
                    else->{}
                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                binding.bgDim.visibility = View.VISIBLE
                binding.bgDim.alpha = slideOffset
            }
        })
        setFragment(PuzzleSolverFragment())
    }

    private fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.bottom_sheet, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun onDimBgClick(@Suppress("UNUSED_PARAMETER") view: View) {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onBackPressed() {
        if(behavior.state == BottomSheetBehavior.STATE_EXPANDED){
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }else{
            super.onBackPressed()
        }
    }

}