/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.Control.GameControl;
import SabotageTanks.Net.Connection;
import SabotageTanks.Interface.BattleField;
import SabotageTanks.Tanks.Tank;
import SabotageTanks.Tanks.TankModel;
import SabotageTanks.Tanks.TankModelsLoader;
import java.awt.Point;
import java.util.List;

public abstract class Game implements Runnable
{   
    protected StateServer gameState;
    protected final Player player;
    protected final Connection connection;
    protected final GameControl control;
    protected final BattleField battleField;
    
    private static final String ARTICLE = "SabotageTanks";        // заголовок окна
    
    protected static final int WIDTH = 800;       // ширина игрового поля
    protected static final int HEIGHT = 600;      // высота игрового поля

    
    public Game(Player player, Connection connection)
    {
        String title = ARTICLE + " - " + connection.getLocalIp() + ":" + connection.getLocalPort();
        
        this.connection = connection;
        this.player = player;
        
        TankModelsLoader modelsLoader = new TankModelsLoader();
        List<TankModel> models = modelsLoader.getModels();;
        
        battleField = new BattleField(WIDTH, HEIGHT, title);
        control = new GameControl(this, player.getId());
        battleField.addKeyListener(control.getKeyListener());      
        battleField.addMouseListener(control.getMouseListener());
        
        gameState = new StateServer();
    }
    
    public void start()
    {
        new Thread(this).start();       // стартуем игру в новом потоке        
    }
    
    @Override
    public void run()
    {
        long lastTime = System.nanoTime();      // время виртуальной машины (накапливаемая)
        double nsPerFrame = 1000000000D/60D;        // сколько наносекунд на один кадр, если 60 кадров в секунду
        long lastClockTime = System.currentTimeMillis();        // текущее время
        
        int frames = 0;     // количество отрисованных фреймов
        double delta = 0D;      // накопленные промежутки времени в наносекундах
        
        while (true)
        {
            long newTime = System.nanoTime();       // берем новое время виртуальной машины
            delta += (newTime - lastTime) / nsPerFrame;     // вычисляем разницу с предыдущим, делим, получаем количество
                                                            // фреймов, которое можно отрисовать
            lastTime = newTime;     // присваиваем новое время
            
            long newClockTime = System.currentTimeMillis();     // новое текущее время
            
            boolean shouldRender = false;       // надо ли рисовать фрейм
            
            while (delta >= 1)      // рисуем вычисленное количество фреймов
            {
                frames++;
                delta--;
                
                // receiving data
                receiveState();
                
                tick();
                
                // sending data
                sendState();
                
                shouldRender = true;
            }
            
            if (shouldRender)       // рисуем фрейм, если можно
            {
                render();
            }
            // каждую секунду выводим сообщение о количестве нарисованных фреймов
            if (newClockTime - lastClockTime >= 1000)       
            {
                System.out.println("framesRendered: " + frames);
                
                frames = 0;
                lastClockTime = newClockTime;
            }
        }
    }
    
    public int getWidth()
    {
        return WIDTH;
    }
    
    public int getHeight()
    {
        return HEIGHT;
    }
    public Point getCursorPosition()
    {
        return battleField.getCursorPosition();
    }
    public double getPlayerSpeed()
    {
        Tank playerTank = getPlayerTank();
        if (getPlayerTank() != null)
        {
            return playerTank.getSpeed();
        } else
        {
            return 0.0d;
        }
    }
    private Tank getPlayerTank()
    {
        return gameState.getTank(player.getId());
    }
    public int getShellsQuantity()
    {
        return gameState.getShells().size();
    }
    public int getPlayerX()
    {
        Tank playerTank = getPlayerTank();
        if (playerTank != null)
        {
            return playerTank.getX();
        } else
        {
            return 0;
        }
    }
    public int getPlayerY()
    {
        Tank playerTank = getPlayerTank();
        if (playerTank != null)
        {
            return playerTank.getY();
        } else
        {
            return 0;
        }
    }
    
    protected abstract void tick();
    protected abstract void sendState();
    protected abstract void receiveState();
    
    // рисуем фрейм
    private void render()
    {
        battleField.draw(gameState.getObjectsToDraw(), this);
    }

}
