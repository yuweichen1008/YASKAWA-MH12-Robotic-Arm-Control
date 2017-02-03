import java.io.UnsupportedEncodingException;

public class JavaRobotAlert extends SendUDP {

    private String alert = new String();


    public JavaRobotAlert(int i) {
		super((i+1));// 2 : ReadAlert / 3 : Reset Alert 
	}

    public String makeAlert(int index) {
		byte[] response = null;
		JavaRobotAlert js = new JavaRobotAlert(index);
		try {
			response = js.send();
			if(response.length ==1){
			    System.out.println("Robot Hold cannot get response!"+"\nYou should check your connection with robot or contact the mechanics or YW Chen.\n\n");
			    return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(response[9] == 0){
			alert = null;
		}else{
			try {
				alert = new String(response, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		
		return alert;
	}


}
