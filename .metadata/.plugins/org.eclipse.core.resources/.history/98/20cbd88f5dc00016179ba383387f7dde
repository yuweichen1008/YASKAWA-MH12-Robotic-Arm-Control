//import java.util.Scanner;

public class JavaRobotExample {
    private static Boolean flag = new Boolean(false);
    private static int[] tool;
    public static void main(String[] args) throws Exception {
	// System.out.println("Enter the angle of yaw :");
	// Scanner sc = new Scanner(System.in);
	// int yaw = sc.nextInt();
	// System.out.println("Enter the angle of pitch :");
	// int pitch = sc.nextInt();
	// int yaw = 10;
	// int pitch = 23;
	// JavaRobot robot = new JavaRobot(yaw, pitch);
	// System.out.println("You have set the robot angle to (yaw, pitch) = (
	// " + yaw + " , " + pitch + " )");
	// flag = JavaRobot.Servo(2); //Hold ON
	// System.out.println("Hold : " + flag.toString());
	JavaRobot jr = new JavaRobot();
	//jr.servo(1);
	tool = jr.read();
	/*
	 * Move to function 
	 * length == 2 : 
	 * yaw/pitch 
	 * length == 3 :
	 * yaw/pitch/speed 
	 * length == 5 : 
	 * X/Y/Z/yaw/pitch 
	 * length == 6 :
	 * X/Y/Z/yaw/pitch/speed
	 */
	jr.moveTo(123, 123);
    }
}
