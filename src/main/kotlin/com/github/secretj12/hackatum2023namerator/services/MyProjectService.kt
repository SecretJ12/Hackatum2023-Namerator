package com.github.secretj12.hackatum2023namerator.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.secretj12.hackatum2023namerator.MyBundle

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    fun getRandomNumber() = (1..100).random()

    fun getNewVariableName() = "newVariableName"
}
