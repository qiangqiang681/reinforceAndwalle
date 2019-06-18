package com.plugin.eliv.plugin

import org.gradle.api.Action

open class AppReinforceExtension {
    var reinforce360: Reinforce360Extension = Reinforce360Extension()
    var reinforceType = ""
    var walleChannelWritterPath = ""
    var autoMultChannel = false
    var extraChannelFilePath = ""
    var channelsOutputFilePath = ""

    fun reinforce360(action: Action<Reinforce360Extension>) {
        action.execute(reinforce360)
    }

    open class Reinforce360Extension : ReinforceExtensions() {
        /** 用户. */
        var user = ""
        /** 密码. */
        var pass = ""

        /** 多渠道. */
        var mulpkg = false
        /** 多渠道配置路径. */
        var mulpkgConfigPath = ""
        /** 增值配置 默认配置只支持x86架构. 其它 -data -crashlog -vmp */
        var addConfig = "-x86"
    }

    open class ReinforceExtensions {
        /** 自动签名 默认开启自动签名. */
        var autoSign = true
        /** 路径. */
        var path = ""
        /** 是否支持加固  默认都是不支持状态. */
        var support = false

        var channelApks = arrayListOf("360")
    }
}
