
public class JavaRobotHold extends SendUDP {

    private static Boolean status = new Boolean(false);
    private static byte[] commandHoldOn = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57, 57,
	    57, 57, -125, 0, 1, 0, 1, 16, 0, 0, 1, 0, 0, 0 };
    private static byte[] commandHoldOff = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
	    57, 57, 57, -125, 0, 1, 0, 1, 16, 0, 0, 2, 0, 0, 0 };

    public Boolean makeHold(int index) {// 1: Hold ON/ 2: Hold OFF
	int[] response = null;
	JavaRobotHold js = new JavaRobotHold();
	try {
	    if (index == 1) {// 1: Hold ON
		response = js.sendint(commandHoldOn);
	    } else {// 2: Hold OFF
		response = js.sendint(commandHoldOff);
	    }
	    if (response.length == 1 || response == null) {
		System.out.println(
			"JavaRobotHold cannot get response, please check the connection or contact  Y.W. Chen");
		return false;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (response[6] == -1879048192 && index == 1) {
	    status = true;
	    System.out.println("Robot Hold ON successfully");
	} else if (response[6] == -1879048192 && index == 2) {
	    status = true;
	    System.out.println("Robot Hold OFF successfully");
	} else if (response[6] == -1879048192) {
	    status = true;
	    System.out.println("Hold already on specific status!");
	} else if (response[6] == -1877016320) {
	    status = false;
	    System.out.println("Robot Servo failed. Error Code : E4A3 -- Format error (processing category error)");
	    System.out.println("Debug : try to change the port connecting to Robot.");
	} else {
	    status = false;
	    System.out.println("Robot Hold failed.");
	}

	return status;
    }
}
