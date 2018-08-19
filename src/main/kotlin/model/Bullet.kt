package model

import business.*
import enums.Direction
import org.itheima.kotlin.game.core.Painter

class Bullet(override val owner:View, override val direction:Direction, block:(Int, Int)->Pair<Int,Int>) :AutoMoveAble, DestoryAble,AttackAble ,SufferAble{
    override var blood: Int =1

    override fun notifyAttack(attackAble: AttackAble): Array<View>? {
        needDestory = true
        return null
    }

    override val attackPower: Int =1

    var needDestory = false
    override fun willAttack(suffer: SufferAble): Boolean {
        if (y >= suffer.y + suffer.height || y + height <= suffer.y || suffer.x + suffer.width <= x || x + width <= suffer.x) {
            return false
        }
        return true
    }

    override fun notifyAttack(sufferAble: SufferAble) {
        needDestory = true
    }

    override fun needDestory(): Boolean {
        if (needDestory || x < 0 || y < 0 || x > Config.gameWidth || y > Config.gameHeight) {
            return true
        }
        return false
    }

    override fun aotoMove() {
        when(direction){
            Direction.UP-> y-= speed
            Direction.DOWN-> y+=speed
            Direction.LEFT-> x -=speed
            Direction.RIGHT-> x +=speed
        }
    }

    override val speed: Int = 8
    override val wight: Int=2
    override var width: Int = Config.blockSize
    override var height: Int = Config.blockSize
    override var x: Int = 0
    override var y: Int = 0

    val imgPath by lazy { when (direction) {
        Direction.UP -> "img/bullet_u.gif"
        Direction.DOWN -> "img/bullet_d.gif"
        Direction.LEFT -> "img/bullet_l.gif"
        Direction.RIGHT -> "img/bullet_r.gif"
    }

    }

    init {
        val size = Painter.size(imgPath)
        width = size[0]
        height = size[1]
        val pair = block(width,height)
        x = pair.first
        y = pair.second
        block(width,height)
    }


    override fun draw() {
        Painter.drawImage(imgPath,x,y)
    }
}