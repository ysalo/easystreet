/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a Car.
 * 
 * @author Yaro Salo
 * @version October 2016
 *
 */
public class Car extends AbstractVehicle {

    
    /**
     * The number of updates between this vehicles death and when it should revive.
     */
    private static final int DEATH_TIME = 10; 
    
    /** 
     * The terrain a car can travel on.
     */
    private final List<Terrain> myValidTerrain;
    
    /**
     * Constructs a Car.
     * 
     * @param theX the x-coordinate of the Car.
     * @param theY the y-coordinate of the Car.
     * @param theDirection the direction of the Car.
     */
    public Car(final int theX, final int theY, final Direction theDirection) {
       
        super(theX, theY, theDirection, DEATH_TIME);
        
        myValidTerrain = new ArrayList<Terrain>();
        myValidTerrain.add(Terrain.STREET);
        myValidTerrain.add(Terrain.LIGHT);
        myValidTerrain.add(Terrain.CROSSWALK);
    }

    /** 
     * {@inheritDoc}
     * 
     * Cars can travel on streets, lights, and crosswalk.
     * Cars stop for red lights and crosswalk light and they stop for yellow
     * crosswalk lights.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        
        boolean flag = true;
        
        if (theTerrain == Terrain.LIGHT && theLight == Light.RED 
                        || !myValidTerrain.contains(theTerrain)) {
            flag = false;
        }
        
        if (theTerrain == Terrain.CROSSWALK 
                        && (theLight == Light.RED || theLight == Light.YELLOW)) {
            flag = false;
        }
        
        return flag;
    }

    /**
     * {@inheritDoc}
     * 
     * A car prefers to drive straight ahead on the street if it can. If it
     * cannot move straight ahead, it turns left if possible; if it cannot turn
     * left, it turns right if possible; as a last resort, it turns around. Cars
     * stop for red lights; if a traffic light is immediately ahead of the car
     * and the light is red, the car stays still and does not move. It does not
     * turn to avoid the light. When the light turns green, the car resumes its
     * original direction. Cars ignore yellow and green lights. Cars stop for
     * red and yellow crosswalk lights, but drive through green crosswalk lights
     * without stopping.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        
        // The direction that is currently straight for the car.
        Direction direction = getDirection();
                
        // Check if the car can't go straight.
        // Otherwise return the direction that is currently straight.
        if (!myValidTerrain.contains(theNeighbors.get(direction))) {
            
            // If it can't go straight check if it can go left.
            if (myValidTerrain.contains(theNeighbors.get(direction.left()))) {
                
                direction = direction.left();
            
            // If it can't go left check if it can go right.
            } else if (myValidTerrain.contains(theNeighbors.get(direction.right()))) {
                
                direction = direction.right();
             
            // If it can't go in those directions it need to turn around.    
            } else {
               
                direction = direction.reverse();
            }
            
        }
        
        return direction;
    }     

}
