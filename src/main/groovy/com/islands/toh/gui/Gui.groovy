package com.islands.toh.gui

import groovy.swing.SwingBuilder
import net.miginfocom.swing.MigLayout

import javax.swing.Action
import javax.swing.InputMap
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.KeyStroke
import java.awt.event.ActionEvent

class Gui {
    static KeyStroke enter = KeyStroke.getKeyStroke("ENTER")
    static KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER")
    static final String TEXT_SUBMIT = "text-submit"
    static final String INSERT_BREAK = "insert-break"



    SwingBuilder swing = new SwingBuilder()

    JFrame newBook_FRM
    JTextArea descriptionInput_TXA
      JButton save_BTN
    JFrame newBookDetails_FRM

    Closure go = {
        println "Going..."
        newBook_FRM.visible = false
        newBookDetails_FRM.visible = true
        newBookDetails_FRM.requestFocus()
        newBookDetails_FRM.title = descriptionInput_TXA.text.split(/[\r\n]+/)?.first()
        println "Gone!"

        return true
    }

    void run() {
        swing.edt {
            newBook_FRM = frame(title:'New Book',size:[500,500],visible: true,defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
                panel(layout:new MigLayout()) {
                    descriptionInput_TXA = textArea(columns:10,constraints:'cell 0 0')
                    descriptionInput_TXA.inputMap.with {
                        put(enter,TEXT_SUBMIT)
                        put(shiftEnter,INSERT_BREAK)
                    }
                    descriptionInput_TXA.actionMap.put(TEXT_SUBMIT, go as Action)
                    save_BTN = button(text:"Go",constraints:'cell 0 1',actionPerformed: go)
                }
            }
            newBookDetails_FRM = frame(title:'',size:[500,500]) {
                panel(layout:new MigLayout()) {

                }
            }
        }
    }
}
