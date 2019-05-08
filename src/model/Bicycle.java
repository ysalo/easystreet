/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a Bicycle.
 * 
 * @author Yaro Salo
 * @version October 2016
 *
 */
public class Bicycle extends AbstractVehicle {

    /**
     * The number of updates between this vehicles death and when it should revive.
     */
    private static final int DEATH_TIME = 30; 
    
    /**
     * The terrain a bicycle can traverse.
     */
    private final List<Terrain> myValidTerrain;
    
    /**
     * Constructs an Bicycle.
     * 
     * @param theX the x-coordinate of the Bicycle.
     * @param theY the y-coordinate of the Bicycle.
     * @param theDirection the direction of the Bicycle.
     */
    public Bicycle(final int theX, final int theY, final Direction theDirection) {
        
        super(theX, theY, theDirection, DEATH_TIME);
        
        // Fill an array list with valid terrain.
        myValidTerrain = new ArrayList<Terrain>();
        myValidTerrain.add(Terrain.STREET);
        myValidTerrain.add(Terrain.LIGHT);
        myValidTerrain.add(Terrain.CROSSWALK);
        myValidTerrain.add(Terrain.TRAIL);
        
    }



    /** 
     * {@inheritDoc}
     * 
     * The bicycle can travel on streets, lights, crosswalks and trails, but prefers trails.
     * It cannot travel through green lights.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        
        boolean flag = true;
        
        // If the terrain is not valid or the terrain is a crosswalk or a light
        // and the light is not green the bicycle cannot pass.
        if (!myValidTerrain.contains(theTerrain) 
                        || (theTerrain == Terrain.LIGHT || theTerrain == Terrain.CROSSWALK)
                        && theLight != Light.GREEN) {
            flag = false;
        }

        return flag;
    }
   

    /**
     * {@inheritDoc}
     * 
     * If the terrain in front of a bicycle is a trail, the bicycle always goes
     * straight ahead in the direction it is facing. If a bicycle is not facing
     * a trail, but there is a trail either to the left or to the right of the
     * bicycle’s current direction, then the bicycle turns to face the trail and
     * moves in that direction. If there is no trail straight ahead, to the
     * left, or to the right, the bicycle prefers to move straight ahead on a
     * street (or light or crosswalk light) if it can. If it cannot move
     * straight ahead, it turns left if possible; if it cannot turn left, it
     * turns right if possible. As a last resort, if none of these three
     * directions is legal (all not streets or lights or crosswalk lights), the
     * bicycle turns around. Bicycles ignore green lights. Bicycles stop for
     * yellow and red lights; if a traffic light or crosswalk light is
     * immediately ahead of the bicycle and the light is not green, the bicycle
     * stays still and does not move unless a trail is to the left or right. If
     * a bicycle is facing a red or yellow light and there is a trail to the
     * left or right, the bicycle will turn to face the trail.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
       
        Direction direction = getDirection();
        

        for (final Entry<Direction, Terrain>  e: theNeighbors.entrySet()) {
            
            // Check to see if there is a trail straight, left or right of the bicycle.
            if ((e.getValue() == Terrain.TRAIL) && (e.getKey() != getDirection().reverse())) {
               
                direction = e.getKey();
            }
        }
        
        //If no trail was found and the bicycle can't go straight enter this statement.
        // Otherwise return the direction of the trail.
        if (theNeighbors.get(direction) != Terrain.TRAIL 
                        && !myValidTerrain.contains(theNeighbors.get(direction))) {
           
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
