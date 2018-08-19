package business

import enums.Direction

//运动的能力
interface MoveAble :View{
    //方向
    var direction: Direction
    //碰撞物可以为空
    var badBlcok:BlockAble?
    //碰撞方向可以为空
    var badDirection:Direction?

    //速度
    val speed:Int
    //  判断当前运动物品和传递的阻挡物品有没有碰撞
    fun willCollision(block: BlockAble):Direction?{
        //预计下一步移动位置
        var x = x
        var y = y
        //下一步的位置
        when (direction) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }

        if (y >= block.y + block.height || y + height <= block.y || block.x + block.width <= x || x + width <= block.x) {
            //处理越界问题
            if (y<0 || y>Config.gameHeight - height||x<0||x>Config.gameWidth-width){
                return direction//向下就碰撞
            }
            return null

        }
        return direction//向下就碰撞
    }
    //通知发生了碰撞
    fun notifyBlock(blockAble: BlockAble?,badDirection: Direction?){
        this.badBlcok = badBlcok
        this.badDirection = badDirection
    }
}