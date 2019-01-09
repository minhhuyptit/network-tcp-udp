package ChatSoloTCP;

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
		Socket socket = server.accept();
		TieuTienTrinhDoc read = new TieuTienTrinhDoc(socket);
		read.start();
		TieuTienTrinhGhi write = new TieuTienTrinhGhi(socket, "Server");
		Thread thread = new Thread(write);
		thread.start();	
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(15797);
		server.execute();
	}
	
}
