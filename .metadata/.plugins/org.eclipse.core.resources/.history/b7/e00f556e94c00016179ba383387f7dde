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
			case 1:
				this.coordinate[i] = xtar - tool[2];
				this.angle[i] = phitar - tool[6];		//pitch
				break;
			case 2:
				this.coordinate[i] = ytar - tool[3];
				this.angle[i] = thetatar - tool[5];		//yaw
				break;
			case 3:
				this.coordinate[i] = ztar - tool[4];
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
			case 1:
				this.coordinate[i] = 0;
				this.angle[i] = phitar - tool[6];		//pitch
				break;
			case 2:
				this.coordinate[i] = 0;
				this.angle[i] = thetatar - tool[5];		//yaw
				break;
			case 3:
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
		int[] diffPosition = robotDisplacement();
		boolean rec = isDoneRec(diffPosition);
		boolean ang = isDoneAng(diffPosition);
		if (rec && ang) {
			setDone(true);
		} else {
			setDone(false);
		}

		return this.isDone;
	}

	public boolean isDoneRec(int[] diffPosition) {
		boolean outBool = false;
		for (int i = 2; i < 5; i++) {
			// Assign difference of coordinate and angle
			this.coordinate[i - 2] = diffPosition[i];
			// Return the boolean value
			if (Math.abs(diffPosition[i]) >= 100) {
				return false;
			} else {
				outBool = true;
			}
		}
		return outBool;
	}

	public boolean isDoneAng(int[] diffPosition) {
		boolean outBool = false;
		for (int i = 5; i < 8; i++) {
			// Assign difference of coordinate and angle
			this.angle[i - 5] = diffPosition[i];
			// Return the boolean value
			if (Math.abs(diffPosition[i]) >= 100) {
				return false;
			} else {
				outBool = true;
			}
		}
		return outBool;
	}

	public int[] robotDisplacement() {
		int[] toolnow = read();
		for (int j = 2; j < 5; j++) {
			this.displacement[j] = (toolnow[j] - tool[j]) / 1000;
		}
		for (int k = 5; k < 8; k++) {
			this.displacement[k] = (toolnow[k] - tool[k]) / 100;
		}

		return this.displacement;
	}

	public int[] read() {
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
		System.out.printf("Coordinate is  X : %d  Y : %d  Z : %d Tz : %d Ty : %d Tz : %d \n\n", this.coordinate[0],
				this.coordinate[1], this.coordinate[2], this.angle[0], this.angle[1], this.angle[2]);
		// To tell whether to move coordinate and move robot to the place first
		if (this.coordinate[0] >= 100 || this.coordinate[1] >= 100 || this.coordinate[2] >= 100) {
			while (!(isDoneRec(this.displacement))) {
				moveRect();// Move to the position first
			}
		} else if (this.angle[0] >= 100 || this.angle[1] >= 100 || this.angle[2] >= 100) {
			while (!(isDoneAng(this.displacement))) {
				movePitch(0); // Set Pitch to 0 first
			}
			while (!(isDoneAng(this.displacement))) {
				moveYaw(this.angle[0]); // Yaw set to assigned value
			}
			while (!(isDoneAng(this.displacement))) {
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
		this.isPitch = false; // Move pitch
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
			this.isPitch = argument[0];//Not useful for now
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

	private void setDone(boolean in) {
		this.isDone = in;
	}
	
	//This function is to assign the Byte[] to each type of parameter which required in generate command
	private void initialize(int speedin) {
		isDone();
		this.speed = speedin;
		this.speedbyte = InttoByteArrayS(speed);
		this.toolNumberbyte = InttoByteArrayS(toolNumber);
		this.typeNumberbyte = InttoByteArrayS(typeNumber);
		this.coordinatebyte = InttoByteArray(coordinate);
		this.anglebyte = InttoByteArray(angle);
		this.coorbyte = InttoByteArray(coor);
	}

}