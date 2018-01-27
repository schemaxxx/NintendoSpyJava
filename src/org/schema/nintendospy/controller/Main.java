package org.schema.nintendospy.controller;

import org.schema.nintendospy.gui.GUIMain;

public class Main {
	
	public static void main(String[] args){
		Main starter = new Main();
		starter.start();
	}
	public void start() {
		try {
			LibLoader.loadSerialLibs();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		GUIMain.start();
		
		
	}
}
