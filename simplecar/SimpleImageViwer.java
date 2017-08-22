package simplecar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SimpleImageViwer extends JFrame {
    
    private ImageIcon imageIcon;
    
    public SimpleImageViwer() {
        imageIcon = new ImageIcon(this.getClass().getResource("/images/bluecar.png"));
        setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
        g2.drawLine(0,0,getWidth(),getHeight());
    }
    
    
    
    public static void main( String[] args ) {
        new SimpleImageViwer().setVisible(true);
                
    }
    
}
