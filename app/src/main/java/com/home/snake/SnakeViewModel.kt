package com.home.snake

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class SnakeViewModel : ViewModel(){
    private lateinit var timer: Timer
    val body = MutableLiveData<List<Position>>()
    val apple = MutableLiveData<Position>()
    val score = MutableLiveData<Int>()
    val gameState = MutableLiveData<GameState>()
    private val snakeBody = mutableListOf<Position>()
    private var direction = Direction.LEFT
    private var point : Int = 0
    private lateinit var applePos: Position
    fun start(){
        score.postValue(point)
        snakeBody.apply {
            add(Position(10,10))
            add(Position(11,10))
            add(Position(12,10))
            add(Position(13,10))
        }.also {
            body.value = snakeBody
        }
        generateApple()
        timer = fixedRateTimer("timer",true,500,400){
            val pos = snakeBody.first().copy().apply {
                when(direction){
                    Direction.LEFT ->x--
                    Direction.RIGHT ->x++
                    Direction.TOP ->y--
                    Direction.DOWN ->y++
                }
                if(snakeBody.contains(this) || x < 0 || x>=20 || y<0 || y>=20){
                    cancel()
                    gameState.postValue(GameState.GAME_OVER)
                }

            }
            snakeBody.add(0,pos)
            if(pos != applePos){
                snakeBody.removeLast()
            }else{
                point+=100
                score.postValue(point)
                generateApple()
            }
            body.postValue(snakeBody)
        }

    }
    fun generateApple(){
        var spots =  mutableListOf<Position>().apply {
            for(i in 0..19)
                for(j in 0..19){
                    add(Position(i,j))
                }
        }
        spots.removeAll(snakeBody)
        spots.shuffle()
        applePos = spots[0]

        /*do {
            applePos = Position(Random.nextInt(20), Random.nextInt(20))
        }while (snakeBody.contains(applePos))*/

        apple.postValue(applePos)
    }
    fun reset(){
        timer.cancel()
        snakeBody.clear()
        body.postValue(snakeBody)
        point = 0
        score.postValue(point)
        direction = Direction.LEFT
        start()

    }
    fun move(dir :Direction){
        direction =dir

    }

}


data class Position (var x : Int,var y :Int)

enum class Direction{
    TOP,DOWN,LEFT,RIGHT
}
enum class GameState{
    ONGOING,GAME_OVER
}