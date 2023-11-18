package com.github.secretj12.hackatum2023namerator.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.secretj12.hackatum2023namerator.MyBundle
import ChatGPT
import com.github.secretj12.hackatum2023namerator.services.MyProjectService
import com.intellij.ui.components.JBTextField
import javax.swing.JButton


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()
        private val chatGPT = toolWindow.project.service<ChatGPT>()

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val gptKey = JBTextField()
            val button = JButton("Set GPT Key").apply {
                addActionListener {
                    chatGPT.setKey(gptKey.text)
                }
            }
            val label = JBLabel(MyBundle.message("randomLabel", "?"))

            add(gptKey)
            add(button)
            add(label)
            add(JButton(MyBundle.message("shuffle")).apply {
                addActionListener {
//                    label.text = MyBundle.message("randomLabel", service.getRandomNumber())
                    label.text = MyBundle.message("randomLabel", chatGPT.getChatResponse("Hello World"));
                }
            })
        }
    }
}
