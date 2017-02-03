/*
 * Modified on Jan 25th, 2016 by Y.W. Chen
 * right reserved by RFVLSI NCTU
 */
package rfvlsi.Robot;

import rfvlsi.Robot.RobotCommand.CommandReadPosition;

public class JavaRobot {
	private UDPNode robotNode = new UDPNode();

	public final RobotPosition refPosition; // the robot should always start at
											// refPosition;

	public RobotPosition getRefPosition() {
		return this.refPosition;
	}

	public UDPNode getUDPNode() {
		return this.robotNode;
	}

	// Constructor with no value
	public JavaRobot() throws Exception {
		this.refPosition = getCurrentPosition();
	}

	public synchronized RobotPosition getCurrentPosition() throws Exception {
		// Note; need to use "synchronized" modifier; only one thread can run
		// this method.
		try {
			CommandReadPosition cmdReadPos = new CommandReadPosition();
			return (RobotPosition) cmdReadPos.sendTo(this, (int) (Math.random() * 150) + 10050);
		} catch (NullPointerException e) {
			System.out.println("Can not get RobotPosition.");
			return null;
		}
	}

	public RobotMoveThread moveTo(RobotPosition target) {
		RobotMoveThread moveThread = new RobotMoveThread(this, target);
		moveThread.start(); // start the new thread to run the run() function.
		return moveThread;
	}
}
