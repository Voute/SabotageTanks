/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Net;

import SabotageTanks.GameLog;
import SabotageTanks.Player;
import SabotageTanks.State;
import SabotageTanks.StatePlayer;
import SabotageTanks.StateServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public final class ConnectionServer extends Connection implements Runnable {

    private final ServerSocket serverSocket;
    private Player player;
    
    public ConnectionServer(int port) throws IOException
    {
        super();
        player = null;
        serverSocket = new ServerSocket(port);
        new Thread(this).start();
    }
    
    @Override
    public void run()
    {
        try {
            Socket socket = serverSocket.accept();
            String message = "client connected, ip " + socket.getInetAddress().getHostAddress();
            System.out.println(message);
            GameLog.write(message);
            initiateNegotiation(socket, serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());

        } catch (IOException ex) {
            GameLog.write(ex);
        }
    }

    public Player getPlayer()
    {
        return player;
    }
    
    @Override
    public void sendState(State state)
    {
        send( gson.toJson(state, StateServer.class) ); 
    }

    @Override
    public State receiveState() throws IOException
    {
        return gson.fromJson(receive(), StatePlayer.class);
    }
    
}
