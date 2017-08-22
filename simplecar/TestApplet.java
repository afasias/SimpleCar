package simplecar;

import java.awt.*;
import javax.swing.*;

public class TestApplet extends JApplet {

    public void init() {
        // TODO start asynchronous download of heavy resources
//        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Geia sou!",100,100);
    }

}
