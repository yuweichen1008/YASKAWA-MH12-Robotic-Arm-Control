import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class UDPNode {
    String host = "192.168.2.250"; // Robot IPAddress
    private int port = 10041; // Computer's Port
    private int robotPort = 10040; // Robot's Port
    private int timeOut = 1000; // Default 1 second
    private byte[] robotCommand = new byte[]{}; // Sending
							       // command
    private byte[] receiveData = new byte[256]; // Received buffer
							      // package of data
    private boolean flag = false;
    InetAddress robotAddress;

    // constructor
    public UDPNode(int port, int timeOut, byte[] robotCommand) {
	this.robotPort = port;
	this.timeOut = timeOut;
	this.robotCommand = robotCommand;
	try {
	    this.robotAddress = InetAddress.getByName(host);
	    // System.out.println("Robot IP address : "
	    // robotAddress.getHostAddress());

	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }

    public UDPNode(byte[] robotCommand) {
	this.robotCommand = robotCommand;
	try {
	    this.robotAddress = InetAddress.getByName(host);
	    // System.out.println("Robot IP address : " +
	    // robotAddress.getHostAddress());

	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }

    public UDPNode() {
	// No command send
	try {
	    this.robotAddress = InetAddress.getByName(host);
	    // System.out.println("Robot IP address : " +
	    // robotAddress.getHostAddress());

	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }

	public byte[] submit() throws IOException {
		int count = 0;
		try {
			DatagramSocket socket = new DatagramSocket(port); // Set // UDP //
			// Socket.
			socket.setSoTimeout(timeOut); // Millisecond
			/*
			 * System.out.println("");
			 * System.out.println("Robot IP address is :" +
			 * robotAddress.toString()); System.out.println("Robot port is : " +
			 * robotPort); System.out.println("Local IP address is : " +
			 * InetAddress.getLocalHost().getHostAddress());
			 * System.out.println("Local Port is :  " + socket.getLocalPort());
			 * System.out.println("Set Timeout : " + socket.getSoTimeout());
			 */
			DatagramPacket request = new DatagramPacket(this.robotCommand, this.robotCommand.length, robotAddress,
					robotPort);
			socket.send(request);
			String command = new String(request.getData(), "UTF-8");
			//System.out.println("Send command : " + command);
			//System.out.println("Robot is listening :" + request.getAddress() + " @" + request.getPort());
			System.out.println("  Sending the command for the 1-th time.");
			DatagramPacket response = new DatagramPacket(receiveData, receiveData.length, request.getAddress(),
					request.getPort());
			byte[] byteData = new byte[] {};
			while (count < 1) {
				try {
					socket.receive(response);
					if (response != null) {
						this.flag = true;
						System.out.println("Received data successfully!");
						break;
					}
				} catch (SocketTimeoutException e) {
					socket.send(request); // resend
					System.out.println("Didn't get the response. \n\n Resending the command for the " + (count + 2) + "-th time.");
					count++;
				}
			}
			if (!(flag)) {
				System.out.println("Robot doesn't response\n");
				socket.close();
				byteData = new byte[] { 0, 0, 0, 0 };
			} else {
				/*
				 * debug part 
				 * // System.out.println(""); // 
				 * String message = new String(response.getData(), // response.getOffset(),response.getLength()); // debug part
				 */
				byteData = response.getData();
				socket.close();
			}

			return byteData;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("UnknownHostException, Please contact Y.W. Chen");
			return null;
		}
	}
}