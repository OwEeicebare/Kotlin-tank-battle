package business

import enums.Direction

//自动移动的能里
interface AutoMoveAble:View {
    //自动移动的方向
    val direction:Direction

    //自动移动的速度
    val speed:Int

    //自动移动的行为
    fun aotoMove()
}