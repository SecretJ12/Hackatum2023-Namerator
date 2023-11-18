package com.github.secretj12.hackatum2023namerator.toolWindow

import com.github.secretj12.hackatum2023namerator.GPTRequester

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.*
import javax.annotation.Nullable
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class KeyInputDialog : DialogWrapper(true) {
    val centerPanel: JPanel = JPanel(GridBagLayout())
    private val gpt: JTextField; ;
    init {
        title = "OpenAI Key"
        gpt = JTextField()
        init()
    }


    @Nullable
    override fun createCenterPanel(): JComponent? {
        val gridbag = GridBag()
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultInsets(Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
        centerPanel.preferredSize = Dimension(400, 100)

        centerPanel.add(JLabel("OpenAI Key (has to support GPT-4)"), gridbag.nextLine().next().weightx(0.2))
        centerPanel.add(gpt, gridbag.nextLine().next().weightx(0.8))
        return centerPanel
    }

    fun getText(text: String ): JComponent {
        var label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(10, 10, 10, 10)
        return label
    }

    override fun doOKAction() {
        GPTRequester.setKey(gpt.text)
        super.doOKAction()
    }

}