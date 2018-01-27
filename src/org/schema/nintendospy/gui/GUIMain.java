package org.schema.nintendospy.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.schema.nintendospy.controller.SerialListener;

import gnu.io.CommPortIdentifier;

public class GUIMain extends JFrame{
	private boolean setupSerial;
	public GUIMain(){
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		openSettings();
		
	}
	public void openSettings(){
		JDialog settings = new JDialog((Dialog)null, "NSpy-Settings", false);
		settings.setSize(500, 400);
		settings.setPreferredSize(new Dimension(500, 400));
		settings.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		settings.setContentPane(new GUISettings(this, settings));
		settings.setVisible(true);
		
		settings.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				if(!setupSerial){
					System.out.println("CLOSED WINDOW -> EXIT");
					System.exit(0);
				}else{
					setupSerial = false;
				}
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
	}
	
	public static SerialListener startSerial(CommPortIdentifier port){
		SerialListener main = new SerialListener();
		main.initialize(port);
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started Serial Listener");
		return main;
	}
	public static void start(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				final GUIMain m = new GUIMain();
//				m.setTitle("NSpy-Settings");
//				m.setLocationRelativeTo(null);
//				m.setUndecorated(true);
//				m.setVisible(true);
				Thread t = new Thread(new Runnable(){
					
					@Override
					public void run() {
						while(true){
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							m.repaint();
						}
					}
				});
				t.setDaemon(true);
				t.start();
			}
		});
		
	}
	public void setupSerial(CommPortIdentifier port, String skinFolder) {
		this.setupSerial = true;
		try {
			final SerialListener serial = startSerial(port);
			
			
			Skin skin = new Skin(new File("./data/skins/"+skinFolder+"/skin.xml"));
			
			
			JDialog f = new JDialog((Dialog)null, "NintendoSpy-Controller", false);
			
			GUIController guiController = new GUIController(skin);
			serial.addObserver(guiController);
			f.setContentPane(guiController);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setAlwaysOnTop(true);
			int reqWidth = skin.background.getWidth();
			int reqHeight = skin.background.getHeight();
			
			
			f.getContentPane().setPreferredSize(new Dimension(reqWidth, reqHeight));
			f.pack();
			
			f.setResizable(false);
			
			f.setVisible(true);
			
		
			f.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					serial.close();
					openSettings();
				}
				
				@Override
				public void windowActivated(WindowEvent e) {
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
