package com.example.othello

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.gridlayout.widget.GridLayout

class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var viewModel: MainActivityViewModel
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null
    private val lastGyroscope = FloatArray(3)
    private var isGyroscopeInitialized = false
    private var cursorX = 0f
    private var cursorY = 0f
    private lateinit var cursor: ImageView
    private lateinit var board: GridLayout
    val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Settings
            if (it.resultCode == 1) {
                val sens = it.data?.getFloatExtra("sens", 50.0f)
                viewModel.setSensitivity(sens!!)
            }
            if(it.resultCode == 2) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        board = findViewById(R.id.gridBoard)
        val turnText = findViewById<TextView>(R.id.colorName)
        val blackCount = findViewById<TextView>(R.id.blackNumber)
        val whiteCount = findViewById<TextView>(R.id.whiteNumber)
        val clickBtn = findViewById<Button>(R.id.clickbtn)
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        cursor = findViewById(R.id.cursor)

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

                board.addView(imageView, params)
                colCount++
            }
            colCount = 0
            rowCount++
        }

        board.post {
            cursorX = (board.width - cursor.width) / 2.toFloat()
            cursorY = (board.height - cursor.height) / 2.toFloat()
            cursor.x = cursorX
            cursor.y = cursorY
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

        clickBtn.setOnClickListener {
            val location = IntArray(2)
            cursor.getLocationOnScreen(location)
            val x = location[0] + cursor.width / 2
            val y = location[1] + cursor.height / 2
            var clickedView: ImageView? = null

            // Find the child view (tile) at the cursor position
            for (i in 0 until board.childCount) {
                val child = board.getChildAt(i)
                val locationChild = IntArray(2)
                child.getLocationOnScreen(locationChild)
                if (x >= locationChild[0] && x < locationChild[0] + child.width &&
                    y >= locationChild[1] && y < locationChild[1] + child.height
                ) {
                    clickedView = child as ImageView
                    break
                }
            }

            // Perform the move if a valid tile is clicked
            if (clickedView != null) {
                val index = board.indexOfChild(clickedView)
                val row = index / board.columnCount
                val col = index % board.columnCount
                viewModel.move(row, col)
            }
        }

        settingsButton.setOnClickListener {
            val toSettings = Intent(this, SettingsActivity::class.java)
            toSettings.putExtra("sens", viewModel.sensitivity.value)
            toSettings.putExtra("uid", viewModel.uid.value.toString())
            settingsLauncher.launch(toSettings)
        }


    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor == gyroscope) {
                if (!isGyroscopeInitialized) {
                    lastGyroscope[0] = event.values[0]
                    lastGyroscope[1] = event.values[1]
                    lastGyroscope[2] = event.values[2]
                    isGyroscopeInitialized = true
                } else {
                    val deltaY = event.values[0] - lastGyroscope[0] // Use deltaY for left-right movement
                    val deltaX = event.values[1] - lastGyroscope[1] // Use deltaX for up-down movement

                    cursorX += deltaX * viewModel.sensitivity.value!!
                    cursorY += deltaY * viewModel.sensitivity.value!!

                    // Limit the cursor movement to the board bounds
                    val boardLeft = board.x
                    val boardTop = board.y
                    val boardRight = boardLeft + board.width - cursor.width
                    val boardBottom = boardTop + board.height - cursor.height

                    cursorX = cursorX.coerceIn(boardLeft, boardRight)
                    cursorY = cursorY.coerceIn(boardTop, boardBottom)

                    // Update the cursor position
                    cursor.x = cursorX
                    cursor.y = cursorY
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not necessary for gyroscope
    }

    override fun onResume() {
        super.onResume()
        val uid = intent.getStringExtra("uid")
        viewModel.setUid(uid!!)
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        gyroscope?.let {
            sensorManager.unregisterListener(this)
        }
    }

}
