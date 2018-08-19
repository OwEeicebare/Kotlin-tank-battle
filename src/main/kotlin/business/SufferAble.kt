package business
//被攻击的能力
interface SufferAble :View{
    //血量值
    var blood: Int

    //通知被攻击到了
    //返回类型是攻击后的效果
    fun notifyAttack(attackAble: AttackAble):Array<View>?
}