package simplecar;

public class SimpleCar3 extends SimpleCar2 {
    
    private Trailer trailer;

    public SimpleCar3(double x, double y, double θ, double L, double μ, double φ, double u) {
        super(x, y, θ, L, μ, φ, u);
    }
    
    public void attachTrailer( Trailer trailer ) {
        this.trailer = trailer;
        trailer.moveTo(x,y);
    }
    
    public Trailer getTrailer() {
        return trailer;
    }

    @Override
    public void advance(double dt) {
        super.advance(dt);
        if (trailer != null) {
            trailer.moveTo(x,y);
        }
    }
    
}
