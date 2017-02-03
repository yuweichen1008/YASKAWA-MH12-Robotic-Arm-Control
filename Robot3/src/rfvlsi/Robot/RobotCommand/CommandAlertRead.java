package rfvlsi.Robot.RobotCommand;

import rfvlsi.Robot.UtilConvert;

public class CommandAlertRead extends RobotCommand {

	private static final byte[] commandAlertRead = { 89, 69, 82, 67, 32, 0, 0, 0, 3, 1, 0, 0, 0, 0, 0, 0, 57, 57, 57,
			57, 57, 57, 57, 57, 112, 0, 1, 0, 0, 1, 0, 0 };

	@Override
	protected byte[] getDatagram() {
		return commandAlertRead;
	}

	@Override
	protected Object parseResult(byte[] response) throws Exception {
		String alert = new String();
		if (response.length == 1) {
			System.out.println("Robot Alert cannot get response, please check the connection or contact  Y.W. Chen");
			alert = null;
		} else if (response[34] == 0 || response[35] == 0) {
			// Status == 0
			alert = null;
			System.out.println("There is no alarm.");
		} else {
			// Try to convert byte to ASCII
			alert = UtilConvert.convert(UtilConvert.swapString(response));
			System.out.println(alert);
		}
		return alert;
	}

}
