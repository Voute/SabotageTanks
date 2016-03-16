/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Net.packages;

public final class ShellMovement {
    
    public static final String N = "ShellMovement";
    public final int shiftX, shiftY;
    public final String id;
    
    public ShellMovement(int shiftX, int shiftY, String id)
    {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.id = id;
    }    
    
}
