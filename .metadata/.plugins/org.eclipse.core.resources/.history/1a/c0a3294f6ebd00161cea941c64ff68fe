
public class JavaRobot extends SendUDP {
	private Boolean botinitial = false;
	private Boolean botReady = false;
	private RobotAngle targetAngle;
	private RobotAngle otherAngle;
	private RobotPosition targetPosition;
	private Tool tool = new Tool();
	private int[] intTool = new int[8];

	// Constructor with no value
	public JavaRobot() {
		this.setInitial(false);
		this.setReady(true);
	}

    // Constructor with theta and phi
	public JavaRobot(int theta_in, int phi_in) {
		this.setInitial(false);
		this.setReady(true);
		this.targetAngle = new RobotAngle(theta_in, phi_in);
	}

	private void setReady(Boolean input) {
		this.botReady = input;
	}

	// initialize boolean function
	private void setInitial(Boolean input) {
		this.botinitial = input;
	}

	private boolean getReady() {
		return botReady;
	}

	private boolean getInitial() {
		return botinitial;
	}

	public void moveTo(int theta, int phi) {
		if (!(botinitial)) {
			// We have to initialize the JavaRobot first
			this.setReady(false);
			RobotReadPosition tg = new RobotReadPosition();
			try {
				this.intTool = tg.read();
				System.out.println("Move to tool point.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			this.setReady(false);
			targetAngle.setTheta(theta); // yaw
			targetAngle.setPhi(phi); // pitch
			System.out.println("Robot start moving");
			System.out.printf("Robot is moving to yaw = ", targetAngle.getTheta(), " and pitch = ",
					targetAngle.getPhi());
		}

	}

	public void test() {
		RobotReadPosition robotread = new RobotReadPosition();
		robotread.read();
	}

	public void move() throws Exception {
		this.setReady(false);
		System.out.println("Robot start moving! ");
		int speed = 10;
		RobotMove robotmove = new RobotMove(targetAngle.getTheta(), targetAngle.getPhi(), intTool, speed);
		System.out.printf("\nRobot is moving to yaw = %d and pitch = %d\n", targetAngle.getTheta(),
				targetAngle.getPhi());
		robotmove.move();
	}

	public Tool Read() {
		RobotReadPosition robotread = new RobotReadPosition();
		Tool toolout = new Tool(robotread.read()); // To store the outcome of  Tool
		return toolout;
	}

	public Boolean alarm(int index) {
		return botReady;

	}

	public Boolean servo(int index) {
		return botReady;

	}

	public Boolean hold(int index) {
		return botReady;

	}

	public Boolean isCloseTo(RobotAngle otherAngle) {
		return (Math.abs(otherAngle.getTheta() - targetAngle.getTheta()) < 0.01)
				&& (Math.abs(otherAngle.getPhi() - targetAngle.getPhi()) < 0.01);
	}

	public Boolean isCloseTo() {
		return (Math.abs(otherAngle.getTheta() - targetAngle.getTheta()) < 0.01)
				&& (Math.abs(otherAngle.getPhi() - targetAngle.getPhi()) < 0.01);
	}

    // Inner class of Tool store information
	class Tool {
		private int toolnumber;
		private int formnumber;
		private int tZ;
		private RobotPosition rectAngular;
		private RobotAngle angular;
		public Tool(int[] toolin) {
			this.toolnumber = toolin[0];
			this.formnumber = toolin[1];
			rectAngular = new RobotPosition(toolin[2], toolin[3], toolin[4]);
			angular = new RobotAngle( toolin[5], toolin[6]);
			
			this.tZ = toolin[7];
		}
		public Tool(){
			
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

		public int[] getPosition() {
			return rectAngular.getPosition();
		}

		public int getTheta() {
			return angular.getTheta();
		}

		public int getPhi() {
			return angular.getPhi();
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
	class RobotPosition {
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
			this.robotposi[1] = X;
			this.robotposi[2] = Y;
			this.robotposi[3] = Z;
			return this.robotposi;
		}
	}

	public static Boolean Servo(int i) {
		JavaRobotServo serbo = new JavaRobotServo(i);// 1 : Servo ON / 2: Servo OFF
		Boolean sevo = serbo.makeServo(1);
		if(sevo && i ==1){
			System.out.println("Servo Start!");
		}else if(!(sevo) && i ==2){
			System.out.println("Servo Off!");
		}else{
			System.out.println("Servo failure");
		}
		return sevo;
	}
	
	public static Boolean Alert(int i) {
		JavaRobotAlert robotAlert = new JavaRobotAlert(i);// 1 : Read Alert / 2 : Reset Alert 
		String alert = robotAlert.makeAlert(i);
		Boolean returnBoolean = new Boolean(true);
		if((!"".equals(alert))){
			returnBoolean = true;//Not any Alert happened
		}else{
			returnBoolean = false;
		}
		return returnBoolean;
	}
	
	public static Boolean Hold(int i) {
		JavaRobotHold serbo = new JavaRobotHold(i);// 1 : Hold ON / 2 : Hold OFF 
		Boolean sevo = serbo.makeHold(i);
		if(sevo && i ==1){
			System.out.println("Hold ON! Yoo-hoo-hoo!");
		}else if(!(sevo) && i ==2){
			System.out.println("Hold Off!");
		}else{
			System.out.println("Hold failure");
		}
		return sevo;
	}
}
