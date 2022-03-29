package pcd01.view;

import pcd01.controller.Controller;
import pcd01.model.*;
import pcd01.utils.P2d;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Simulation view
 *
 * @author aricci
 *
 */
public class SimulationView implements View {
        
	private final VisualiserFrame frame;

    public SimulationView(){
    	frame = new VisualiserFrame(620, 620);
	}

	public void display(SimulationState state){
 	   frame.display(state.getBodies(), state.getVt(), state.getSteps(), state.getBounds());
    }

	@Override
	public void start() {
		SwingUtilities.invokeLater(() -> this.frame.setVisible(true));
	}

	@Override
	public void modelUpdated(Model model) {
		display(model.getState());
	}

	public static class VisualiserFrame extends JFrame {

        private final VisualiserPanel panel;

        public VisualiserFrame(int w, int h){
            setTitle("Bodies Simulation");
            setSize(w,h);
            setResizable(false);
            panel = new VisualiserPanel(w,h);
            getContentPane().add(panel);
            addWindowListener(new WindowAdapter(){
    			public void windowClosing(WindowEvent ev){
    				System.exit(-1);
    			}
    			public void windowClosed(WindowEvent ev){
    				System.exit(-1);
    			}
    		});
        }
        
        public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
        	try {
	        	SwingUtilities.invokeAndWait(() -> {
	        		panel.display(bodies, vt, iter, bounds);
	            	repaint();
	        	});
        	} catch (Exception ignored) {

			}
        }
        
        public void updateScale(double k) {
        	panel.updateScale(k);
        }    	
    }

    public static class VisualiserPanel extends JPanel implements KeyListener {
        
    	private ArrayList<Body> bodies;
    	private Boundary bounds;
    	
    	private long nIter;
    	private double vt;
    	private double scale = 1;
    	
        private final long dx;
        private final long dy;
        
        public VisualiserPanel(int w, int h){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
			this.addKeyListener(this);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
			requestFocusInWindow(); 
        }

        public void paint(Graphics g){    		    		
    		if (bodies != null) {
        		Graphics2D g2 = (Graphics2D) g;
        		
        		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		          RenderingHints.VALUE_ANTIALIAS_ON);
        		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        		          RenderingHints.VALUE_RENDER_QUALITY);
        		g2.clearRect(0,0,this.getWidth(),this.getHeight());

        		
        		int x0 = getXcoord(bounds.getX0());
        		int y0 = getYcoord(bounds.getY0());
        		
        		int wd = getXcoord(bounds.getX1()) - x0;
        		int ht = y0 - getYcoord(bounds.getY1());
        		
    			g2.drawRect(x0, y0 - ht, wd, ht);
    			
	    		bodies.forEach( b -> {
	    			P2d p = b.getPos();
			        int radius = (int) (10*scale);
			        if (radius < 1) {
			        	radius = 1;
			        }
			        g2.drawOval(getXcoord(p.getX()),getYcoord(p.getY()), radius, radius); 
			    });		    
	    		String time = String.format("%.2f", vt);
	    		g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + nIter + " (UP for zoom in, DOWN for zoom out)", 2, 20);
    		}
        }
        
        private int getXcoord(double x) {
        	return (int)(dx + x*dx*scale);
        }

        private int getYcoord(double y) {
        	return (int)(dy - y*dy*scale);
        }
        
        public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
            this.bodies = bodies;
            this.bounds = bounds;
            this.vt = vt;
            this.nIter = iter;
        }
        
        public void updateScale(double k) {
        	scale *= k;
        }

		@Override
		public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 38){  		/* KEY UP */
					scale *= 1.1;
				} else if (e.getKeyCode() == 40){  	/* KEY DOWN */
					scale *= 0.9;  
				} 
		}

		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
    }
}