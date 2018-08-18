package model

import business.View
import org.itheima.kotlin.game.core.Painter

//铁墙
class Steel(override var x: Int, override var y: Int) :View {
    override var width: Int = Config.blockSize

    override var height: Int = Config.blockSize


    override fun draw() {
        Painter.drawImage("img/steel.gif",x,y)
    }
}