package business
//所有空间的父接口
interface View {
    //渲染级别
    val wight:Int
    var x:Int
    var y:Int
    var width:Int
    var height:Int
    fun draw()
}