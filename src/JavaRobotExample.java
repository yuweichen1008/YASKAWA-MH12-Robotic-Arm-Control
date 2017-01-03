
public class JavaRobotExample {

    public JavaRobotExample() {

    }

    public static void main(String[] args) {
	/*
	 * 
	 * 
	 * JavaRobotServo re = new JavaRobotServo(); re.makeServo(1);
	 * JavaRobotHold ha = new JavaRobotHold(); ha.makeHold(2);JavaRobot jr =
	 * new JavaRobot(); jr.read(); jr.moveTo(10,10,10); jr.ask();
	 */
    	// Read Alert
	JavaRobot jr = new JavaRobot();
	
	// moveTo function 
    // length == 0 : move to tool position
    // length == 2 : yaw /pitch 
    // length == 3 : yaw /pitch /speed 
    // length == 5 : X   /Y    /Z     /yaw  /pitch 
    // length == 6 : X   /Y    /Z     /yaw  /pitch  /speed
	//jr.moveTo(100,0,0, 0,0);
	jr.ask();
	//jr.moveTo(-500,-1000,200);
    }
}
