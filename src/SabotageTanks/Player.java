/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks;

import java.awt.Color;

public final class Player {
    
    //  цвета квадратов    
    public static final Color[] TANK_COLORS = {Color.GRAY,
                                                Color.YELLOW,
                                                Color.BLUE,
                                                Color.MAGENTA, 
                                                Color.ORANGE,
                                                Color.CYAN,
                                                Color.pink
                                                };
    
    private final String name;
    private final Color color;
    
    public Player(String playerName, Color playerColor)
    {
        name = playerName;
        color = playerColor;
    }
    
    public String getName()
    {
        return name;
    }
    public Color getColor()
    {
        return color;
    }
    public String getId()
    {
        return name;
    }
    public static Color getRandomColor()
    {
        return TANK_COLORS[ (int)(Math.random()*7) ];
    }
}
