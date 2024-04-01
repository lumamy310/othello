package com.example.othello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val board = findViewById<GridLayout>(R.id.gridBoard)
        val turnText = findViewById<TextView>(R.id.colorName)
        val blackCount = findViewById<TextView>(R.id.blackNumber)
        val whiteCount = findViewById<TextView>(R.id.whiteNumber)


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
                        if((viewModel.whiteWin.value == true) || (viewModel.blackWin.value == true) || (viewModel.draw.value == true)) {
                            print("no click allowed")
                        }
                        else {
                            val index = board.indexOfChild(view)
                            val row = index / board.columnCount
                            val col = index % board.columnCount
                            viewModel.move(row, col)
                        }
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
                    Snackbar.LENGTH_SHORT
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

        viewModel.turn.observe(this) { turn ->
            val whiteStr = "White's turn"
            val blackStr = "Black's turn"
            if(turn == 0) {
                turnText.setText(blackStr)
            }
            else {
                turnText.setText(whiteStr)
            }
            if(viewModel.whitePass.value == true && viewModel.blackPass.value == true) {
                viewModel.checkWin()
                return@observe
            }
            else if(viewModel.hasValidMove() == false) {
                Snackbar.make(board, "No valid moves - passing", Snackbar.LENGTH_SHORT).show()
                viewModel.nextTurn()
            }
        }

        viewModel.blackCounter.observe(this) { blackCounter ->
            val newCount = blackCounter.toString()
            blackCount.setText(newCount)
        }

        viewModel.whiteCounter.observe(this) { whiteCounter ->
            val newCount = whiteCounter.toString()
            whiteCount.setText(newCount)
        }

        viewModel.whiteWin.observe(this) { win ->
            if(win == true) {
                val winText = "White wins!"
                Snackbar.make(board, "White wins!", Snackbar.LENGTH_LONG).show()
                turnText.setText(winText)
            }
        }

        viewModel.blackWin.observe(this) { win ->
            if(win == true) {
                val winText = "Black wins!"
                Snackbar.make(board, "Black wins!", Snackbar.LENGTH_LONG).show()
                turnText.setText(winText)
            }
        }

        viewModel.draw.observe(this) { win ->
            if(win == true) {
                val winText = "Draw"
                Snackbar.make(board, "Draw!", Snackbar.LENGTH_LONG).show()
                turnText.setText(winText)
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