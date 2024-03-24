package com.example.othello

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private val _uid = MutableLiveData<String>()
    val uid: LiveData<String> get() = _uid

    private val _boardState = MutableLiveData<Array<Array<String>>>()
    val boardState: LiveData<Array<Array<String>>> = _boardState

    private val _boardExists = MutableLiveData<Boolean>(false)
    val boardExists: LiveData<Boolean> get() = _boardExists

    // 0 is black, white is 1. Black always goes first
    private val _turn = MutableLiveData<Int>(0)
    val turn: LiveData<Int> get() = _turn

    private val _validMoveFlag = MutableLiveData<Boolean>()
    val validMoveFlag: LiveData<Boolean> get() = _validMoveFlag

    fun setUid(uid: String) {
        _uid.value = uid
    }

    fun resetBoard() {
        val board = Array(8) { Array(8) { "blank" } }
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                if((row == 3 && col == 3) || (row == 4 && col == 4)) {
                    board[row][col] = "white"
                }
                else if ((row == 3 && col == 4) || (row == 4 && col == 3)) {
                    board[row][col] = "black"
                }
                else{
                    board[row][col] = "blank"
                }
            }
        }
        _boardState.value = board
        _turn.value = 0
        _boardExists.value = true
    }

    fun move(row: Int, col: Int) {
        val board = _boardState.value!!
        var color = ""

        if(_turn.value == 0) {
            color = "black"
        }
        else {
            color = "white"
        }

        //check for valid move
        val directions = listOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1), /* Current position */ Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        Log.d(TAG, "row $row col $col of selected tile")

        if (board!![row][col] != "blank") {
            _validMoveFlag.value = false
            return
        }

        var validMove = false
        for ((dx, dy) in directions) {
            var r = row + dx
            var c = col + dy
            var foundOpponent = false
            val cellsToFlip = mutableListOf<Pair<Int, Int>>()

            while (r in board!!.indices && c in board!![0].indices) {
                if (board!![r][c] == color) {
                    if (foundOpponent) {
                        validMove = true
                        cellsToFlip.forEach { (flipRow, flipCol) ->
                            board[flipRow][flipCol] = color
                        }
                        board[row][col] = color
                    }
                    break
                } else if (board[r][c] == "blank") {
                    break
                } else {
                    foundOpponent = true
                    cellsToFlip.add(Pair(r, c))
                }
                r += dx
                c += dy
            }
        }

        _validMoveFlag.value = validMove
        if(validMove == true && _turn.value == 0) {
            _boardState.value = board!!
            _turn.value = 1
        }
        else if (validMove == true && _turn.value == 1) {
            _boardState.value = board!!
            _turn.value = 0
        }
    }

    fun getValidMove(): Boolean? {
        return _validMoveFlag.value
    }

    fun getBoard(): Array<Array<String>>? {
        return _boardState.value
    }


}