import java.awt.event.MouseEvent;
import java.awt.Graphics;

public abstract class State {
    public DessinPanel2 panel;

    State(DessinPanel2 dp) {
        this.panel = dp;
    }
    
    public abstract void paintComponent(Graphics g);
    public abstract void mouseDragged(MouseEvent event);

}
