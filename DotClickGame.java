
package GUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.*;

public class aMatikyan_DotGame extends JFrame implements ActionListener {

	private final int CANVAS_WIDTH = 300;
	private final int CANVAS_HEIGHT = 575;
	private final int SPLIT_DIST = 40; // how far to shoot the split piece away
	private final int STEP = 5; // how far points moves per iteration
	private final int POINT_SPEED = 25; // how fast point moves in milliseconds
	private final int [] xChange = {-10, 10};
	private final int [] yChange = {-10, 10};
	
	private DrawPanel canvas;
	private Timer timer;
	private int numClicks;
	
	
	public aMatikyan_DotGame() {
		setSize(300, 575);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		canvas = new DrawPanel();
		canvas.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		canvas.setBackground(Color.white);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black));

		// set up menu items for game options 
		JMenuBar menu = new JMenuBar(); 
		JMenu difficulty = new JMenu("Difficulty");
		JMenuItem baby = new JMenuItem("Baby");
		JMenuItem normal = new JMenuItem("Normal");
		JMenuItem nm = new JMenuItem("Nightmare");
		baby.addActionListener(this);
		normal.addActionListener(this);
		nm.addActionListener(this);
		
		difficulty.add(baby);
		difficulty.add(normal); 
		difficulty.add(nm); 
		
		menu.add(difficulty);
		setJMenuBar(menu);
	
		
		add(canvas);

		setVisible(true);
		
		
		timer = new Timer(5, new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
			
				
				for(Point each: canvas.allPoints) {
					each.move();
				}
				repaint();
				
				if(canvas.allPoints.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Game Over \n Number of Clicks: " + numClicks);
					timer.stop();
				}
				
			}
				
		});

	}

	public void actionPerformed(ActionEvent ae) {
		
		
		
		if(ae.getActionCommand().equals("Baby")){
			newGame(40,1);
		}else if(ae.getActionCommand().equals("Normal")) {
			newGame(50,2);
		} else if(ae.getActionCommand().equals("Nightmare")) {
			newGame(75,3);
		}
	}
	
	// envoked when user selects a menu item
	// removes all current points and creates new ones based on mode selected
	public void newGame(int size, int count) {
		
		canvas.allPoints.clear();
		numClicks = 0; 
		
		int each = 0;
		while(each < count) {
			Point next = new Point(size);
			canvas.allPoints.add(next); 
			each++; 
		}
		repaint(); 
		timer.start();
	}
	

	public class DrawPanel extends JPanel implements MouseListener {

		private ArrayList<Point> allPoints;

		public DrawPanel() {
			allPoints = new ArrayList<Point>();
			this.addMouseListener(this);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (Point next : allPoints) {

				g.setColor(next.pColor);
				g.fillOval(next.xLoc, next.yLoc, next.size, next.size);

			}
		}
		
		public ArrayList<Point> getPoints(){
			return allPoints; 
		}
		
		public void mousePressed(MouseEvent me) {
			
			int mx = me.getX();
			int my = me.getY(); 
			
			for (int x = allPoints.size()-1; x >= 0; x--) {
				
				int xMax = allPoints.get(x).xLoc + allPoints.get(x).size;
				int yMax = allPoints.get(x).yLoc + allPoints.get(x).size;
				
				// if the mouse is over the dot check the action 
				if( mx >= allPoints.get(x).xLoc && mx <= xMax) {
					if( my >= allPoints.get(x).yLoc && my <= yMax) {
						hit(x); 
					}
				}
				
			}
		
			numClicks++; 
		}
		
		public void hit(int pt) {
			
			int newSize = allPoints.get(pt).size / 2; 
			// if a new points size is over 15 make 4 new points
			if(newSize >= 15) {
				for(int x = 0; x < 2; x++) {
					for(int y = 0; y < 2; y++) {
						int newXc = allPoints.get(pt).xLoc + xChange[x];
						int newYc = allPoints.get(pt).yLoc + yChange[y];
		
						if(newXc >= 0 && newXc <= 282) {
							if(newYc >= 0 && newYc <= 514) {
								allPoints.add(new Point(newXc, newYc, newSize));
							}
						}
							
					}
				}
			}
			allPoints.remove(pt); 
			
			
			
		}

		public void mouseReleased(MouseEvent arg0) {
			// do not implement

		}

		public void mouseClicked(MouseEvent arg0) {
			// do not implement

		}

		public void mouseEntered(MouseEvent arg0) {
			// do not implement

		}

		public void mouseExited(MouseEvent arg0) {
			// do not implement

		}

	}

	public class Point {
		private int xLoc; // this is the top left corner of the "box" that the point is drawn in.
		private int yLoc;
		private Color pColor;
		private int size;
		private int xDir; // direction it's currently moving.
		private int yDir;

		// randomly makes a point of size s
		public Point(int s) {
			this((int) (Math.random() * (282 - s)), (int) (Math.random() * (514 - s)), s);

		}

		// makes a point of size s at a given location
		public Point(int xL, int yL, int s) {

			pColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
			size = s;
			xLoc = xL;
			yLoc = yL;
			int[] directions = genDirection();
			xDir = directions[0];
			yDir = directions[1];
		}

		private int[] genDirection() {

			int xVal;
			int yVal;

			do {
				xVal = (int) (Math.random() * 3 - 1);
				yVal = (int) (Math.random() * 3 - 1);
			} while (xVal == 0 && yVal == 0);

			int[] toRet = { xVal, yVal };

			return toRet;
		}

		public boolean equals(Object other) {
			Point op = (Point) other;
			return this.xLoc == op.xLoc && this.yLoc == op.yLoc && pColor.equals(op.pColor);
		}
		
		// if edge of a point meets with a verticle border return true
		public boolean hitsVert() {
		
			if(xLoc == 0 || (xLoc == 283-this.size))
				return true; 
			
			return false;
		}
		
		// if edge of a point meets with a horizontal border return true
		public boolean hitsHoriz() {
			if(yLoc == 0 || (yLoc == 515-this.size))
				return true;
			
			return false;
		}
		
		public void move() {
			// if the point hits the top border then reverse its y direction
			if(this.hitsHoriz()) 
				yDir = yDir*-1;
			
			// if the point hits the left or right side revers its x direction
			if(this.hitsVert())
				xDir = xDir*-1; 
			
			// update the positon of the point
			xLoc += xDir;
			yLoc += yDir; 
			
		}
	}

	public static void main(String[] args) {
		new aMatikyan_DotGame();
	}

}
