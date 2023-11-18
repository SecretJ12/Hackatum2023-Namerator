package com.github.secretj12.hackatum2023namerator.toolWindow

import com.github.secretj12.hackatum2023namerator.GPTRequester
import com.github.secretj12.hackatum2023namerator.actions.NamerateAll

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
import javax.swing.JSlider
import javax.swing.JTextField


class MaxNameLengthDialog : DialogWrapper(true) {

    val centerPanel: JPanel = JPanel(GridBagLayout())
    private val gpt: JTextField; ;
    init {
        title = "Max Variable Length"
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

        centerPanel.add(JLabel("Please input the maximum length of variables that should get refactored)"), gridbag.nextLine().next().weightx(0.2))
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
        println(gpt.text)
//        catch if the number is not parsable
        var maxNameLength = -1
        try {
            maxNameLength = Integer.parseInt(gpt.text)
        } catch (e: NumberFormatException) {
            println("Not a number")
        }

        NamerateAll.setMaxNameLength(maxNameLength)
        super.doOKAction()
    }

}