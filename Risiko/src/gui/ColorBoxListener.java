/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Elisa
 */
public class ColorBoxListener implements ActionListener {

    private String[] updateColors;
    private JComboBox[] colorBoxs;

    public ColorBoxListener(JComboBox[] colorBoxs, String[] updateColors) {
        this.updateColors = updateColors;
        this.colorBoxs = colorBoxs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource();
        String selected = (String) comboBox.getSelectedItem();

        for (int i = 0; i < colorBoxs.length; i++) {

            if (colorBoxs[i] == (comboBox) && !updateColors[i].equals(selected)) {
                String colorTmp = updateColors[i];
                updateColors[i] = selected;
                for (int j = 0; j < colorBoxs.length; j++) {
                    if (colorBoxs[j] != comboBox && colorBoxs[j].getSelectedItem().equals(selected)) {
                        colorBoxs[j].setSelectedItem(colorTmp);
                        updateColors[j] = colorTmp;
                    }
                }
            }

        }

    }

    public String[] getUpdateColors(int length) {
        return Arrays.copyOfRange(updateColors, 0, length);
    }

}
