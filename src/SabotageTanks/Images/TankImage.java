/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Images;

import java.awt.image.BufferedImage;

/**
 *
 * @author YTokmakov
 */
    public class TankImage
    {
        public final String type;
        public final BufferedImage image;
        
        public TankImage(String imageType, BufferedImage image)
        {
            type = imageType;
            this.image = image;
        }
    }
