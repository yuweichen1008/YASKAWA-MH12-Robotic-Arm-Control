
public class JavaRobotHold extends SendUDP {
    
    private Boolean status = new Boolean(false);

    // Constructor for JavaRobotHold
    public JavaRobotHold(int i) {
	super((i + 5));// Hold 6: ON / 7: OFF
    }

    public Boolean makeHold(int index) {
	int[] response = null;
	JavaRobotHold js = new JavaRobotHold(index);
	try {
	    response = js.sendint();
	    if (response.length == 1) {
		System.out.println("Robot Hold cannot get response!"+"\nYou should check your connection with robot or contact the mechanics or YW Chen.\n\n");
		return false;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (response[6] == 144 && index == 1) {
	    status = true;
	    System.out.println("Robot Hold ON successfully");
	} else if (response[6] == 144 && index == 2) {
	    status = true;
	    System.out.println("Robot Hold OFF successfully");
	} else {
	    status = false;
	}

	return status;
    }
}
