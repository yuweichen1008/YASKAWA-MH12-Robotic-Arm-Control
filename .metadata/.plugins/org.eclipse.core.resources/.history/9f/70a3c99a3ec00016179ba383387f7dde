import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class RobotMove extends SendUDP{

    private boolean isYaw = false;
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
    private Integer x = new Integer(0);
    private Integer y = new Integer(0);
    private Integer z = new Integer(0);
    private Integer yaw = new Integer(0);
    private Integer pitch = new Integer(0);
    private Integer zr = new Integer(0);
    private byte[] Reserv = { 0, 0, 0, 0 };
    private Integer toolNumber = new Integer(0);
    private Integer typeNumber = new Integer(0);
    private int[] coor = new int[] { 3 };
    private int[] extension = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; 
    	// Axis = Tool(9:32);  X, Y, Z, TX, TY, TZ
    private int[] coordinate = new int[3];
    private int[] angle = new int[3];
    private int[] tool = new int[8];
    private byte[] speedbyte;
    private byte[] toolNumberbyte;
    private byte[] typeNumberbyte;
    private byte[] coordinatebyte;
    private byte[] anglebyte;
    private byte[] coorbyte;
    private boolean isDone = false;
    // constructor of assigning X, Y, Z, and angle values
    public RobotMove(int xin, int yin, int zin, int theta, int phi, int[] tool, int speedin) {
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[0];
	this.typeNumber = tool[1];
	this.x = tool[2];
	this.y = tool[3];
	this.z = tool[4];
	this.yaw = tool[5];
	this.pitch = tool[6];
	this.zr = tool[7];
	// Assigning X,Y,Z,Tx,Ty,Tz
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 1:
		this.coordinate[i] = this.x - xin;
		this.angle[i] = this.yaw - theta;
		break;
	    case 2:
		this.coordinate[i] = this.y - yin;
		this.angle[i] = this.pitch - phi;
		break;
	    case 3:
		this.coordinate[i] = this.z - zin;
		this.angle[i] = this.zr;
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
	this.x = tool[2];
	this.y = tool[3];
	this.z = tool[4];
	this.zr = tool[7];
	this.yaw = tool[5];
	this.pitch = tool[6];
	this.zr = tool[7];
	// Assigning X,Y,Z,Tx,Ty,Tz
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 1:
		this.coordinate[i] = this.x;
		this.angle[i] = this.yaw;
		break;
	    case 2:
		this.coordinate[i] = this.y;
		this.angle[i] = this.pitch;
		break;
	    case 3:
		this.coordinate[i] = this.z;
		this.angle[i] = this.zr;
		break;
	    }
	}
	initialize(speedin);
    }

    // constructor of simply move theta and phi
    public RobotMove(int theta, int phi, int[] tool, int speedin) {
	// Basic setting
	this.tool = tool;
	this.toolNumber = tool[0];
	this.typeNumber = tool[1];
	this.x = tool[2];
	this.y = tool[3];
	this.z = tool[4];
	this.yaw = tool[5];
	this.pitch = tool[6];
	this.zr = tool[7];
	for (int i = 0; i < 3; i++) {
	    switch (i) {
	    case 0:
		this.coordinate[i] = this.x;
		this.angle[i] = this.yaw - theta;
		break;
	    case 1:
		this.coordinate[i] = this.y;
		this.angle[i] = this.pitch - phi;
		break;
	    case 2:
		this.coordinate[i] = this.z;
		this.angle[i] = this.zr;
		break;
	    }
	}
	initialize(speedin);
    }

    // inner constructor to send command
    private RobotMove(byte[] generatedCommand) {
	super(generatedCommand); // Assign Command to SendUDP
    }

    // methods to get coordinate
    public int[] getCoordinate() {
	return this.coordinate;
    }

    // methods to get angle
    public int[] getAngle() {
	return this.angle;
    }

    // method to get Tool
    public int[] getTool() {
	return this.tool;
    }

    public Boolean isDone() {
	RobotMove robot = new RobotMove(newCommand);
	try {
	    robot.sendint();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return this.isDone;
    }
    
    public int[] robotDisplacement(){
	int[] displacement = new int[6];
	RobotReadPosition tg = new RobotReadPosition();
	int[] toolnow = tg.read();
	for(int j =0; j <6 ; j++){
	    displacement[j] = toolnow[j+2];   
	}	
	return displacement;
    }
    // move function(main function)
    public void move() throws Exception {
	// Check whether there are placement first. If there are changes in
	// placement, the function will stop after change the
	// placement rather than move angular, you would have to call the
	// function again.
	System.out.printf("Coordinate is  X : %d  Y : %d  Z : %d Tz : %d Ty : %d Tz : %d \n\n", this.coordinate[0],
		this.coordinate[1], this.coordinate[2], this.angle[0], this.angle[1], this.angle[2]);
	// To tell whether to move coordinate and move robot to the place first
	if (this.x == this.tool[2] && this.y == this.tool[3] && this.z == this.tool[z]) {
	    while (!(isDone())) {
		moveRect();// Move to the position first
	    }
	} else {
	    while (!(isDone())) {
		movePitch(0); // Set Pitch to 0 first
	    }
	    while (!(isDone())) {
		moveYaw(this.angle[0]); // Yaw set to assigned value
	    }
	    while (!(isDone())) {
		movePitch(this.angle[1]); // Pitch set to assigned value
	    }
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
	this.newCommand = generateCommand(isYaw);
    }

    private void moveYaw(int yaw) {
	this.isYaw = true; // Move Yaw
	int[] anglemodified = new int[3];
	int[] coordinatemodified = { 0, 0, 0 };
	anglemodified[0] = yaw;
	anglemodified[1] = 0;
	anglemodified[2] = 0;
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand(isYaw);
    }

    private void movePitch(int pitch) {
	this.isYaw = false; // Move pitch
	int[] anglemodified = new int[3];
	int[] coordinatemodified = { 0, 0, 0 };
	anglemodified[0] = 0;
	anglemodified[1] = pitch;
	anglemodified[2] = 0;
	this.anglebyte = InttoByteArray(anglemodified);
	this.coordinatebyte = InttoByteArray(coordinatemodified);
	this.newCommand = generateCommand(isYaw);
    }

    private byte[] generateCommand(boolean... argument) {
	/*
	 * Command for moving in angular To tell whether the command for
	 * Rectangular movement or angular only If there are no any Rectangular
	 * movement, then do not give any argument of boolean Boolean is to tell
	 * isYaw or not
	 */
	ArrayList<Byte> arraylist = new ArrayList<Byte>();
	byte[] returnbyte = new byte[] {};
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
	    this.isYaw = argument[0];
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
	for (int i = 0, len = arraylist.size(); i < len; i++)
	    returnbyte[i] = arraylist.get(i);
	// System.out.println("The Command in byte form is : " + returnbyte);//
	// deBug ArrayList
	return returnbyte;
    }

    // This function is type-transfer function from int to byte[]
    private static byte[] InttoByteArrayS(int input) {
	boolean isEmpty = true;
	byte[] transfered = new byte[4];

	ByteBuffer byteBuffer = ByteBuffer.allocate(4);
	IntBuffer intBuffer = byteBuffer.asIntBuffer();
	intBuffer.put(input);
	transfered = byteBuffer.array();

	for (byte b : transfered) {
	    if (b != 0) {
		isEmpty = false;
		break;
	    }
	}
	if (isEmpty) {
	    return null;
	} else {
	    return transfered;
	}
    }

    private void initialize(int speedin) {
	this.speed = speedin;
	this.speedbyte = InttoByteArrayS(speed);
	this.toolNumberbyte = InttoByteArrayS(toolNumber);
	this.typeNumberbyte = InttoByteArrayS(typeNumber);
	this.coordinatebyte = InttoByteArray(coordinate);
	this.anglebyte = InttoByteArray(angle);
	this.coorbyte = InttoByteArray(coor);
    }

}