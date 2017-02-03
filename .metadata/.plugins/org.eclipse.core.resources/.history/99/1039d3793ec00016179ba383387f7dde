import java.io.IOException;

public class JavaRobot extends SendUDP {
    private Boolean botReady = false;
    private RobotAngle targetAngle;
    private RobotPosition targetPosition;
    private RobotAngle currentAngle;
    private RobotPosition currentPosition;
    private int[] tool = new int[8];
    private int speed = 1000;
    
    
    // Constructor with no value
    public JavaRobot() {
	this.setInitial();
	this.setReady(false);
    }

    /*
     * Move to function
     * length == 2 : yaw/pitch
     * length == 3¡@: yaw/pitch/speed
     * length == 5 : X/Y/Z/yaw/pitch
     * length == 6 : X/Y/Z/yaw/pitch/speed
     */
    public void moveTo(int... anglespeedposition) {
	this.setReady(false);
	if (anglespeedposition.length == 2) {
	    this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
	} else if (anglespeedposition.length == 3) {
	    this.speed = anglespeedposition[3];
	}else if(anglespeedposition.length == 5){
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	} else if (anglespeedposition.length == 6) {
	    this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
		    anglespeedposition[2]);
	    this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
	    this.speed = anglespeedposition[5];
	}
	int count = 0;
	try {
	    while (!(getReady())) {
		setReady(robotReady());
		if (this.targetPosition != null && this.tool != null) {
		    RobotMove rm = new RobotMove(targetPosition.getPosition()[0], targetPosition.getPosition()[1],
			    targetPosition.getPosition()[2], targetAngle.getPhi(), targetAngle.getTheta(), this.tool,
			    speed);
		    rm.move();
		} else if (this.targetPosition == null && this.tool != null) {
		    RobotMove rm = new RobotMove(targetAngle.getTheta(), targetAngle.getPhi(), this.tool, speed);
		    rm.move();
		} else {
		    setReady(true);
		    System.out.println("We cannot get the tool information from Robot, please check the connection or contact Y.W. Chen");
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
        
    //Bonus -- for the communicated with localhost with local store tool information
    public void sendBackToOriginalPosition(){
	
    }
    
    private void setReady(Boolean input) {
	this.botReady = input;
    }

    // initialize boolean function
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
	}
    }

    private boolean getReady() {
	return botReady;
    }
    
    private boolean robotReady(){
	robotDisplacement();
	return isCloseTo();
    }
    
    public void robotDisplacement(){
	int[] displacement = new int[6];
	int[] toolnow = read();
	if (toolnow == null) {
	    this.currentPosition = new RobotPosition(9999, 9999, 9999);
	    this.currentAngle = new RobotAngle(9999, 9999);
	} else {
	    displacement[0] = toolnow[2] - this.targetPosition.getPosition()[0]; // X
										 // displacement
	    displacement[1] = toolnow[3] - this.targetPosition.getPosition()[1]; // Y
										 // displacement
	    displacement[2] = toolnow[4] - this.targetPosition.getPosition()[2]; // Z
										 // displacement
	    displacement[3] = toolnow[5] - this.targetAngle.getPhi(); // Yaw
								      // displacement
	    displacement[4] = toolnow[6] - this.targetAngle.getTheta(); // Pitch
									// displacement
	    this.currentPosition = new RobotPosition(displacement[0], displacement[1], displacement[2]);
	    this.currentAngle = new RobotAngle(displacement[3], displacement[4]);
	}
    }

    public int[] read() {
	RobotReadPosition robotread = new RobotReadPosition();
	int[] toolout = robotread.read(); // To store the outcome of Tool
	return toolout;
    }

    public Boolean isCloseTo() {
	if (currentPosition.getPosition()[0] == 9999 && currentPosition.getPosition()[1] == 9999
		&& currentPosition.getPosition()[2] == 9999) {
	    return (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100)
		    && (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100);
	} else {
	    return (Math.abs(currentPosition.getPosition()[0] - targetPosition.getPosition()[0]) < 10)
		    && (Math.abs(currentPosition.getPosition()[1] - targetPosition.getPosition()[1]) < 10)
		    && (Math.abs(currentPosition.getPosition()[2] - targetPosition.getPosition()[2]) < 10)
		    && (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100)
		    && (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100);
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

	public void settX(int input) {
	    angular.setPhi(input);
	}

	public void settY(int input) {
	    angular.setTheta(input);
	}

	public int[] getSetting() {
	    int[] setting = { formnumber, toolnumber };
	    return setting;
	}
	
	public int[] getPosition() {
	    return rectAngular.getPosition();
	}

	public int getTheta() {
	    return angular.getTheta();
	}

	public int getPhi() {
	    return angular.getPhi();
	}
	
	public int getZr(){
	    return this.tZ;
	}
    }
    

    // Inner class of RobotAngle contains yaw and pitch
    class RobotAngle {

	private int theta;
	private int phi;

	public RobotAngle(int theta, int phi) {
	    this.theta = theta;
	    this.phi = phi;
	}

	public RobotAngle() {
	    // Initialize the RobotAngle with (0,0)
	    this.theta = 0;
	    this.phi = 0;
	}

	// basic void function set and get angle
	public void setTheta(int angle) {
	    this.theta = angle;
	}

	public void setPhi(int angle) {
	    this.phi = angle;
	}

	public int getTheta() {
	    return this.theta;
	}

	public int getPhi() {
	    return this.phi;
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
	JavaRobotServo serbo = new JavaRobotServo(1);// Servo ON
	serbo.makeServo(1);
	Boolean sevo = serbo.makeServo(1);
	if (sevo) {
	    System.out.println("Servo Start!");
	}
    }
    
    public Boolean servo(int i) {
	JavaRobotServo serbo = new JavaRobotServo(i);// 1 : Servo ON / 2: Servo
						     // OFF
	Boolean sevo = serbo.makeServo(1);
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
	JavaRobotAlert robotAlert = new JavaRobotAlert(i);// 1 : Read Alert / 2
							  // : Reset Alert
	String alert = robotAlert.makeAlert(i);
	Boolean returnBoolean = new Boolean(true);
	if ((!"".equals(alert))) {
	    returnBoolean = true;// Not any Alert happened
	} else {
	    returnBoolean = false;
	}
	return returnBoolean;
    }
    
    public static void holdOn() {
	JavaRobotHold hold = new JavaRobotHold(1);// 1 : Hold ON / 2 : Hold OFF
	Boolean holdd = hold.makeHold(1);
	if (holdd) {
	    System.out.println("Hold ON! Yoo-hoo-hoo!");
	}
    }
    
    public Boolean hold(int i) {
	JavaRobotHold hold = new JavaRobotHold(i);// 1 : Hold ON / 2 : Hold OFF
	Boolean sevo = hold.makeHold(i);
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
