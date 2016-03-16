/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.TextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomizeFrame extends JFrame
{
//    private final Connection connection;
    private final JPanel rootPanel,
                         westPanel,
                         centerPanel;
    private final JTextField nameField;
    
    public CustomizeFrame()
    {
//        this.connection = connection;
        
        rootPanel = new JPanel(new BorderLayout());
        
        nameField = new JTextField("PlayerName");
        
        centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(Color.gray);
        centerPanel.add(nameField);
        
        
        westPanel = new JPanel(new FlowLayout());
        westPanel.setBackground(Color.black);
        
        rootPanel.add(centerPanel,BorderLayout.CENTER);
        rootPanel.add(westPanel,BorderLayout.WEST);
        
        setContentPane(rootPanel);
    }
    
    
}
