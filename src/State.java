import java.awt.event.MouseEvent;
import java.awt.Graphics;

public abstract class State {
    protected DessinPanel2 dp;

    State(DessinPanel2 dp) {
        this.dp = dp;
    }
    
    public abstract void paintComponent(Graphics g);
    public abstract void mouseDragged(MouseEvent event);

}
