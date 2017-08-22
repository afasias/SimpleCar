package simplecar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JApplet;

public class CarSimulatorApplet extends JApplet implements KeyListener {

    private SimpleCar2 car;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean endPressed = false;
    private ImageIcon bgIcon;
    private ImageIcon carIcon;
    private int terainOpacity = 0x77;

    public void init() {
        int width = getWidth();
        int height = getHeight();
//        setPreferredSize(new Dimension(width, height));
//        setFocusable(true);

        car = new SimpleCar2(width / 2, height / 2, -Math.PI / 8, 50, 40, Math.PI / 12, 0);
        bgIcon = new ImageIcon(this.getClass().getResource("/images/g3082.png"));
        carIcon = new ImageIcon(this.getClass().getResource("/images/bluecar.png"));

        new Thread() {
            @Override
            public void run() {
                double acceleration = 100; // pixels/sec/sec
                double deceleration = 300; // pixels/sec/sec
                double steering = Math.PI / 4; // radians/sec
                double dt = 0.03;
                while (true) {
                    car.advance(dt);
                    if (endPressed) {
                        car.decelerate(deceleration * dt);
                    } else if (upPressed && !downPressed) {
                        if (car.getSpeed() < 0) {
                            car.decelerate(deceleration * dt);
                        } else {
                            car.accelerate(acceleration * dt);
                        }
                    } else if (downPressed && !upPressed) {
                        if (car.getSpeed() > 0) {
                            car.decelerate(deceleration * dt);
                        } else {
                            car.accelerate(-acceleration * dt);
                        }
                    } else {
                        car.friction(dt);
                    }
                    if (leftPressed && !rightPressed) {
                        car.steerLeft(steering * dt);
                    } else if (rightPressed && !leftPressed) {
                        car.steerRight(steering * dt);
                    } else if (!leftPressed && !rightPressed) {
                        car.steeringFriction(dt);
                    }
                    repaint();
                    try {
                        Thread.sleep((int) (dt * 1000));
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }.start();

        addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawString("hese me",100,100);
        if (true) return;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
        g2.setColor(new Color(0xff, 0xff, 0xff, 0xff - terainOpacity));
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawCar(g2);
    }

    private void drawCar(Graphics2D g2) {

        g2.setColor(Color.BLACK);
        g2.setBackground(Color.DARK_GRAY);

        double carLength = 2 * car.getWheelbase();
        double rearAxisFactor = (carLength - car.getWheelbase()) / 2 / carLength;
        double carWidth = car.getWheelbase();

        double wheelDiameter = 0.5 * car.getTrack();
        double wheelWidth = 0.35 * wheelDiameter;

        double rearLeftWheelX = car.getX() + car.getTrack() / 2 * Math.cos(car.getTheta() - Math.PI / 2);
        double rearLeftWheelY = car.getY() + car.getTrack() / 2 * Math.sin(car.getTheta() - Math.PI / 2);
        drawRectangle(g2, rearLeftWheelX, rearLeftWheelY, car.getTheta(), wheelDiameter, wheelWidth, 0.5);

        double rearRightWheelX = car.getX() + car.getTrack() / 2 * Math.cos(car.getTheta() + Math.PI / 2);
        double rearRightWheelY = car.getY() + car.getTrack() / 2 * Math.sin(car.getTheta() + Math.PI / 2);
        drawRectangle(g2, rearRightWheelX, rearRightWheelY, car.getTheta(), wheelDiameter, wheelWidth, 0.5);

        double frontAxisX = car.getX() + car.getWheelbase() * Math.cos(car.getTheta());
        double frontAxisY = car.getY() + car.getWheelbase() * Math.sin(car.getTheta());

        double frontLeftWheelX = frontAxisX + car.getTrack() / 2 * Math.cos(car.getTheta() - Math.PI / 2);
        double frontLeftWheelY = frontAxisY + car.getTrack() / 2 * Math.sin(car.getTheta() - Math.PI / 2);
        drawRectangle(g2, frontLeftWheelX, frontLeftWheelY, car.getTheta() + car.getLeftPhi(), wheelDiameter, wheelWidth, 0.5);

        double frontRightWheelX = frontAxisX + car.getTrack() / 2 * Math.cos(car.getTheta() + Math.PI / 2);
        double frontRightWheelY = frontAxisY + car.getTrack() / 2 * Math.sin(car.getTheta() + Math.PI / 2);
        drawRectangle(g2, frontRightWheelX, frontRightWheelY, car.getTheta() + car.getRightPhi(), wheelDiameter, wheelWidth, 0.5);

//        // white-box car
//        g2.setBackground(Color.WHITE);
//        drawRectangle(g2, car.getX(), car.getY(), car.getTheta(), carLength, carWidth, rearAxisFactor);

        Polygon p = getPolygonFromRectangle(car.getX(), car.getY(), car.getTheta(), carLength, carWidth, rearAxisFactor);
        AffineTransform transform = new AffineTransform();
        transform.translate(p.xpoints[0],p.ypoints[0]);
        transform.scale((double)carLength/carIcon.getIconWidth(),(double)carWidth/carIcon.getIconHeight());
        transform.rotate(car.getTheta());
        g2.drawImage(carIcon.getImage(), transform, null);


//        // Arrow
//        g2.drawLine((int) Math.round(car.getX()), (int) (Math.round(car.getY())), (int) Math.round(frontAxisX), (int) (Math.round(frontAxisY)));
//        g2.drawLine((int) Math.round(frontAxisX + 0.2 * car.getWheelbase() * Math.cos(car.getTheta() - 3 * Math.PI / 4)),
//                (int) Math.round(frontAxisY + 0.2 * car.getWheelbase() * Math.sin(car.getTheta() - 3 * Math.PI / 4)),
//                (int) Math.round(frontAxisX), (int) (Math.round(frontAxisY)));
//        g2.drawLine((int) Math.round(frontAxisX + 0.2 * car.getWheelbase() * Math.cos(car.getTheta() + 3 * Math.PI / 4)),
//                (int) Math.round(frontAxisY + 0.2 * car.getWheelbase() * Math.sin(car.getTheta() + 3 * Math.PI / 4)),
//                (int) Math.round(frontAxisX), (int) (Math.round(frontAxisY)));
    }

    private Polygon getPolygonFromRectangle(double x, double y, double theta, double length, double width, double factor) {
        Polygon p = new Polygon();

        double bx = x - length * factor * Math.cos(theta);
        double by = y - length * factor * Math.sin(theta);

        double fx = x + length * (1.0 - factor) * Math.cos(theta);
        double fy = y + length * (1.0 - factor) * Math.sin(theta);

        int x1 = (int) Math.round(bx + width / 2 * Math.cos(theta - Math.PI / 2));
        int y1 = (int) Math.round(by + width / 2 * Math.sin(theta - Math.PI / 2));
        p.addPoint(x1, y1);

        int x2 = (int) Math.round(bx + width / 2 * Math.cos(theta + Math.PI / 2));
        int y2 = (int) Math.round(by + width / 2 * Math.sin(theta + Math.PI / 2));
        p.addPoint(x2, y2);

        int x3 = (int) Math.round(fx + width / 2 * Math.cos(theta + Math.PI / 2));
        int y3 = (int) Math.round(fy + width / 2 * Math.sin(theta + Math.PI / 2));
        p.addPoint(x3, y3);

        int x4 = (int) Math.round(fx + width / 2 * Math.cos(theta - Math.PI / 2));
        int y4 = (int) Math.round(fy + width / 2 * Math.sin(theta - Math.PI / 2));
        p.addPoint(x4, y4);
        
        return p;
    }
    
    private void drawRectangle(Graphics2D g2, double x, double y, double theta, double length, double width, double factor) {
        Polygon p = getPolygonFromRectangle(x, y, theta, length, width, factor);
        Color oldColor = g2.getColor();
        g2.setColor(g2.getBackground());
        g2.fillPolygon(p);
        g2.setColor(oldColor);
        g2.drawPolygon(p);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 27:
                System.exit(0);
                break;
            case 38:
                upPressed = true;
                break;
            case 40:
                downPressed = true;
                break;
            case 37:
                leftPressed = true;
                break;
            case 39:
                rightPressed = true;
                break;
            case 35:
                endPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 38:
                upPressed = false;
                break;
            case 40:
                downPressed = false;
                break;
            case 37:
                leftPressed = false;
                break;
            case 39:
                rightPressed = false;
                break;
            case 35:
                endPressed = false;
                break;
            case 82:
                car.reset(getWidth() / 2, getHeight() / 2);
                break;
            case 45:
                if (terainOpacity > 0) {
                    terainOpacity = terainOpacity - 0x11;
                    repaint();
                }
                break;
            case 61:
                if (terainOpacity < 0xff) {
                    terainOpacity = terainOpacity + 0x11;
                    repaint();
                }
                break;
        }
//        System.out.println("Pressed: "+e.getKeyCode());
    }

//    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        frame.setTitle("Simple Car Simulator");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new CarSimulator2());
//        frame.pack();
//        frame.setVisible(true);
//    }
}
