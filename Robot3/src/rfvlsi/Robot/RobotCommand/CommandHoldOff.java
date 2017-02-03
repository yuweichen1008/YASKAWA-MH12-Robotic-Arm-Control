package rfvlsi.Robot.RobotCommand;

import rfvlsi.Robot.UtilConvert;

public class CommandHoldOff extends RobotCommand {

	private static boolean status;
	private static byte[] commandHoldOff = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57, 57,
			57, 57, 57, -125, 0, 1, 0, 1, 16, 0, 0, 2, 0, 0, 0 };

	@Override
	protected byte[] getDatagram() {
		return commandHoldOff;
	}

	@Override
	protected Object parseResult(byte[] responseByte) {
		int[] response = UtilConvert.byteArrayToInt(responseByte);
		if (response.length <= 6) {
			System.out.println("JavaRobotHold cannot get response, please check the connection or contact  Y.W. Chen");
			CommandHoldOff.status = false;
		}
		if (response[6] == 144) {
			System.out.println("Successfully hold OFF!");
			CommandHoldOff.status = true;
		} else if (response[6] == -1877016320) {
			System.out.println("Robot Servo failed. Error Code : E4A3 -- Format error (processing category error)");
			System.out.println("Debug : try to change the port connecting to Robot.");
			CommandHoldOff.status = false;
		} else {
			System.out.println("Robot Hold failed.");
			CommandHoldOff.status = false;
		}
		return CommandHoldOff.status;
	}
}
