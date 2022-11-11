package tictactoe

import java.lang.Math.abs
import kotlin.math.absoluteValue

data class Vector(
    val xWin: Boolean = false,
    val oWin: Boolean = false,
    val empties: Boolean = false
)

class Grid(initState: CharArray) {
    private val grid = mutableListOf(
        mutableListOf('_', '_', '_'),
        mutableListOf('_', '_', '_'),
        mutableListOf('_', '_', '_')
    )
    init {
        var pos = 0
        for (y in grid) {
            for (x in y.indices) {
                y[x] = initState[x + pos]
            }
            pos += 3
        }
    }

    fun printGrid() {
        println("---------")
        for (y in grid) {
            println("| " + y.joinToString(" ") + " |")
        }
        println("---------")
    }

    fun update(x: Int, y: Int): Boolean {
        return if (grid[y-1][x-1] == 'X' || grid[y-1][x-1] == 'O') {
            false
        } else {
            grid[y-1][x-1] = 'X'
            true
        }
    }

    fun analyzeGrid(): String {

        val columnVars = mutableListOf<MutableList<Char>>()
        val diagonal1 = mutableListOf<Char>()
        val diagonal2 =  mutableListOf<Char>()
        val columns = mutableListOf<Pair<Int,Vector>>()
        val rows = mutableListOf<Pair<Int,Vector>>()
        var pos = 0
        var numX = 0
        var numO = 0

        for (y in grid) {
            for (x in y.indices) {

                if (x == grid.indexOf(y)) {
                    diagonal1.add(y[x])
                }
                if (x == grid.lastIndex - grid.indexOf(y)) {
                    diagonal2.add(y[x])
                }
                if (y[x] == 'X') {
                    numX++
                }
                if (y[x] == 'O') {
                    numO++
                }
                if (columnVars.size - 1 < x ) {
                    columnVars.add(mutableListOf(y[x]))
                } else {
                    columnVars[x].add(y[x])
                }
            }
            rows.add(
                grid.indexOf(y) to
                        Vector(y.all { it == 'X' }, y.all { it == 'O' }, y.any { it == '_' })
            )
            pos += 3
        }
        for (column in columnVars){
            columns.add(
                columnVars.indexOf(column) to
                        Vector(column.all { it == 'X' }, column.all { it == 'O' }, column.any { it == '_' })
            )
        }
        var xWinner = false
        var oWinner = false

        if (columns.any { it.second.xWin } || rows.any { it.second.xWin } || diagonal1.all { it == 'X' } || diagonal2.all { it == 'X' }) {
            xWinner = true
        }
        if (columns.any {
                it.second.oWin } || rows.any { it.second.oWin } || diagonal1.all { it == 'O' } ||  diagonal2.all { it == 'O' }) {
            oWinner = true
        }
        return if (xWinner || oWinner) { //|| (kotlin.math.abs(numO - numX) > 1)) {
/*            if (xWinner && oWinner || kotlin.math.abs(numO - numX) > 1) {
                "Impossible"
            } else */
            if (xWinner) {
                "X wins"
            } else {
                "O wins"
            }
        } else {
            if (rows.any { it.second.empties }) {
                "Game not finished"
            } else {
                "Draw"
            }
        }
    }
}

fun main() {
    val grid = Grid("X__X___XX".toCharArray())
    grid.printGrid()
    while (true) {
        val userMove = readln().split(" ").toTypedArray().reversed()
        if (!userMove.all { it.asSequence().all { c -> c.isDigit() } }) {
            println("You should enter numbers!")
        } else if (
            userMove.first().toInt() < 1 ||
            userMove.first().toInt() >3 ||
            userMove.last().toInt() < 1 ||
            userMove.last().toInt() > 3) {
            println("Coordinates should be from 1 to 3!")
        } else {
            if (!grid.update(userMove.first().toInt(), userMove.last().toInt())) {
                println("This cell is occupied! Choose another one!")

            } else {
                grid.printGrid()
                val result = grid.analyzeGrid()
                if (result == "Draw" || result == "X wins" || result == "O wins") {
                    println(result)
                    break
                } else {
                    continue
                }
            }
        }
    }
}