import java.io.IOException;

public class JavaRobot extends SendUDP {
	private Boolean botReady = false;
	private RobotAngle targetAngle;
	private RobotPosition targetPosition;
	private RobotAngle currentAngle;
	private RobotPosition currentPosition;
	private int[] tool = new int[8];
	private int speed = 100;		//default 100

	// Constructor with no value
	public JavaRobot() {
		this.setInitial();
		this.setReady(true);	//Ready to send command
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
		this.setReady(false);
		if (anglespeedposition.length == 0) { // Move to tool
			this.targetPosition = null;
			this.targetAngle = null;
		} else if (anglespeedposition.length == 1) { // Move to tool with speed
			this.targetPosition = null;
			this.targetAngle = null;
			this.speed = anglespeedposition[0];
		} else if (anglespeedposition.length == 2) { // Move yaw and pitch
			this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
		} else if (anglespeedposition.length == 3) { // Move yaw and pitch with
														// speed
			this.targetAngle = new RobotAngle(anglespeedposition[0], anglespeedposition[1]);
			this.speed = anglespeedposition[3];
		} else if (anglespeedposition.length == 5) { // Move X, Y, Z, yaw and
														// pitch
			this.targetPosition = new RobotPosition(anglespeedposition[0], anglespeedposition[1],
					anglespeedposition[2]);
			this.targetAngle = new RobotAngle(anglespeedposition[3], anglespeedposition[4]);
		} else if (anglespeedposition.length == 6) { // Move X, Y, Z, yaw and
														// pitch with speed
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
				if (this.targetPosition != null && this.tool != null) {
					RobotMove rm = new RobotMove(targetPosition.getPosition()[0], targetPosition.getPosition()[1],
							targetPosition.getPosition()[2], targetAngle.getTheta(), targetAngle.getPhi(), this.tool,
							this.speed);
					rm.move();
				} else if (this.targetPosition == null && this.targetAngle != null && this.tool != null) {
					RobotMove rm = new RobotMove(targetAngle.getTheta(), targetAngle.getPhi(), this.tool, this.speed);
					rm.move();
				} else if(this.targetPosition == null && this.targetAngle == null && this.tool != null){
					RobotMove rm = new RobotMove(this.tool, this.speed);
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
			System.out.println("Did get the tool.");
		}
	}

	public boolean getReady() {
		return botReady;
	}

	private boolean robotReady() {
		robotDisplacement();		//function to calculate the difference between target and current
		return isCloseTo();			//return the boolean value of previous description
	}

	public void robotDisplacement() {
		int[] displacement = new int[6];
		int[] toolnow = read();
		if (toolnow == null) {
			this.currentPosition = new RobotPosition(9999, 9999, 9999);	//prevent the error code
			this.currentAngle = new RobotAngle(9999, 9999); // prevent the error
															// code
		} else {
			displacement[0] = toolnow[2] - this.targetPosition.getPosition()[0]; // X
			// displacement
			displacement[1] = toolnow[3] - this.targetPosition.getPosition()[1]; // Y
			// displacement
			displacement[2] = toolnow[4] - this.targetPosition.getPosition()[2]; // Z
			// displacement
			displacement[3] = toolnow[5] - this.targetAngle.getTheta(); // Yaw
			// displacement
			displacement[4] = toolnow[6] - this.targetAngle.getPhi(); // Pitch
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

	public void askAngle(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				
			}
		
		}).start();
	}
	public boolean isCloseTo() {
		if (currentPosition.getPosition()[0] == 9999 && currentPosition.getPosition()[1] == 9999
				&& currentPosition.getPosition()[2] == 9999) {
			return (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100)						// Yaw
					&& (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100);						// Pitch
		} else {
			return (Math.abs(currentPosition.getPosition()[0] - targetPosition.getPosition()[0]) < 10)		//X
					&& (Math.abs(currentPosition.getPosition()[1] - targetPosition.getPosition()[1]) < 10)	//Y
					&& (Math.abs(currentPosition.getPosition()[2] - targetPosition.getPosition()[2]) < 10)	//Z
					&& (Math.abs(currentAngle.getTheta() - targetAngle.getTheta()) < 100)					// Yaw
					&& (Math.abs(currentAngle.getPhi() - targetAngle.getPhi()) < 100);						//Pitch
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

		public int getZr() {
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
		Boolean sevo = JavaRobotServo.makeServo(i);// 1 : Servo ON / 2: Servo OFF
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
		String alert = JavaRobotAlert.makeAlert(i);// 1 : Read Alert / 2 : Reset Alert
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
