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
 * Represents a Human.
 * 
 * @author Yaro Salo
 * @version October 23 2016
 */
public class Human extends AbstractVehicle {
    
    /**
     * The number of updates between this vehicles death and when it should revive.
     */
    private static final int DEATH_TIME = 50; 
    
    /** 
     * The terrain a Human can travel on.
     */
    private final List<Terrain> myValidTerrain;
    
    /**
     * Constructs a Human.
     * 
     * @param theX the x-coordinate of the Human.
     * @param theY the y-coordinate of the Human.
     * @param theDirection the direction of the Human.
     */
    public Human(final int theX, final int theY, final Direction theDirection) {
        
        super(theX, theY, theDirection, DEATH_TIME);
        
        myValidTerrain = new ArrayList<Terrain>();
        myValidTerrain.add(Terrain.GRASS);
        myValidTerrain.add(Terrain.CROSSWALK);
        
    }


    /**
     * {@inheritDoc}
     * 
     * Humans can travel on grass and crosswalks. Humans do not cross green
     * crosswalk lights.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        
        boolean flag = true;
        
        if (!myValidTerrain.contains(theTerrain)) {
            flag = false;
        }
        
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.GREEN) {
            
            flag = false;
        }
        
        
        return flag;
    }

    /**
     * {@inheritDoc} 
     * Humans move in a random direction (straight, left, right,
     * or reverse), always on grass or crosswalks. A human never reverses
     * direction unless there is no other option. If a human is next to a
     * crosswalk it will always choose to turn to face in the direction of the
     * crosswalk. (The map of terrain will never contain crosswalks that are so
     * close together that a human might be adjacent to more than one at the
     * same time.) Humans do not travel through crosswalks when the crosswalk
     * light is green. If a human is facing a green crosswalk, it will wait
     * until the light changes to yellow and then cross through the crosswalk.
     * The human will not turn to avoid the crosswalk. Humans travel through
     * crosswalks when the crosswalk light is yellow or red. Humans ignore the
     * color of traffic lights.
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
            
            // A human will turn to face a crosswalk.
            if (theNeighbors.get(d) == Terrain.CROSSWALK) {
                direction = d;
            }
            
            // If the terrain in the given direction is not valid remove that direction.
            if (!myValidTerrain.contains(theNeighbors.get(d))) {
                itr.remove();
            }

           
        }
       
        //If the human is not facing a cross walk enter this statement.
        // Otherwise return in which direction is a crosswalk.
        if (Terrain.CROSSWALK != theNeighbors.get(direction)) {
        
            // No valid terrain to left right or straight then need to reverse.
            if (validDirection.isEmpty()) {
                
                direction = direction.reverse();
            
            } else {
                
                // Randomly choose a direction from validDirection.
                final int i = rand.nextInt(validDirection.size());
                direction = validDirection.get(i);
               
                // Get rid of all values left in validDirection for the next time the 
                // function gets called.
                validDirection.clear();      
            }
        }
        return direction; 
    }

}




