
import rfvlsi.Robot.JavaRobot;
import rfvlsi.Robot.RobotMoveThread;
import rfvlsi.Robot.RobotPosition;
import rfvlsi.Robot.RobotRotation;

public class JavaRobotExample {

	public static void main(String[] args) throws Exception {
		try {
			JavaRobot jr = new JavaRobot();
			RobotPosition currentPosition = jr.getCurrentPosition();
			System.out.println("The absolute position values are : \n");
			System.out.println(currentPosition.toString());
			int x = currentPosition.getX();
			int y = currentPosition.getY();
			int z = currentPosition.getZ();
			// Use case 1: queary current position;
			double rX = (double) currentPosition.getRx() / (double) 10000;
			double rY = (double) currentPosition.getRy() / (double) 10000;
			double rZ = (double) currentPosition.getRz() / (double) 10000;
			RobotRotation refRobotRotation = new RobotRotation(rX, rY, rZ);
			// refRobotRotation.toString();
			double[][] refRotationalMatrix = RobotRotation.getRotationalMatrix(refRobotRotation);
			// Print Rotational Matrix
			RobotRotation.printMatrix(refRotationalMatrix);
			// Use case 2: moving
			double roll = 0; // degree
			double pitch = 0; // degree
			double yaw = 0; // degree
			RobotRotation assignPosi = new RobotRotation(Math.toRadians(roll), Math.toRadians(pitch),
					Math.toRadians(yaw));
			double[][] assignMatix = RobotRotation.getRotationalMatrix(assignPosi);
			int[] Rxyz = RobotRotation.createRobotRotationFromRotationalMatrix(assignMatix);
			double[][] targetMatrix = RobotRotation.multMatrix(assignMatix, refRotationalMatrix);
			int[] targetRxyz = new int[3];
			if (Math.cos(Rxyz[1]) > 0) {
				targetRxyz = RobotRotation.createRobotRotationFromRotationalMatrix(targetMatrix);
			} else {
				targetRxyz = RobotRotation.createRobotRotationFromRotationalMatrix2(targetMatrix);
			}
			int Rx_result = (int) Math.toDegrees(targetRxyz[0]) * 10000;
			int Ry_result = (int) Math.toDegrees(targetRxyz[1]) * 10000;
			int Rz_result = (int) Math.toDegrees(targetRxyz[2]) * 10000;
			RobotPosition targetPosition = new RobotPosition(x, y, z, Rx_result, Ry_result, Rz_result);
			RobotMoveThread mThread = new RobotMoveThread(jr, targetPosition);
			// You can do other program in between.

			// do other things.
			
			
			
			
		} catch (

		NullPointerException e) {
			System.out.println("End!");
		}
	}
}
