package eu.vicci.turtlebot;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//...

public class Engine {
	private static final int SERIAL_PORT = 11043;

	private InputStream input;
	private OutputStream output;
	private SerialPort port;
	private boolean connected;

	// ...

	public Engine() {
		connected = false;
	}

	public void connect(CommPortIdentifier id) {
		if (connected)
			disconnect();

		try {
			port = (SerialPort) id.open("Engine", SERIAL_PORT);
			input = port.getInputStream();
			output = port.getOutputStream();

			port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.addEventListener(createSerialPortEventListener());
			port.notifyOnDataAvailable(true);
			connected = true;
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
	}

	protected SerialPortEventListener createSerialPortEventListener() {
		// ...
	}

	public boolean isConnected() {
		return connected;
	}

	public List<String> getPorts() {
		List<String> result = new ArrayList<String>();

		for (CommPortIdentifier id : getIdentifiers()) {
			result.add(id.getName());
		}

		return result;
	}

	public void drive() {
		// ...
	}
	
	public void disconnect() {
		try {
			connected = false;
			if (input != null) input.close();
			if (output != null) output.close();
			if (port != null) port.close();

			input = null;
			output = null;
			port = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}