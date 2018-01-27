package org.schema.nintendospy.controller;

public class NESandSNESPacketReader implements PacketReader{
	 public ControllerState readPacketButtons (int[] packet)
     {
		 
		 ControllerState state = new ControllerState();
         if (packet.length < state.buttons.length){ return null;}


         for (int i = 1 ; i < state.buttons.length ; ++i) {
        	 
             state.setButton (i-1, packet[i] != 0x00);
         }
         state.setButton (Button.RIGHT.ordinal(), packet[8] != 0x00);
         return state;
     }
}
