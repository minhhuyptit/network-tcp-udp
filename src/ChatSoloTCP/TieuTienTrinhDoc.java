package ChatSoloTCP;

import java.io.DataInputStream;
import java.net.Socket;

public class TieuTienTrinhDoc extends Thread{
	
	private Socket socket;
	
	
	
	public TieuTienTrinhDoc(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while(true) {
				String sms = dis.readUTF();
				System.out.println(sms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
