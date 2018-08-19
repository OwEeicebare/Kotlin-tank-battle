package model

import business.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

//铁墙
class Steel(override var x: Int, override var y: Int) :BlockAble ,SufferAble,DestoryAble{
    override fun needDestory(): Boolean {
        return blood<=0
    }

    override var blood: Int= 5


    override fun notifyAttack(attack: AttackAble): Array<View>? {
        blood -= attack.attackPower
        Composer.play("snd/blast.wav")
        return arrayOf(Blast(x, y))
    }

    override val wight: Int = 2
    override var width: Int = Config.blockSize

    override var height: Int = Config.blockSize


    override fun draw() {
        Painter.drawImage("img/steel.gif",x,y)
    }
}