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
import java.net.Socket;

/**
 *
 * @author ytokmakov
 */
public final class ConnectionClient extends Connection {

    public ConnectionClient(String ip, int port, Player player) throws IOException
    {
        super();
        Socket socket =  new Socket(ip, port);
        String message = "connected to server, ip " + socket.getInetAddress().getHostAddress();
        System.out.println(message);
//        send("player");
//        send(gson.toJson(player));
        GameLog.write(message);
        initiateNegotiation(socket, socket.getInetAddress().getHostAddress(), socket.getLocalPort());
    }

    @Override
    public void sendState(State playerControl)
    {
        send(gson.toJson((StatePlayer)playerControl) );
    }

    @Override
    public State receiveState() throws IOException
    {
        return gson.fromJson(receive(), StateServer.class);
    }
    
}
