/*
 * Modified on Dec. 14, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class RobotMove extends SendUDP{

    private boolean isPitch = false;
    private static final byte[] head = { 89, 69, 82, 67, 32, 00, 104, 00, 03, 01, 00, 00, 00, 00, 00, 00, 57, 57, 57,
	    57, 57, 57, 57, 57 };// Header part
    private static final byte[] suh = { -118, -16, -16, 0, -16, 2, 0, 0 };// Sub-header
    // part
    private byte[] newCommand = new byte[] {};
    private byte[] setting_rect = { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 23 };
    // Setting of Speed to 1 mm/s and Cartesian coordinate(17)
    private byte[] setting_ang = { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 25 };
    // Setting of Speed to 1 degree/s and Cartesian coordinate(19)
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
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[0];
	this.typeNumber = tool[1];
	// Assigning difference of coordinate and angle
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 0:
		this.coordinate[i] = xtar * 1000;
		this.angle[i] = phitar * 100; // pitch
		break;
	    case 1:
		this.coordinate[i] = ytar * 1000;
		this.angle[i] = thetatar * 100; // yaw
		break;
	    case 2:
		this.coordinate[i] = ztar * 1000;
		this.angle[i] = 0;
		break;
	    }
	}
	initialize(speedin);
    }

    // constructor of simply move theta and phi
    public RobotMove(int phitar, int thetatar, int[] tool, int speedin) {
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[0];
	this.typeNumber = tool[1];
	// Assigning difference of coordinate and angle
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 0:
		this.coordinate[i] = 0;
		this.angle[i] = phitar * 100; // pitch in 0.01 degree
		break;
	    case 1:
		this.coordinate[i] = 0;
		this.angle[i] = thetatar * 100; // yaw in 0.01 degree
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
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[0];
	this.typeNumber = tool[1];
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
	isDone();
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
	for (int i = 2; i < 5; i++) {
	    if (Math.abs(this.displacement[2]) >= 1000 || Math.abs(this.displacement[3]) >= 1000
		    || Math.abs(this.displacement[4]) >= 1000 || Math.abs(this.displacement[5]) >= 100
		    || Math.abs(this.displacement[6]) >= 100) {
		this.isDone = false;
		break;
	    } else {
		this.isDone = true;
	    }
	}

	return this.isDone;
    }

    public boolean isDoneRec() {
	robotDisplacement();
	if (Math.abs(this.displacement[2]) >= 1000 || Math.abs(this.displacement[3]) >= 1000
		|| Math.abs(this.displacement[4]) >= 1000) {
	    this.isDone = false;
	} else {
	    this.isDone = true;
	}
	System.out.printf("Remaining Displacement  X : %d  Y : %d  Z : %d ", this.coordinate[0], this.coordinate[1], this.coordinate[2]);
	return this.isDone;
    }

    public boolean isDoneAng() {
	robotDisplacement();
	if (Math.abs(this.displacement[5]) >= 100 || Math.abs(this.displacement[6]) >= 100) {
	    this.isDone = false;
	} else {
	    this.isDone = true;
	}
	System.out.println("Remaining Angular Pitch : " + Math.floor(this.displacement[5]/1000) + "Yaw : " + Math.floor(this.displacement[6]/1000));
	return this.isDone;
    }

    public void robotDisplacement() {
	int[] toolnow = read();
	int tX = toolnow[2];
	int tY = toolnow[3];
	int tZ = toolnow[4];
	
	this.displacement[2] = (toolnow[2] - this.tool[2]) / 1000;
	this.displacement[3] = (toolnow[3] - this.tool[3]) / 1000;
	this.displacement[4] = (toolnow[4] - this.tool[4]) / 1000;
	// Pitch value
	if (Math.abs(tZ) <= Math.abs(tX))
	    this.displacement[5] = (-(9000 + tY/100));
	else if (Math.abs(tZ) > Math.abs(tX))
	    this.displacement[5] = (9000 + tY/100);
	// Yaw value
	if (tX >= 0 && tX * tZ >= 0) {
	    this.displacement[6] = (tX/100 + tZ/100 - 18000);
	} else if (tX < 0 && tZ >= 0) {
	    if (Math.abs(tX) > Math.abs(tZ))
		this.displacement[6] = ((18000 + tZ/100) - Math.abs(tX)/100);
	    else
		this.displacement[6] = (tZ/100 - (18000 - tX/100));
	} else if (tX > 0 && tZ < 0) {
	    if (Math.abs(tZ) > Math.abs(tX))
		this.displacement[6] = (tX/100 + (18000 - Math.abs(tZ)/100));
	    else
		this.displacement[6] = (-(18000 - tX/100 - tZ/100));
	} else if (tX < 0 && tZ < 0) {
	    this.displacement[6] = (tX/100 + tZ/100 + 18000);
	// Tz value
	}
	this.displacement[7] = 0;
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
	try{
	if (this.coordinate[0] >= 100 || this.coordinate[1] >= 100 || this.coordinate[2] >= 100) {
	    while (!(isDoneRec())) {
		moveRect();// Move to the position first
	    }
	} else if (this.angle[0] >= 100 || this.angle[1] >= 100 || this.angle[2] >= 100) {
	    while (!(isDoneAng())) {
		
		movePitch(0); // Set Pitch to 0 first
	    }
	    while (!(isDoneAng())) {
		moveYaw(this.angle[0]); // Yaw set to assigned value
	    }
	    while (!(isDoneAng())) {
		movePitch(this.angle[1]); // Pitch set to assigned value
	    }
	}
	} catch(NullPointerException e){
	    System.out.println("You don't get the tool");
	}
    }

    private void moveRect() {
	int[] anglemodified = { 0, 0, 0 };
	int[] coordinatemodified = new int[3];
	coordinatemodified[0] = this.coordinate[0];
	coordinatemodified[1] = this.coordinate[1];
	coordinatemodified[2] = this.coordinate[2];
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
	anglemodified[0] = 0;
	anglemodified[1] = yaw;
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
	anglemodified[0] = pitch;
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
	for (byte i : suh)
	    arraylist.add(i);
	if (argument.length == 0) {
	    for (byte i : setting_rect)
		arraylist.add(i);
	    for (int i = 0; i < speedbyte.length; i++)
		arraylist.add((39 + i), speedbyte[i]);
	} else if (argument.length == 1) {
	    this.isPitch = argument[0];// Not useful for now
	    for (byte i : setting_ang)
		arraylist.add(i);
	    for (int i = 0; i < speedbyte.length; i++)
		arraylist.add((39 + i), speedbyte[i]);
	}
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
	//System.out.println("The Command in byte form is : " + returnbyte);//
	// deBug ArrayList
	return returnbyte;
    }
}