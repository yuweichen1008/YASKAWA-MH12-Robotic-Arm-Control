package rfvlsi.Robot;

import rfvlsi.Robot.RobotCommand.CommandAlertRead;
import rfvlsi.Robot.RobotCommand.CommandHoldOff;
import rfvlsi.Robot.RobotCommand.CommandMove;
import rfvlsi.Robot.RobotCommand.CommandServoOn;

public class RobotMoveThread extends Thread {

	RobotPosition targetPosition;
	JavaRobot robot;
	boolean isRobotMoveFinished = false;

	public RobotMoveThread(JavaRobot robot, RobotPosition targetPosition) {
		this.robot = robot;
		this.targetPosition = targetPosition;
	}

	@Override
	public void run() {
		// prepare to move
		CommandServoOn cmdServoOn = new CommandServoOn();
		CommandHoldOff cmdHoldOff = new CommandHoldOff();
		CommandAlertRead cmdAlertRead = new CommandAlertRead();

		try {
			cmdAlertRead.sendTo(robot, 10043);// Read Alarm.
			cmdHoldOff.sendTo(robot, 10044); // Hold off
			cmdServoOn.sendTo(robot, 10045); // Server on
			CommandMove cmdMove = new CommandMove(targetPosition, 500);
			cmdMove.sendTo(robot, 10046);
			Thread.sleep(10);
			long threadId = Thread.currentThread().getId();
			System.out.println("Thread ID is Thread-"+ threadId);
			while (!(updateRobotMoveFinished())) {
				Thread.sleep(1500);// pause
				getCurrentPosition();
			}
		} catch (Exception e) {
			System.out.println("Some Error happened in MoveThread -" + Thread.currentThread().getId());
			return;
			// e.printStackTrace();
		}

	}

	private boolean updateRobotMoveFinished() throws Exception {
		// this function is not open to users.
		this.isRobotMoveFinished = robot.getCurrentPosition().isCloseTo(this.targetPosition, robot.refPosition);
		return this.isRobotMoveFinished;

	}

	public void getCurrentPosition() throws Exception {
		System.out.println("Target Position : ");
		this.targetPosition.toString();
		System.out.println("Current Position : ");
		this.robot.getCurrentPosition().toString();
	}

	public boolean isRobotMoveFinished() throws Exception {
		// this function is intended to be queried by user.
		updateRobotMoveFinished();
		if(this.isRobotMoveFinished){
			System.out.println("Robot hasn't finished.");
		}
		return this.isRobotMoveFinished;
	}
}
