/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import SabotageTanks.GraphicObjects.GameObject;
import SabotageTanks.GraphicObjects.Shell;
import SabotageTanks.GraphicObjects.Tank;
import SabotageTanks.GraphicObjects.TankArea;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class StateServer extends State {
    
    private List<Tank> tankList;
    private List<Shell> shellList;
    
    public StateServer()
    {
        tankList = Collections.synchronizedList(new ArrayList<Tank>());
        shellList = Collections.synchronizedList(new ArrayList<Shell>());
    }

    public ArrayList<GameObject> getObjectsToDraw() {
        
        ArrayList<GameObject> returnArray = new ArrayList<GameObject>();
        returnArray.addAll(tankList);
        returnArray.addAll(shellList);
        return returnArray;
        
    }
    public void addTank(Tank tank)
    {
        tankList.add(tank);
    }
    
    public void updatePlayer(StatePlayer playerControl)
    {
        synchronized(tankList)
        {
            Iterator iter = tankList.iterator();
            while (iter.hasNext())
            {
                Tank tank = (Tank)iter.next();
                if (tank != null && tank.getId().matches(playerControl.id))
                {
                    tank.control(playerControl);
                    return;
                }
            }
        }
    }
    
    public boolean tankCanMove(Tank movingTank)
    {
        
        synchronized (tankList)
        {
            Iterator iter = tankList.iterator();
            while (iter.hasNext())
            {
                Tank iterTank = (Tank)iter.next();
                if ( (!movingTank.sameId(iterTank)) && movingTank.isCrossing(iterTank))
                {
                    return false;
                }
            }
        }
        
        return true;
    }

    public boolean tankCanMove(TankArea tankArea, String tankId)
    {
        synchronized (tankList)
        {
            Iterator iter = tankList.iterator();
            while (iter.hasNext())
            {
                Tank iterTank = (Tank)iter.next();
                if ( (!iterTank.getId().matches(tankId)) && iterTank.isCrossing(tankArea))
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public List<Tank> getTanks()
    {
        return tankList;
    }
    public List<Shell> getShells()
    {
        return shellList;
    }
    public Tank getTank(String tankId)
    {
        synchronized (tankList)
        {
            Iterator iter = tankList.iterator();
            while (iter.hasNext())
            {
                Tank iterTank = (Tank)iter.next();
                if (iterTank.getId().matches(tankId))
                {
                    return iterTank;
                }
            }
        }
        
        return null;
    }
}
