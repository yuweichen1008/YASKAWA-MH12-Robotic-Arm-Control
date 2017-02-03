/*
 * Modified on January 25th, 2016 by Y.W., Chen
 * right reserved by RFVLSI NCTU
 */
package rfvlsi.Robot.RobotCommand;

import java.util.ArrayList;

import rfvlsi.Robot.RobotPosition;
import rfvlsi.Robot.UtilConvert;

public class CommandMove extends RobotCommand {
	private Integer speed = new Integer(0);// Speed = speed * 10 unit
	private RobotPosition targetPosition;

	// constructor of assigning X, Y, Z, and angle values
	public CommandMove(RobotPosition targetPosition, int speedin) {
		this.targetPosition = targetPosition;
		this.speed = speedin;
	}

	@Override
	public Object parseResult(byte[] response) {
		return true;
	}

	protected byte[] getDatagram() {

		int[] anglemodified = new int[3];
		int[] coordinatemodified = new int[3];
		int coor = 3;
		byte[] setting_rect = { 01, 00, 00, 00, 00, 00, 00, 00, 01, 00, 00, 00, 17, 00, 00, 00 };
		// Setting of Robot -- Station -- Classification -- Operation
		// Coordinate(Robot coordinate)
		// byte[] setting_ang = { 01, 00, 00, 00, 00, 00, 00, 00, 02, 00, 00,
		// 00, 19, 00, 00, 00 };
		// Setting of Robot -- Station -- Classification -- Operation
		// Coordinate(Tool coordinate)
		final byte[] head = { 89, 69, 82, 67, 32, 00, 104, 00, 03, 01, 00, 00, 00, 00, 00, 00, 57, 57, 57, 57, 57, 57,
				57, 57 };// Header part
		final byte[] suh = { -118, 00, 01, 00, 01, 02, 00, 00 };// Sub-header
		byte[] Reserv = { 0, 0, 0, 0 };
		byte[] typeByte = { 0, 0, 0, 0 };
		int[] extension = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		// Axis = Tool(9:32); X, Y, Z, TX, TY, TZ
		coordinatemodified[0] = this.targetPosition.getX(); // X in mm
		coordinatemodified[1] = this.targetPosition.getY(); // Y in mm
		coordinatemodified[2] = this.targetPosition.getZ(); // Z in mm
		anglemodified[0] = this.targetPosition.getRx(); // Rx in 0.1 degree
		anglemodified[1] = this.targetPosition.getRy(); // Ry in 0.1 degree
		anglemodified[2] = this.targetPosition.getRz(); // Rz in 0.1 degree
		byte[] speedbyte = UtilConvert.swap(UtilConvert.InttoByteArraySingle(speed));
		byte[] toolNumberbyte = UtilConvert.swap(UtilConvert.InttoByteArraySingle(this.targetPosition.getTool()));
		byte[] coordinatebyte = UtilConvert.InttoByteArrayMove(coordinatemodified);
		byte[] anglebyte = UtilConvert.InttoByteArrayMove(anglemodified);
		byte[] coorbyte = UtilConvert.swap(UtilConvert.InttoByteArraySingle(coor));

		ArrayList<Byte> arraylist = new ArrayList<Byte>();
		// Command = [ Head,sub-head, Setting, Axis, Reserve, Type, Extend,
		// ToolNo, Coordinate, Extension];
		for (byte i : head)
			arraylist.add(i);
		for (byte i : suh)
			arraylist.add(i);
		for (byte i : setting_rect)
			arraylist.add(i);
		for (int i = 0; i < speedbyte.length; i++)
			arraylist.add((44 + i), speedbyte[i]);
		for (byte i : coordinatebyte)
			arraylist.add(i);
		for (byte i : anglebyte)
			arraylist.add(i);
		for (byte i : Reserv)
			arraylist.add(i);
		for (byte i : typeByte)
			arraylist.add(i);
		for (byte i : Reserv)
			arraylist.add(i);
		for (byte i : toolNumberbyte)
			arraylist.add(i);
		for (byte i : coorbyte)
			arraylist.add(i);
		for (byte i : UtilConvert.InttoByteArrayMove(extension))
			arraylist.add(i);

		byte[] returnbyte = new byte[arraylist.size()];
		for (int i = 0, len = arraylist.size(); i < len; i++) {
			if (arraylist.get(i) != null) {
				returnbyte[i] = arraylist.get(i);
			}
		}
		return returnbyte;
	}
}