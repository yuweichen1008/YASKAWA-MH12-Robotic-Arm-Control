/*
 * Modified on January 3rd, 2016 by Y.W., Chen
 * right reserved by RFVLSI NCTU
 */
package rfvlsi.Robot.RobotCommand;

import rfvlsi.Robot.RobotPosition;
import rfvlsi.Robot.UtilConvert;

public class CommandReadPosition extends RobotCommand {

	private final byte[] command = { 89, 69, 82, 67, 32, 00, 00, 00, 03, 01, 00, 00, 00, 00, 00, 00, 57, 57, 57, 57, 57,
			57, 57, 57, 117, 00, 101, 00, 00, 01, 00, 00 };

	@Override
	protected byte[] getDatagram() {
		return this.command;
	}

	@Override
	protected Object parseResult(byte[] responseByte) throws Exception {
		int[] response = UtilConvert.byteArrayToInt(responseByte);
		if (response.length != 64 || response[6] == 73600) {
			System.out.println("Please Check the connection with Robot!");
			return null;
		}
		RobotPosition readPosition = new RobotPosition(response[13], response[14], response[15], response[16],
				response[17], response[18], response[9], response[10]);
		// 9 : tool number 10: form number
		return readPosition;
	}
	// Constructor of RobotReadPosition
}
