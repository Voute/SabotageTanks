/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Images;

import SabotageTanks.Images.ImageLoader.TankImage;
import java.awt.image.BufferedImage;

public class TankAnimator
{
    private BufferedImage movingSprite;
    private BufferedImage tower;
    
    public TankAnimator(ImageLoader.TankImageResources imageResources)
    {
        for (TankImage tankImage : imageResources.images)
        {
            switch (tankImage.name)
            {
                case "moving": movingSprite = tankImage.image;
                    break;
                case "tower": tower = tankImage.image;
                    break;
            }
        }
    }
    
    public void tick()
    {
        
    }
    
    public BufferedImage getTowerImage()
    {
        return tower;
    }
    
    public BufferedImage getImage
}
