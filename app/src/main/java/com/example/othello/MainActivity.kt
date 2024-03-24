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


        if(viewModel.boardExists.value == false){
            viewModel.resetBoard()
        }
        var rowCount = 0
        var colCount = 0
        for(row in viewModel.boardState.value!!) {
            for (col in row) {
                val imageView = ImageView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    if (col == "blank") {
                        setImageResource(R.drawable.blank_tile)
                    } else if (col == "white") {
                        setImageResource(R.drawable.white_tile)
                    } else {
                        setImageResource(R.drawable.black_tile)
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setPadding(0, 0, 0, 0)
                }
                val params = GridLayout.LayoutParams().apply {
                    width = (50 * resources.displayMetrics.density + 0.5f).toInt()
                    height = (50 * resources.displayMetrics.density + 0.5f).toInt()
                    rowSpec = GridLayout.spec(rowCount)
                    columnSpec = GridLayout.spec(colCount)
                }

                //todo add click listener to change piece
                board.addView(imageView, params)
                colCount++
            }
            colCount = 0
            rowCount++
        }





        //OLD LOGIC FOR BUILDING BOARD WITHOUT VIEWMODEL
//        val numRows = 8
//        val numCols = 8
//        for (row in 0 until numRows) {
//            for (col in 0 until numCols) {
//                val imageView = ImageView(this).apply {
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    if((row == 3 && col == 3) || (row == 4 && col == 4)) {
//                        setImageResource(R.drawable.white_tile)
//                    }
//                    else if ((row == 3 && col == 4) || (row == 4 && col == 3)) {
//                        setImageResource(R.drawable.black_tile)
//                    }
//                    else {
//                        setImageResource(R.drawable.blank_tile)
//                    }
//                    scaleType = ImageView.ScaleType.CENTER_CROP
//                    setPadding(0,0,0,0)
//                }
//
//                val params = GridLayout.LayoutParams().apply {
//                    width = (50 * resources.displayMetrics.density + 0.5f).toInt()
//                    height = (50 * resources.displayMetrics.density + 0.5f).toInt()
//                    rowSpec = GridLayout.spec(row)
//                    columnSpec = GridLayout.spec(col)
//                }
//
//                //todo add click listener to change piece
//                board.addView(imageView, params)
//            }
//        }




    }

    // todo uncomment when entry point becomes login
//    override fun onResume() {
//        super.onResume()
//        val uid = intent.getStringExtra("uid")
//        viewModel.setUid(uid!!)
//    }
}