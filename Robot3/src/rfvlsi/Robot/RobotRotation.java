package rfvlsi.Robot;

public class RobotRotation {
	public double rX;
	public double rY;
	public double rZ;
	
	public RobotRotation(double rXin,double rYin,double rZin){
		rX = Math.toRadians(rXin);
		rY = Math.toRadians(rYin);
		rZ = Math.toRadians(rZin);
	}
	
	public double getRx(){
		return rX;
	}
	public double getRy(){
		return rY;
	}
	public double getRz(){
		return rZ;
	}
	
	public String toString(){
		System.out.println("Rx  = " + getRx());	//in radians
		System.out.println("Ry  = " + getRy());	//in radians
		System.out.println("Rz  = " + getRz());	//in radians
		return null;

	}
	
	public static double[][] getRotationalMatrix(RobotRotation obj) {
		double[][] answer = new double[3][3];
		double[][] temp = new double[3][3];
		double Mx[][] = { { 1, 0, 0 }, { 0, Math.cos(obj.getRx()), -Math.sin(obj.getRx()) },
				{ 0, Math.sin(obj.getRx()), Math.cos(obj.getRx()) } };
		double Mz[][] = { { Math.cos(obj.getRz()), -Math.sin(obj.getRz()), 0 },
				{ Math.sin(obj.getRz()), Math.cos(obj.getRz()), 0 }, { 0, 0, 1 } };
		double My[][] = { { Math.cos(obj.getRy()), 0, Math.sin(obj.getRy()) }, { 0, 1, 0 },
				{ -Math.sin(obj.getRy()), 0, Math.cos(obj.getRy()) } };
		temp = multMatrix(Mx, My);
		answer = multMatrix(temp, Mz);
		return answer;

	}

	public  static double[][] multMatrix(double[][] a, double[][] b) {
		if (a.length == 0)
			return new double[0][0];
		if (a[0].length != b.length)
			return null; // invalid dimensions

		int n = a[0].length;
		int m = a.length;
		int p = b[0].length;

		double answer[][] = new double[m][p];

		// For loop
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				answer[i][j] = 0;
				for (int k = 0; k < n; k++) {
					answer[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return answer;

	}

	public static void printMatrix(double[][] inputMatrix) {
		System.out.println("Matrix[" + inputMatrix.length + "][" + inputMatrix[0].length + "]");
		int rows = inputMatrix.length;
		int columns = inputMatrix[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				System.out.printf("%9.5f ", inputMatrix[i][j]);
			}
			System.out.println();
		}
		System.out.println();

	}

	public static int[] createRobotRotationFromRotationalMatrix(double[][] inputMatix) {
		int[] answerArray = new int[3];
		double Rz=Math.atan2(inputMatix[1][0],inputMatix[0][0]);
        double Ry=Math.atan2(-inputMatix[2][0],Math.sqrt(Math.pow(inputMatix[2][1],2)+Math.pow(inputMatix[2][2],2)));
        double Rx=Math.atan2(inputMatix[2][1],inputMatix[2][2]);
		answerArray[0] = (int)Rx;
		answerArray[1] = (int)Ry;
		answerArray[2] = (int)Rz;
		return answerArray;
	}

	public static int[] createRobotRotationFromRotationalMatrix2(double[][] inputMatix) {
		int[] answerArray = new int[3];
		double Rz=Math.atan2(inputMatix[1][0],inputMatix[0][0]);
        double Ry=Math.atan2(-inputMatix[2][0],Math.sqrt(Math.pow(inputMatix[2][1],2)+Math.pow(inputMatix[2][2],2)));
        double Rx=Math.atan2(inputMatix[2][1],inputMatix[2][2]);
        answerArray[0] = (int)Rx;
		answerArray[1] = (int)Ry;
		answerArray[2] = (int)Rz;
		return answerArray;
	}
}
