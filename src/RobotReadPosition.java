/*
 * Modified on December 13th, 2016
 * 
 * Attention
 * 
 * This function is used to read the data from robot
 * This function is used only for initialize the tool variable
 * That is, we will assign the "integer array" named "tool" for the later on work
 */

public class RobotReadPosition extends SendUDP {
    private static int[] command = { 1497715267, 536870912, 50397184, 0, 960051513, 960051513, 1962960128, 65536 };
    public int[] tool = new int[8];

    // Constructor of RobotReadPosition
    public RobotReadPosition() {
	super(command);
    }

    public RobotReadPosition(int[] toolin) {
	super(command);
	this.tool = toolin;
    }

    // Read will send command and return tool form data
    public int[] read() {
	int[] response = new int[8];
	// System.out.println("Read");
	RobotReadPosition robotread = new RobotReadPosition();
	try {
	    response = robotread.sendint();
	    if (response == null) {
		return null;
	    } else {
		// System.out.println("respnse = " + response.toString());
		// System.out.println("Return has length " + response.length);
		if (response.length != 64 || response[6] == 73600) {
			System.out.println("Format Error");
		    return null;
		}
		for (int i = 8; i < 19; i++) {
		    // System.out.println(response[i]);
		    switch (i) {
		    case 9:
			this.tool[0] = response[i]; // Form
			break;
		    case 10:
			this.tool[1] = response[i]; // Tool number
			break;
		    case 13:
			this.tool[2] = response[i]; // X
			break;
		    case 14:
			this.tool[3] = response[i]; // Y
			break;
		    case 15:
			this.tool[4] = response[i]; // Z
			break;
		    case 16:
			this.tool[5] = response[i]; // Rx
			break;
		    case 17:
			this.tool[6] = response[i]; // Ry
			break;
		    case 18:
			this.tool[7] = response[i]; // Rz
			break;
		    }

		}
		// deal with response with SendUDP's public funuction to
		// generate int32 information in tool, which contains
		// [Tool_no,Form,X,Y,Z,Xr,Yr,Zr], please deal with this in int32
		// form and i will modified the data in RobotMove and JavaRobot
		// functions.
		return this.tool;
	    }
	} catch (NullPointerException e) {
	    e.printStackTrace();
	    return null;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
