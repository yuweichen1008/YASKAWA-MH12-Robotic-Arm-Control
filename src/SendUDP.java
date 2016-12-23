
/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class SendUDP extends Thread {
    private byte[] command = new byte[] {};

    // Constructor
    public SendUDP() {
    }

    public SendUDP(byte[] robotCommand) {
	this.command = robotCommand;
    }

    public SendUDP(int[] IntCommand) {
	byte[] byteComm = null;
	try {
	    byteComm = InttoByteArray(IntCommand);
	    // Change Int32 form to byte form
	} catch (Exception e) {
	    e.printStackTrace();
	}
	this.command = byteComm;
    }

    public byte[] send(byte[] command) throws Exception {
	UDPNode Command = new UDPNode(command);
	byte[] response = new byte[] {};
	try {
	    response = Command.submit();
	    if (byteArrayToInt(response)[0] == 0) {
		System.out.println("Cannot get response");
		Thread.sleep(10);
		return new byte[] { 0 };
	    } else {
		Thread.sleep(200);
		return swap(response);
	    }
	} catch (SocketTimeoutException e) {
	    System.out.println("Cannot get response");
	    Thread.sleep(10);
	    return new byte[] { 0 };
	}
    }

    public byte[] send() throws Exception {
	UDPNode Command = new UDPNode(command);
	byte[] response = new byte[] {};
	try {
	    response = Command.submit();
	    if (byteArrayToInt(response)[0] == 0) {
		System.out.println("Cannot get response");
		Thread.sleep(10);
		return new byte[] { 0 };
	    } else {
		Thread.sleep(200);
		return swap(response);
	    }
	} catch (SocketTimeoutException e) {
	    System.out.println("Cannot get response");
	    Thread.sleep(10);
	    return new byte[] { 0 };
	}
    }

    public int[] sendint(byte[] command) throws Exception {
	UDPNode Command = new UDPNode(command);
	byte[] response = new byte[] {};
	try {
	    response = Command.submit();
	    if (byteArrayToInt(response)[0] == 0) {
		System.out.println("Cannot get response");
		Thread.sleep(10);
		return new int[] { 0 };
	    } else {
		Thread.sleep(200);
		return byteToint32(response);
	    }
	} catch (SocketTimeoutException e) {
	    System.out.println("Cannot get response");
	    Thread.sleep(10);
	    return new int[] { 0 };
	}
    }

    public int[] sendint() throws Exception {
	UDPNode Command = new UDPNode(command);
	byte[] response = new byte[] {};
	try {
	    response = Command.submit();
	    if (byteArrayToInt(response)[0] == 0) {
		System.out.println("Cannot get response");
		Thread.sleep(10);
		return new int[] { 0 };
	    } else {
		Thread.sleep(200);
		return byteToint32(swap(response));
	    }
	} catch (SocketTimeoutException e) {
	    System.out.println("Cannot get response");
	    Thread.sleep(10);
	    return new int[] { 0 };
	}
    }

    /*
     * Below here are functions to deal with byte, int8, int32 and String array
     * transformation
     */
    public int[] byteArrayToInt(byte[] b) {
	if (b != null) {
	    int[] transfered = new int[(b.length) / 4];
	    for (int j = 0; j < (b.length / 4); j++) {
		transfered[j] = b[(j * 4) + 3] & 0xFF | (b[(j * 4) + 2] & 0xFF) << 8 | (b[(j * 4) + 1] & 0xFF) << 16
			| (b[(j * 4) + 0] & 0xFF) << 24;

	    }
	    return transfered;
	} else {
	    return null;
	}
    }

    public byte[] InttoByteArray(int[] inputIntArray) {
	byte[] transfered = new byte[(inputIntArray.length * 4)];
	ByteBuffer byteBuffer = ByteBuffer.allocate(inputIntArray.length * 4);
	IntBuffer intBuffer = byteBuffer.asIntBuffer();
	intBuffer.put(inputIntArray);
	transfered = byteBuffer.array();

	if (byteBuffer.hasArray()) {
	    return transfered;
	} else {
	    return null;
	}

    }

    public byte[] InttoByteArraySingle(int input) {
	byte[] transfered = new byte[4];
	ByteBuffer byteBuffer = ByteBuffer.allocate(4);
	IntBuffer intBuffer = byteBuffer.asIntBuffer();
	intBuffer.put(input);
	transfered = byteBuffer.array();
	if (byteBuffer.hasArray()) {
	    return transfered;
	} else {
	    return null;
	}
    }
    public byte[] InttoByteArrayMove(int[] inputIntArray) {
	byte[] transfered = new byte[(inputIntArray.length * 4)];
	ByteBuffer byteBuffer = ByteBuffer.allocate(inputIntArray.length * 4);
	IntBuffer intBuffer = byteBuffer.asIntBuffer();
	intBuffer.put(inputIntArray);
	transfered = byteBuffer.array();

	if (byteBuffer.hasArray()) {
	    return swap(transfered);
	} else {
	    return null;
	}

    }
    public byte[] swap(byte[] ibytes) {
	byte[] obytes = new byte[ibytes.length];
	for (int i = 0; i < ibytes.length; i++) {
	    if ((i + 1) % 4 == 0 && i != 0) {
		byte[] first = { ibytes[i - 3], ibytes[i - 2] };
		byte[] second = { ibytes[i - 1], ibytes[i] };
		obytes[i - 3] = second[1];
		obytes[i - 2] = second[0];
		obytes[i - 1] = first[1];
		obytes[i] = first[0];
		// System.out.println("IBYTES" + i + " : " + ibytes[i - 3] + " "
		// + ibytes[i - 2] + " " + ibytes[i - 1]+ " " + ibytes[i]);
		// System.out.println("OBYTES" + i + " : " + obytes[i - 3] + " "
		// + obytes[i - 2] + " " + obytes[i - 1]+ " " + obytes[i]);
	    }
	}

	return obytes;
    }

    public byte[] hexStringToByteArray(String s) {
	int len = s.length();
	byte[] data = new byte[len / 2];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	}
	return data;
    }

    public int[] byteToint32(byte[] inputByteArray) {
	int[] x = new int[(inputByteArray.length) / 4];
	// System.out.println(x);
	for (int i = 1; i < (inputByteArray.length); i = i + 4) {
	    int offSet = (i - 1);
	    int length = 4;
	    int a = Math.floorDiv(i, 4);
	    // public static ByteBuffer wrap(byte[] array, int offset, int
	    // length) Wraps a byte array into a buffer.
	    x[a] = java.nio.ByteBuffer.wrap(inputByteArray, offSet, length).getInt();
	}
	return x;
    }

    public int[] stringTointArray(String[] commandByt) {

	byte[] commandbyteArray = new byte[commandByt.length];
	for (int i = 0; i < commandByt.length; i++) {
	    byte[] e = hexStringToByteArray(commandByt[i]);
	    commandbyteArray[i] = e[0];
	}
	byte[] changedCommandbyteArray = swap(commandbyteArray);
	/*
	 * Debug purpose Byte[] commandByteArray =
	 * byteString.toObjects(changedCommandbyteArray); List<Byte> byteList2 =
	 * Arrays.asList(commandByteArray); System.out.println(byteList2);
	 * //another swap function : Collections.reverse(byteList2);
	 * System.out.println(Arrays.toString(commandByteArray));
	 * 
	 */
	// byte[] into int32
	int[] haha = byteToint32(changedCommandbyteArray);
	for (int i : haha) {
	    System.out.println(i);
	}
	return haha;
    }
}
