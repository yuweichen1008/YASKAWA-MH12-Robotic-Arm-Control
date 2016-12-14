/*
 * Modified on Dec. 14, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
import java.io.IOException;

public class JavaRobot extends SendUDP {
    private boolean botReady = false;
    private boolean initial = false;
    private RobotAngle targetAngle;
    private RobotPosition targetPosition = new RobotPosition();
    private RobotAngle currentAngle;
    private RobotPosition currentPosition;
    private int[] tool = new int[8];
    private int speed = 100; // default 100

    // Constructor with no value
    public JavaRobot() {
	this.setInitial();
	this.setReady(true); // Ready to send command
    }

	/*
	 * Move to function 
	 * length == 0 : (move to tool)
	 * length == 1 : speed(move to tool) 
	 * length == 2 : yaw/pitch 
	 * length == 3 : yaw/pitch/speed
	 * length == 5 : X/Y/Z/yaw/pitch 
	 * length == 6 : X/Y/Z/yaw/pitch/speed
	 */
    public void moveTo(int... anglespeedposition) {
	if (!(this.initial)) {
	    System.out.println("Please initialize the JavaRobot first");
	    return;
	}
	this.setReady(false);
	if (anglespeedposition.length == 0) { // Move to tool
	    this.targetPosition.setX(0);
	    this.targetPosition.setY(0);
	    this.targetPosition.setZ(0);
	    this.targetAngle.setTheta(0);
	    this.targetAngle.setPhi(0);
	} else if (anglespeedposition.length == 1) { // Move to tool with speed
	    this.targetPosition.setX(0);
	    this.targetPosition.setY(0);
	    this.targetPosition.setZ(0);
	    this.targetAngle.setTheta(0);
	    this.targetAngle.setPhi(0);
	    this.speed = anglespeedposition[0];
	} else if (anglespeedposition.length == 2) { // Move yaw and pitch
	    this.targetPosition.setX(0);
	    this.targetPosition.setY(0);
	    this.targetPosition.setZ(0);
	    this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
	} else if (anglespeedposition.length == 3) { 
	    // Move yaw and pitch with speed
	    this.targetPosition.setX(0);
	    this.targetPosition.setY(0);
	    this.targetPosition.setZ(0);
	    this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
	    this.speed = anglespeedposition[3];
	} else if (anglespeedposition.length == 5) { 
	    // Move X, Y, Z, yaw and  pitch
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	} else if (anglespeedposition.length == 6) { 
	    // Move X, Y, Z, yaw and pitch with speed
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	    this.speed = anglespeedposition[5];
	} else {
	    System.out.println("You didn't enter the right arguments.");
	    return;
	}
	int count = 0;
	try {
	    while (!(getReady())) {
		setReady(robotReady());
		if (this.targetPosition.getPosition()[0] !=0 && this.targetPosition.getPosition()[1] !=0 && this.targetPosition.getPosition()[2] !=0) {
		    RobotMove rm = new RobotMove(targetPosition.getPosition()[0], targetPosition.getPosition()[1],
			    targetPosition.getPosition()[2],targetAngle.getPhi(), targetAngle.getTheta(),this.tool, this.speed);
		    rm.move();
		    count++;
		} else if (this.targetPosition.getPosition()[0] ==0 && this.targetPosition.getPosition()[1] ==0 && this.targetPosition.getPosition()[2] ==0 && this.targetAngle.getTheta() != 0 && this.targetAngle.getPhi() != 0) {
		    RobotMove rm = new RobotMove(targetAngle.getPhi(),targetAngle.getTheta(), this.tool, this.speed);
		    rm.move();
		    count++;
		} else if (this.targetPosition.getPosition()[0] ==0 && this.targetPosition.getPosition()[1] ==0 && this.targetPosition.getPosition()[2] ==0 && this.targetAngle.getTheta() == 0 && this.targetAngle.getPhi() == 0) {
		    RobotMove rm = new RobotMove(this.tool, this.speed);
		    rm.move();
		    count++;
		} else {
		    setReady(true);
		    System.out.println(
			    "We cannot get the tool information from Robot, please check the connection or contact Y.W. Chen");
		    break;
		}
		Thread.sleep(100);// pause
		System.out.println("Pause " + count + " times");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
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
	this.tool = read();
	if (this.tool == null) {
	    System.out.println("Didn't get the tool.");
	} else {
	    System.out.println("Form is " + this.tool[0]);
	    System.out.println("Tool is " + this.tool[1]);
	    System.out.println("X is " + this.tool[2]);
	    System.out.println("Y is " + this.tool[3]);
	    System.out.println("Z is " + this.tool[4]);
	    System.out.println("Yaw is " + this.tool[5]);
	    System.out.println("Pitch is " + this.tool[6]);
	    System.out.println("Zr is " + this.tool[7]);
	    this.initial = true;
	}
    }

    public boolean getReady() {
	return botReady;
    }
    
    
    //Function for users to ask the current position
    public void ask(){
	if (this.currentPosition.getPosition()[0] <= 1000 && this.currentPosition.getPosition()[1] <= 1000
		&& this.currentPosition.getPosition()[2] <= 1000) {
	    System.out.println("X is : " + this.currentPosition.getPosition()[0] + ", Y is : "
		    + this.currentPosition.getPosition()[1] + ", Z is : " + this.currentPosition.getPosition()[2]);
	    System.out.println(
		    "Pitch is : " + this.currentAngle.getPhi() + " and Yaw is : " + this.currentAngle.getTheta());
	} else {
	    System.out.println(
		    "Pitch is : " + this.currentAngle.getPhi() + " and Yaw is : " + this.currentAngle.getTheta());
	}
	
    }
    
    private boolean robotReady() {
	askCurrent();
	// function to calculate the difference between target and current
	return isCloseTo(); // return the boolean value of previous description
    }

    public void askCurrent() {
	int[] displacement = new int[6];
	int[] toolnow = read();
	int tX;
	int tY;
	int tZ;
	 this.currentPosition = new RobotPosition(0, 0, 0);
	 this.currentAngle = new RobotAngle(0, 0);
	if (toolnow.length == 1 || toolnow == null) {
	    // prevent the error code
	    // prevent the error code
	    System.out.println("No response");
	    return;
	} else {
	    try {
		// Target - Current
		displacement[0] = (toolnow[2] - this.tool[2]);
		// X displacement
		displacement[1] = (toolnow[3] - this.tool[3]);
		// Y displacement
		displacement[2] = (toolnow[4] - this.tool[4]);
		// Z displacement
		// in 0.01 degree
		tX = toolnow[2] / 100;
		tY = toolnow[3] / 100;
		tZ = toolnow[4] / 100;
		// Pitch value
		if (Math.abs(tZ) <= Math.abs(tX))
		    this.currentAngle.setPhi(-(9000 + tY));
		else if (Math.abs(tZ) > Math.abs(tX))
		    this.currentAngle.setPhi((9000 + tY));
		// Yaw value
		if (tX >= 0 && tX * tZ >= 0) {
		    this.currentAngle.setTheta(tX + tZ - 18000);
		} else if (tX < 0 && tZ >= 0) {
		    if (Math.abs(tX) > Math.abs(tZ))
			this.currentAngle.setTheta((18000 + tZ) - Math.abs(tX));
		    else
			this.currentAngle.setTheta(tZ - (18000 - tX));
		} else if (tX > 0 && tZ < 0) {
		    if (Math.abs(tZ) > Math.abs(tX))
			this.currentAngle.setTheta(tX + (18000 - Math.abs(tZ)));
		    else
			this.currentAngle.setTheta(-(18000 - tX - tZ));
		} else if (tX < 0 && tZ < 0) {
		    this.currentAngle.setTheta(tX + tZ + 18000);
		}
		this.currentPosition = new RobotPosition(displacement[0], displacement[1], displacement[2]);
		System.out.println("X is : " + (this.currentPosition.getPosition()[0] / 1000) + ", Y is : "
			+ (this.currentPosition.getPosition()[1] / 1000) + ", Z is : "
			+ (this.currentPosition.getPosition()[2] / 1000));
	    } catch (NullPointerException e) {
		throw new IllegalStateException("A response has a null property", e);
	    }
	}
    }

    public int[] read() {
	RobotReadPosition robotread = new RobotReadPosition();
	int[] toolout = robotread.read(); // To store the outcome of Tool
	return toolout;
    }

    public boolean isCloseTo() {
	if (!(this.initial) ) {
	    System.out.println("Please initialize the JavaRobot first");
	    return false;
	}
	// If not assigning any x, y and z displacement
	if (targetPosition.getPosition()[0] == 0 && targetPosition.getPosition()[1] == 0 && targetPosition.getPosition()[2] == 0) {
	    return (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100) 		// Pitch
		    && (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100); 	// Yaw
	} else {
	    return (Math.abs(currentPosition.getPosition()[0] - targetPosition.getPosition()[0]) < 1000) // X
		    && (Math.abs(currentPosition.getPosition()[1] - targetPosition.getPosition()[1]) < 1000) // Y
		    && (Math.abs(currentPosition.getPosition()[2] - targetPosition.getPosition()[2]) < 1000) // Z
		    && (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100) 		// Pitch
		    && (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100); 	// Yaw
	}
    }

    public void initialize() {
	setInitial();
	if (!(this.initial)) {
	    System.out.println("Initialize failed!");
	}
    }

    // Inner class of Tool store information
    class Tool {
	private int toolnumber;
	private int formnumber;
	private int tZ;
	private RobotPosition rectAngular;
	private RobotAngle angular;

	public Tool(int[] toolin) {
	    this.formnumber = toolin[0];
	    this.toolnumber = toolin[1];
	    rectAngular = new RobotPosition(toolin[2], toolin[3], toolin[4]);
	    angular = new RobotAngle(toolin[5], toolin[6]);

	    this.tZ = toolin[7];
	}

	public Tool() {

	}

	public void setrX(int input) {
	    rectAngular.setX(input);
	}

	public void setrY(int input) {
	    rectAngular.setY(input);
	}

	public void setrZ(int input) {
	    rectAngular.setZ(input);
	}

	public void setPitch(int input) {
	    angular.setPhi(input);
	}

	public void setYaw(int input) {
	    angular.setTheta(input);
	}

	public int[] getSetting() {
	    int[] setting = { formnumber, toolnumber };
	    return setting;
	}

	public int[] getPosition() {
	    return rectAngular.getPosition();
	}
	//Pitch
	public int getPhi() {
	    return angular.getPhi();
	}
	//Yaw
	public int getTheta() {
	    return angular.getTheta();
	}

	public int getZr() {
	    return this.tZ;
	}
    }

    // Inner class of RobotAngle contains yaw and pitch
    class RobotAngle {

	private int phi;
	private int theta;

	public RobotAngle(int phi,int theta) {
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
	    this.phi = angle;		//Pitch
	}
	
	public void setTheta(int angle) {
	    this.theta = angle;		//Yaw
	}

	public int getPhi() {
	    return this.phi;		//Pitch
	}
	
	public int getTheta() {
	    return this.theta;		//Yaw
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

    public static void servoOn() {
	Boolean sevo = JavaRobotServo.makeServo(1);// Servo ON
	if (sevo) {
	    System.out.println("Servo Start!");
	}
    }

    public static void servoOff() {
	Boolean sevo = JavaRobotServo.makeServo(2);// Servo OFF
	if (sevo) {
	    System.out.println("Servo Stop!");
	}
    }

    public Boolean servo(int i) {
	Boolean sevo = JavaRobotServo.makeServo(i);// 1 : Servo ON / 2: Servo
						   // OFF
	if (sevo && i == 1) {
	    System.out.println("Servo Start!");
	} else if (!(sevo) && i == 2) {
	    System.out.println("Servo Off!");
	} else {
	    System.out.println("Servo failure");
	}
	return sevo;
    }

    public Boolean alert(int i) {
	String alert = JavaRobotAlert.makeAlert(i);// 1 : Read Alert / 2 : Reset
						   // Alert
	Boolean returnBoolean = new Boolean(true);
	if ((!"".equals(alert))) {
	    returnBoolean = true;// Not any Alert happened
	} else {
	    returnBoolean = false;// There are Alert happened
	}
	return returnBoolean;
    }

    public static void holdOn() {
	Boolean hold = JavaRobotHold.makeHold(1);// 1 : Hold ON
	if (hold) {
	    System.out.println("Hold ON! Yoo-hoo-hoo!");
	}
    }

    public static void holdOff() {
	Boolean hold = JavaRobotHold.makeHold(2);// 2 : Hold OFF
	if (hold) {
	    System.out.println("Hold OFF!");
	}
    }

    public Boolean hold(int i) {
	Boolean sevo = JavaRobotHold.makeHold(i);// 1 : Hold ON / 2 : Hold OFF
	if (sevo && i == 1) {
	    System.out.println("Hold ON! Yoo-hoo-hoo!");
	} else if (!(sevo) && i == 2) {
	    System.out.println("Hold Off!");
	} else {
	    System.out.println("Hold failure");
	}
	return sevo;
    }
}
