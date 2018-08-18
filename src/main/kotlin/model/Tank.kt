package model

import business.View
import org.itheima.kotlin.game.core.Painter

//我方坦克
class Tank(override var x: Int, override var y: Int) :View {
    override var width: Int = Config.blockSize //宽高都是64

    override var height: Int = Config.blockSize

    //方向


    override fun draw() {
        Painter.drawImage("img/tank_u.gif",x,y)
    }
}