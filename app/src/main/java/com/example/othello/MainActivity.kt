package com.example.othello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val board = findViewById<GridLayout>(R.id.gridBoard)

        val numRows = 8
        val numCols = 8
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val imageView = ImageView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setImageResource(R.drawable.blank_tile) // Set your image resource here
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setPadding(0,0,0,0) // Optional: Set padding between ImageViews
                }

                val params = GridLayout.LayoutParams().apply {
                    width = (50 * resources.displayMetrics.density + 0.5f).toInt() // Convert 50dp to pixels
                    height = (50 * resources.displayMetrics.density + 0.5f).toInt()
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(col)
                }

                board.addView(imageView, params)
            }
        }




    }

    // todo uncomment when entry point becomes login
//    override fun onResume() {
//        super.onResume()
//        val uid = intent.getStringExtra("uid")
//        viewModel.setUid(uid!!)
//    }
}