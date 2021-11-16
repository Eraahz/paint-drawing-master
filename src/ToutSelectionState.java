import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;

public class ToutSelectionState extends State {

    ToutSelectionState(DessinPanel2 dp) {
        super(dp);
    }

    @Override
    public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (FormGeo f : dp.formesGeo) {
			f.dessine(g2);
			if (dp.courant != null) {
				dp.lightSquares(g2, dp.courant);
				dp.repaint();
			}
			for (FormGeo selec : dp.selectedFormesGeo) {
				dp.lightSquares(g2, selec);
				dp.repaint();
			}
		}
	}
    
    @Override
	public void mouseDragged(MouseEvent event) {
		Point p = event.getPoint();
		if (dp.courant == null) {
			if (dp.lastFormGeo == null) {
				dp.lastFormGeo = new FormGeo(dp.typeDeForme);
				dp.lastFormGeo.setCouleur(FormGeo.getCouleurCourante());
				dp.add(dp.lastFormGeo);
			}
			dp.lastFormGeo.setFrameFromDiagonal(dp.lastPointPress, p);
		} else {
			double dx = p.getX() - dp.lastPointPress.getX();
			double dy = p.getY() - dp.lastPointPress.getY();

			if (!dp.selectedFormesGeo.contains(dp.courant)) {
				dp.courant.moveBy(dx, dy);

			}

			try {

				for (FormGeo f : dp.selectedFormesGeo) {
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
				dp.changeState(dp.initialState);
			}
			dp.lastPointPress = p;
		}
		dp.repaint();
	}
    
}
