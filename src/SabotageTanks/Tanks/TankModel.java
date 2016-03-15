/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Tanks;

import SabotageTanks.GameServer;
import java.util.List;

public class TankModel
{
    private final String name;
    private List<TankImage> images;
    private List<TankParameter> parameters;
    
    public TankModel(String modelName)
    {
        name = modelName;
    }
    
    public Tank createTank(int x, int y, GameServer gameServer, String tankId)
    {
        return new Tank();
    }
    
    public TankAnimator createAnimator()
    {
        return new TankAnimator(name, images);
    }
    
    public boolean addParameter(TankParameter newParameter)
    {
        for (TankParameter parameter : parameters)
        {
            if (parameter.name.matches(newParameter.name))
            {
                return false;
            }
        }
        
        parameters.add(newParameter);
        return true;
    }
    
    public boolean addImage(TankImage newImage)
    {
        for (TankImage image : images)
        {
            if (image.type.matches(newImage.type))
            {
                return false;
            }
        }
        
        images.add(newImage);
        return true;
    }
}