package org.schema.nintendospy.controller;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;

public class SerialListener extends Observable implements SerialPortEventListener {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // MacOSX
			"/dev/tty.usbmodem621",
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM1", // Windows
			"COM2", // Windows
			"COM3", // Windows
			"COM4", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedInputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;

	public PacketReader reader = new NESandSNESPacketReader();
	
	public void initialize(CommPortIdentifier portId) {
		if (portId == null) {
			System.err.println("Could not find COM port.");
			return;
		}else{
			System.err.println("Starting on Port: "+portId.getName());
		}
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedInputStream(serialPort.getInputStream());
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void initialize() {
		// the next line is for Raspberry Pi and
		// gets us into the while loop and was suggested here was suggested
		// http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
//		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.err.println("Checking port: "+currPortId.getName());
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		initialize(portId);

		
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			if (!System.getProperty("os.name").equals("Mac OS X")) {
				serialPort.removeEventListener();
			
				serialPort.close();
			}else{
				System.out.println("mac version crashes on closing ports so we have to end the executable here. Port was: "+serialPort.getName());
				System.exit(0);
			}
			System.out.println("Serial Port closed: "+serialPort.getName());
		}
	}
	final byte[] buffer = new byte[1024*1024];
	public static final int split = (0x0A);
	IntArrayList read = new IntArrayList();
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {

				while (input.available() > 0) {
					int readMe = input.available();
					input.read(buffer, 0, readMe);
					for (int i = 0; i < readMe; i++) {
						read.add(buffer[i] & 0xFF);
					}
				}
				int firstSplitIndex;
				do {
					firstSplitIndex = -1;
					for (int i = 0; i < read.size(); i++) {
						if (read.getInt(i) == split) {
							firstSplitIndex = i;
							break;
						}
					}
					if (firstSplitIndex < 0) {
						return;
					}

					int sndSplitIndex = -1;
					for (int i = firstSplitIndex + 1; i < read.size(); i++) {
						if (read.getInt(i) == split) {
							sndSplitIndex = i;
							break;
						}
					}
					if (sndSplitIndex < 0) {
						return;
					}
					// Grab the latest packet out of the buffer and fire it off
					// to
					// the receive event listeners.
					int packetStart = firstSplitIndex;
					int packetSize = sndSplitIndex - firstSplitIndex;

					// Clear our buffer up until the last split character.
					int[] pack = new int[packetSize];

					int p = 0;
					for (int i = packetStart; i < sndSplitIndex; i++) {
						pack[p++] = read.get(i);
					}
					packetReceived(pack);
					for (int i = 0; i < sndSplitIndex; i++) {
						read.remove(0);
					}
				} while (firstSplitIndex >= 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}
	int oCh = 0;
	private void packetReceived(int[] pack) {
		ControllerState readPacketButtons = reader.readPacketButtons(pack);
		
		
//		if(oCh%10 == 0){
//			System.err.println("READ; "+pack.length+": "+readPacketButtons);
//		}
		
		oCh++;
		
		setChanged();
		notifyObservers(readPacketButtons);
	}

	

}