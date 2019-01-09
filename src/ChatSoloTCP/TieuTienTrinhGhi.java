package ChatSoloTCP;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TieuTienTrinhGhi implements Runnable{
	private Socket socket;
	private String name;
	
	public TieuTienTrinhGhi(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			while(true) {
				String sms = sc.nextLine();
				dos.writeUTF(name + ": " + sms);
				dos.flush();
				if(sms.equals("exit")) {
					socket.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
	}
}
