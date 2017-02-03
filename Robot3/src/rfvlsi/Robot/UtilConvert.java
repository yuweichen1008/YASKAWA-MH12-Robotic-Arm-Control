package rfvlsi.Robot;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class UtilConvert {
	/*
	 * Below here are functions to deal with byte, int8, int32 and String array
	 * transformation
	 */
	public static int[] byteArrayToInt(byte[] b) {
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

	public static byte[] InttoByteArray(int[] inputIntArray) {
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

	public static byte[] InttoByteArraySingle(int input) {
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

	public static byte[] InttoByteArrayMove(int[] inputIntArray) {
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

	public static byte[] swap(byte[] ibytes) {
		byte[] obytes = new byte[ibytes.length];
		for (int i = 0; i < ibytes.length; i++) {
			if ((i + 1) % 4 == 0 && i != 0) {
				byte[] first = { ibytes[i - 3], ibytes[i - 2] };
				byte[] second = { ibytes[i - 1], ibytes[i] };
				obytes[i - 3] = second[1];
				obytes[i - 2] = second[0];
				obytes[i - 1] = first[1];
				obytes[i] = first[0];
			}
		}
		return obytes;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static int[] byteToint32(byte[] inputByteArray) {
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

	public static int[] stringTointArray(String[] commandByt) {

		byte[] commandbyteArray = new byte[commandByt.length];
		for (int i = 0; i < commandByt.length; i++) {
			byte[] e = hexStringToByteArray(commandByt[i]);
			commandbyteArray[i] = e[0];
		}
		byte[] changedCommandbyteArray = swap(commandbyteArray);

		int[] haha = byteToint32(changedCommandbyteArray);
		for (int i : haha) {
			System.out.println(i);
		}
		return haha;
	}

	public static byte[] swapString(byte[] ibytes) {
		byte[] obytes = new byte[ibytes.length];
		for (int i = 0; i < ibytes.length; i++) {
			if ((i + 1) % 4 == 0 && i != 0) {
				byte[] first = { ibytes[i - 3], ibytes[i - 2] };
				byte[] second = { ibytes[i - 1], ibytes[i] };
				obytes[i - 3] = second[1];
				obytes[i - 2] = second[0];
				obytes[i - 1] = first[1];
				obytes[i] = first[0];
			}
		}
		return obytes;
	}

	public static String convert(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length);
		for (int i = 43; i < data.length; ++i) {
			if (data[i] < 0)
				throw new IllegalArgumentException();
			sb.append((char) data[i]);
		}
		return sb.toString();
	}
}
