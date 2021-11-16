import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;

public class ToutSelectionState extends State {

    ToutSelectionState(DessinPanel2 panel) {
        super(panel);
    }

    @Override
    public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (FormGeo f : panel.formesGeo) {
			f.dessine(g2);
			if (panel.courant != null) {
				panel.lightSquares(g2, panel.courant);
				panel.repaint();
			}
			for (FormGeo selec : panel.selectedFormesGeo) {
				panel.lightSquares(g2, selec);
				panel.repaint();
			}
		}
	}
    
    @Override
	public void mouseDragged(MouseEvent event) {
		Point p = event.getPoint();
		if (panel.courant == null) {
			if (panel.lastFormGeo == null) {
				panel.lastFormGeo = new FormGeo(panel.typeDeForme);
				panel.lastFormGeo.setCouleur(FormGeo.getCouleurCourante());
				panel.add(panel.lastFormGeo);
			}
			panel.lastFormGeo.setFrameFromDiagonal(panel.lastPointPress, p);
		} else {
			double dx = p.getX() - panel.lastPointPress.getX();
			double dy = p.getY() - panel.lastPointPress.getY();

			if (!panel.selectedFormesGeo.contains(panel.courant)) {
				panel.courant.moveBy(dx, dy);

			}

			try {

				for (FormGeo f : panel.selectedFormesGeo) {
					f.moveBy(dx, dy);
				}
				/**
				 * @exception ConcurrentModificationException
				 *                Cette exception peut être levée par les
				 *                méthodes qui ont détecté une modification
				 *                concurrente d'un objet lorsque cette
				 *                modification n'est pas autorisée.
				 */
			} catch (ConcurrentModificationException e) {
				panel.changeState(panel.initialState);
			}
			panel.lastPointPress = p;
		}
		panel.repaint();
	}
    
}
