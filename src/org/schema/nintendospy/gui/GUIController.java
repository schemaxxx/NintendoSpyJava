package org.schema.nintendospy.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.schema.nintendospy.controller.Button;
import org.schema.nintendospy.controller.ControllerState;
import org.schema.nintendospy.gui.Skin.SkinButton;
import org.xml.sax.SAXException;

public class GUIController extends JPanel implements Observer{

	
	private Skin skin;




	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public GUIController(String skin) throws IOException {
		
		try {
			this.skin = new Skin(new File("./data/"+skin+"/skin.xml"));
		} catch (Exception e) {
			throw new IOException(e);
		}
		setPreferredSize(new Dimension(this.skin.background.getWidth(), this.skin.background.getHeight()));	
		
		
	}
	public GUIController(Skin skin){
		this.skin = skin;
		setPreferredSize(new Dimension(this.skin.background.getWidth(), this.skin.background.getHeight()));	
	}
	private final HashMap<Button, Boolean> buttonState = new HashMap<Button, Boolean> ();
	private boolean changed;
	
	
	
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		System.err.println("PPP");
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(skin.background, 0, 0, skin.background.getWidth(), skin.background.getHeight(), null);
		
		for(SkinButton b : skin.buttons.values()){
			if(!buttonState.containsKey(b.button) || buttonState.get(b.button)){
				g2.drawImage(b.image, b.x, b.y, b.width, b.height, null);
			}
			
		}
    }
	
	public static void main(String[] args){
		final JFrame f = new JFrame("Conroller");
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				
				try {
					String sfile = "nes-default";
					Skin skin = new Skin(new File("./data/"+sfile+"/skin.xml"));
					
					
					f.setContentPane(new GUIController(skin));
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					int reqWidth = skin.background.getWidth();
					int reqHeight = skin.background.getHeight();
					
					f.getContentPane().setPreferredSize(new Dimension(reqWidth, reqHeight));
					f.pack();
					
					f.setResizable(false);
					
					f.setVisible(true);
					
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		
		Thread t = new Thread(new Runnable(){
			
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					f.repaint();
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
	private void changeState(Button b, boolean c) {
		if(!buttonState.containsKey(b) || buttonState.get(b).booleanValue() != c){
			buttonState.put(b, c);
			changed = true;
		}
	}
	@Override
	public void update(Observable o, Object arg) {
//		System.err.println("SSJJS "+arg);
		if(arg instanceof ControllerState){
			ControllerState c = (ControllerState)arg;
			for(Button b : Button.values()){
				changeState(b, c.buttons[b.ordinal()]);
			}
			if(changed){
				repaint();
				changed = false;
			}
		}
	}
	
}
