/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Images;

import SabotageTanks.Images.TankImage;
import java.awt.image.BufferedImage;
import java.util.List;

public class TankAnimator
{
    private final String model;
    private BufferedImage movingSprite;
    private BufferedImage tower;
    
    public TankAnimator(String tankModel, List<TankImage> images)
    {
        model = tankModel;
        
        for (TankImage tankImage : images)
        {
            switch (tankImage.type)
            {
                case "moving": movingSprite = tankImage.image;
                    break;
                case "tower": tower = tankImage.image;
                    break;
            }
        }
    }
    
    public String getModel()
    {
        return model;
    }
    
    public void tick()
    {
        
    }
    
    public BufferedImage getTowerImage()
    {
        return tower;
    }
    
}
