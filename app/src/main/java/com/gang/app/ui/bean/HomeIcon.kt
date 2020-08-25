package com.gang.app.ui.bean

/**
 *
 * @ProjectName:    gang
 * @Package:        com.gang.app.ui.fragment.bean
 * @ClassName:      HomeIcon
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     2020/8/5 16:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2020/8/5 16:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class HomeIcon {

    var id: String? = null
    var icon: String? = null
    var name: String? = null
    var content: String? = null
    var type: String? = null
    var url: String? = null
    var hasPermission = false

    constructor(id: Int, icon: Int, name: String) {
        this.id = id.toString()
        this.icon = icon.toString()
        this.name = name
    }

    constructor(id: Int, icon: String, name: String) {
        this.id = id.toString()
        this.icon = icon
        this.name = name
    }

    constructor(type: String, name: String, content: String, url: String, permission: Boolean) {
        this.type = type
        this.name = name
        this.content = content
        this.url = url
        hasPermission = permission
    }

}