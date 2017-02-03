package rfvlsi.Robot;

public class RobotPosition {
	private final int x;
	private final int y;
	private final int z;
	private final int Rx;
	private final int Ry;
	private final int Rz;
	private final int ToolNumber;
	private final int FormNumber;
	
	public RobotPosition(int X, int Y, int Z, int Rx, int Ry, int Rz, int Tool, int Form) {
	    this.x = X;
	    this.y = Y;
	    this.z = Z;
	    this.Rx = Rx;
	    this.Ry = Ry;
	    this.Rz= Rz;
	    this.ToolNumber = Tool;
	    this.FormNumber = Form;
	}
	
	public RobotPosition(int X, int Y, int Z, int Rx, int Ry, int Rz) throws Exception {
		JavaRobot jr = new JavaRobot();
	    this.x = X;
	    this.y = Y;
	    this.z = Z;
	    this.Rx = Rx;
	    this.Ry = Ry;
	    this.Rz= Rz;
	    this.ToolNumber = jr.getCurrentPosition().getTool();
	    this.FormNumber = jr.getCurrentPosition().getForm();
	}
	

	// basic void to set and get position
	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public int getZ(){
		return this.z;
	}

	public int getRx(){
		return this.Rx;
	}
	public int getRy(){
		return this.Ry;
	}
	
	public int getRz() {
	    return this.Rz; 
	}

	public int getTool(){
		return this.ToolNumber;
	}
	
	public int getForm(){
		return this.FormNumber;
	}
	public boolean isCloseTo(RobotPosition targetPosition, RobotPosition refPosition) {
		return ((Math.abs(Math.abs(refPosition.getX() - this.getX()) - Math.abs(targetPosition.getX())) <= 100) // 0.1 mm
				&& (Math.abs(Math.abs(refPosition.getY() - this.getY()) - Math.abs(targetPosition.getY())) <= 100) // 0.1 mm
				&& (Math.abs(Math.abs(refPosition.getZ() - this.getZ()) - Math.abs(targetPosition.getZ())) <= 100) // 0.1 mm
				&& (Math.abs(Math.abs(refPosition.getRx() - this.getRx()) - Math.abs(targetPosition.getRx())) <= 1000) // 0.1 degree = 1000* 0.0001 degree
				&& (Math.abs(Math.abs(refPosition.getRy() - this.getRy()) - Math.abs(targetPosition.getRy())) <= 1000) // 0.1 degree = 1000* 0.0001 degree
				&& (Math.abs(Math.abs(refPosition.getRz() - this.getRz()) - Math.abs(targetPosition.getRz())) <= 1000)); // 0.1 degree = 1000* 0.0001 degree
	}
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("Current X is : " + this.getX());
		sb.append(", Y is : " + this.getY());
		sb.append(", Z is : " + this.getZ());
		sb.append(", Roll (Rx) is : "+this.getRx());
		sb.append(", Pitch (Ry): "+this.getRy());
		sb.append(", Yaw (Rz): "+this.getRz());
		return sb.toString();
	}
}
