/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Direction;
import model.Light;
import model.Terrain;
import model.Truck;

import org.junit.Before;
import org.junit.Test;


/**
 * 
 * Unit tests for class Truck.
 * 
 * @author Yaro Salo
 * @version October 28 2016
 */
public class TruckTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;
    
    /** 
     * An instance of a truck initialized before every test.
     */
    private Truck myTruck;
    
    /**
     * A method to initialize the test fixtures before each test.
     */
    @Before 
    public void setUp() {
        myTruck = new Truck(5, 7, Direction.WEST);
    }
    
    /** Test the Truck constructor. */
    @Test
    public void testTruckConstructor() {
        
        final Truck t = new Truck(20, 30, Direction.WEST);
        
        assertEquals("Truck x coordinate not initialized correctly!", 20, t.getX());
        assertEquals("Truck y coordinate not initialized correctly!", 30, t.getY());
        assertEquals("Truck direction not initialized correctly!",
                     Direction.WEST, t.getDirection());
        assertEquals("Truck death time not initialized correctly!", 0, t.getDeathTime());
        assertTrue("Truck isAlive() fails initially!", t.isAlive());
    }
    
    /** Test the Truck setters. */
    @Test
    public void testTruckSetters() {
        
        myTruck.setX(12);
        assertEquals("Truck setX failed!", 12, myTruck.getX());
        myTruck.setY(13);
        assertEquals("Truck setY failed!", 13, myTruck.getY());
        myTruck.setDirection(Direction.SOUTH);
        assertEquals("Truck setDirection failed!", Direction.SOUTH, myTruck.getDirection());
    }
    
    
    /** 
     * Test the canPass() method.
     */
    @Test
    public void testCanPass() {
        
        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.STREET);
        validTerrain.add(Terrain.LIGHT);
        validTerrain.add(Terrain.CROSSWALK);
                
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                
                if (destinationTerrain == Terrain.STREET) {
                
                    // trucks can pass STREET under any light condition
                    assertTrue("Truck should be able to pass STREET"
                               + ", with light " + currentLightCondition,
                               myTruck.canPass(destinationTerrain, currentLightCondition));
                } else if (destinationTerrain == Terrain.CROSSWALK) {
                    
                    // Trucks can pass CROSSWALK unless the light is RED
                    if (currentLightCondition == Light.RED) {
                        assertFalse("Truck should NOT be able to pass " + destinationTerrain
                            + ", with light " + currentLightCondition,
                            myTruck.canPass(destinationTerrain,
                                          currentLightCondition));
                     //light is yellow or green
                    } else if (currentLightCondition != Light.RED) {
                        
                        assertTrue("Trucks should be able to pass " + destinationTerrain
                            + ", with light " + currentLightCondition,
                            myTruck.canPass(destinationTerrain,
                                          currentLightCondition));
                    }
                
                } else if (!validTerrain.contains(destinationTerrain)) {
 
                    assertFalse("Truck should NOT be able to pass " + destinationTerrain
                        + ", with light " + currentLightCondition,
                        myTruck.canPass(destinationTerrain, currentLightCondition));
                }
            } 
        }
    }
    
    /**
     * Test the choose direction method surrounded with valid direction.
     */
    @Test
    public void testChooseDirectionSurroundedByValid() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.LIGHT);
        neighbors.put(Direction.EAST, Terrain.CROSSWALK);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        
        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;
        
        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = myTruck.chooseDirection(neighbors);
            
            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) { // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) { 
                seenSouth = true;
            }
        }
 
        assertTrue("Truck chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenWest && seenNorth && seenSouth);
            
        assertFalse("Truck chooseDirection() reversed direction when not necessary!",
                    seenEast);
    }
    
    /**
     * Test the choose direction method when the Truck has to reverse. 
     */
    @Test
    public void testChooseDirectionOnValidMustReverse() {
        
        for (final Terrain t : Terrain.values()) {
            if (t != Terrain.STREET && t != Terrain.CROSSWALK && t != Terrain.LIGHT) {
                
                final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
                neighbors.put(Direction.WEST, t);
                neighbors.put(Direction.NORTH, t);
                neighbors.put(Direction.EAST,  Terrain.STREET);
                neighbors.put(Direction.SOUTH, t);
                
                // the truck must reverse and go EAST
                assertEquals("Truck chooseDirection() failed "
                                + "when reverse was the only valid choice!",
                             Direction.EAST, myTruck.chooseDirection(neighbors));
            }
                
        }
    }

    /**
     * Test the rest method inherited from AbstractVehicle.
     */
    @Test 
    public void testReset() {
        
        myTruck.setX(12);
        myTruck.setY(13);
        myTruck.setDirection(Direction.SOUTH);
        
        myTruck.reset();
        
        assertEquals("Failed to reset the X!" , 5, myTruck.getX());
        assertEquals("Failed to reset the Y!" , 7, myTruck.getY());
        assertEquals("Failed to reset the X!" , Direction.WEST, myTruck.getDirection());
        assertTrue(myTruck.isAlive());
    }
    
    /**
     * Test collision behavior with self.
     */
    @Test 
    public void testCollideSelf() {
        
        final Truck other = new Truck(20, 30, Direction.WEST);
        myTruck.collide(other);
        assertTrue("My Truck should not die!", myTruck.isAlive());
        
        other.collide(myTruck);
        assertTrue("The other Truck should not die!", myTruck.isAlive());
    }
    
    /**
     * Test the getImageFileName method.
     */
    @Test 
    public void testGetImageFileName() {
        assertEquals("truck.gif", myTruck.getImageFileName());
        // Do not need to test other option because a truck cannot die.
        
    }
    
    /**
     * Test the toString method.
     */
    @Test 
    public void testToString() {
        assertEquals("Truck", myTruck.toString());
        // Do not need to test other option because a truck cannot die.
    }
}
    
