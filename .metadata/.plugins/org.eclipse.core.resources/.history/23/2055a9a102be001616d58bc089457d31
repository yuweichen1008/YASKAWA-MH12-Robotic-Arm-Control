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
	public int[] tool = new int[8];
	private Boolean read = new Boolean(false);
	// Constructor of RobotReadPosition
	public RobotReadPosition() {
		super(command);
	}

	// Read will send command and return tool form data
	public int[] read() {
		int[] response = new int[8];
		System.out.println("Called");
		RobotReadPosition robotread = new RobotReadPosition();
		try {
			response = robotread.sendint();
			if (response == null) {
				return null;
			} else {
				System.out.println("respnse = " + response.toString());
				//System.out.println("Return has length " + response.length);
				for (int i = 0; i < response.length; i++) {
					this.tool[i] = response[i];
				}
				this.read = true;
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
	//
	public int[] getPosition() {
		if (this.read.booleanValue() == true) {
			int[] position = new int[3];
			position[0] = tool[3];
			position[1] = tool[4];
			position[2] = tool[5];
			return position;
		} else {
			System.out.println("You have to read() first! If you still have any problems, please refer to the note.");
			return null;
		}
	}

	public int[] getAngle() {
		if (this.read.booleanValue() == true) {
			int[] angle = new int[2];
			angle[0] = tool[6];
			angle[1] = tool[7];
			return angle;
		} else {
			System.out.println("You have to read() first! If you still have any problems, please refer to the note.");
			return null;
		}
	}

}
