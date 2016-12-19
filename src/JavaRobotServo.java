/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
public class JavaRobotServo extends SendUDP {

    private static Boolean status = new Boolean(false);
    private static byte[] commandServoOn = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
	    57, 57, 57, -125, 0, 2, 0, 1, 16, 0, 0, 1, 0, 0, 0 };
    private static byte[] commandServoOFF = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
	    57, 57, 57, -125, 0, 2, 0, 1, 16, 0, 0, 2, 0, 0, 0 };

    public Boolean makeServo(int index) {// 1: Servo ON / 2: Servo OFF
	int[] response = null;
	JavaRobotServo js = new JavaRobotServo();
	try {
	    if (index == 1) {// 1: Servo ON
		response = js.sendint(commandServoOn);
	    } else {// 2: Servo OFF
		response = js.sendint(commandServoOFF);
	    }
	    if (response.length == 1 || response == null) {
		System.out.println(
			"JavaRobotServo cannot get response, please check the connection or contact  Y.W. Chen");
		return false;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (response[6] == -1879048192 && index == 1) {
	    status = true;
	    System.out.println("Robot Servo ON successfully");
	} else if (response[6] == -1879048192 && index == 2) {
	    status = true;
	    System.out.println("Robot Servo OFF successfully");
	} else if (response[6] == -1879048192) {
	    status = true;
	    System.out.println("Servo already on specific status!");
	} else if (response[6] == -1877016320) {
	    status = false;
	    System.out.println("Robot Servo failed. Error Code : E4A3 -- Format error (processing category error)");
	    System.out.println("Debug : try to change the port connecting to Robot.");
	} else {
	    status = false;
	    System.out.println("Robot Servo failed.");
	}

	return status;
    }
}
