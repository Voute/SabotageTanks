/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Interface;

import SabotageTanks.Net.Connection;
import SabotageTanks.Tanks.TankModelsLoader;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomizeFrame extends JFrame
{
    private final Connection connection;
    private final TankModelsLoader modelsLoader;
    private JPanel rootPanel,
                   leftPanel,
                   rightPanel;
    private JTextField nameField;
    private JList modelsList = new JList();
    
    public CustomizeFrame(TankModelsLoader modelsLoader, Connection connection)
    {
        this.connection = connection;
        this.modelsLoader = modelsLoader;
                
        FlowLayout rootLayout = new FlowLayout();
        rootLayout.setAlignment(FlowLayout.CENTER);
        rootLayout.setHgap(30);
        rootLayout.setVgap(30);
        rootPanel = new JPanel(rootLayout);
        rootPanel.setBackground(Color.black);
        
        leftPanel = new JPanel(new FlowLayout());
        leftPanel.setBackground(Color.red);
        rightPanel = new JPanel(new FlowLayout());
        rightPanel.setBackground(Color.green);
        
        rootPanel.add(leftPanel);
        rootPanel.add(rightPanel);
        
        String[] list = {"q", "w", "e"};
        modelsList = new JList(modelsLoader.getModelsNames().toArray());
        nameField = new JTextField("PlayerName");
        
        leftPanel.add(nameField);
        rightPanel.add(modelsList);
        
        rootPanel.add(leftPanel);
        rootPanel.add(rightPanel);
        
        add(rootPanel);
        setSize(500, 300);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

}
