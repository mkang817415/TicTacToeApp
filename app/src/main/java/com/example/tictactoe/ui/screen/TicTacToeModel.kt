package com.example.tictactoe.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel

enum class Player{
    X, O
}

data class BoardCell(val row: Int, val col: Int)

class TicTacToeModel: ViewModel() {

    var board by mutableStateOf(
        Array(3){Array(3) {null as Player?} }
    )

    var currentPlayer by mutableStateOf(Player.X)
    var winner by mutableStateOf<Player?>(null)

    var playerXWins by mutableIntStateOf(0)
    var playerOWins by mutableIntStateOf(0)
    var round: Int = 1

    fun onCellClicked(cell: BoardCell){
        if (board[cell.row][cell.col] == null){
            board[cell.row][cell.col] = currentPlayer
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X

        }
    }

    fun endGame() {

        playerXWins = 0
        playerOWins = 0
        round = 1
        resetGame()
    }

    fun resetGame() {
        board = Array(3) { Array(3) { null as Player? } }
        currentPlayer = Player.X
        winner = null
    }

    fun checkWinner() {
        for (i in 0..2) {
            // Check rows
            if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winner = board[i][0]
                if (winner == Player.X) {
                    playerXWins++
                } else {
                    playerOWins++
                }
                round++
                resetGame()
            }
            // Check columns
            if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winner = board[0][i]
                if (winner == Player.X) {
                    playerXWins++
                } else {
                    playerOWins++
                }
                round++
                resetGame()
            }
        }

        // top left to bottom right diagonal
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = board[0][0]
            if (winner == Player.X) {
                playerXWins++
            } else {
                playerOWins++
            }
            round++
            resetGame()
        } else if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winner = board[0][2]
            if (winner == Player.X) {
                playerXWins++
            } else {
                playerOWins++
            }
            round++
            resetGame()

        } else {
            // No winner yet
            for (i in 0 .. 2){
                for (j in 0 .. 2){
                    if (board[i][j] == null){
                        return
                    }
                }
            }
            round++
            resetGame()

        }


    }



}