/*
 * Modified on Dec. 13, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
public class JavaRobotExample {
	private static Boolean flag = new Boolean(false);
	private static int[] tool;
	private static int speed = 100;

	public static void main(String[] args) throws Exception {
		JavaRobot jr = new JavaRobot();
		// jr.servo(1);
		tool = jr.read();
		if (jr.getReady()) {
			JavaRobot.servoOn();
			jr.read();
			jr.servo(1);
			jr.servo(2);
			jr.hold(1);
			jr.hold(2);
			jr.alert(1);
			jr.alert(2);
			/*
			 * Move to function 
			 * length == 0 : (move to tool)
			 * length == 1 : speed(move to tool) 
			 * length == 2 : yaw/pitch 
			 * length == 3 : yaw/pitch/speed
			 * length == 5 : X/Y/Z/yaw/pitch 
			 * length == 6 : X/Y/Z/yaw/pitch/speed
			 */
			jr.moveTo();
			jr.moveTo(speed);
			jr.moveTo(123, 123);
			
			tool = jr.read();
			System.out.println("Tool is" + tool.toString());
		}
	}
}
