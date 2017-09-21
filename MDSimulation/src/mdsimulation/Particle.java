package MDSimulation;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import edu.princeton.cs.algs4.StdRandom;

/**
 *
 * @authors Madison Fichtner
 * Class that sets up the particle with all the physics for the rest of the program.
 */

public class Particle {
    private double rx, ry;        // position
    private double vx, vy;        // velocity
    private int count;            // number of collisions so far
    private final double radius;  // radius
    private final double mass;    // mass
    private final Color color;    // color
    private double INFINITY = Double.POSITIVE_INFINITY; // variable to represent time when a particle never makes a collision
    
    // Initializes a particle at a given coordinate and with a given velocity
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color)
    {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }
    
    // Initializes a particle at a random coordinate with a random velocity
    public Particle()
    {
        rx = StdRandom.uniform(0.0, 1.0); //StdRandom.uniform generates a random real number between (x,y)
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-.01, .01); //setting the velocity to negative will move the particle to the left
        vy = StdRandom.uniform(-.01, .01);
        radius = .005;
        mass = 1;
        color = Color.BLACK;
    }
    
    public int count()
    {
        return count;
    }
    // Returns time until the particle collides with vertical wall, and if it doesn't hit a wall, return a negative number
    public double collidesY()
    {
        if(vx > 0)
            return (1.0 - rx - radius) / vx;
        else if (vx < 0)
            return (radius - rx) / vx;
        else
            return INFINITY;
    }
    
    // Returns time until the particle collides with a horizontal wall, and if it doesn't hit a wall, return a negative number
    public double collidesX()
    {
        if(vy > 0)
            return (1.0 - ry - radius) / vy;
        else if(vy < 0) 
            return (radius - ry) / vy;
        else
            return INFINITY;
    }
    
    // Update the invoking particle to simulate it bouncing off vertical wall, and updates the number of collisions with count
    public void bounceY()
    {
        vx = -vx;
        count++;
    }
    
    // Update the invoking particle to simualte it bouncing off horizontal wall, and updates the number of collisions with count
    public void bounceX()
    {
        vy = -vy;
        count++;
    }
    
    // Update both particles to simulate them bouncing off each other
    public void bounce(Particle b)
    {
        double dx = b.rx - this.rx;     // Calculate the change in x
        double dy = b.ry - this.ry;     // Calculate the change in y
        double dvx = b.vx - this.vx;    // Calculate the change in velocity of X
        double dvy = b.vy - this.vy;    // Calculate the change in volocity of Y
        double dvdr = dx * dvx + dy * dvy;     // Calculate change in velocity times change in location
        double distance = this.radius + b.radius; // Distance between particle centers
        
        // Calculate the force required to calculate post collision velocities
        double force = 2 * this.mass * b.mass * dvdr / ((this.mass +  b.mass) * distance);
        double forceX = force * dx / distance;
        double forceY = force * dy / distance;
        
        // Update velocities of particles using force
        this.vx += forceX / this.mass;
        this.vy += forceY / this.mass;
        b.vx -= forceX / b.mass;
        b.vy -= forceY / b.mass;
        
        this.count++;
        b.count++;
    }
    
    // Calculates the time for a collision between particles
    public double timeToHit(Particle b)
    {
        if(this == b)
            return INFINITY;
        double dx  = b.rx - this.rx;
        double dy  = b.ry - this.ry;
        double dvx = b.vx - this.vx;
        double dvy = b.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        if (dvdr > 0)
            return INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double angle = this.radius + b.radius;
        double d = (dvdr*dvdr) - dvdv * (drdr - angle*angle);
        if (d < 0) 
            return INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }
    
    // Draws the dots on the canvas with their respected sizes
    public void draw()
    {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }

    // move based on dt(change in time)
    public void move(double dt)
    {
        rx += vx * dt;
        ry += vy * dt;
    }
}
