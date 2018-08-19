package model

import business.*
import enums.Direction
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import java.util.*


//敌方坦克
class Enemy(override var x: Int, override var y: Int) : AutoMoveAble,MoveAble,BlockAble,AotoShotAble,SufferAble,DestoryAble{
    override fun needDestory(): Boolean {
        return blood<=0
    }

    override var blood: Int = 4


    override fun notifyAttack(attackAble: AttackAble): Array<View>? {
        if (attackAble.owner is Enemy){
            return null
        }else{
            blood -= attackAble.attackPower
            Composer.play("snd/hit.wav")
            return arrayOf(Blast(x, y))
        }

    }

    //开始时间
    var startTime = System.currentTimeMillis()
    override fun autoShot(): Bullet? {
        //调用时间
        var curTime = System.currentTimeMillis()//1005

        //计算x和y
        var bulletX = 0
        var bulletY = 0
        if (curTime - startTime > 1000) {
            //时间重新赋值
            startTime = curTime
            return Bullet( this,direction) { bulletWidth, bulletHeight ->
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
            }
        }
        return null
    }

    override var badBlcok: BlockAble? = null

    override var badDirection: Direction? = null

    override val speed: Int =4


    override fun aotoMove() {
        //如果碰撞  停下来
        if (direction == badDirection) {
            //改变方向
            direction = randomDirection(badDirection)
            return
        }
        when (direction) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
    }
    //随机生成一个方向和当前方向不能一致
    fun randomDirection(badDirection: Direction?):Direction{
        val random = Random().nextInt(4)
        //方向数组
        val values = Direction.values()
        val direction = values[random]
        //是否是碰撞的方向
        if (direction == badDirection) {
            return randomDirection(badDirection)
        }
        return direction
    }

    override val wight: Int = 1

    override var width: Int = Config.blockSize

    override var height: Int = Config.blockSize

    override var direction:Direction = Direction.DOWN



    override fun draw()  = Painter.drawImage(when(direction){
        Direction.UP-> "img/enemy_1_u.gif"
        Direction.DOWN-> "img/enemy_1_d.gif"
        Direction.LEFT-> "img/enemy_1_l.gif"
        Direction.RIGHT-> "img/enemy_1_r.gif"
    },x,y)
}
