package com.plugin.eliv.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * Created by eliv on 19-6-13.
 */

fun String.checkNull(msg: String): String {
    if (isNullOrBlank()) {
        throw IllegalArgumentException(msg)
    }
    return this
}

val Project.applicationVariants
    get() = project.extensions
        .findByType(AppExtension::class.java)
        ?.applicationVariants
        ?.filter {
            logger.log(LogLevel.INFO, "${it.buildType.name == "release"}")
            it.buildType.name == "release"
        }

val ApplicationVariant.keystore: AppReinforceTask.KeyStore
    get() = with(signingConfig) {
        if (storeFile == null || !storeFile.exists()) {
            throw IllegalArgumentException("您没有配置签名")
        }
        AppReinforceTask.KeyStore(storeFile.absolutePath, storePassword, keyAlias, keyPassword)
    }

fun Project.execCmd(cmd: String) {
    project.exec {
        it.commandLine("sh", "-c", cmd)
    }
}