
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
	JavaRobot jr = new JavaRobot();
	jr.ask();
	jr.moveTo(10, 10);

    }

}
