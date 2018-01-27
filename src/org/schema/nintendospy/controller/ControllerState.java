package org.schema.nintendospy.controller;

public class ControllerState {
	
	
	
	public final boolean[] buttons = new boolean[Button.values().length];


	public void setButton(int i, boolean b) {
		buttons[i] = b;
	}


	@Override
	public String toString() {
		
		StringBuffer b = new StringBuffer(); 
		for(int i = 0; i < buttons.length; i++){
			b.append("["+Button.values()[i].name()+" : "+buttons[i]+"] ");
		}
		return b.toString();
	} 
	
	
}
