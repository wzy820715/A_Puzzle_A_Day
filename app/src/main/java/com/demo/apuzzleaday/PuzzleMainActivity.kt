package com.demo.apuzzleaday

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.demo.apuzzleaday.databinding.ActivityPuzzleMainBinding
import com.demo.apuzzleaday.viewmodel.PuzzleViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlin.properties.Delegates

class PuzzleMainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPuzzleMainBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val mViewMode: PuzzleViewModel by viewModels()
    private var backPressedTime by Delegates.observable(0L) { _, old, new ->
        if (new - old < 3000) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.double_click_to_exit), Toast.LENGTH_SHORT).show()
        }
    }

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
        if(mViewMode.isFragmentAdded)
            return
        mViewMode.isFragmentAdded = true
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
            backPressedTime = System.currentTimeMillis()
        }
    }

}