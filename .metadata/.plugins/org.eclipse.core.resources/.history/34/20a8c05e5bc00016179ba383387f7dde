import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 *  A simple datagram server
 *  Shows how to send and receive UDP packets in Java
 *
 *  @author  P. Tellenbach, http://www.heimetli.ch
 *  @version V1.01
 */

public class DatagramServer
{
   private final static int PACKETSIZE = 4096 ;
//ip address RFVLSI is 192.168.1.134
	private static String[] respo = new String[] { "59", "45", "52", "43", "20", "00", "68", "00", "01", "01", "00",
			"00", "00", "00", "00", "00", "39", "39", "39", "39", "39", "39", "39", "39", "8a", "00", "01", "00", "01",
			"02", "00", "00", "01", "00", "00", "00", "00", "00", "00", "00", "01", "00", "00", "00", "e8", "03", "00",
			"00", "11", "00", "00", "00", "fc", "c7", "0e", "00", "7c", "ce", "00", "00", "6b", "19", "05", "00", "ff",
			"0d", "ed", "ff", "d4", "45", "f2", "ff", "b4", "7a", "f7", "ff", "00", "00", "00", "00", "00", "00", "00",
			"00", "00", "00", "00", "00", "01", "00", "00", "00", "03", "00", "00", "00", "00", "00", "00", "00", "00",
			"00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
			"00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00" };
	private static String[] readRespo = new String[] { "59", "45", "52", "43", "20", "00", "34", "00", "03", "01", "01",
			"00", "00", "00", "00", "80", "39", "39", "39", "39", "39", "39", "39", "39", "81", "00", "00", "00", "00",
			"00", "00", "00", "10", "00", "00", "00", "00", "00", "00", "00", "01", "00", "00", "00", "00", "00", "00",
			"00", "00", "00", "00", "00", "fc", "c7", "0e", "00", "7c", "ce", "00", "00", "6b", "19", "05", "00", "ff",
			"0d", "ed", "ff", "d4", "45", "f2", "ff", "b4", "7a", "f7", "ff", "00", "00", "00", "00", "00", "00", "00",
			"00" };

	public static void main(String args[])
   {
      // Check the arguments
      if( args.length != 1 )
      {
         System.out.println( "usage: DatagramServer port" ) ;
         return ;
      }

      try
      {
         // Convert the argument to ensure that is it valid
         int port = Integer.parseInt( args[0] ) ;
         byte[] data = hexStringToByteArray(readRespo);
         // Construct the socket
         DatagramSocket socket = new DatagramSocket( port ) ;
         System.out.println("Data sent is : ");
         for(byte i : data){
        	 System.out.print(" " + i); 
         }
         System.out.println( "The server is ready..." ) ;
         
         for( ;; )
         {
            // Create a packet
            DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;

            // Receive a packet (blocking)
            socket.receive( packet ) ;

            // Print the packet
            System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()) ) ;

            
            // Return the packet to the sender
            
            //retrive packet's information to send back response
            InetAddress clientIP = packet.getAddress();
            int clientPort = packet.getPort();
            DatagramPacket response = new DatagramPacket(data,data.length,clientIP,clientPort );
            socket.send( response ) ;
            System.out.println("Send to client IP : port are "+ clientIP.toString() +" @"+ clientPort);
            System.out.println("Send response length is : " + data.length);
            
        }  
     }
     catch( Exception e )
     {
        System.out.println( e ) ;
     }
  }
	public static byte[] hexStringToByteArray(String[] s) {
		int length = s.length;
		byte[] data = new byte[PACKETSIZE];
		for (int j = 0; j < length; j++) {
			for (int i = 0; i <= 2; i += 2) {
				data[j] =  (byte) ((Character.digit(respo[j].charAt(0), 16) << 4)+ (Character.digit(respo[j].charAt(0 + 1), 16)));
			}
		}
		for (int i = 33; i < data.length; i++) {
			if ((i + 1) % 4 == 0 && i != 0) {
				byte[] first = { data[i - 3], data[i - 2] };
				byte[] second = { data[i - 1], data[i] };
				data[i - 3] = second[1];
				data[i - 2] = second[0];
				data[i - 1] = first[1];
				data[i] = first[0];
				//System.out.println("OBYTES" + i + " : " + data[i - 3] + " " + data[i - 2] + " " + data[i - 1] + " " + data[i]);
			}
		}
		return data;
	    }
}