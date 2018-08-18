package model

import org.itheima.kotlin.game.core.Painter
import javax.swing.text.View
//水墙
class Water(override var x: Int, override var y: Int) :business.View {
    override var width: Int = Config.blockSize //宽高都是64

    override var height: Int = Config.blockSize


    override fun draw() {
       Painter.drawImage("img/water.gif",x,y)
    }
}