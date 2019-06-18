package com.eliv.plugin

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
        var user = ""
        var pass = ""
        var addConfig = "-x86"
    }

    open class ReinforceExtensions {
        var path = ""
        var support = false
        var channelApks = arrayListOf("360")
    }
}
