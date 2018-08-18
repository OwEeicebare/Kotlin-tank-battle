import business.View
import javafx.scene.input.KeyEvent
import model.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Window
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow : Window("坦克大战", "img/logo.jpg", Config.gameWidth, Config.gameHeight) {
    //保存控件的集合
    val list = CopyOnWriteArrayList<View>()

    override fun onCreate() {
        Composer.play("snd/start.wav")
        var file = File(javaClass.getResource("map/1.map").path)
        var lines = file.readLines()
        lines.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                when (c) {
                    '砖' -> list.add(Wall(col * Config.blockSize, row * Config.blockSize))
                    '铁' -> list.add(Steel(col * Config.blockSize, row * Config.blockSize))
                    '水' -> list.add(Water(col * Config.blockSize, row * Config.blockSize))
                    '草' -> list.add(Grass(col * Config.blockSize, row * Config.blockSize))
                    '我' -> list.add(Tank(col * Config.blockSize, row * Config.blockSize))

                }
            }
        }

    }

    override fun onDisplay() {//绘制模块
        list.forEach {
            it.draw()
        }
    }

    override fun onKeyPressed(event: KeyEvent) {//键盘监控

    }

    override fun onRefresh() {

    }
}