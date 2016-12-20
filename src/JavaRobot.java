
/*
 * Modified on Dec. 14, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.IOException;

public class JavaRobot extends SendUDP {
    private boolean botReady = false;
    private boolean initial = false;
    private boolean moveTool = false;
    private RobotAngle targetAngle;
    private RobotPosition targetPosition;
    private RobotAngle currentAngle;
    private RobotPosition currentPosition;
    private int[] tool = new int[8];
    private int speed = 100; // default 100
    private int rX = 0;
    private int rY = 0;
    private int rZ = 0;
    // Constructor with no value
    public JavaRobot() {
	this.setInitial();
	this.setReady(true); // Ready to send command
    }

    /*
     * Move to function length == 0 : (move to tool) length == 1 : speed(move to
     * tool) length == 2 : yaw/pitch length == 3 : yaw/pitch/speed length == 5 :
     * X/Y/Z/yaw/pitch length == 6 : X/Y/Z/yaw/pitch/speed
     */
    public void moveTo(int... anglespeedposition) {
	if (!(this.initial)) {
	    System.out.println("Please initialize the JavaRobot first");
	    return;
	}
	this.setReady(false);
	if (anglespeedposition.length == 0) { 
	    // Move to tool
	    this.moveTool = true;
	} else if (anglespeedposition.length == 1) { // Move to tool with speed
	    this.speed = anglespeedposition[0];
	    this.moveTool = true;
	} else if (anglespeedposition.length == 2) { // Move yaw and pitch
	    this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
	} else if (anglespeedposition.length == 3) {
	    // Move yaw and pitch with speed
	    this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
	    this.speed = anglespeedposition[2];
	    this.moveTool = false;
	} else if (anglespeedposition.length == 5) {
	    // Move X, Y, Z, yaw and pitch
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	    this.moveTool = false;
	} else if (anglespeedposition.length == 6) {
	    // Move X, Y, Z, yaw and pitch with speed
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	    this.speed = anglespeedposition[5];
	    this.moveTool = false;
	} else {
	    System.out.println("You didn't enter the right arguments.");
	    return;
	}
	int count = 0;
	try {
	    while (!(getReady())) {
		setReady(robotReady()); // To tell whether to move
		if (this.targetPosition.getPosition()[0] != 0 && this.targetPosition.getPosition()[1] != 0
			&& this.targetPosition.getPosition()[2] != 0) {
		    RobotMove rm = new RobotMove(targetPosition.getPosition()[0], targetPosition.getPosition()[1],
			    targetPosition.getPosition()[2], targetAngle.getPhi(), targetAngle.getTheta(), this.tool,
			    this.speed);
		    rm.move();

		} else if (this.targetPosition.getPosition()[0] == 0 && this.targetPosition.getPosition()[1] == 0
			&& this.targetPosition.getPosition()[2] == 0
			&& (this.targetAngle.getPhi() != 0 || this.targetAngle.getTheta() != 0)) {
		    RobotMove rm = new RobotMove(targetAngle.getPhi(), targetAngle.getTheta(), this.tool, this.speed);
		    rm.move();

		} else if (this.targetPosition.getPosition()[0] == 0 && this.targetPosition.getPosition()[1] == 0
			&& this.targetPosition.getPosition()[2] == 0 && this.targetAngle.getPhi() == 0
			&& this.targetAngle.getTheta() == 0 && this.moveTool == true) {
		    RobotMove rm = new RobotMove(this.tool, this.speed);
		    rm.move();

		} else {
		    setReady(true);
		    System.out.println(
			    "Cannot get the tool information from Robot, please check the connection or contact Y.W. Chen");
		    break;
		}
		Thread.sleep(100);// pause
		System.out.println("Pause " + count++ + " times");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    System.out
		    .println("You don't get the tool, please check your connection. JavaRobot.moveTo() has stopped!!");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    // Bonus -- for the communicated with localhost with local store tool
    // information
    public void sendBackToOriginalPosition() {

    }

    private void setReady(Boolean input) {
	this.botReady = input;
    }

    // initialize JavaRobot with reading the tool(original position) first.
    private void setInitial() {
	this.targetPosition = new RobotPosition();
	this.currentPosition = new RobotPosition();
	this.targetAngle = new RobotAngle();
	this.currentAngle = new RobotAngle();
	this.tool = read();
	if (this.tool == null) {
	    System.out.println("Didn't get the tool.");
	} else {
	    System.out.println("Form is " + this.tool[0]);
	    System.out.println("Tool is " + this.tool[1]);
	    System.out.println("X is " + this.tool[2]);
	    System.out.println("Y is " + this.tool[3]);
	    System.out.println("Z is " + this.tool[4]);
	    System.out.println("Pitch is " + this.currentAngle.getPhi());
	    System.out.println("Yaw is " + this.currentAngle.getTheta());
	    this.initial = true;
	}
    }

    public boolean getReady() {
	return botReady;
    }

    // Function for users to ask the current position
    public void ask() {
	askCurrent();
	if (this.currentPosition.getPosition()[0] == 0 && this.currentPosition.getPosition()[1] == 0
		&& this.currentPosition.getPosition()[2] == 0 && this.currentAngle.getPhi() == 0
		&& this.currentAngle.getPhi() == 0) {
	    System.out.println("You don't get the tool");
	} else if (this.currentPosition.getPosition()[0] <= 1000 && this.currentPosition.getPosition()[1] <= 1000
		&& this.currentPosition.getPosition()[2] <= 1000) {
	    System.out.println("Current X is : " + this.currentPosition.getPosition()[0] / 1000 + " mm, Y is : "
		    + this.currentPosition.getPosition()[1] / 1000 + " mm, Z is : "
		    + this.currentPosition.getPosition()[2] / 1000 + " mm.");
	    System.out.println("Current Pitch is : " + this.currentAngle.getPhi() / 100 + " centidegrees and Yaw is : "
		    + this.currentAngle.getTheta() / 100 + " centidegrees.");
	} else {
	    System.out.println("Current Pitch is : " + this.currentAngle.getPhi() / 100 + " centidegrees and Yaw is : "
		    + this.currentAngle.getTheta() / 100 + " centidegrees.");
	}

    }

    private boolean robotReady() {
	askCurrent();
	// function to calculate the difference between target and current
	return isCloseTo(); // return the boolean value of previous description
    }

    private void askCurrent() {
	int[] displacement = new int[6];
	int[] toolnow = read();
	int tX;
	int tY;
	int tZ;
	if (toolnow == null || toolnow.length == 1) {
	    return;
	} else {

	    if (toolnow.length == 1 || toolnow == null) {
		// prevent the error code
		System.out.println("No response");
		return;
	    } else {
		try {
		    // Target - Current in 1 mm
		    displacement[0] = (toolnow[2] - this.tool[2]);// X
		    displacement[1] = (toolnow[3] - this.tool[3]);// Y
		    displacement[2] = (toolnow[4] - this.tool[4]);// Z
		    // in 0.01 degree
		    tX = toolnow[2] / 100;
		    tY = toolnow[3] / 100;
		    tZ = toolnow[4] / 100;

		    // Pitch value
		    if (Math.abs(tZ) <= Math.abs(tX))
			displacement[3] = -(9000 + tY);
		    else if (Math.abs(tZ) > Math.abs(tX))
			displacement[3] = (9000 + tY);
		    // Yaw value
		    if (tX >= 0 && tZ >= 0) {
			displacement[4] = (tX + tZ - 18000);
		    } else if (tX >= 0 && tZ < 0) {
			if (Math.abs(tZ) > Math.abs(tX))
			    displacement[4] = (tX + tZ + 18000);
			else
			    displacement[4] = (tX + tZ - 18000);
		    } else if (tX < 0 && tZ >= 0) {
			if (Math.abs(tX) > Math.abs(tZ))
			    displacement[4] = (tX + tZ + 18000);
			else
			    displacement[4] = (tX + tZ - 18000);
		    } else if (tX < 0 && tZ < 0) {
			displacement[4] = (tX + tZ + 18000);

		    }
		    // Tz value
		    this.currentPosition.setX(displacement[0]);
		    this.currentPosition.setX(displacement[1]);
		    this.currentPosition.setX(displacement[2]);
		    this.currentAngle.setPhi(displacement[3]);
		    this.currentAngle.setTheta(displacement[4]);
		    this.rX = toolnow[5];
		    this.rY = toolnow[6];
		    this.rZ = toolnow[7];
		    System.out.println("Current X is : " + (this.currentPosition.getPosition()[0] / 1000)
			    + " mm, Y is : " + (this.currentPosition.getPosition()[1] / 1000) + " mm, Z is : "
			    + (this.currentPosition.getPosition()[2] / 1000) + " mm.");
		    System.out.println("Pitch is : " + this.currentAngle.getPhi() / 100 + " centidegrees and Yaw is : "
			    + this.currentAngle.getTheta() / 100 + " centidegrees.");
		} catch (NullPointerException e) {
		    throw new IllegalStateException("A response has a null property", e);
		}
	    }
	}
    }

    public int[] read() {
	RobotReadPosition robotread = new RobotReadPosition();
	int[] toolout = robotread.read(); // To store the outcome of Tool
	return toolout;
    }

    public boolean isCloseTo() {
	if (!(this.initial)) {
	    System.out.println("Please initialize the JavaRobot first");
	    return false;
	}
	// If not assigning any x, y and z displacement
	if(this.moveTool == true){
	    return(Math.abs(currentPosition.getPosition()[0] - this.tool[2]) < 1000) // X
		    && (Math.abs(currentPosition.getPosition()[1] - this.tool[3]) < 1000) // Y
		    && (Math.abs(currentPosition.getPosition()[2] - this.tool[4]) < 1000) // Z
		    && (Math.abs(this.rX - this.tool[5]) < 1000) // rX
		    && (Math.abs(this.rY - this.tool[6]) < 1000) // rY
		    && (Math.abs(this.rZ - this.tool[7]) < 1000);
	}else if (targetPosition.getPosition()[0] == 0 && targetPosition.getPosition()[1] == 0
		&& targetPosition.getPosition()[2] == 0 && this.moveTool == false) {
	    return (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100) // Pitch
		    && (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100); // Yaw
	} else {
	    return (Math.abs(currentPosition.getPosition()[0] - targetPosition.getPosition()[0]) < 1000) // X
		    && (Math.abs(currentPosition.getPosition()[1] - targetPosition.getPosition()[1]) < 1000) // Y
		    && (Math.abs(currentPosition.getPosition()[2] - targetPosition.getPosition()[2]) < 1000) // Z
		    && (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100) // Pitch
		    && (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100); // Yaw
	}
    }

    public void initialize() {
	setInitial();
	if (!(this.initial)) {
	    System.out.println("Initialize failed!");
	}
    }

    // Inner class of RobotAngle contains yaw and pitch
    class RobotAngle {

	private int phi;
	private int theta;

	public RobotAngle(int phi, int theta) {
	    this.phi = phi;
	    this.theta = theta;
	}

	public RobotAngle() {
	    // Initialize the RobotAngle with (0,0)
	    this.phi = 0;
	    this.theta = 0;
	}

	// basic void function set and get angle
	public void setPhi(int angle) {
	    this.phi = angle; // Pitch
	}

	public void setTheta(int angle) {
	    this.theta = angle; // Yaw
	}

	public int getPhi() {
	    return this.phi; // Pitch
	}

	public int getTheta() {
	    return this.theta; // Yaw
	}

    }

    // Inner class of Robot Position contains X, Y and Z
    public class RobotPosition {
	private int X;
	private int Y;
	private int Z;
	private int[] robotposi = new int[3];

	public RobotPosition(int X, int Y, int Z) {
	    this.X = X;
	    this.Y = Y;
	    this.Z = Z;
	}

	public RobotPosition() {
	    this.X = 0;
	    this.Y = 0;
	    this.Z = 0;
	}

	// basic void to set and get position
	public void setX(int position) {
	    this.X = position;
	}

	public void setY(int position) {
	    this.Y = position;
	}

	public void setZ(int position) {
	    this.Z = position;
	}

	public int[] getPosition() {
	    this.robotposi[0] = X;
	    this.robotposi[1] = Y;
	    this.robotposi[2] = Z;
	    return this.robotposi;
	}
    }
}
