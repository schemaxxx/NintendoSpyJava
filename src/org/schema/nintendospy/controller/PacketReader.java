package org.schema.nintendospy.controller;

public interface PacketReader {
	public ControllerState readPacketButtons(int[] packet);
}
