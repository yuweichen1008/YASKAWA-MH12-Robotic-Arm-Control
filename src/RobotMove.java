
/*
 * Modified on Dec. 14, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.IOException;
import java.util.ArrayList;

public class RobotMove extends SendUDP {

    private boolean isPitch = false;
    private boolean moveTool = false;
    private static final byte[] head = { 89, 69, 82, 67, 32, 00, 104, 00, 03, 01, 00, 00, 00, 00, 00, 00, 57, 57, 57,
	    57, 57, 57, 57, 57 };// Header part
    private static final byte[] suh = { -118, 00, 03, 00, 01, 02, 00, 00 };// Sub-header
    private static final byte[] sub_tool = { -118, 00, 01, 00, 01, 02, 00, 00 };
    // part
    private byte[] newCommand = new byte[] {};
    private byte[] setting_rect = { 01, 00, 00, 00, 00, 00, 00, 00, 01, 00, 00, 00, 17, 00, 00, 00 };
    // Setting of Robot -- Station -- Classification -- Operation
    // Coordinate(Robot coordinate)
    private byte[] setting_ang = { 01, 00, 00, 00, 00, 00, 00, 00, 02, 00, 00, 00, 19, 00, 00, 00 };
    // Setting of Robot -- Station -- Classification -- Operation
    // Coordinate(Tool coordinate)
    private Integer speed = new Integer(0);// Speed = speed * 10 unit
    private byte[] Reserv = { 0, 0, 0, 0 };
    private Integer toolNumber = new Integer(0);
    private Integer typeNumber = new Integer(0);
    private int[] coor = new int[] { 3 };
    private int[] extension = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    // Axis = Tool(9:32); X, Y, Z, TX, TY, TZ
    private int[] coordinate = new int[3];
    private int[] angle = new int[3];
    private int[] tool = new int[8];
    private int[] displacement = new int[8];
    private byte[] speedbyte;
    private byte[] toolNumberbyte;
    private byte[] typeNumberbyte;
    private byte[] coordinatebyte;
    private byte[] anglebyte;
    private byte[] coorbyte;
    private boolean isDone = false;

    // constructor of assigning X, Y, Z, and angle values
    public RobotMove(int xtar, int ytar, int ztar, int phitar, int thetatar, int[] tool, int speedin) {
	this.moveTool = false;
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[1];
	this.typeNumber = tool[0];
	// Assigning difference of coordinate and angle
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 0:
		this.coordinate[i] = xtar * 1000;// X in 1 mm
		this.angle[i] = phitar * 10000; // pitch in 1 degree
		break;
	    case 1:
		this.coordinate[i] = ytar * 1000; // Y in 1 mm
		this.angle[i] = thetatar * 10000; // yaw in 1 degree
		break;
	    case 2:
		this.coordinate[i] = ztar * 1000; // Z in 1 mm
		this.angle[i] = 0;
		break;
	    }
	}
	initialize(speedin);
    }

    // constructor of simply move theta and phi
    public RobotMove(int phitar, int thetatar, int[] tool, int speedin) {
	this.moveTool = false;
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[1];
	this.typeNumber = tool[0];
	// Assigning difference of coordinate and angle
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 0:
		this.coordinate[i] = 0;
		this.angle[i] = phitar * 10000; // pitch in 1 degree
		break;
	    case 1:
		this.coordinate[i] = 0;
		this.angle[i] = thetatar * 10000; // yaw in 1 degree
		break;
	    case 2:
		this.coordinate[i] = 0;
		this.angle[i] = 0;
		break;
	    }
	}
	initialize(speedin);
    }

    // constructor of simply move to tool
    public RobotMove(int[] tool, int speedin) {
	this.moveTool = true;
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[1];
	this.typeNumber = tool[0];
	// Assigning difference of coordinate and angle
	for (int i = 0; i < 3; i++) {
	    this.coordinate[i] = 0;
	    this.angle[i] = 0;
	}
	initialize(speedin);
    }

    // This function is to assign the Byte[] to each type of parameter which
    // required in generate command
    private void initialize(int speedin) {
	robotDisplacement();
	this.speed = speedin;
	this.speedbyte = InttoByteArraySingle(speed);
	this.toolNumberbyte = InttoByteArraySingle(toolNumber);
	this.typeNumberbyte = InttoByteArraySingle(typeNumber);
	this.coordinatebyte = InttoByteArray(coordinate);
	this.anglebyte = InttoByteArray(angle);
	this.coorbyte = InttoByteArray(coor);
    }

    // inner constructor to send command
    private RobotMove(byte[] generatedCommand) {
	super(generatedCommand); // Assign Command to SendUDP
    }

    // method to get coordinate
    public int[] getCoordinate() {
	return this.coordinate;
    }

    // method to get angle
    public int[] getAngle() {
	return this.angle;
    }

    // method to get Tool
    public int[] getTool() {
	return this.tool;
    }

    public boolean isDone() {
	robotDisplacement();
	if (Math.abs(this.displacement[0]) >= 1000 && Math.abs(this.displacement[1]) >= 1000
		&& Math.abs(this.displacement[2]) >= 1000 && Math.abs(this.displacement[3]) >= 1000
		&& Math.abs(this.displacement[4]) >= 1000 && Math.abs(this.displacement[7]) >= 1000) {
	    this.isDone = false;

	} else {
	    this.isDone = true;

	}
	return this.isDone;
    }

    private boolean isDoneRec() {
	robotDisplacement();
	if (this.displacement[2] == 9999 && this.displacement[3] == 9999 && this.displacement[4] == 9999) {
	    return true; // True to stop the loop
	} else {
	    if (Math.abs(this.coordinate[0] - this.displacement[2] / 1000) >= 1
		    || Math.abs(this.coordinate[1] - this.displacement[3] / 1000) >= 1
		    || Math.abs(this.coordinate[2] - this.displacement[4] / 1000) >= 1) {
		this.isDone = false;
	    } else {
		this.isDone = true;
	    }
	    System.out.printf("Remaining Displacement  X : %d mm  Y : %d mm Z : %d mm.",
		    (this.coordinate[0] - this.displacement[2] / 1000),
		    (this.coordinate[1] - this.displacement[3] / 1000),
		    (this.coordinate[2] - this.displacement[4] / 1000));
	    return this.isDone;
	}
    }

    private boolean isDoneAng(int index) {
	// index == 0 : pitch to 0
	// index == 1 : yaw to certain value
	// index == 2 : pitch to certain value
	robotDisplacement();
	if (this.displacement[5] == 9999 && this.displacement[6] == 9999) {
	    return true; // True to stop the loop
	} else {
	    if (index == 0) {
		// Set pitch to 0
		if (Math.abs(0 - this.displacement[6]) >= 9800) {
		    this.isDone = false;
		} else {
		    this.isDone = true;
		}
	    } else if (index == 1) {
		// Set yaw to value this.angle[1]
		if (Math.abs(this.angle[1] - this.displacement[6]) >= 9800) {
		    this.isDone = false;
		} else {
		    this.isDone = true;
		}
	    } else if (index == 2) {
		// Set pitch to value this.angle[0]
		if (Math.abs(this.angle[0] - this.displacement[5]) >= 9800) {
		    this.isDone = false;
		} else {
		    this.isDone = true;
		}
	    } else {
		System.out.println("Error calling isDoneAng! index should be 1 or 2.");
		return false;
	    }
	    System.out.println("Remaining Angular Pitch : " + Math.floor((this.angle[0] - this.displacement[5] / 100))
		    + " centidegrees Yaw : " + Math.floor((this.angle[1] - this.displacement[6] / 100))
		    + " centidegrees.");
	    return this.isDone;
	}
    }

    public void robotDisplacement() {
	int[] toolnow = read();
	if (toolnow == null) {
	    for (int i = 2; i < 7; i++) {
		this.displacement[i] = 9999;
	    }

	} else {
	    this.displacement[2] = (toolnow[2] - this.tool[2]);
	    this.displacement[3] = (toolnow[3] - this.tool[3]);
	    this.displacement[4] = (toolnow[4] - this.tool[4]);

	    int tX = toolnow[5] / 100;
	    int tY = toolnow[6] / 100;
	    int tZ = toolnow[7] / 100;
	    // Pitch value
	    if (Math.abs(tZ) <= Math.abs(tX))
		displacement[5] = -(9000 + tY);
	    else if (Math.abs(tZ) > Math.abs(tX))
		displacement[5] = (9000 + tY);
	    // Yaw value
	    if (tX >= 0 && tZ >= 0) {
		displacement[6] = (tX + tZ - 18000);
	    } else if (tX < 0 && tZ < 0) {
		displacement[6] = (tX + tZ + 18000);
	    } else if (tX * tZ <= 0) {
		int temp;
		temp = tX + tZ;
		if (temp < 0)
		    displacement[6] = temp + 18000;
		else
		    displacement[6] = temp - 18000;
	    }
	    this.displacement[0] = (toolnow[5] - this.tool[5]);
	    this.displacement[1] = (toolnow[6] - this.tool[6]);
	    this.displacement[7] = (toolnow[7] - this.tool[7]);
	    System.out.println(
		    "Displacement X : " + this.displacement[2] / 1000 + " mm, Y : " + this.displacement[3] / 1000
			    + " Z : " + this.displacement[4] / 1000 + " mm, Pitch : " + this.displacement[5] / 100
			    + " centidegrees, Yaw : " + this.displacement[6] / 100 + " centidegrees.");
	}
    }

    private int[] read() {
	RobotReadPosition robotread = new RobotReadPosition();
	int[] toolout = robotread.read(); // To store the outcome of Tool
	return toolout;
    }

    // move function(main function)
    public void move() throws Exception {
	// Check whether there are placement first. If there are changes in
	// placement, the function will stop after change the
	// placement rather than move angular, you would have to call the
	// function again.
	// To tell whether to move coordinate and move robot to the place first
	try {
	    JavaRobotServo servo = new JavaRobotServo();
	    servo.makeServo(1);
	    JavaRobotHold hold = new JavaRobotHold();
	    if (this.moveTool) {
		// Move to tool
		int loopcnt = 0;
		while (!(isDone())) {
		    hold.makeHold(2); // Hold off
		    moveTool();// Move to the position first
		    Thread.sleep((long) (Math.abs((this.angle[0] - this.displacement[5]) / 100) / speed + 0.02));
		    loopcnt++;
		    if (loopcnt > 100) {
			System.out.println("Loop over 100");
			return;
		    }
		}
		System.out.println("Robot has been in the tool position successfully!");
	    } else if (this.coordinate[0] >= 100 || this.coordinate[1] >= 100 || this.coordinate[2] >= 100) {
		int loopcnt = 0;
		while (!(isDoneRec())) {
		    hold.makeHold(2); // Hold off
		    moveRect();// Move to the position first
		    Thread.sleep((long) (Math.abs((this.angle[0] - this.displacement[5]) / 100) / speed + 0.02));
		    loopcnt++;
		    if (loopcnt > 100) {
			System.out.println("Loop over 100");
			return;
		    }
		}
		System.out.println("Robot has moved to specific X,Y,Z!");
	    } else if (this.angle[0] >= 10000 || this.angle[1] >= 10000) {// Assign
									  // angle
									  // is
									  // greater
									  // than
									  // 1
									  // degree

		int loopcnt = 0;
		while (!(isDoneAng(0))) {
		    hold.makeHold(2); // Hold off
		    movePitch(-this.displacement[5]);
		    // Set Pitch to 0 first
		    Thread.sleep((long) (Math.abs((this.angle[0] - this.displacement[5]) / 100) / speed + 0.02));
		    loopcnt++;
		    if (loopcnt > 100) {
			System.out.println("Loop over 100");
			return;
		    }
		}
		loopcnt = 0;
		while (!(isDoneAng(1))) {
		    hold.makeHold(2); // Hold off
		    moveYaw(this.angle[1] - this.displacement[6]);
		    // Yaw set to assigned value
		    Thread.sleep((long) (Math.abs((this.angle[1] - this.displacement[6]) / 100) / speed + 0.02));
		    loopcnt++;
		    if (loopcnt > 100) {
			System.out.println("Loop over 100");
			return;
		    }
		}
		loopcnt = 0;
		while (!(isDoneAng(2))) {
		    hold.makeHold(2); // Hold off
		    movePitch(this.angle[0] - this.displacement[5]);
		    // Pitch set to assigned value
		    Thread.sleep((long) (Math.abs((this.angle[0] - this.displacement[5]) / 100) / speed + 0.02));
		    loopcnt++;
		    if (loopcnt > 100) {
			System.out.println("Loop over 100");
			return;
		    }
		}
		System.out.println("Robot has moved to specific Angles!");
	    } else {
		System.out.println("RobotMove.move() command not be set properly, please check.");
		return;
	    }
	} catch (NullPointerException e) {
	    System.out.println("You don't get the tool");
	}
    }

    private void moveTool() {
	int[] anglemodified = new int[3];
	int[] coordinatemodified = new int[3];
	coordinatemodified[0] = this.tool[2]; // X
	coordinatemodified[1] = this.tool[3]; // Y
	coordinatemodified[2] = this.tool[4]; // Z
	anglemodified[0] = this.tool[5]; // Rx
	anglemodified[1] = this.tool[6]; // Ry
	anglemodified[2] = this.tool[7]; // Rz
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand();
	RobotMove robot = new RobotMove(this.newCommand);
	try {
	    robot.sendint();
	    Thread.sleep(5);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    System.out.println("isDone in RobotMove Thread being interrupted");
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void moveRect() {
	int[] anglemodified = { 0, 0, 0 };
	int[] coordinatemodified = new int[3];
	coordinatemodified[0] = this.coordinate[0] - this.displacement[2]; // X
	coordinatemodified[1] = this.coordinate[1] - this.displacement[3]; // Y
	coordinatemodified[2] = this.coordinate[2] - this.displacement[4]; // Z
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand();
	RobotMove robot = new RobotMove(this.newCommand);
	try {
	    robot.sendint();
	    Thread.sleep(5);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    System.out.println("isDone in RobotMove Thread being interrupted");
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void moveYaw(int yaw) {
	this.isPitch = false; // Move Yaw
	int[] coordinatemodified = { 0, 0, 0 };
	int[] anglemodified = new int[3];
	anglemodified[0] = yaw;
	anglemodified[1] = 0;
	anglemodified[2] = 0;
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand(isPitch);
	RobotMove robot = new RobotMove(this.newCommand);
	try {
	    robot.sendint();
	    Thread.sleep(5);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    System.out.println("isDone in RobotMove Thread being interrupted");
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void movePitch(int pitch) {
	this.isPitch = true; // Move pitch
	int[] coordinatemodified = { 0, 0, 0 };
	int[] anglemodified = new int[3];
	anglemodified[0] = 0;
	anglemodified[1] = pitch;
	anglemodified[2] = 0;
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand(isPitch);
	RobotMove robot = new RobotMove(this.newCommand);
	try {
	    robot.sendint();
	    Thread.sleep(5);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    System.out.println("isDone in RobotMove Thread being interrupted");
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private byte[] generateCommand(boolean... argument) {
	/*
	 * Command for moving in angular To tell whether the command for
	 * Rectangular movement or angular only If there are no any Rectangular
	 * movement, then do not give any argument of boolean Boolean is to tell
	 * isYaw or not
	 */
	ArrayList<Byte> arraylist = new ArrayList<Byte>();
	// Command = [ Head,sub-head, Setting, Axis, Reserve, Type, Extend,
	// ToolNo, Coordinate, Extension];
	for (byte i : head)
	    arraylist.add(i);
	if (argument.length == 0 && !(this.moveTool)) {
	    for (byte i : suh)
		arraylist.add(i);
	    for (byte i : setting_rect)
		arraylist.add(i);
	} else if (argument.length == 1) {
	    // this.isPitch = argument[0];// Not useful for now
	    for (byte i : suh)
		arraylist.add(i);
	    for (byte i : setting_ang)
		arraylist.add(i);
	} else if (argument.length == 0 && this.moveTool) {
	    for (byte i : sub_tool)
		arraylist.add(i);
	    for (byte i : setting_rect)
		arraylist.add(i);
	}
	for (int i = 0; i < speedbyte.length; i++)
	    arraylist.add((41 + i), speedbyte[i]);
	for (byte i : coordinatebyte)
	    arraylist.add(i);
	for (byte i : anglebyte)
	    arraylist.add(i);
	for (byte i : Reserv)
	    arraylist.add(i);
	for (byte i : typeNumberbyte)
	    arraylist.add(i);
	for (byte i : Reserv)
	    arraylist.add(i);
	for (byte i : toolNumberbyte)
	    arraylist.add(i);
	for (byte i : coorbyte)
	    arraylist.add(i);
	for (byte i : InttoByteArray(extension))
	    arraylist.add(i);

	byte[] returnbyte = new byte[arraylist.size()];
	for (int i = 0, len = arraylist.size(); i < len; i++) {
	    if (arraylist.get(i) != null) {
		returnbyte[i] = arraylist.get(i);
	    }
	}
	/*
	 * for(int i = 45; i < 77; i++){
	 * System.out.println("The Command in byte form " + i + " is :  " +
	 * returnbyte[i]); }
	 */
	// deBug ArrayList
	return returnbyte;
    }
}