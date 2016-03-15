/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.Control.TankMovement;
import SabotageTanks.GraphicObjects.Shell;
import SabotageTanks.GraphicObjects.Tank;
import SabotageTanks.GraphicObjects.TankArea;
import SabotageTanks.Net.ConnectionServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Game {
    
    private List<Player> playerList;
    
    public GameServer(Player player, ConnectionServer connectionServer)
    {
        super(player, connectionServer);
        playerList = new ArrayList<>();
        playerList.add(player);
    }

    public List<Player> getPlayerList()
    {
        return playerList;
    }
    
    public void addPlayer(Player newPlayer)
    {
        for (Player player : playerList)
        {
            if (player.getId().matches(newPlayer.getId()))
            {
                return;
            }
        }
        playerList.add(newPlayer);
    }
    
    public Player getPlayer(String playerId)
    {
        for (Player player : playerList)
        {
            if (player.getId().matches(playerId))
            {
                return player;
            }
        }
        return null;
    }
    
    @Override
    protected void tick() {
        
        // массив для снарядов, которые вышли за границы фрейма
        ArrayList<Shell> removeShellList = new ArrayList<Shell>();

        for (Shell shell: gameState.getShells())
        {
            int shellX = shell.nextX();
            int shellY = shell.nextY();
            
            if (shellX <= getWidth() && shellY <= getHeight()
                && shellX >= 0 && shellY >= 0)
            {
                for (Tank tank: gameState.getTanks())
                {
                    if ( tank.containsXY(shell.getX(), shell.getY()) &&
                         !tank.getId().matches(shell.getId())  &&
                         !tank.getBursting()
                       )
                    {
                        removeShellList.add(shell);
                        tank.setBursting(true);
                        break;
                    }
                }
            } else {
                // добавляем к удалению из массива
                removeShellList.add(shell);
            }
        }
        // удаляем из массива снарядов вышедшие за границы экрана
        gameState.getShells().removeAll(removeShellList);  
    
    }

    @Override
    protected void sendState()
    {
        connection.sendState(gameState);
    }

    @Override
    protected void receiveState()
    {
        List<StatePlayer> states = new ArrayList<>();
        
        states.add(control.getPlayerState());
                
        try {
            StatePlayer clientState = (StatePlayer)connection.receiveState();
            if (clientState != null)
            {
                states.add(clientState);
            }
        } catch (IOException ex) {
            GameLog.write(ex);
        }       

        for (StatePlayer state : states)
        {
            if (state.mouseMiddlePressed)
            {
                generatePlayerTank(state.id);               
            }
            gameState.updatePlayer(state);
        }
    }
    
    public boolean checkTankCanMove(TankArea tankArea, String tankId)
    {
        return gameState.tankCanMove(tankArea, tankId);
    }
    
    public void addShell(Shell shell)
    {
        gameState.getShells().add(shell);
    }
    
    public void generatePlayerTank(String ownerId)
    {
        if (gameState.getTank(ownerId) == null)
        {
            Tank newPlayerTank = null;
            boolean possibleTank = false;

            while (!possibleTank)
            {
                int randomX = (int) ( Math.random()*(WIDTH - Tank.WIDTH) + (int)(Tank.WIDTH / 2) );
                int randomY = (int) ( Math.random()*(HEIGHT - Tank.HEIGHT) + (int)(Tank.HEIGHT / 2) );
                newPlayerTank = new Tank(Tank.getRandomColor(), randomX, randomY, ownerId, this);

                possibleTank = gameState.tankCanMove(newPlayerTank);
            }

            gameState.addTank(newPlayerTank);            
        }
    }
}
