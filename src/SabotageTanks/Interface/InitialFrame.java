/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Interface;

import SabotageTanks.Game;
import SabotageTanks.GameClient;
import SabotageTanks.GameLog;
import SabotageTanks.GameServer;
import SabotageTanks.Net.ConnectionClient;
import SabotageTanks.Net.ConnectionServer;
import SabotageTanks.Player;
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
    
    private ActionListener getActionListener()
    {
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {   
                String port = portField.getText();
                String ip = ipField.getText();
                String playerName = nameField.getText();

                if      ( playerName.isEmpty() || playerName.matches("ИмяИгрока") )
                {
                    nameField.setText("");
                    ShowMessage.nameIsEmpty();
                }
                else if (port.isEmpty() || portField.getColumns() != port.length())
                {
                    ShowMessage.portIsEmpty();
                }
                else if (ip.isEmpty()
        //                || 14 != ip.length()
                        )
                {
                    ShowMessage.ipIsEmpty();
                }
                else 
                {
                    int intPort = Integer.parseInt(port);

                    if (serverRadio.isSelected())
                    {
                        try {
                            Player player = new Player(playerName, Player.getRandomColor());
                            ConnectionServer connectionServer = new ConnectionServer(intPort);

                            game = new GameServer(player, connectionServer);
                            connectionServer.setPlayerList(((GameServer)game).getPlayerList());
                        } catch (IOException ex) {
                            GameLog.write(ex);
                            ShowMessage.startingServerFail();
                        }
                    } else
                    {
                        try {
                            Player player = new Player(playerName + "1", Player.getRandomColor());
                            ConnectionClient connectionClient = new ConnectionClient(ip, intPort, player);
                            game = new GameClient(player, connectionClient);
                        } catch (IOException ex) {
                            GameLog.write(ex);
                            ShowMessage.connectingServerFail();
                        }
                    }

                    if (game != null)
                    {
                        setVisible(false);
                        new CustomizeFrame().setVisible(true);
//                        game.start();
                    }

                } 
            }
        };
    }
    
}
