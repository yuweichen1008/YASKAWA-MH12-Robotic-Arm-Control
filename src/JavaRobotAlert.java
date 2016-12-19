/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.UnsupportedEncodingException;

public class JavaRobotAlert extends SendUDP {

    private static String alert = new String();
    private static byte[] commandAlertRead = { 89, 69, 82, 67, 32, 0, 0, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
	    57, 57, 57, 112, 0, 1, 0, 0, 1, 0, 0 };
    private static byte[] commandAlertReset = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
	    57, 57, 57, -126, 0, 1, 0, 1, 16, 0, 0, 1, 0, 0, 0 };

    public String makeAlert(int index) {// 1 : ReadAlert / 2 : Reset  Alert
	byte[] response = null;
	JavaRobotAlert js = new JavaRobotAlert();
	try {
	    if (index == 1) {// 1 : ReadAlert
		response = js.send(commandAlertRead);
	    } else {// 2 : Reset Alert
		response = js.send(commandAlertReset);
	    }
	    if (response.length == 1) {
		System.out.println("Robot Alert cannot get response, please check the connection or contact  Y.W. Chen");
		return null;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (response[25] == 0) {//Status == 0
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
