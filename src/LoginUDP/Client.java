package LoginUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	private void excecute() throws IOException {
		DatagramSocket client = new DatagramSocket();
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
			
			DatagramPacket id_DP = createPacket(id, 1);
			DatagramPacket pass_DP = createPacket(pass, 2);
			client.send(id_DP);
			client.send(pass_DP);
			
			String [] arr_result = recieveData(client).split("_");
			result = Integer.parseInt(arr_result[0]);
			if(result == -1) {
				System.out.println("Sai password. Vui lòng nhập lại");
			}else if(result == -2) {
				System.out.println("User không tồn tại. Vui lòng nhập lại");
			}else {
				System.out.println("Số tiền của bạn là: " + result);
				flag = false;
			}	
		}while(flag);
		client.close();
	}

	
	private DatagramPacket createPacket(int data, int index) {
		String str = String.valueOf(data) + "_" + index;
		byte[] arrData = str.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
	}
	
	private DatagramPacket createPacket(String data, int index) {
		String str = data + "_" + index;
		byte[] arrData = str.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
	}
	
	private String recieveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		return new String(recieve_Packet.getData()).trim();
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.excecute();
	}
}
