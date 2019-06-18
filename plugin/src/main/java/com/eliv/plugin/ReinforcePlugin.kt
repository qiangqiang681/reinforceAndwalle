package com.eliv.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by eliv on 19-6-13.
 */
class ReinforcePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project.plugins.hasPlugin("com.android.application")) {
            project.extensions.create("AppReinforce", AppReinforceExtension::class.java)
            project.tasks.create("appReinforce", AppReinforceTask::class.java)
            project.afterEvaluate {
                it.tasks.findByPath("assembleReleaseChannels")?.finalizedBy("appReinforce")
            }
        }

    }
}