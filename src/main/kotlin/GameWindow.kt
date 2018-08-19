import business.*
import enums.Direction
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import model.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow : Window("坦克大战", "img/logo.jpg", Config.gameWidth, Config.gameHeight) {
    //游戏结束的标记
    var gameover = false
    //保存控件的集合
    val list = CopyOnWriteArrayList<View>()

    //大本营
    val camp by lazy { Camp(Config.gameWidth / 2 - Config.blockSize, Config.gameHeight - Config.blockSize - 32) }

    lateinit var tank: Tank
    //敌方坦克的数量
    val totalEnemy = 15
    //已经出生的敌方坦克
    var bornEnemy = 0
    //界面上最多显示敌方坦克数量
    val showEnemy = 3
    //出生点index
    var bornlndex = 0

    //出生点位置
    val locationList = ArrayList<Pair<Int,Int>>()

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
                    '敌' -> {
                        //list.add(Enemy(col * Config.blockSize, row * Config.blockSize))
                        locationList.add(col * Config.blockSize to row * Config.blockSize)
                    }
                    '我' -> {
                        tank = (Tank(col * Config.blockSize, row * Config.blockSize))
                        list.add(tank)
                    }

                }
            }
        }

        //对集合排序
        list.sortBy { it.wight }
        //大本营
        list.add(camp)
    }

    override fun onDisplay() {//绘制模块

        list.forEach {
            it.draw()
        }
        if (gameover==true){
            list.clear()
           Painter.drawImage("img/gameover.gif",380,380)
        }
    }

    override fun onKeyPressed(event: KeyEvent) {//键盘监控
        var code = event.code
        when (code) {
            KeyCode.W -> {
                tank.move(Direction.UP)
            }
            KeyCode.S -> {
                tank.move(Direction.DOWN)
            }
            KeyCode.A -> {
                tank.move(Direction.LEFT)
            }
            KeyCode.D -> {
                tank.move(Direction.RIGHT)
            }
            KeyCode.J -> {
                //发射子弹
                var bullet: Bullet = tank.shot()
                list.add(bullet)
            }
        }
    }

    override fun onRefresh() {//刷新

        //运动和阻挡的检测
        val moveList = list.filter { it is MoveAble }
        //所有阻挡的物品
        val blockList = list.filter { it is BlockAble }
        //拿到每一个运动物和每一个阻挡物进行碰撞判断
        for (move in moveList) {
            move as MoveAble
            var badBlock: BlockAble? = null
            var badDirection: Direction? = null
            for (block in blockList) {
                block as BlockAble
                //判断当前移动物体和碰撞物体是否是同一个
                if (move == block) {
                    continue
                }
                var direction = move.willCollision(block)
                if (direction != null) {
                    badBlock = block
                    badDirection = direction
                    break
                }
            }
            //通知移动的物品有没有发生碰撞(和谁发生了碰撞 碰撞的方向)
            move.notifyBlock(badBlock, badDirection)
        }
        //处理自动移动
        val autoMoveList = list.filter { it is AutoMoveAble }
        autoMoveList.forEach {
            it as AutoMoveAble
            it.aotoMove()
        }
        //销毁判断
        //找到所有具有销毁能力的元素
        val destoryList = list.filter { it is DestoryAble }
        destoryList.forEach {
            it as DestoryAble
            if (it.needDestory()) {
                list.remove(it)
                //有没有销毁效果
                val array = it.destoryXg()
                if(array!=null){
                    list.addAll(array)
                }
            }
        }
        //攻击与被攻击的判断
        //攻击
        val attackList = list.filter { it is AttackAble }
        //被攻击
        val sufferList = list.filter { it is SufferAble }
        for (attack in attackList) {
            attack as AttackAble
            for (suffer in sufferList) {
                suffer as SufferAble
                //如果子弹是受攻击者发出的就跳出循环
                   if ( attack.owner == suffer)continue
                    if (attack == suffer)continue
                var result = attack.willAttack(suffer)
                if (result) {
                    //找到碰撞者
                    //通知攻击者(受攻击者)
                    attack.notifyAttack(suffer)
                    //通知受攻击者(攻击者)
                    val blast = suffer.notifyAttack(attack)
                    blast?.let { list.addAll(it) }
                    //跳出里层循环
                    break
                }
                }
            }
        //自动射击
        val enemyList = list.filter { it is AotoShotAble }
        enemyList.forEach {
            it as AotoShotAble
            val bullet:Bullet? = it.autoShot()
            bullet?.let { list.add(it) }
        }
        //判断敌方坦克是否出生
        if (list.filter { it is Enemy }.size < showEnemy && bornEnemy < totalEnemy){
            bornEnemy()
        }
       //大本营不在，自己死掉，敌方全部死掉，结束游戏
        if (list.find { it is Camp }==null||list.find { it is Tank }==null||(list.filter { it is Enemy }.size == 0&&bornEnemy == totalEnemy)){
            gameover = true
        }
    }

    private fun bornEnemy() {
        bornlndex = bornlndex % locationList.size
        //出生点随机生成
        val pair = locationList[bornlndex]
        //判断有没有其他坦克在这个位置上

        //创建一个敌方坦克，并添加到集合种
        val enemy = Enemy(pair.first, pair.second)
        //现在所有移动的物体
        val moveList = list.filter { it is MoveAble }
        for (move in moveList) {
            move as MoveAble
            var direction = move.willCollision(enemy)
            if (direction != null) {
                bornlndex++
                 bornEnemy()
                return
            }
        }
        list.add(enemy)
        bornlndex++
        bornEnemy++
    }
}



