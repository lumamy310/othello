package com.example.othello

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
        val board = _boardState.value

        //check for valid move
        if(board!![row][col] != "blank"){
            _validMoveFlag.value = false
            return
        }
        if(_turn.value == 0){

        }
    }

}