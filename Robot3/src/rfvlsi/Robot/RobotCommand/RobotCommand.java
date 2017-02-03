package rfvlsi.Robot.RobotCommand;

import rfvlsi.Robot.JavaRobot;

public abstract class RobotCommand {
	// return the datagram in byte[] to send to Robot
	protected abstract byte[] getDatagram();

	public Object sendTo(JavaRobot robot, int port) throws Exception {
		byte[] response = robot.getUDPNode().submit(this.getDatagram(), port);
		return this.parseResult(response);
	};

	// parse the result, and return true for success.
	protected abstract Object parseResult(byte[] response) throws Exception;

}
