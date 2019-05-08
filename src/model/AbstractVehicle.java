/*
 * TCSS 305 - Autumn 2016
 * Assignment 3 - Easy Street
 */

package model;


/**
 * Represents default behavior for the Vehicle subclasses.
 * 
 * @author Yaro Salo
 * @version October 22 2016
 */
public abstract class AbstractVehicle implements Vehicle {
    
    /** The x-coordinate of this vehicle. */
    private int myX;
    
    /** The y-coordinate of this vehicle. */
    private int myY;
    
    /** The direction of this vehicle. */
    private Direction myDirection;
    
    /** The number of updates between this vehicles death and when it should revive. */
    private final int myDeathTime;
    
    /** Store the state of the vehicle alive or dead. */
    private boolean myIsAlive;
    
    /** The x-coordinate of this vehicle at initialization. */
    private final int myInitialX;
    
    /** The y-coordinate of this vehicle at initialization. */
    private final int myInitialY;
    
    /** The direction of this vehicle at initialization. */
    private final Direction myIntialDirection;
    
    /** The number of turns that have passed since this vehicles "death". */
    private int myPokes;
    

    
    /**
     * Initialize the instance fields.
     * 
     * @param theX is the x-coordinate of this vehicle.
     * @param theY is the y-coordinate of this vehicle.
     * @param theDirection is the direction of this vehicle.
     * @param theDeathTime is the number of turns this vehicle should stay dead for. 
     */
    protected AbstractVehicle(final int theX, final int theY, final Direction theDirection, 
                           final int theDeathTime) {
        
        super();  

        //Store the initial values of this vehicle to use when it needs to reset.
        myInitialX = theX; 
        myInitialY = theY;
        myIntialDirection = theDirection;
        
        myX = theX;
        myY = theY;
        myDirection = theDirection;
        myDeathTime = theDeathTime;
        
        // All vehicles are alive at the start.
        myIsAlive = true;
        myPokes = 0;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void collide(final Vehicle theOther) {
        // Compare the death times to decide if this vehicle 
        // "dies" in the collision. Lower death time "kills" higher death time.
        if ((this.isAlive() && theOther.isAlive()) 
                        && (this.myDeathTime > theOther.getDeathTime())) {
            this.myIsAlive = false;   
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getImageFileName() {      
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName().toLowerCase());
        if(!this.isAlive()) sb.append("_dead");
        sb.append(".gif");
        return sb.toString();
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathTime() {
        return myDeathTime;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Direction getDirection() {
        return myDirection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {  
        return myX;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return myY;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return myIsAlive;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void poke() {
        myPokes += 1;
        //time to revive.
        if (myPokes == myDeathTime) {
            myPokes = 0;
            myIsAlive = true;
            this.setDirection(Direction.random());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        setX(myInitialX);
        setY(myInitialY);
        setDirection(myIntialDirection);
        myIsAlive = true;
        myPokes = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setDirection(final Direction theDirection) {
        myDirection = theDirection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setX(final int theX) {
        myX = theX;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setY(final int theY) {
        myY = theY;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (myIsAlive) {
            sb.append(getClass().getSimpleName());
        } else {
            sb.append("pokes: ");
            sb.append(myPokes);
        }
        return sb.toString();  
    }
}
