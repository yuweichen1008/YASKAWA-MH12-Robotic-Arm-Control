package rfvlsi.Robot.RobotCommand;
/*
 * Modified on January 3rd, 2016 by Y.W., Chen
 * right reserved by RFVLSI NCTU
 */

import rfvlsi.Robot.UtilConvert;

public class CommandServoOn extends RobotCommand {

	private static boolean status;
	private static final byte[] commandServoOn = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57, 57,
			57, 57, 57, 57, -125, 0, 2, 0, 1, 16, 0, 0, 1, 0, 0, 0 };

	@Override
	protected byte[] getDatagram() {
		return commandServoOn;
	}

	@Override
	protected Object parseResult(byte[] responseByte) throws Exception {
		int[] response = UtilConvert.byteArrayToInt(responseByte);
		if (response.length == 1 || response == null) {
			System.out.println("JavaRobotServo cannot get response, please check the connection or contact  Y.W. Chen");
			CommandServoOn.status = false;
		}
		if (response[6] == 144) {
			System.out.println("Successfully Servo ON!");
			CommandServoOn.status = true;
		} else if (response[6] == -1877016320) {
			System.out.println("Robot Servo On failed. Error Code : E4A3 -- Format error (processing category error)");
			System.out.println("Debug : Try to change the port connecting to Robot.");
			CommandServoOn.status = false;
		} else if (response[7] == 13392){
			System.out.println("Robot Servo On failed. Error Code : 3450 -- Servo power cannot be turned ON.");
			System.out.println("Debug : Check the mode.");
		} else {
			System.out.println("Robot Servo On failed.");
			CommandServoOn.status = false;
		}
		return CommandServoOn.status;
	}

}
