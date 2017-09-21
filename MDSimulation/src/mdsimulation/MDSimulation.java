package MDSimulation;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import java.awt.Color;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Madison Fichtner
 * Class that deals with all particles while they are simulated on the canvas.
 * Can recieve a simple number as an argument (number of particles) and the program will
 * randomly generate the given number of particles with random x and y coordinates and velocities
 * 
 * Can receive a .txt file with the specific structure of:
 * 
 * numParticles
 * xCoord, yCoord, xVel, yVel, radius, mass, rValue, gValue, bValue 
 * ... with the above line structure repeated for each particle expected
 * xCoord, yCoord, xVel, yVel, radius, mass are Doubles; rValue, gValue, bValue are Integers
 */


public class MDSimulation 
{
    
    private PQ<Event> pq;
    private Particle[] particles;
    private double t = 0.0;
    private final static double HZ = .5;
    
    public static void main(String[] args) throws FileNotFoundException {
        // creates canvas for std draw to occur
        StdDraw.setCanvasSize(1500,1500);
        // allows the particles to be redrawn without lag
        StdDraw.enableDoubleBuffering();
        // create new array of particles
        Particle[] particles;
        
        //Algorithm for reading the input file
        //file reads as rx ry vx vy mass radius r g b
        if(args.length == 1)
        {
            int n = Integer.parseInt(args[0]);
            particles = new Particle[n];
            for(int i = 0; i < n; i++)
            {
                particles[i] = new Particle();
            }
        }    
        
        else //if the file is existent and not empty
        {
            int n = StdIn.readInt(); //first int in file is number of particles 
 
            particles = new Particle[n];
            for(int i = 0; i < n; i++)
            {
                //readDouble() reads next double using StdIn interface
                double rx = StdIn.readDouble(); // reads in X coord
                double ry = StdIn.readDouble(); // reads in Y coord
                double vx = StdIn.readDouble(); // reads in X velocity
                double vy = StdIn.readDouble(); // reads in Y velocity
                double radius = StdIn.readDouble(); // reads in radius
                double mass = StdIn.readDouble();  // reads in mass
                int r = StdIn.readInt(); // reads in R value of color
                int g = StdIn.readInt(); // reads in G value of color
                int b = StdIn.readInt(); // reads in B value of color
    
                Color color = new Color(r, g, b); // creates color from given r,g,b values
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color); // creates particle given above variables
            }
        }
        
        MDSimulation system = new MDSimulation(particles); // create new simulation given the array of particles created
        system.simulate(10000); 
    }
    
    public MDSimulation(Particle[] particles)
    {
        this.particles = particles;
    }
    
    //predicts when particle a makes a collision and adds it to the PQ
    private void predict(Particle a, double time)
    {
        if (a == null) 
            return;
        
        //particle-particle collision, add to queue
        for (int i = 0; i < particles.length; i++)
        {
            double dt = a.timeToHit(particles[i]);
            if(t + dt <= time)
            {
                    pq.insert(new Event(t + dt, a, particles[i]));
            }
        }
        
        //particle-wall collision add to queue
        double X = a.collidesY();
        double Y = a.collidesX();
        //if time + times to hit vertical/horizontal wall <= total time, insert event into queue
        if(t + X <= time)
        {
            //insert into queue as an event(time, particle a, particle b)
            pq.insert(new Event(t + X, a, null));
        }
        if(t + Y <= time)
        {
            //insert into queue as an event(time, particle a, particle b)
            pq.insert(new Event(t + Y, null, a));
        }
    }
    
    //redraw particles
    private void redraw(double time)
    {
        StdDraw.clear();
        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(20);
        if (t < time) {
            pq.insert(new Event(t + 1.0 / HZ, null, null));
        }
    }
    
    //simulates the system of particles
    public void simulate(double time)
    {
        //instantiates the priority queue
        pq = new PQ<Event>();
        for(int i = 0; i < particles.length; i++)
        {
            predict(particles[i], time);
        }
        pq.insert(new Event(0, null, null));
        
        while(!pq.isEmpty())
        {
            Event E = pq.delMin();
            if (!E.isValid())
                continue;
            Particle a = E.a;
            Particle b = E.b;
            
            for(int i = 0; i < particles.length; i++)
                particles[i].move(E.time - t);
            t = E.time;
            
            if(a != null && b != null)
                a.bounce(b);
            else if(a != null && b == null)
                a.bounceY();
            else if(a == null && b != null)
                b.bounceX();
            else if(a == null && b == null)
                redraw(time);
        predict(a, time);
        predict(b, time);
        }
    }
    
    // Event class that keeps track of events in the priority queue, the collisions between
    // walls and particles, and particles and particles
    private static class Event implements Comparable<Event>
    {
        private final double time;
        private final Particle a, b;
        private final int countA, countB;
        
        public Event(double t, Particle a, Particle b)
        {
            this.time = t;
            this.a = a;
            this.b = b;
            if(a != null)
                countA = a.count();
            else
                countA = -1;
            if(b != null)
                countB = b.count();
            else
                countB = -1;
        }
        
        public int compareTo(Event b)
        {
            return Double.compare(this.time, b.time);
        }
        
        public boolean isValid()
        {
            if(a != null && a.count() != countA) 
                return false;
            if(b != null && b.count() != countB)
                return false;
            return true;
        }
    }
}
