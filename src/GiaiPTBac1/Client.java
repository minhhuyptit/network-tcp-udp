package GiaiPTBac1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	private Scanner sc = new Scanner(System.in);
	
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void execute() throws IOException {
		//Cấu hình Client
		Socket client = new Socket(host, port);
		//Nhập dữ liệu
		int a = input("Nhập vào a: ");
		int b = input("Nhập vào b: ");
		
		DataInputStream dis = new DataInputStream(client.getInputStream());
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		
		//Gửi dữ liệu đến Server
		dos.writeInt(a);
		dos.writeInt(b);
		
		//Nhận dữ liệu từ Server
		System.out.println(dis.readUTF());
		
		dis.close();
		dos.close();
		client.close();
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.execute();
	}
	
	private int input(String request) {
		int number = 0;
		boolean flag = true;
		do {
			try {
				System.out.print(request);
				number = Integer.parseInt(sc.nextLine());
				flag = false;
			} catch (Exception e) {
				System.out.println("Dữ liệu không đúng định dạng. Vui lòng nhập lại: ");
			}
		} while (flag);
		return number;
	}
}
