package GiaiPTBac1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port;

	public Server(int port) {
		this.port = port;
	}
	
	private void execute() throws IOException {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Server is listening...");
		
		while(true) {
			Socket socket = server.accept();
			PTBac1 ptBac1 = new PTBac1(socket);
			ptBac1.start();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(15797);
		server.execute();
	}
}

class PTBac1 extends Thread{
	private Socket socket;

	public PTBac1(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			int a = dis.readInt();
			int b = dis.readInt();
			if(a == 0) {
				if(b == 0) {
					dos.writeUTF("Phương trình vô số nghiệm");
				}else {
					dos.writeUTF("Phương trình vô nghiệm");
				}
			}else {
				double result = (double)-b/a;
				dos.writeUTF("Phương trình có nghiệm = " + result);
			}
			dis.close();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
