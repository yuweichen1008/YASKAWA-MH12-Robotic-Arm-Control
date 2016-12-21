
public class JavaRobotAlert extends SendUDP {

	private static String alert = new String();
	private static byte[] commandAlertRead = { 89, 69, 82, 67, 32, 0, 0, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
			57, 57, 57, 112, 0, 1, 0, 0, 1, 0, 0 };
	private static byte[] commandAlertReset = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
			57, 57, 57, -126, 0, 1, 0, 1, 16, 0, 0, 1, 0, 0, 0 };

	public static String makeAlert(int index) {// 1 : ReadAlert / 2 : Reset
												// Alert
		byte[] response = null;
		JavaRobotAlert js = new JavaRobotAlert();
		try {
			if (index == 1) { // 1 : ReadAlert
				response = js.send(commandAlertRead);
				if (response.length == 1) {
					System.out.println(
							"Robot Alert cannot get response, please check the connection or contact  Y.W. Chen");
					return null;
				} else if (response[34] == 0 || response[35] == 0) {
					// Status == 0
					alert = null;
					System.out.println("There is no alarm.");
				} else {
					// Try to convert byte to ASCII
					alert = convert(swapString(response));
					System.out.println(alert);
				}
			} else {// 2 : Reset Alert
				response = js.send(commandAlertReset);
				if (response[34] == 0 || response[35] == 0) {
					System.out.println("Reset alarm successfully.");
				} else {
					System.out.println("Some Error on alarm reset command JavaRobotAlert(2)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alert;
	}

	private static String convert(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length);
		for (int i = 43; i < data.length; ++i) {
			if (data[i] < 0)
				throw new IllegalArgumentException();
			sb.append((char) data[i]);
		}
		return sb.toString();
	}

	private static byte[] swapString(byte[] ibytes) {
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
}
