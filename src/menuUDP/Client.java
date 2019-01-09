package menuUDP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	private int a;
	private int b;
	private int c;
	private int d;
	private Scanner sc = new Scanner(System.in);

	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	private void execute() throws IOException {
		DatagramSocket client = new DatagramSocket();

		// Nhập dữ liệu
		a = input("Nhập vào số a: ");
		b = input("Nhập vào số b: ");
		c = input("Nhập vào số c: ");
		d = input("Nhập vào số d: ");

		// Đóng gói Packet
		DatagramPacket a_DP = createPacket(a, 1);
		DatagramPacket b_DP = createPacket(b, 2);
		DatagramPacket c_DP = createPacket(c, 3);
		DatagramPacket d_DP = createPacket(d, 4);

		// Gửi các Packet đến Server
		client.send(a_DP);
		client.send(b_DP);
		client.send(c_DP);
		client.send(d_DP);

		while (true) {
			// Nhận menu và in ra màn hình
			System.out.println("\n" + receiveData(client));

			int choose = input("Nhập vào tùy chọn của bạn: ");
			// Gửi tùy chọn đến Server
			DatagramPacket choose_DP = createPacket(choose, -1);
			client.send(choose_DP);
			switch (choose) {
			case 1:
				String[] GCD = receiveData(client).split("_");
				System.out.println("Ước chung lớn nhất = " + GCD[0]);
				break;
			case 2:
				String[] LCM = receiveData(client).split("_");
				System.out.println("Bội chung nhỏ nhất = " + LCM[0]);
				break;

			case 3:
				int[] mangASC = new int[4];
				String[] arr_a_ASC = receiveData(client).split("_");
				String[] arr_b_ASC = receiveData(client).split("_");
				String[] arr_c_ASC = receiveData(client).split("_");
				String[] arr_d_ASC = receiveData(client).split("_");
				mangASC[Integer.parseInt(arr_a_ASC[1]) - 1] = Integer.parseInt(arr_a_ASC[0]);
				mangASC[Integer.parseInt(arr_b_ASC[1]) - 1] = Integer.parseInt(arr_b_ASC[0]);
				mangASC[Integer.parseInt(arr_c_ASC[1]) - 1] = Integer.parseInt(arr_c_ASC[0]);
				mangASC[Integer.parseInt(arr_d_ASC[1]) - 1] = Integer.parseInt(arr_d_ASC[0]);
				
				System.out.print("Danh sách sau khi sắp xếp tăng dần:");
				for (int item : mangASC) {
					System.out.print(item + " ");
				}
				break;
			case 4:
				int[] mangDESC = new int[4];
				String[] arr_a_DESC = receiveData(client).split("_");
				String[] arr_b_DESC = receiveData(client).split("_");
				String[] arr_c_DESC = receiveData(client).split("_");
				String[] arr_d_DESC = receiveData(client).split("_");
				mangDESC[Integer.parseInt(arr_a_DESC[1]) - 1] = Integer.parseInt(arr_a_DESC[0]);
				mangDESC[Integer.parseInt(arr_b_DESC[1]) - 1] = Integer.parseInt(arr_b_DESC[0]);
				mangDESC[Integer.parseInt(arr_c_DESC[1]) - 1] = Integer.parseInt(arr_c_DESC[0]);
				mangDESC[Integer.parseInt(arr_d_DESC[1]) - 1] = Integer.parseInt(arr_d_DESC[0]);
				
				System.out.print("Danh sách sau khi sắp xếp giảm dần:");
				for (int item : mangDESC) {
					System.out.print(item + " ");
				}
				break;
			case 5:
				String[] sumOfOdd  = receiveData(client).split("_");
				System.out.println("Tổng các số lẻ = " + sumOfOdd[0]);
				break;
			case 6:
				String[] sumOfEven = receiveData(client).split("_");
				System.out.println("Tổng các số chẵn = " + sumOfEven[0]);
				break;
			case 7:
				int [] arr_songuyento = new int[4];

				String[] arr_Value1 = receiveData(client).split("_");
				int value1 = Integer.parseInt(arr_Value1[0]);
				int key1   = Integer.parseInt(arr_Value1[1]);	
				
				
				while(key1 != -1 && value1 != -1) {
					arr_songuyento[key1-1] = value1;
					arr_Value1 = receiveData(client).split("_");
					value1 = Integer.parseInt(arr_Value1[0]);
					key1   = Integer.parseInt(arr_Value1[1]);
				}
				
				System.out.print("Các số nguyên tố: ");
				for (int item : arr_songuyento) {
					if(item != 0) {	//Nếu == 0 tức là phần tử mảng dư thừa
						System.out.print(item + " ");							
					}
				}
				break;
			case 8:
				int [] arr_sochinhphuong = new int[4];
				String[] arr_Value2 = receiveData(client).split("_");
				int value2 = Integer.parseInt(arr_Value2[0]);
				int key2   = Integer.parseInt(arr_Value2[1]);
				
				while(key2 != -1 && value2 != -1) {
					arr_sochinhphuong[key2-1] = value2;
					arr_Value2 = receiveData(client).split("_");
					value2 = Integer.parseInt(arr_Value2[0]);
					key2   = Integer.parseInt(arr_Value2[1]);
				}
				
				System.out.print("Các số chính phương: ");
				for (int item : arr_sochinhphuong) {
					if(item != 0) {	//Nếu == 0 tức là phần tử mảng dư thừa
						System.out.print(item + " ");							
					}
				}
				break;
			case 9:
				int [] arr_sohoanhao = new int[4];
				String[] arr_Value3 = receiveData(client).split("_");
				int value3 = Integer.parseInt(arr_Value3[0]);
				int key3   = Integer.parseInt(arr_Value3[1]);
				
				while(key3 != -1 && value3 != -1) {
					arr_sohoanhao[key3-1] = value3;
					arr_Value3 = receiveData(client).split("_");
					value3 = Integer.parseInt(arr_Value3[0]);
					key3   = Integer.parseInt(arr_Value3[1]);
				}
				
				System.out.print("Các số hoàn hảo: ");
				for (int item : arr_sohoanhao) {
					if(item != 0) {	//Nếu == 0 tức là phần tử mảng dư thừa
						System.out.print(item + " ");							
					}
				}
				break;
			case 10:
				break;
				
			default:
				System.out.println("Vui lòng chỉ nhập số trong menu!");
				break;
			}
			if(choose == 10) break; // Bằng 10 thoát
		}
		client.close();
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 15797);
		client.execute();
	}
	
	private String receiveData(DatagramSocket client) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket receive_DP = new DatagramPacket(temp, temp.length);
		client.receive(receive_DP);
		return new String(receive_DP.getData()).trim();
	}

	private DatagramPacket createPacket(int data, int index) {
		String str = String.valueOf(data) + "_" + index;
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
				System.out.println("Dữ liệu không đúng định dạng. Vui lòng nhập lại!");
			}
		} while (flag);
		return number;
	}
}
