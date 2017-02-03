/*
 * Modified on November 29th, 2016
 * 
 * Attention
 * 
 * This function is used to read the data from robot
 * This function is used only for initialize the tool variable
 * That is, we will assign the "integer array" named "tool" for the later on work
 */
public class RobotReadPosition extends SendUDP {
    private static int[] command = { 1497715267, 536870912, 50397184, 0, 960051513, 960051513, 1962960128, 65536 };
    private int[] response = new int[] {};
    private int[] tool = new int[8];

    public RobotReadPosition() {
	super(command);
    }

    public int[] read() {
	System.out.println("Called");
	RobotReadPosition robotread = new RobotReadPosition();
	try {
	    this.response = robotread.sendint();
	    if (this.response == null) {
		return null;
	    } else {
		System.out.println("respnse = " + this.response.toString());
		System.out.println("Return has length " + this.response.length);

		// deal with response with SendUDP's public funuction to
		// generate int32 information in tool, which contains
		// [Tool_no,Form,X,Y,Z,Xr,Yr,Zr], please deal with this in int32
		// form and i will modified the data in RobotMove and JavaRobot
		// functions.
	    }
	} catch (NullPointerException e) {
	    e.printStackTrace();
	    return null;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	return tool;
    }

}
