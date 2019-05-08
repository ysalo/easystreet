/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Taxi.
 * 
 * @author Yaro Salo
 * @version October 2016
 *
 */
public class Taxi extends Car {

    /** Number of turns a taxi will stay stopped at a red crosswalk light. */
    private static final int MY_GO_RED = 3;
    
    /** Number of turns that have passed since the Taxi has stopped at a red
     *  crosswalk light.
     */
    private int myCrossPokes;
    
    /** 
     * The terrain a car can travel on.
     */
    private final List<Terrain> myValidTerrain;
    
    /**
     * Constructs an Taxi.
     * 
     * @param theX the x-coordinate of the Taxi.
     * @param theY the y-coordinate of the Taxi.
     * @param theDirection the direction of the Taxi.
     */
    public Taxi(final int theX, final int theY, final Direction theDirection) {
        
        super(theX, theY, theDirection);
        myCrossPokes = 0;
        myValidTerrain = new ArrayList<Terrain>();
        myValidTerrain.add(Terrain.STREET);
        myValidTerrain.add(Terrain.LIGHT);
        myValidTerrain.add(Terrain.CROSSWALK);
    }


    /**
     * {@inheritDoc}
     * 
     * Taxis stop for (temporarily) red crosswalk lights. If a crosswalk light
     * is immediately ahead of the taxi and the crosswalk light is red, the Taxi
     * stays still and does not move for 3 clock cycles or until the crosswalk
     * light turns green, whichever occurs first. It does not turn to avoid the
     * crosswalk light. When the crosswalk light turns green, or after 3 clock
     * cycles, whichever happens first, the taxi resumes its original direction.
     * A Taxi will drive through yellow or green crosswalk lights without
     * stopping.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        
        boolean flag = true;
          
        if (theTerrain == Terrain.LIGHT && theLight == Light.RED 
                        || !myValidTerrain.contains(theTerrain)) {
           
            flag = false;
        }
        
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            
            flag = false;
            myCrossPokes += 1;
           
            if (myCrossPokes == MY_GO_RED) {
                flag = true;
                myCrossPokes = 0;
            }
            
        }
        
        return flag;
    }


}
