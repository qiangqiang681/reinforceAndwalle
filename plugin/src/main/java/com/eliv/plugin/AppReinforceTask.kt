package com.eliv.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Created by eliv on 19-6-13.
 */
open class AppReinforceTask : DefaultTask() {


    @TaskAction
    fun doTask() {
        val reinforceExtension = project.extensions.getByType(AppReinforceExtension::class.java)
        when (reinforceExtension.reinforceType) {
            Constant.ReinforceType.TYPE_360 -> do360Reinforce(reinforceExtension)
        }
    }

    private fun do360Reinforce(reinforceExtension: AppReinforceExtension) {
        if (!reinforceExtension.reinforce360.support) {
            logger.log(LogLevel.ERROR, "not support!!!")
            return
        }
        var outApkPath = ""

        val user = reinforceExtension.reinforce360.user.checkNull("缺少360加固平台账户")
        val pass = reinforceExtension.reinforce360.pass.checkNull("缺少360加固平台账户密码")
        val jarPath = reinforceExtension.reinforce360.path.checkNull("缺少360加固路径")
        val addConfig = reinforceExtension.reinforce360.addConfig
        val autoMultChannel = reinforceExtension.autoMultChannel

        val walleChannelWritterPath =
            if (autoMultChannel) reinforceExtension.walleChannelWritterPath.checkNull("缺少 walle jar 路径") else ""
        val extraChannelFilePath =
            if (autoMultChannel) reinforceExtension.extraChannelFilePath.checkNull("缺少签名渠道配置") else ""
        val channelsOutputFilePath =
            if (autoMultChannel) reinforceExtension.channelsOutputFilePath.checkNull("缺少渠道包输出路径") else ""

        project.applicationVariants?.filter {
            it.name.toLowerCase().contains("release", true)
        }?.forEach { appVariant ->
            val (storePath, storePassword, keyAlias, keyPassword) = appVariant.keystore
            appVariant.outputs.firstOrNull { it.outputFile.exists() }?.let { variant ->
                val apkFile = variant.outputFile
                val apkPath = apkFile.absolutePath
                outApkPath = File(apkFile.parentFile, "reinforce_360").let {
                    if (!it.exists()) {
                        it.mkdirs()
                    }
                    it.absolutePath
                }

                project.execCmd("java -jar $jarPath -version")
                project.execCmd("java -jar $jarPath -login $user $pass")
                project.execCmd("java -jar $jarPath -config $addConfig")
                project.execCmd("java -jar $jarPath -importsign $storePath $storePassword $keyAlias $keyPassword")
                project.execCmd("java -jar $jarPath -showsign")
                project.execCmd("java -jar $jarPath -jiagu $apkPath $outApkPath -autosign")
            }
        } ?: logger.log(LogLevel.ERROR, "没有找到需要360加固的apk")
        logger.log(LogLevel.INFO, "========360加固结束==========")

        if (autoMultChannel)
            setMultChannel(walleChannelWritterPath, extraChannelFilePath, channelsOutputFilePath, outApkPath)

    }

    private fun setMultChannel(
        walleChannelWritterPath: String,
        extraChannelFilePath: String,
        channelsOutputFilePath: String,
        outApkPath: String
    ) {

        File(outApkPath).listFiles().firstOrNull()?.let {
            project.execCmd("java -jar $walleChannelWritterPath batch2 -f $extraChannelFilePath ${it.absolutePath} $channelsOutputFilePath")
        } ?: logger.log(LogLevel.ERROR, "没有找到签名apk")

        logger.log(LogLevel.INFO, "========生成加固渠道包结束==========")
    }


    data class KeyStore(
        val path: String,
        val pass: String,
        val alias: String,
        val aliasPass: String
    )
}