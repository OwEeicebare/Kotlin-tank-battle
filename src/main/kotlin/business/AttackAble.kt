package business
//攻击的能力
interface AttackAble :View{
    val owner:View
    //攻击力
    val attackPower:Int
    //判断有没有攻击到
    fun willAttack(sufferAble: SufferAble):Boolean
    //攻击到目标了
    fun notifyAttack(sufferAble: SufferAble)
}