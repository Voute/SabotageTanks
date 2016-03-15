/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.GraphicObjects;

import static SabotageTanks.GraphicObjects.Tank.HEIGHT;
import com.google.gson.annotations.Expose;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;

/**
 *
 * @author YTokmakov
 */
public final class TankArea extends Polygon
{
    private final double radius; 
    private double rotation;      // поворот по умолчанию        
    private double x,      // координата размещения по оси Х (центр)
                   y;      // координата размещения по оси У (центр)
    
    public TankArea(double x, double y, double radius)
    {
        this.x = x;
        this.y = y;
        this.rotation = Math.PI / 2;
        this.addPoint((int)(x - (HEIGHT / 2)), (int)(y - (HEIGHT / 2)));
        this.addPoint((int)(x + (HEIGHT / 2)), (int)(y - (HEIGHT / 2)));
        this.addPoint((int)(x + (HEIGHT / 2)), (int)(y + (HEIGHT / 2)));
        this.addPoint((int)(x - (HEIGHT / 2)), (int)(y + (HEIGHT / 2)));
        this.radius = radius;
    }
    
    public TankArea(double newX, double newY, double radius, double newRotation)
    {
        this.x = newX;
        this.y = newY;
        this.rotation = newRotation;
        
        int x0 = (int)(Math.cos(newRotation - Math.PI / 4) * radius);
        int y0 = (int)(Math.sin(newRotation - Math.PI / 4) * radius);

        int x1 = (int)(Math.cos(newRotation + Math.PI / 4) * radius);
        int y1 = (int)(Math.sin(newRotation + Math.PI / 4) * radius);

        int x2 = (int)(Math.cos(newRotation + Math.PI / 4 * 3) * radius);
        int y2 = (int)(Math.sin(newRotation + Math.PI / 4 * 3) * radius);

        int x3 = (int)(Math.cos(newRotation - Math.PI / 4 * 3) * radius);
        int y3 = (int)(Math.sin(newRotation - Math.PI / 4 * 3) * radius);

        this.addPoint( (int)(newX + x0), (int)(newY + y0) );
        this.addPoint( (int)(newX + x1), (int)(newY + y1) );
        this.addPoint( (int)(newX + x2), (int)(newY + y2) );            
        this.addPoint( (int)(newX + x3), (int)(newY + y3) );
        
        this.radius = radius;
    }
    
    public TankArea(Point[] points, double radius, int x, int y, double rotation)
    {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        
        for (Point point: points)
        {
            this.addPoint(point.x, point.y);
        }
        
        this.radius = radius;
    }

    private Point[] getPoints(int Xcenter, int Ycenter, double newRotation)
    {
        Point[] returnPoints = new Point[4];

        int x0 = (int)(Math.cos(newRotation - Math.PI / 4) * radius);
        int y0 = (int)(Math.sin(newRotation - Math.PI / 4) * radius);

        int x1 = (int)(Math.cos(newRotation + Math.PI / 4) * radius);
        int y1 = (int)(Math.sin(newRotation + Math.PI / 4) * radius);

        int x2 = (int)(Math.cos(newRotation + Math.PI / 4 * 3) * radius);
        int y2 = (int)(Math.sin(newRotation + Math.PI / 4 * 3) * radius);

        int x3 = (int)(Math.cos(newRotation - Math.PI / 4 * 3) * radius);
        int y3 = (int)(Math.sin(newRotation - Math.PI / 4 * 3) * radius);

        returnPoints[0] = new Point(Xcenter + x0, Ycenter + y0);
        returnPoints[1] = new Point(Xcenter + x1, Ycenter + y1);
        returnPoints[2] = new Point(Xcenter + x2, Ycenter + y2);            
        returnPoints[3] = new Point(Xcenter + x3, Ycenter + y3);

        return returnPoints;
    }
    boolean contains(int x[], int y[])
    {
        for (int i = 0; i < x.length; i++)
        {
            if (this.contains(x[i], y[i]))
            {
                return true;
            }
        }
        return false;
    } 
    double calculateXshift(double rotation, double movementShift)
    {
        return Math.cos(rotation) * movementShift;
    }
    double calculateYshift(double rotation, double movementShift)
    {
        return Math.sin(rotation) * movementShift;
    }
    private void refreshPoints(Point[] newPoints)
    {
        this.reset();
        for (Point point: newPoints)
        {
            this.addPoint(point.x, point.y);
        }
    }
    public TankArea assumeNewLocation(double movementShift, double rotationShift)
    {
        double newRotation = rotation + rotationShift;

        double Xnew = x + calculateXshift(newRotation, movementShift);
        double Ynew = y + calculateYshift(newRotation, movementShift);

        return new TankArea(Xnew, Ynew, radius, newRotation);
    }
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
