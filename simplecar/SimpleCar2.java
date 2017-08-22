package simplecar;

public class SimpleCar2 extends SimpleCar {
    
    private double μ;
    
    public SimpleCar2( double x, double y, double θ, double L, double μ, double φ, double u ) {
        super(x, y, θ, L, φ, u);
        this.μ = μ;
    }
    
    public SimpleCar2 clone() {
        return new SimpleCar2(x, y, θ, L, μ, φ, u);
    }
    
    public void reset( double x, double y ) {
        this.x = x;
        this.y = y;
        this.u = 0;
    }
    
    public double getTrack() {
        return μ;
    }
    
    public double getLeftPhi() {
        return Math.atan(L / (L / Math.tan(φ) + μ/2));
    }
    
    public double getRightPhi() {
        return Math.atan(L / (L / Math.tan(φ) - μ/2));
    }
}
