package rfvlsi.Robot.RobotCommand;

import rfvlsi.Robot.UtilConvert;

public class CommandAlertReset extends RobotCommand {

	private static boolean status;
	private static final byte[] commandAlertReset = { 89, 69, 82, 67, 32, 0, 4, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57,
			57, 57, 57, 57, 57, -126, 0, 1, 0, 1, 16, 0, 0, 1, 0, 0, 0 };

	@Override
	protected byte[] getDatagram() {
		return commandAlertReset;
	}

	@Override
	protected Object parseResult(byte[] responseByte) throws Exception {
		int[] response = UtilConvert.byteArrayToInt(responseByte);
		if (response[34] == 0 || response[35] == 0) {
			System.out.println("Reset alarm successfully.");
			CommandAlertReset.status = true;
		} else {
			System.out.println("Some Error on alarm reset command JavaRobotAlert(2)");
			CommandAlertReset.status = false;
		}
		return CommandAlertReset.status;
	}
}
