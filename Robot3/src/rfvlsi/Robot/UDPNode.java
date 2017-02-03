/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
package rfvlsi.Robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPNode {
	String host = "192.168.2.250"; // Robot IPAddress
	private int robotPort = 10040; // Computer's Port
	private int timeOut = 1000; // Default 1 second

	private boolean flag = false;
	InetAddress robotAddress;


	public UDPNode() {
		try {
			this.robotAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public byte[] submit(byte[] command, int inport) throws IOException {
		int count = 0;
		try {
			DatagramSocket socket = new DatagramSocket(inport); // Set UDP 
			socket.setSoTimeout(timeOut);

			DatagramPacket request = new DatagramPacket(command, command.length, robotAddress, robotPort);
			socket.send(request);

			byte[] receiveData = new byte[256];
			DatagramPacket response = new DatagramPacket(receiveData, receiveData.length, request.getAddress(),
					request.getPort());
			byte[] byteData = new byte[] {};
			while (count < 1) {
				if (count == 0)
					try {
						socket.receive(response);
						if (response != null) {
							this.flag = true;
							break;
						}
					} catch (SocketTimeoutException e) {
						socket.send(request);
						count++;
					}
			}
			if (!(flag)) {
				socket.close();
				byteData = new byte[] { 0, 0, 0, 0 };
			} else {
				byteData = response.getData();
				socket.close();
			}
			// Note, YJ use swap here, please check.
			// YW notice Jan3rd, but why?
			return UtilConvert.swap(byteData);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("UnknownHostException, Please contact Y.W. Chen");
			return null;
		}
	}
}