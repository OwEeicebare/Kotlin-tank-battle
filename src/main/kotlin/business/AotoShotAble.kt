package business

import model.Bullet

//自动射击
interface AotoShotAble :View{
    fun autoShot(): Bullet?
}