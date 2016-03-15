/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.GraphicObjects.Shell;
import SabotageTanks.GraphicObjects.Tank;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ytokmakov
 */

public final class PlayerState extends State implements Cloneable {
    
    @Expose public boolean upPressed,
                   downPressed,
                   leftPressed,
                   rightPressed;
    @Expose public boolean mouseLeftPressed,
                   mouseRightPressed,
                   mouseMiddlePressed;
    @Expose public int cursorX = 0,
               cursorY = 0;
    @Expose public final String id;
    
    public PlayerState(String id)
    {
        this.id = id;
    }
    
    public void pressedUp() { upPressed = true; }
    public void pressedDown() { downPressed = true; }
    public void pressedLeft() { leftPressed = true; }
    public void pressedRight() { rightPressed = true; }
    
    public void pressedMouseLeft() { mouseLeftPressed = true; }
    public void pressedMouseMiddle() { mouseMiddlePressed = true; }
    public void pressedMouseRight() { mouseRightPressed = true; }
    
    public void releasedUp() { upPressed = false; }
    public void releasedDown() { downPressed = false; }
    public void releasedLeft() { leftPressed = false; }
    public void releasedRight() { rightPressed = false; }
    
    public void releasedMouseLeft() { mouseLeftPressed = false; }
    public void releasedMouseMiddle() { mouseMiddlePressed = false; }
    public void releasedMouseRight() { mouseRightPressed = false; }
    
    public void reset()
    {
        upPressed = false;
        downPressed =  false;
        leftPressed =  false;
        rightPressed =  false;
        mouseLeftPressed =  false;
        mouseRightPressed =  false;
        mouseMiddlePressed =  false;
        cursorX = 0;
        cursorY = 0;
    }
    public void resetMouse()
    {
        mouseLeftPressed =  false;
        mouseRightPressed =  false;
        mouseMiddlePressed =  false;
    }
    public PlayerState clone() throws CloneNotSupportedException 
    {
        return (PlayerState)super.clone();
    }
}


//public final class StatePlayer extends State {
//    
//    Tank tank;
//    List<Shell> shellList;
//    
//    public StatePlayer()
//    {
//        shellList = Collections.synchronizedList(new ArrayList<Shell>());
//    }
//    public void updateTank(Tank updatingTank)
//    {
//        tank.setBursting(updatingTank.getBursting());
//    }
//}
