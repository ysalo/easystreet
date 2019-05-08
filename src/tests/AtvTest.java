package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Atv;
import model.Direction;
import model.Human;
import model.Light;
import model.Terrain;
import model.Truck;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Unit tests for class Atv.
 * 
 * @author Yaro Salo
 * @version October 28 2016
 */

public class AtvTest {
    
    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;
    
    /** 
     * An instance of a Atv initialized before every test.
     */
    private Atv myAtv;
    
    /**
     * A method to initialize the test fixtures before each test.
     */
    @Before 
    public void setUp() {
        myAtv = new Atv(20, 30, Direction.WEST);
    }
    
    /** Test the Truck constructor. */
    @Test
    public void testAtvConstructor() {
        
        final Atv atv = new Atv(20, 30, Direction.WEST);
        
        assertEquals("Atv x coordinate not initialized correctly!", 20, atv.getX());
        assertEquals("Atv y coordinate not initialized correctly!", 30, atv.getY());
        assertEquals("Atv direction not initialized correctly!",
                     Direction.WEST, atv.getDirection());
        assertEquals("Atv death time not initialized correctly!", 20, atv.getDeathTime());
        assertTrue("Atv isAlive() fails initially!", atv.isAlive());
    }
    
    /** Test the Atv setters. */
    @Test
    public void testAtvkSetters() {
        
        myAtv.setX(12);
        assertEquals("Atv setX failed!", 12, myAtv.getX());
        myAtv.setY(13);
        assertEquals("Atv setY failed!", 13, myAtv.getY());
        myAtv.setDirection(Direction.SOUTH);
        assertEquals("Atv setDirection failed!", Direction.SOUTH, myAtv.getDirection());
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
        validTerrain.add(Terrain.GRASS);
        validTerrain.add(Terrain.TRAIL);
                
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                
                if (destinationTerrain == Terrain.STREET) {
                
                    // atv's can pass STREET under any light condition
                    assertTrue("Atv should be able to pass STREET"
                               + ", with light " + currentLightCondition,
                               myAtv.canPass(destinationTerrain, currentLightCondition));
                } else if (destinationTerrain == Terrain.LIGHT) {
                    
                    // atv's can pass LIGHT under any light condition
                    assertTrue("Atv should be able to pass LIGHT"
                               + ", with light " + currentLightCondition,
                               myAtv.canPass(destinationTerrain, currentLightCondition));
                    
                } else if (destinationTerrain == Terrain.CROSSWALK) { 
                        
                    // atv's can pass CROSSWALK under any light condition
                    assertTrue("Atv should be able to pass CROSSWALK"
                                   + ", with light " + currentLightCondition,
                                   myAtv.canPass(destinationTerrain, currentLightCondition));
                } else if (destinationTerrain == Terrain.GRASS) {
                    
                    // atv's can pass GRASS under any light condition
                    assertTrue("Atv should be able to pass GRASS"
                                   + ", with light " + currentLightCondition,
                                   myAtv.canPass(destinationTerrain, currentLightCondition));

                } else if (destinationTerrain == Terrain.TRAIL) {
                    
                    // atv's can pass TRAIL under any light condition
                    assertTrue("Atv should be able to pass TRAIL"
                                   + ", with light " + currentLightCondition,
                                   myAtv.canPass(destinationTerrain, currentLightCondition));
                } else {
                    // atv's can't drive on walls
                    assertFalse("Atv should NOT be able to pass " + destinationTerrain
                        + ", with light " + currentLightCondition,
                        myAtv.canPass(destinationTerrain, currentLightCondition));
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
            final Direction d = myAtv.chooseDirection(neighbors);
            
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
 
        assertTrue("Atv chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenWest && seenNorth && seenSouth);
            
        assertFalse("Atv chooseDirection() reversed direction when not necessary!",
                    seenEast);
    }
   
    /**
     * Test the rest method inherited from AbstractVehicle.
     */
    @Test 
    public void testReset() {
        
        final Truck truck = new Truck(20, 30, Direction.WEST);
        
        myAtv.setX(12);
        myAtv.setY(13);
        myAtv.setDirection(Direction.SOUTH);
        myAtv.collide(truck);
        
        myAtv.reset();
        
        assertEquals("Failed to reset the X!" , 20, myAtv.getX());
        assertEquals("Failed to reset the Y!" , 30, myAtv.getY());
        assertEquals("Failed to reset the X!" , Direction.WEST, myAtv.getDirection());
        assertTrue("Failed to revive" , myAtv.isAlive());  
    }
    
    /**
     * Test collision behavior with self.
     */
    @Test 
    public void testCollideSelf() {
        
        final Atv other = new Atv(20, 30, Direction.WEST);
        myAtv.collide(other);
        assertTrue("My Truck should not die!", myAtv.isAlive());
        
        other.collide(myAtv);
        assertTrue("The other Atv should not die!", myAtv.isAlive());
    }
    
    /**
     * Test collision behavior with a stronger vehicle.
     */
    @Test 
    public void testCollideStronger() {
    
        final Truck truck = new Truck(20, 30, Direction.WEST);
        myAtv.collide(truck);
        assertFalse("My Atv should  die!", myAtv.isAlive());
        
        truck.collide(myAtv);
        assertTrue("The Truck should not die!", truck.isAlive());
        
    }
    
    /**
     * Test collision behavior with a weaker vehicle.
     */
    @Test 
    public void testCollideWeaker() {
    
        final Human human = new Human(20, 30, Direction.WEST);
        myAtv.collide(human);
        assertTrue("My Atv should not die!", myAtv.isAlive());
        
        human.collide(myAtv);
        assertFalse("The Human should die!", human.isAlive());
        
    }
    
    /** 
     * Test getImageFile when the vehicle is alive.
     */
    @Test 
    public void testGetImageFileAlive() {
        assertEquals("atv.gif", myAtv.getImageFileName());
    }
    
    /** 
     * Test getImageFile when the vehicle is dead.
     */
    @Test 
    public void testGetImageFileDead() {
        final Truck truck = new Truck(10, 20, Direction.WEST);
        myAtv.collide(truck);
        
        assertEquals("atv_dead.gif", myAtv.getImageFileName());
    }
    
    /** 
     * Test toString when the vehicle is alive.
     */
    @Test 
    public void testToStringAlive() {
        assertEquals("Atv", myAtv.toString());
    }
    
    /** 
     * Test toString when the vehicle is dead.
     */
    @Test 
    public void testToStringDead() {
        final Truck truck = new Truck(10, 20, Direction.WEST);
        myAtv.collide(truck);
        myAtv.poke();
        myAtv.poke();
        
        assertEquals("pokes: 2", myAtv.toString());
    }
    
}
