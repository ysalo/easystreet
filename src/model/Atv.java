/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;

import java.util.Map;

/**
 * Represents an Atv. 
 * 
 * @author Yaro Salo
 * @version October 23 2016
 */
public class Atv extends AbstractVehicle {

    /**
     * The number of updates between this vehicles death and when it should revive.
     */
    private static final int DEATH_TIME = 20; 
    
    /**
     * Constructs an Atv.
     * 
     * @param theX the x-coordinate of the Atv.
     * @param theY the y-coordinate of the Atv.
     * @param theDirection the direction of the Atv.
     */
    public Atv(final int theX, final int theY, final Direction theDirection) {
        
        super(theX, theY, theDirection, DEATH_TIME);
    }

    /** 
     * {@inheritDoc}
     * 
     * The Atv travels through all lights without stopping.
     * The Atv cannot travel on walls.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return !(theTerrain == Terrain.WALL);
    }

    /**
     * {@inheritDoc}
     * 
     * The Atv travels in random direction on all terrain except walls.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        
        Direction direction = Direction.random();
        
        // Loop until the Atv "doesn't want to" drive into a wall or 
        // reverse direction.
        do {
            
            direction = Direction.random();
            
        } while (theNeighbors.get(direction) == Terrain.WALL 
                        || direction == getDirection().reverse());
        
        return direction;
    }
   
}
