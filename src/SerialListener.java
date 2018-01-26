import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class SerialListener implements SerialPortEventListener {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // MacOSX
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

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	public static final int split = (0x0A);
	List<Integer> read = new ArrayList<Integer>();
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
			
				while (input.available() > 0) {
					read.add(input.read());
				}
				int firstSplitIndex = -1;
				for (int i = 0; i < read.size(); i++) {
					if (read.get(i).intValue() == split) {
						firstSplitIndex = i;
						break;
					}
				}
				if (firstSplitIndex < 0){
					return;
				}

				int sndSplitIndex = -1;
				for (int i = firstSplitIndex + 1; i < read.size(); i++) {
					if (read.get(i).intValue() == split) {
						sndSplitIndex = i;
						break;
					}
				}
				if (sndSplitIndex < 0){
					return;
				}
//				System.err.println("III "+firstSplitIndex+" -> "+sndSplitIndex);
				// Grab the latest packet out of the buffer and fire it off to
				// the receive event listeners.
				int packetStart = firstSplitIndex;
				int packetSize = sndSplitIndex - firstSplitIndex;
				// PacketReceived (this, read.GetRange (packetStart,
				// packetSize).ToArray ());

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
//				String inputLine = input.readLine();
//				System.out.println(inputLine);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	private void packetReceived(int[] pack) {
		ControllerState readPacketButtons = reader.readPacketButtons(pack);
		System.err.println("READ; "+pack.length+": "+readPacketButtons);
	}

}