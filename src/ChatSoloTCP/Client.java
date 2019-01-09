package ChatSoloTCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
	private InetAddress host;
	private int port;
	
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void execute() throws IOException {
		Socket client = new Socket(host, port);
		TieuTienTrinhDoc read = new TieuTienTrinhDoc(client);
		read.start();
		TieuTienTrinhGhi write = new TieuTienTrinhGhi(client, "Huy");
		Thread thread = new Thread(write);
		thread.start();	
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.execute();
	}
}
