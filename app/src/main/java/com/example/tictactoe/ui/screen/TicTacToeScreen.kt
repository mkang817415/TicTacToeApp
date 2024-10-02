package com.example.tictactoe.ui.screen

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.example.tictactoe.ui.screen.TicTacToeModel
import kotlinx.coroutines.launch


@Composable
fun TicTacToeScreen(modifier: Modifier,
                    viewModel: TicTacToeModel = viewModel()
) {
    val board = viewModel.board
    val currentPlayer = viewModel.currentPlayer
    val winner = viewModel.winner
    val playerXWins =  viewModel.playerXWins
    val playerOWins = viewModel.playerOWins
    val round = viewModel.round

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.round, round),
        )


        if (winner != null) {
            Text(
                text = stringResource(R.string.winner_woooww, winner.name),
            )
        } else {
            Text(
                text = stringResource(R.string.current_player, currentPlayer.name)
            )
        }

        TicTacToeBoard(board,
           onCellClick = { boardCell ->
               viewModel.onCellClicked(boardCell)
               viewModel.checkWinner()
           })

        Button(
            onClick = { viewModel.endGame()},
        ) {
            Text(text = stringResource(R.string.end_game))
        }

        Text(
            text = stringResource(R.string.scoreboard)
        )
        Text(
            text = stringResource(R.string.player_x, playerXWins),
        )
        Text(
            text = stringResource(R.string.player_o, playerOWins),
        )



    }
}

@Composable
fun TicTacToeBoard(
    board: Array<Array<Player?>>,
    onCellClick: (BoardCell) -> Unit
) {
    val imageBackGround = ImageBitmap.imageResource(id = R.drawable.richie)

    val textMeasurer = rememberTextMeasurer()



    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1.0f)
            .pointerInput(key1 = Unit) {
                detectTapGestures { offset ->

                    val row = (offset.y / (size.height / 3)).toInt()
                    val col = (offset.x / (size.width / 3)).toInt()

                    onCellClick(BoardCell(row, col))
                }
            }
    ) {
        var gridSize = size.minDimension
        var thirdSize = gridSize / 3

        drawImage(
            image = imageBackGround,
            srcOffset = IntOffset(0,0),
            srcSize = IntSize(imageBackGround.width, imageBackGround.height),
            dstOffset = IntOffset(0,0),
            dstSize = IntSize(gridSize.toInt(),gridSize.toInt())
        )

        //draw text
        val textLayoutResult: TextLayoutResult =
            textMeasurer.measure(
                text = " ",
                style = TextStyle(fontSize = thirdSize.toSp(),
                    fontWeight = FontWeight.Bold)
            )
        val textSize = textLayoutResult.size

        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(thirdSize / 2 - textSize.width / 2,
                thirdSize / 2 - textSize.height / 2)

        )

        for (i in 1..2) {
            drawLine(
                color = Color.Black,
                strokeWidth = 5f,
                start = Offset(thirdSize * i, 0f),
                end = Offset(thirdSize * i, gridSize)
            )

            drawLine(
                color = Color.Black,
                strokeWidth = 5f,
                start = Offset(0f, thirdSize * i),
                end = Offset(gridSize, thirdSize * i)
            )

            for (row in 0..2) {
                for (col in 0..2) {
                    val player = board[row][col]

                    if (player != null) {
                        val centerX = col * thirdSize + thirdSize / 2
                        val centerY = row * thirdSize + thirdSize / 2

                        if (player == Player.X) {
                            drawLine(
                                color = Color.Blue,
                                strokeWidth = 8f,
                                pathEffect = PathEffect.cornerPathEffect(4f),
                                start = androidx.compose.ui.geometry.Offset(
                                    centerX - thirdSize / 4,
                                    centerY - thirdSize / 4
                                ),
                                end = androidx.compose.ui.geometry.Offset(
                                    centerX + thirdSize / 4,
                                    centerY + thirdSize / 4
                                ),
                            )
                            drawLine(
                                color = Color.Blue,
                                strokeWidth = 8f,
                                pathEffect = PathEffect.cornerPathEffect(4f),
                                start = androidx.compose.ui.geometry.Offset(
                                    centerX + thirdSize / 4,
                                    centerY - thirdSize / 4
                                ),
                                end = androidx.compose.ui.geometry.Offset(
                                    centerX - thirdSize / 4,
                                    centerY + thirdSize / 4
                                ),
                            )
                        } else {
                            drawCircle(
                                color = Color.Red,
                                style = Stroke(width = 8f),
                                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                                radius = (thirdSize / 4),
                            )
                        }
                    }
                }
            }
        }
    }
}

