/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Net.packages;

/**
 *
 * @author YTokmakov
 */
public final class TankMovement {
    
    public static final String N = "TankMovement";
    public final int shiftX, shiftY;
    public final int shiftBarrelX, shiftBarrelY;
    public final double shiftRotation;
    public final String id;
    
    public TankMovement(int shiftX, int shiftY, String id, double shiftRotation, int shiftBarrelX, int shiftBarrelY)
    {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.id = id;
        this.shiftRotation = shiftRotation;
        this.shiftBarrelX = shiftBarrelX;
        this.shiftBarrelY = shiftBarrelY;
    }    
    
}
