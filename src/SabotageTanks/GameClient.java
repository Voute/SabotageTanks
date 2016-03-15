/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.Control.TankMovement;
import SabotageTanks.GraphicObjects.Tank;
import SabotageTanks.Net.ConnectionClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ytokmakov
 */
public class GameClient extends Game{

    public GameClient(Player player, ConnectionClient connectionServer)
    {
        super(player, connectionServer);
    }
    
    @Override
    protected void tick()  
    {
//        TankMovement playerMovement = control.getPlayerMovement();
//        if (playerState.tank != null)
//        {
//            playerState.tank.move(playerMovement); 
//        }
        //nothing to do on the client side
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
            gameState = newGameState;
//            if (newGameState != null && playerState.tank != null)
//            {
//                for (Tank updatedTank:newGameState.getTanks())
//                {
//                    gameState.updateTank(updatedTank);
//                }
//                Tank updatedTank = gameState.getTank(playerState.tank.getId());
//                if (updatedTank != null)
//                {
//                    playerState.updateTank(updatedTank);               
//                }
//            }
            
        } catch (IOException ex) {
            GameLog.write(ex);
        }
    }
    
}
