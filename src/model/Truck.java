/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
 



/**
 * Represents a Truck.
 * 
 * @author Yaro Salo
 * @version October 2016
 *
 */
public final class Truck extends AbstractVehicle {

   
    /**
     * The number of updates between this vehicles death and when it should revive.
     * A truck never dies therefore it's death time is 0.
     */
    private static final int DEATH_TIME = 0; 
    
    /**
     * The terrain a truck can travel on.
     */
    private final List<Terrain> myValidTerrain;
    
    /**
     * Constructs a Truck.
     * 
     * @param theX the x-coordinate of the Truck.
     * @param theY the y-coordinate of the Truck.
     * @param theDirection the direction of the Truck.
     */
    public Truck(final int theX, final int theY, final Direction theDirection) {
        
        super(theX, theY, theDirection, DEATH_TIME);
        
        myValidTerrain = new ArrayList<Terrain>();
        myValidTerrain.add(Terrain.STREET);
        myValidTerrain.add(Terrain.LIGHT);
        myValidTerrain.add(Terrain.CROSSWALK);
    }


    /** 
     * {@inheritDoc}
     * 
     * Trucks can travel on streets, lights and crosswalks.
     * Trucks stop for red crosswalk lights and drive through the rest.
     * 
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        
        boolean flag = true;
        
        if (!myValidTerrain.contains(theTerrain) 
                        || theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            
            flag = false;
        }
        
        return flag;
    }

    /**
     * {@inheritDoc}
     * 
     * Trucks randomly select to go straight, turn left, or turn right. As a
     * last resort, if none of these three directions is legal (all not streets,
     * lights, or crosswalks), the truck turns around.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        
        final Random rand = new Random();
        
        // Get the current direction of the truck.
        Direction direction = getDirection();
  
        // Fill an array list with directions to the left right and straight.
        final ArrayList<Direction> validDirection = new ArrayList<Direction>();
        validDirection.add(direction);
        validDirection.add(direction.left());
        validDirection.add(direction.right());
        
        final Iterator<Direction> itr = validDirection.iterator();
       
        while (itr.hasNext()) {
            
            final Direction d = itr.next(); 
            
            // If the terrain in the given direction is not valid remove that direction.
            if (!myValidTerrain.contains(theNeighbors.get(d))) {
                itr.remove();
            }
           
        }
       
        // If there is no valid terrain the truck has to reverse direction.
        if (validDirection.isEmpty()) {
            direction = direction.reverse();
        
        // Otherwise there is a valid terrain that is not in the reverse direction. 
        } else {
           
            // Randomly choose a direction from validDirection.
            final int i = rand.nextInt(validDirection.size());
            direction = validDirection.get(i);
           
            // Get rid of all values left in validDirection.
            validDirection.clear();
            
        }

        return direction;
    }
    
       
} 

