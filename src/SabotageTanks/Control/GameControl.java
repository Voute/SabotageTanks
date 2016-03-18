/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import SabotageTanks.Tanks.Tank;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;
import SabotageTanks.Game;
import SabotageTanks.GameLog;
import SabotageTanks.StatePlayer;

public class GameControl {
    
    private Game game;
    private Tank playerTank = null;
    private StatePlayer playerState;
    
    public GameControl(Game game, String playerId)
    {
        this.game = game;
        playerState = new StatePlayer(playerId);
    }
    
    public KeyListener getKeyListener()      // обработчик клавиш клавы
    {
        return new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {  }
            @Override
            public void keyPressed(KeyEvent e)      // кнопка клавы зажата
            {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    playerState.pressedUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    playerState.pressedLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    playerState.pressedRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    playerState.pressedDown();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {       // кнопка клавы отпущена
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_UP: playerState.releasedUp();
                                         break;
                    case KeyEvent.VK_LEFT: playerState.releasedLeft();
                                           break;
                    case KeyEvent.VK_RIGHT: playerState.releasedRight();
                                            break;
                    case KeyEvent.VK_DOWN: playerState.releasedDown();
                                           break;
                }
            }
        };
    }
    public MouseListener getMouseListener()
    {
        return new MouseListener()      // обработчик клавиш мыши
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {

            }
            @Override
            synchronized public void mousePressed(MouseEvent e)     // кнопка мыши зажата
            {
                if (e.getButton() == 1)     // левая кнопка мыши (shot)
                {
                    playerState.pressedMouseLeft();
                }
                else if (e.getButton() == 3 &&        // правая кнопка мыши (смена цвета)
                           playerTank != null)
                {  
                    playerState.pressedMouseRight();
                }
                else if (e.getButton() == 2)        // средняя кнопка мыши (ресаем игрока)
                {
                    playerState.pressedMouseMiddle();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }
    public StatePlayer getPlayerState()
    {
        try {
            StatePlayer returnState = playerState.clone();
            
            Point cursor = game.getCursorPosition();
            if (cursor != null)
            {
                returnState.cursorX = cursor.x;
                returnState.cursorY = cursor.y;
            }
            
            playerState.resetMouse();
            return returnState;
        } catch (CloneNotSupportedException ex) {
            GameLog.write(ex);
            return null;
        }
    }

}
