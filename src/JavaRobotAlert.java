/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.UnsupportedEncodingException;

public class JavaRobotAlert extends SendUDP {

	private static String alert = new String();

	public JavaRobotAlert(int i) {
		super((i + 1));// 2 : ReadAlert / 3 : Reset Alert
	}

	public static String makeAlert(int index) {
		byte[] response = null;
		JavaRobotAlert js = new JavaRobotAlert(index);
		try {
			response = js.send();
			if (response.length == 1) {
				System.out.println("Robot Alert cannot get response, please check the connection or contact  Y.W. Chen");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response[9] == 0) {
			alert = null;
		} else {
			try {
				// Try to convert byte to ASCII
				alert = new String(response, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			;
		}

		return alert;
	}

}
