/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.GraphicObjects;

import SabotageTanks.Control.TankMovement;
import SabotageTanks.GameLog;
import SabotageTanks.GameServer;
import SabotageTanks.Player;
import static SabotageTanks.Player.TANK_COLORS;
import SabotageTanks.PlayerState;
import com.google.gson.annotations.Expose;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YTokmakov
 */
    public class Tank implements GameObject     // объект для управления - танк
    {
        @Expose private final String id;
        
        @Expose public final static int WIDTH = 40;
        @Expose public final static int HEIGHT = 40;
        @Expose public final static double rotationSpeed = 0.02D;
        @Expose public TankArea area;
        
        @Expose public List<BurstPiece> burstPieces;
        @Expose private int burstRenders;
        @Expose private static double speed = 1.5D;
        @Expose private final static double START_SPEED = 1.5D;
        @Expose private final static double ACCELERATION = 0.01D;
        @Expose private final static double MAX_SPEED = 3.5D;
        @Expose private final static double circumscribedRadius = HEIGHT / Math.sqrt(2);        // радиус описанной окружности
        @Expose private boolean bursting;
        @Expose private boolean readyToReset;
        
        @Expose public int XbarrelTip,     // Х координата вершины ствола
                   YbarrelTip;     // У координата вершины ствола
        
        @Expose private Color color;        // цвет квадрата
        transient private GameServer game;
        
        
        public Tank(Color color, int Xaxis, int Yaxis, String tankId, GameServer gameServer)
        {
            this.bursting = false;
            this.id = tankId;
            this.color = color;
            game = gameServer;
            
            readyToReset = false;
            
            int x = Xaxis + WIDTH / 2;
            int y = Yaxis + HEIGHT / 2;
            
            area = new TankArea(x, y, circumscribedRadius);
            burstPieces = new ArrayList<>();
//            move(new TankMovement());
            
            XbarrelTip = area.getX();
            YbarrelTip = Yaxis;
        }  
        public void shot(int targetX, int targetY)
        {
            try {
                game.addShell(new Shell(area.getX(), area.getY(), targetX, targetY, XbarrelTip, YbarrelTip, getId()));
            } catch (Exception ex) {
                GameLog.write(ex);
            }
        } 
            
        private void calculateBarrel(int Xtarget, int Ytarget)
        {
            int Xdelta = Xtarget - area.getX();
            int Ydelta = Ytarget - area.getY();
            double S = Math.sqrt( Math.pow( Xdelta, 2 ) +
                                  Math.pow( Ydelta, 2 )
                                );
            double radius = WIDTH / 2;            
            XbarrelTip = area.getX() + (int)(radius * Xdelta / S);
            YbarrelTip = area.getY() + (int)(radius * Ydelta / S);
        }
//        public void setX(double newX)
//        {
//            x = newX;
//        }
//        public void setY(double newY)
//        {
//            y = newY;
//        }
        public void setBursting(boolean bursting)
        {
            this.bursting = bursting;
        }
        public boolean getBursting()
        {
            return bursting;
        }
        public boolean getReadyToReset()
        {
            return readyToReset;
        }
//        // перемещает квадрат на новые координаты
//        public void move(TankMovement movement)
//        {
//            if (!bursting)
//            {
//                if ( movement.isNoMovement() )
//                {
//                    stopAcceleration();
//                }
//
//
//                rotation += movement.rotationShift;
//                x += area.calculateXshift(rotation, movement.movementShift);
//                y += area.calculateYshift(rotation, movement.movementShift);
//                area.calculateNewLocation(getX(), getY(), circumscribedRadius, rotation);
//
//                calculateBarrel(movement.cursorX, movement.cursorY);
//            } else
//            {
//                if (!burstPieces.isEmpty())
//                {
//                    for (BurstPiece piece:burstPieces)
//                    {
//                        piece.tick();
//                    }
//                }
//            }
//        }
        // перекрывают ли границы квадрата указанные границы другого квадрата
        public Polygon assumeMove(double movementShift, double rotationShift)
        {
            return area.assumeNewLocation(movementShift, rotationShift);
        }
        public boolean isCrossing(TankMovement movement, TankArea testingArea)
        {
            TankArea assumedTankArea = area.assumeNewLocation(movement.movementShift, movement.rotationShift);
            
                return assumedTankArea.contains(testingArea.xpoints, testingArea.ypoints) ||
                       testingArea.contains(assumedTankArea.xpoints, assumedTankArea.ypoints)
                       ;
        }
        public boolean isCrossing(Tank checkingTank)
        {
            return checkingTank.getArea().contains(area.xpoints, area.ypoints);
        }
        public boolean isCrossing(TankArea tankArea)
        {
            return tankArea.contains(area.xpoints, area.ypoints);
        }
        public boolean containsXY(int x, int y)
        {
            return area.contains(x, y);
        }       
        public int getX()        // возвращает Х координату квадрата
        {
            return (int)area.getX();
        }
        public int getY()        // возвращает Y координату квадрата
        {
            return (int)area.getY();
        }
//        public double getXabsolute()
//        {
//            return x;
//        }
//        public double getYabsolute()
//        {
//            return y;
//        }
        public TankArea getArea()
        {
            return area;
        }
        public void changeColor(Color newColor)
        {
            color = newColor;
        }
        public double speed()
        {   if ( speed < MAX_SPEED )
            {
                speed += ACCELERATION;
            }
            return speed;
        }
        public double getSpeed()
        {
            return speed;
        }
        public void stopAcceleration()
        {
            speed = START_SPEED;
        }
        public Color getColor()     // возвращает цвет квадрата
        {
            return color;
        }
        public static Color getRandomColor()
        {
            return TANK_COLORS[ (int)(Math.random()*7) ];
        }
        @Override
        public String getId()
        {
            return id;
        }
        public boolean sameId(GameObject gameObject)
        {
            return (gameObject.getId().matches(getId()));
        }
        public static Tank generate(Player owner, GameServer game, List<Shell> ownerShellList)
        {
            int randomX = (int) ( Math.random()*(game.getWidth() - WIDTH) + (int)(WIDTH / 2) );
            int randomY = (int) ( Math.random()*(game.getHeight() - HEIGHT) + (int)(HEIGHT / 2) );
            return new Tank(owner.getColor(), randomX, randomY, owner.getName(), game);
        }
//        public void updateStats(Tank updatingTank)
//        {
////            this.bursting = updatingTank.bursting;
//            if (updatingTank.bursting)
//            {
//                this.bursting = true;
//            }
//            if (!bursting)
//            {
//                this.XbarrelTip = updatingTank.XbarrelTip;
//                this.YbarrelTip = updatingTank.YbarrelTip;
//                this.x = updatingTank.x;
//                this.y = updatingTank.y;
//                this.rotation = updatingTank.rotation;
//                this.area = updatingTank.area;        
//                this.color = updatingTank.color;
//            }
//        }
        
        public void update(PlayerState control)
        {
            TankMovement movement = new TankMovement();
            
            if (control.cursorX != 0 || control.cursorY != 0)
            {
                calculateBarrel(control.cursorX, control.cursorY);
            }
            
            if (control.rightPressed)      // поворот вправо
            {
                movement.rotationShift += rotationSpeed;
            }
            if (control.leftPressed)       // поворот влево
            {
                movement.rotationShift -= rotationSpeed;                
            } 
            if (control.upPressed)     // движение вперед
            {
                movement.movementShift -= speed();
            }
            if (control.downPressed)   // движение назад
            {
                movement.movementShift += speed();
                if (!movement.isNoRotation())      // если зажат поворот, инвертируем поворот
                {
                    movement.rotationShift = -movement.rotationShift;
                }
            }

            if ( movement.isNoMovement() )
            {
                stopAcceleration();
            }

            TankArea assumedTankArea = area.assumeNewLocation(movement.movementShift, movement.rotationShift);
            
            if (game.checkTankCanMove(assumedTankArea, id))
            {
                area = assumedTankArea;
            }
            if (control.mouseLeftPressed)
            {
                shot(control.cursorX, control.cursorY);
            }
        }
        
        @Override
        public double getCircumscribedRadius()
        {
            return circumscribedRadius;
        }
        
//        public static Tank generateRandom(int fieldWidth, int fieldHeight, String owner)
//        {
//            Color randomColor = TANK_COLORS[ (int)(Math.random()*7) ];
//            int randomX = (int) ( Math.random()*(fieldWidth - Tank.WIDTH) + (int)Tank.WIDTH / 2 );
//            int randomY = (int) ( Math.random()*(fieldHeight - Tank.HEIGHT) + (int)Tank.HEIGHT / 2 );
//            return new Tank(randomColor, randomX, randomY, owner);
//        }

    @Override
    public void draw(Graphics2D graph)
    {
        int x = area.getX();
        int y = area.getY();
        
        if (!bursting)
        {
            // рисуем танк
            graph.setColor(color);
            graph.fillPolygon(area);       
            graph.setColor(Color.BLACK);
            
            int[] xx = {x, XbarrelTip};
            int[] yy = {y, YbarrelTip};
            graph.drawPolyline(xx, yy, 2);

        } else if (!readyToReset)
        {
            if (burstPieces.isEmpty())
            {
                burstRenders = 0;
                
                int[] xx = new int[9];
                int[] yy = new int[9];
                int half = (int)(HEIGHT / 2);

                xx[0] = x - half;
                xx[1] = x;
                xx[2] = x + half;
                xx[3] = x - half;
                xx[4] = x;
                xx[5] = x + half;
                xx[6] = x - half;
                xx[7] = x;
                xx[8] = x + half;

                yy[0] = y - half;
                yy[1] = y - half;
                yy[2] = y - half;
                yy[3] = y;
                yy[4] = y;
                yy[5] = y;
                yy[6] = y + half;
                yy[7] = y + half;
                yy[8] = y + half;

                BurstPiece polygon = new BurstPiece(-1,-1);
                polygon.addPoint(xx[0], yy[0]);
                polygon.addPoint(xx[1], yy[1]);
                polygon.addPoint(xx[4], yy[4]);
                polygon.addPoint(xx[3], yy[3]);
                burstPieces.add(polygon);

                polygon = new BurstPiece( 1,-1);
                polygon.addPoint(xx[1], yy[1]);
                polygon.addPoint(xx[2], yy[2]);
                polygon.addPoint(xx[5], yy[5]);
                polygon.addPoint(xx[4], yy[4]);
                burstPieces.add(polygon);

                polygon = new BurstPiece( -1, 1);
                polygon.addPoint(xx[3], yy[3]);
                polygon.addPoint(xx[4], yy[4]);
                polygon.addPoint(xx[7], yy[7]);
                polygon.addPoint(xx[6], yy[6]);
                burstPieces.add(polygon);   

                polygon = new BurstPiece( 1, 1);
                polygon.addPoint(xx[4], yy[4]);
                polygon.addPoint(xx[5], yy[5]);
                polygon.addPoint(xx[8], yy[8]);
                polygon.addPoint(xx[7], yy[7]);
                burstPieces.add(polygon);
            }
            
            if (burstRenders <= 15)
            {
                graph.setColor(color);
                for (BurstPiece piece:burstPieces)
                {
                    graph.fill(piece);
                }
                burstRenders++;
            } else
            {
                burstPieces.clear();
                readyToReset = true;
            }
            
        }
    }
        

    private class BurstPiece extends Polygon
    {
        private int Xshift;
        private int Yshift;
        
        BurstPiece(int Xshift, int Yshift)
        {
            this.Xshift = Xshift;
            this.Yshift = Yshift;
        }
        void tick()
        {
            reset();
            for (int i = 0; i < 4; i++)
            {
                addPoint(xpoints[i] + Xshift, ypoints[i] + Yshift);
            }
        }
    }
    }


