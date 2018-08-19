package business
//销毁的能力
interface DestoryAble:View {
    //是否需要销毁
    fun needDestory():Boolean
    //销毁效果
    fun destoryXg():Array<View>?{
        return null
    }
}
