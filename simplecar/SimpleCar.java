package simplecar;

public class SimpleCar {
    
    protected static double MAX_STEERING_ANGLE = 2*Math.PI/7;
    protected static double MAX_SPEED = 200;
    protected static double MAX_REVERSE_SPEED = 100;
    
    protected double x;
    protected double y;
    protected double θ;
    protected double L;
    protected double φ;
    protected double u;

    public SimpleCar( double x, double y, double θ, double L, double φ, double u ) {
        this.x = x;
        this.y = y;
        this.θ = θ;
        this.L = L;
        this.φ = φ;
        this.u = u;
    }
    
    public void advance( double dt ) {
        double Δx = u * Math.cos(θ) * dt;
        double Δy = u * Math.sin(θ) * dt;
        double Δθ = (u/L) * Math.tan(φ) * dt;
        x = x + Δx;
        y = y + Δy;
        θ = θ + Δθ;
    }
    
    public void accelerate( double Δu ) {
        u = u + Δu;
        if (u > MAX_SPEED) {
            u = MAX_SPEED;
        } else if (u < -MAX_REVERSE_SPEED) {
            u = -MAX_REVERSE_SPEED;
        }
    }
    
    public void decelerate( double Δu ) {
        if (u > 0) {
            u = u - Δu;
            if (u < 0) {
                u = 0;
            }
        } else {
            u = u + Δu;
            if (u > 0) {
                u = 0;
            }
        }
    }
    
    public void friction( double dt ) {
        u = u * (1 - 0.5 * dt);
    }
    
    public void steeringFriction( double dt ) {
        φ = φ * (1 - 2 * dt * Math.abs(u) / MAX_SPEED);
    }
    
    public void steerLeft( double Δφ ) {
        φ = φ - Δφ;
        if (φ < -MAX_STEERING_ANGLE) {
            φ = -MAX_STEERING_ANGLE;
        }
    }
    
    public void steerRight( double Δφ ) {
        φ = φ + Δφ;
        if (φ > MAX_STEERING_ANGLE) {
            φ = MAX_STEERING_ANGLE;
        }
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getTheta() {
        return θ;
    }
    
    public double getWheelbase() {
        return L;
    }
    
    public double getPhi() {
        return φ;
    }
    
    public double getSpeed() {
        return u;
    }
}
