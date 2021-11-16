import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;

public class AucuneSelectionState extends State {
    DessinPanel2 dp;

    AucuneSelectionState(DessinPanel2 dp) {
        super(dp);
    }

	@Override
	public void paintComponent(Graphics g) {
		// Nothing
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

			// attrape l'erreur en cas ou deux object entre dans le tableau
			// DONC "si apres une selection on selectionne lautre figure"
			try {

				for (FormGeo f : dp.selectedFormesGeo) {
					f.moveBy(dx, dy);
					if (dp.selectedFormesGeo.size() >= 0) {
						dp.selectedFormesGeo.clear();							
					}
				}
				/**
				 * @exception ConcurrentModificationException
				 *                Cette exception peut être levée par les
				 *                méthodes qui ont détecté une modification
				 *                concurrente d'un objet lorsque cette
				 *                modification n'est pas autorisée.
				 */
			} catch (ConcurrentModificationException e) {
				//Toutselec = 0;
				dp.changeState(dp.initialState);
			}
			dp.lastPointPress = p;
		}
		dp.repaint();
	}
    
}
