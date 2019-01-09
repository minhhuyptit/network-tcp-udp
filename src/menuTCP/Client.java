package menuTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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
		Socket client = new Socket(host, port);
		DataInputStream dis = new DataInputStream(client.getInputStream());
		DataOutputStream dos = new DataOutputStream(client.getOutputStream());

		// Nhập dữ liệu
		a = input("Nhập vào số a: ");
		b = input("Nhập vào số b: ");
		c = input("Nhập vào số c: ");
		d = input("Nhập vào số d: ");
		dos.writeInt(a);
		dos.writeInt(b);
		dos.writeInt(c);
		dos.writeInt(d);

		while (true) {
			// Nhận menu và in ra màn hình
			System.out.println(dis.readUTF());

			int choose = input("Nhập vào tùy chọn của bạn: ");
			//Gửi tùy chọn đến Server
			dos.writeInt(choose);
			switch (choose) {
			case 1:
				System.out.println("Ước chung lớn nhất = " + dis.readInt());
				break;
			case 2:
				System.out.println("Bội chung nhỏ nhất = " + dis.readInt());
				break;
			case 3:
				System.out.print("Danh sách sau khi sắp xếp tăng dần: ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				break;
			case 4:
				System.out.print("Danh sách sau khi sắp xếp giảm dần: ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				System.out.print(dis.readInt() + " ");
				break;
			case 5:
				System.out.println("Tổng các số lẻ = " + dis.readInt());
				break;
			case 6:
				System.out.println("Bội chung số chẵn = " + dis.readInt());
				break;
			case 7:
				System.out.println("Các số nguyên tố: ");
				int songuyento = dis.readInt();
				while(songuyento != -1) {
					System.out.print(songuyento + " ");
					songuyento = dis.readInt();
				}
				break;

			case 8:
				System.out.println("Các số chính phương: ");
				int sochinhphuong = dis.readInt();
				while(sochinhphuong != -1) {
					System.out.print(sochinhphuong + " ");
					sochinhphuong = dis.readInt();
				}
				break;

			case 9:
				System.out.println("Các số hoàn hảo: ");
				int sohoanhao = dis.readInt();
				while(sohoanhao != -1) {
					System.out.print(sohoanhao + " ");
					sohoanhao = dis.readInt();
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
				System.out.println(request);
				number = Integer.parseInt(sc.nextLine());
				flag = false;
			} catch (Exception e) {
				System.out.println("Dữ liệu không đúng định dạng. Vui lòng nhập lại!");
			}
		} while (flag);
		return number;
	}
}
