/****************************************************************************
 * @author Josaphat Mayuba Ndele	et Andres Garcia Cotton					*					*
 * Les programmes permet a faire de dessin de forme rectanglulaire et 		*
 * les ellipses	on peut l'enregistre et ouvrir le meme fichier.				*											*
 * 																			*
 ****************************************************************************/
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.naming.Context;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * @param DessinPanel2
 *            tableau contenant les coordonner de dessin a selectionner
 * @param formesGeo
 *            Tableau contenant les formes geometriques
 * @param courant
 *            lorsqu'un figure est selectionner
 * @param lastPointPress
 *            le point precedent
 * @param Toutselec
 *            une facon pour faire la verification en cas ou on a selectionner
 *            ou nom voir mouseDragged
 * @param Toutselec
 *            sert a montre si tout est selectionner une methode cree pour
 *            verifier
 * 
 */
public class DessinPanel2 extends JPanel {

	public ArrayList<FormGeo> formesGeo;
	public ArrayList<FormGeo> selectedFormesGeo;
	public FormGeo courant;
	public Point2D lastPointPress;
	public FormGeo lastFormGeo = null;
	public String lastFichier = ".";
	//private int Toutselec;
	public Color couleur;
	public int TAILLECARREE = 30;
	public FormGeo.Type typeDeForme = FormGeo.Type.RECT;
	public Object object;
	public int touche;
	public State state;
	
	public State initialState;

	public DessinPanel2() {
		this.initialState = new AucuneSelectionState(this);
		changeState(initialState);
		formesGeo = new ArrayList<FormGeo>();
		selectedFormesGeo = new ArrayList<FormGeo>();
		courant = null;
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
	}
	
	public void changeState(State state) {
		this.state = state;;
	}

	// NON UTILISER POUR NOTRE PROGRAMME
	public void setFichier(String n) {
		lastFichier = n;
	}

	public String getFichier() {
		return lastFichier;
	}

	// EFFACE TOUT
	public void clearAll() {

		clearSelected();
		formesGeo.clear();
		paintComponent(getGraphics());
	}

	// EFFACE LA FIGURE SELECTIONNER
	public void clearSelected() {

		for (FormGeo f : selectedFormesGeo) {
			f.setSelected(false);
		}
		selectedFormesGeo.clear();
		repaint();
	}

	// EFFACE LA FIGURE CONTENU DANS LE TABLEAU
	/** selectedFormesGeo */
	public void deleteSelected() {

		for (FormGeo f : selectedFormesGeo) {
			remove(f);
		}
		selectedFormesGeo.clear();
		repaint();
	}

	public void selectTout() {
		ToutSelectionState toutSelectionState = new ToutSelectionState(this);
		changeState(toutSelectionState);
		selectedFormesGeo.clear();
		selectedFormesGeo.addAll(formesGeo);
		for (FormGeo f : selectedFormesGeo) {
			f.setSelected(true);
		}
		repaint();
	}

	public void coloreSelected() {
		Color couleur = FormGeo.getCouleurCourante();
		for (FormGeo f : selectedFormesGeo) {
			f.setCouleur(couleur);
		}
		paintComponent(getGraphics());
	}

	public void setCouleur(Color c) {
		FormGeo.setCouleurCourante(c);
	}

	public void setTypeDessin(FormGeo.Type f) {
		typeDeForme = f;
	}

	public FormGeo find(Point2D p) {

		for (FormGeo f : formesGeo) {
			if (f.contains(p)) {
				return f;
			}
		}
		return null;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		state.paintComponent(g);
	}
	

	/**
	 * @param lightSquares
	 *            cree le point carre pour unE FIGURE selectionner
	 * @param g2D
	 * @param f
	 */
	public void lightSquares(Graphics2D g2D, FormGeo f) {
		double x = f.getX();
		double y = f.getY();
		double w = f.getWidth();
		double h = f.getHeight();
		g2D.setColor(Color.BLACK);

		g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y - 3.0, 6.0, 6.0));

		g2D.fill(new Rectangle.Double(x - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));
		g2D.fill(new Rectangle.Double(x + w - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));

		g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y + h - 3.0, 6.0, 6.0));

	}

	// AJOUTE LA FORME DANS LE TABLEAU FORMEGEO
	public void add(FormGeo f) {
		formesGeo.add(f);
	}

	public void remove(FormGeo f) {
		// ton code
		formesGeo.remove(f);
	}

	// LORS LA SOURIS EST PRESSER

	private class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent event) {

			Point p = event.getPoint();
			courant = find(p);
			lastPointPress = p;
			lastFormGeo = null;

		}

		// LORS DE CLIQUE DE LA SOURIS
		public void mouseClicked(MouseEvent event) {
			changeState(initialState);
			Point p = event.getPoint();
			double x = p.getX();
			double y = p.getY();
			if (courant == null && lastFormGeo == null) {
				FormGeo f = new FormGeo(typeDeForme, x - TAILLECARREE / 2, y
						- TAILLECARREE / 2, TAILLECARREE, TAILLECARREE);
				f.setCouleur(FormGeo.getCouleurCourante());
				add(f);
			} else {
				if (!selectedFormesGeo.contains(courant)) {
					courant.setSelected(true);
					selectedFormesGeo.add(courant);
				}
			}
			repaint();
		}
	}

	/**
	 * * @
	 * 
	 * @exception ConcurrentModificationException
	 *                Cette exception peut être levée par les méthodes qui ont
	 *                détecté une modification concurrente d'un objet lorsque
	 *                cette modification n'est pas autorisée.
	 *                "http://docs.oracle.com/javase/7/docs/api/java/util/
	 *                ConcurrentModificationException.html"
	 * 
	 * 
	 */
	// Lorsque on appui sur Maj(shift) enfonce

	private class MouseMotionHandler implements MouseMotionListener {
		// MOUVEMENT DE LA SOURIS
		public void mouseMoved(MouseEvent event) {
			if (find(event.getPoint()) == null)
				setCursor(Cursor.getDefaultCursor());
			else
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		}

		// DEPLACEMENT DE L'ELEMENT AVEC LA SOURIS
		public void mouseDragged(MouseEvent event) {
			state.mouseDragged(event);
		}
	}

	// ENREGISTRER FICHIER
	public void enregistre(ObjectOutputStream out) {
		try {
			for (FormGeo f : formesGeo) {
				f.setSelected(true);
				out.writeObject(f);

			}

		} catch (IOException io) {
			System.exit(1);
		}
	}

	// OUVRIR FICHIER SAUVERGARDER
	/**
	 * @exception IOException
	 *                ClassNotFoundException Attrape l'exception lors que la
	 *                chaine de caractere du fichier ne pas egal a nul pendant
	 *                l'execution de la boucle while
	 * 
	 * @param in
	 *            objet a lire
	 */
	public void lireInfo(ObjectInputStream in) {
		try {
			// EFFACE TOUT LE FIGURE EN CAS D'OUVERTURE D'UN FICHIER
			// SAUVERGARDER
			formesGeo.clear();

			object = in.readObject();
			while (object != null) {
				if (object != null) {
					formesGeo.add((FormGeo) object);
					object = in.readObject();
				}
			}
			in.close();
			repaint();
		}

		catch (IOException | ClassNotFoundException e) {
			formesGeo.add((FormGeo) object);
			repaint();
		}
	}

}