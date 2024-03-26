package com.example.othello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

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

                    setOnClickListener { view ->
                        val index = board.indexOfChild(view)
                        val row = index / board.columnCount
                        val col = index % board.columnCount
                        viewModel.move(row, col)
                    }

                }
                val params = GridLayout.LayoutParams().apply {
                    width = (50 * resources.displayMetrics.density + 0.5f).toInt()
                    height = (50 * resources.displayMetrics.density + 0.5f).toInt()
                    rowSpec = GridLayout.spec(rowCount)
                    columnSpec = GridLayout.spec(colCount)
                }

                board.addView(imageView, params)
                colCount++
            }
            colCount = 0
            rowCount++
        }


        viewModel.validMoveFlag.observe(this) {
            if(viewModel.getValidMove() == false) {
                Snackbar.make(
                    board,
                    "Invalid move - try again",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.boardState.observe(this) { boardState ->
            for (row in 0 until boardState.size) {
                for (col in 0 until boardState[row].size) {
                    val imageView = board.getChildAt(row * boardState.size + col) as ImageView
                    when (boardState[row][col]) {
                        "blank" -> imageView.setImageResource(R.drawable.blank_tile)
                        "white" -> imageView.setImageResource(R.drawable.white_tile)
                        "black" -> imageView.setImageResource(R.drawable.black_tile)
                    }
                }
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