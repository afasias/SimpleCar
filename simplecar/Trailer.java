package simplecar;

public class Trailer {
    
    protected double x;
    protected double y;
    protected double θ;
    protected double L;
    
    public Trailer( double L ) {
        this.L = L;
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
    
    public double getAxleLength() {
        return L;
    }
    
    public void moveTo( double x, double y ) {
        double x0 = this.x - L * Math.cos(θ);
        double y0 = this.y - L * Math.sin(θ);
        double f1 = Math.atan2(this.y - y0,this.x - x0);
        double f2 = Math.atan2(y - y0,x - x0);
        double Δθ = f2 - f1;
        θ = θ + Δθ;
        this.x = x;
        this.y = y;
    }
    
}
