package DHCP_UDP;

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
		// Tạo Client Socket
		DatagramSocket client = new DatagramSocket();
		// Gửi data giả để nhận menu
		client.send(createPacket("connect"));
		// Nhận menu
		String menu = recieveData(client);
		
		Alive alive = new Alive(client, host, port);
		alive.start();

		while (true) {
			// In ra menu
			System.out.println(menu);

			int choose = input("Nhập vào tùy chọn của bạn: ");

			switch (choose) {
			case 1:
				client.send(createPacket(-1, 1)); // key == 1 đánh dấu gửi menu tùy chọn 1
				// Nhận dữ liệu về
				System.out.println("DHCP của bạn là: " + recieveData(client));
				break;
			case 2:
				boolean flag = true;
				do {
					int number = input("Nhập vào DHCP bạn muốn: ");
					client.send(createPacket(number, 2)); // key == 2 đánh dấu gửi menu tùy chọn 2

					int result = Integer.parseInt(recieveData(client));
					if (result == -1) {
						System.out.println("Số DHCP phải nằm trong khoảng(0-255). Vui lòng chọn số DHCP khác!");
					} else if (result == -2) {
						System.out.println("Số DHCP này đã được sử dụng. Vui lòng chọn số DHCP khác!");
					} else if (result == -3) {
						System.out.println("DHCP của bạn là: " + number);
						flag = false;
					}
				} while (flag);
				break;
			case 3:
				client.send(createPacket(-1, 3)); // key == 3 đánh dấu gửi menu tùy chọn 3
				alive.stop();
				System.out.println("Kết thúc");
				break;

			default:
				System.out.println("Vui lòng chỉ nhập số trong menu");
				break;
			}
			if (choose == 3) {
				break;
			}
		}

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

	private DatagramPacket createPacket(String value) {
		byte[] arrData = value.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
	}

	private DatagramPacket createPacket(int value, int index) {
		String str = String.valueOf(value) + "_" + index;
		byte[] arrData = str.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
	}

	private String recieveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		return new String(recieve_Packet.getData()).trim();
	}

}

class Alive extends Thread {
	private DatagramSocket client;
	private InetAddress host;
	private int port;

	public Alive(DatagramSocket client, InetAddress host, int port) {
		this.client = client;
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		while (true) {
			try {
				client.send(createPacket("alive"));
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private DatagramPacket createPacket(String value) {
		byte[] arrData = value.getBytes();
		return new DatagramPacket(arrData, arrData.length, host, port);
	}
}
