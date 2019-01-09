package LoginTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void execute() throws IOException {
		Socket client = new Socket(host, port);
		DataInputStream dis = new DataInputStream(client.getInputStream());
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		
		Scanner sc = new Scanner(System.in);
		
		int id = 0;
		String pass = "";
		boolean flag = true;
		int result = 0;
		do {
			System.out.print("Nhập vào id: ");
			try {
				id = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("id phải là số nguyên. Vui lòng nhập lại!");
				System.out.print("Nhập vào id: ");
				id = Integer.parseInt(sc.nextLine());
			}
			
			System.out.print("Nhập vào password: ");
			pass = sc.nextLine();
			dos.writeInt(id);
			dos.writeUTF(pass);
			result = dis.readInt();
			if(result == -1) {
				System.out.println("Sai password. Vui lòng nhập lại");
			}else if(result == -2) {
				System.out.println("User không tồn tại. Vui lòng nhập lại");
			}else {
				System.out.println("Số tiền của bạn là: " + result);
				flag = false;
			}	
			
		} while (flag);
		dis.close();
		dos.close();
		client.close();
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.execute();
	}
}
