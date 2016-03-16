/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.Tanks.Tank;
import SabotageTanks.Net.ConnectionClient;
import java.io.IOException;

public class GameClient extends Game{

    public GameClient(Player player, ConnectionClient connectionServer)
    {
        super(player, connectionServer);
    }
    
    @Override
    protected void tick()  
    {
        for(Tank tank : gameState.getTanks())
        {
            tank.tick();
        }
    }

    @Override
    protected void sendState()
    {
        connection.sendState(control.getPlayerState());
    }

    @Override
    protected void receiveState()
    {
        try {
            StateServer newGameState = (StateServer)connection.receiveState();
            gameState.update(newGameState);
        } catch (IOException ex) {
            GameLog.write(ex);
        }
    }
    
}
