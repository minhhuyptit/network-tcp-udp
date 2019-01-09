package GiaiPTBac2_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
		DatagramSocket client = new DatagramSocket();
		
		//Nhập dữ liệu
		int a = input("Nhập vào a: ");
		int b = input("Nhập vào b: ");
		int c = input("Nhập vào c: ");
		
		//Đóng gói thành Packet và gửi đến Server
		DatagramPacket a_DP = createPacket(a, 1);	
		DatagramPacket b_DP = createPacket(b, 2);
		DatagramPacket c_DP = createPacket(c, 3);
		
		client.send(a_DP);
		client.send(b_DP);
		client.send(c_DP);
		
		//Nhận dữ liệu từ Server
		String [] result = receiveData(client).split("_");
		double value = Double.parseDouble(result[0]);
		int key		 = Integer.parseInt(result[1]);
		if(key == -1) {
			System.out.println("Phương trình vô nghiệm");
		}else if(key == -2) {
			System.out.println("Phương trình vô số nghiệm");
		}else if(key == -3) {
			System.out.println("Phương trình có 1 nghiệm = " + value);
		}else if(key == -4) {
			String [] result1 = receiveData(client).split("_");
			double value_1 = Double.parseDouble(result1[0]);
			System.out.println("Phương trình có 2 nghiệm phân biệt: ");
			System.out.println("x1 = " + value);
			System.out.println("x2 = " + value_1);	
		}
		client.close();
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.execute();
	}
	
	private String receiveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		return new String(recieve_Packet.getData()).trim();
	}
	
	private DatagramPacket createPacket(int value, int index) {
		String str = String.valueOf(value) + "_" + index;
		byte[] arrData = str.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
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
