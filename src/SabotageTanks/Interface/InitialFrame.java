/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Interface;

import SabotageTanks.Exceptions.InitialFrameDataIncorrectException;
import SabotageTanks.Game;
import SabotageTanks.GameClient;
import SabotageTanks.GameLog;
import SabotageTanks.GameServer;
import SabotageTanks.Net.Connection;
import SabotageTanks.Net.ConnectionClient;
import SabotageTanks.Net.ConnectionServer;
import SabotageTanks.Player;
import SabotageTanks.Tanks.TankModelsLoader;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Enumeration;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class InitialFrame extends JFrame
{
    private final String TITLE = "SabotageTanks - подключение";
    private final int FRAME_HEIGHT = 120,
                      FRAME_WIDTH = 250;
                     
    
    private final JPanel rootPanel;
    private final ButtonGroup radioGroup;
    private final JRadioButton serverRadio,
                         clientRadio;
                        
    private final JButton startButton;
    private final JTextField portField,
                             nameField,
                             ipField;
                      
    private Game game;
    
    public static void main(String[] args)       
    {
        new InitialFrame().setVisible(true);
    }
    
    public InitialFrame()
    {
        super();
        
        try {
            String jarPath = InitialFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            jarPath = URLDecoder.decode(jarPath, "UTF-8");
            jarPath = jarPath.substring(1);
            GameLog.initiate(jarPath);
        } catch (URISyntaxException | IOException ex) {
            GameLog.write(ex);
            ShowMessage.initiatingLogFail();
        }
        
        setTitle(TITLE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        nameField = new JTextField();
        nameField.setColumns(20);
        nameField.setText("ИмяИгрока1");
        
        portField = new JTextField();
        portField.setColumns(4);
        portField.setText("5005");
        
        ipField = new JTextField();
        ipField.setColumns(14);
        ipField.setText(getLocalAddress());
        
        serverRadio = new JRadioButton("Сервер");
        serverRadio.setSelected(true);
        clientRadio = new JRadioButton("Клиент");
        
        radioGroup = new ButtonGroup();
        radioGroup.add(serverRadio);
        radioGroup.add(clientRadio);     
        
        startButton = new JButton("Старт");
        startButton.addActionListener(getActionListener());
        
        rootPanel = new JPanel(new FlowLayout());
        rootPanel.add(nameField);
        rootPanel.add(ipField);
        rootPanel.add(portField);
        rootPanel.add(serverRadio);
        rootPanel.add(clientRadio);
        rootPanel.add(startButton);
        
        setContentPane(rootPanel);
    }

    private String getIP() throws InitialFrameDataIncorrectException
    {
        String ip = ipField.getText();
        int countDotes = ip.length() - ip.replace(".", "").length();
   
        if ( countDotes != 3 || ip.length() > 15 )
        {
            throw new InitialFrameDataIncorrectException(InitialFrameDataIncorrectException.IP);
        }
        
        return ip;
    }
    
    private int getPort() throws InitialFrameDataIncorrectException
    {
        String portText = portField.getText();
        
        try
        {        
            int port = Integer.parseInt(portText);
            
            if (portText.length() == 4) { return port; } else
            {
                throw new InitialFrameDataIncorrectException(InitialFrameDataIncorrectException.PORT);
            }            
            
        } catch(Exception ex)
        {
            throw new InitialFrameDataIncorrectException(InitialFrameDataIncorrectException.PORT);
        }

    }
    
    private String getLocalAddress()
    {
        try {
            
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            
            while (interfaces.hasMoreElements())
            {
                NetworkInterface inter = interfaces.nextElement();
                
                Enumeration<InetAddress> addresses = inter.getInetAddresses();
                
                while (addresses.hasMoreElements())
                {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address && address.isSiteLocalAddress())
                    {
                        return address.getHostAddress();
                    }
                }
            }
            
        } catch (SocketException ex) {
            GameLog.write(ex);
        }
        return null;
    }
    
    private ConnectionServer startServer(int port) throws IOException
    {
        try
        {
            return new ConnectionServer(port); 
        } catch (IOException ex)
        {
            ShowMessage.startingServerFail();
            throw ex;
        }
    }
    
    private ConnectionClient startClient(String ip, int port) throws IOException
    {
        try
        {
            return new ConnectionClient(ip, port);
        } catch (IOException ex)
        {
            ShowMessage.connectingServerFail();
            throw ex;
        }
    }
    
    private ActionListener getActionListener()
    {
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {   
                try {
                    int port = getPort();
                    String ip = getIP();
                    
                    TankModelsLoader modelsLoader = new TankModelsLoader();
                    Connection connection = null;
                    
                    if (serverRadio.isSelected())
                    {
                        connection = startServer(port);
                        
                    } else if (clientRadio.isSelected())
                    {
                        connection = startClient(ip, port);
                    }
                    
                    new CustomizeFrame(modelsLoader, connection).setVisible(true);
                    setVisible(false);

                } catch (InitialFrameDataIncorrectException | IOException ex) {
                    GameLog.write(ex);
                }
            }
        };
    }
    
}
