package model

import business.*

import enums.Direction
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

//我方坦克
class Tank(override var x: Int, override var y: Int) :MoveAble,BlockAble,SufferAble,DestoryAble {
    override fun needDestory(): Boolean {
        return blood<=0
    }

    override var blood: Int = 8


    override fun notifyAttack(attackAble: AttackAble): Array<View>? {
         blood -= attackAble.attackPower
        Composer.play("snd/hit.wav")
        return arrayOf(Blast(x, y))

    }

    override val wight: Int = 1

    override var width: Int = Config.blockSize //宽高都是64

    override var height: Int = Config.blockSize

    //方向
    override var direction:Direction = Direction.UP

    //速度
    override val speed:Int = 32

    override var badBlcok:BlockAble? = null
    override var badDirection:Direction? = null
    //负责坦克移动或者转换方向
    fun move(direction: Direction){
        //如果要改变的方向和当前方向不一致改变方向之后不再执行移动的方法
        if (this.direction != direction){
            this.direction = direction
            return
        }
        //判断有没有碰撞
        if (badDirection == direction)return
        //方向

        //移动

        when(direction){
            Direction.UP-> y-= speed
            Direction.DOWN-> y+=speed
            Direction.LEFT-> x -=speed
            Direction.RIGHT-> x +=speed
        }

    }

    override fun draw() =Painter.drawImage(when(direction){
                Direction.UP-> "img/tank_u.gif"
                Direction.DOWN-> "img/tank_d.gif"
                Direction.LEFT-> "img/tank_l.gif"
                Direction.RIGHT-> "img/tank_r.gif"
                },x,y)

    fun shot(): Bullet {
        //var bulletWidth = 16
       // var bulletHeight = 32
        var bulletX = 0
        var bulletY = 0
        return Bullet(this,direction,{bulletWidth,bulletHeight->
            when (direction) {
                Direction.UP -> {
                    bulletX = x + (width - bulletWidth) / 2
                    bulletY = y - bulletHeight / 2
                }
                Direction.DOWN -> {
                    bulletX = x + (width - bulletWidth) / 2
                    bulletY = y + height - bulletHeight / 2
                }
                Direction.LEFT -> {
                    bulletX = x - bulletWidth / 2
                    bulletY = y + (height - bulletHeight) / 2

                }
                Direction.RIGHT -> {
                    bulletX = x + width - bulletWidth / 2
                    bulletY = y + (height - bulletHeight) / 2
                }
            }
            bulletX to bulletY

        })

    }

}


