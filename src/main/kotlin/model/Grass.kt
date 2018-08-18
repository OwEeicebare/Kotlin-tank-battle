package model

import business.View
import org.itheima.kotlin.game.core.Painter
//草地
class Grass(override var x: Int, override var y: Int) : View {
    override var width: Int = Config.blockSize

    override var height: Int = Config.blockSize


    override fun draw() {
        Painter.drawImage("img/grass.gif",x,y)
    }

}