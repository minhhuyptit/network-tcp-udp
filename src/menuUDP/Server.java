package menuUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

public class Server {
	private int port;
	private InetAddress clientIP;
	private int clientPort;
	private int a;
	private int b;
	private int c;
	private int d;

	public Server(int port) {
		this.port = port;
	}

	private void execute() throws IOException {
		// Tạo Socket
		DatagramSocket server = new DatagramSocket(port);
		System.out.println("Server is listening....");
		// Nhận gói tin từ Client
		int[] mangSo = new int[4];
		String[] arr_a = receiveData(server).split("_");
		String[] arr_b = receiveData(server).split("_");
		String[] arr_c = receiveData(server).split("_");
		String[] arr_d = receiveData(server).split("_");

		mangSo[Integer.parseInt(arr_a[1]) - 1] = Integer.parseInt(arr_a[0]);
		mangSo[Integer.parseInt(arr_b[1]) - 1] = Integer.parseInt(arr_b[0]);
		mangSo[Integer.parseInt(arr_c[1]) - 1] = Integer.parseInt(arr_c[0]);
		mangSo[Integer.parseInt(arr_d[1]) - 1] = Integer.parseInt(arr_d[0]);

		a = mangSo[0];
		b = mangSo[1];
		c = mangSo[2];
		d = mangSo[3];

		while (true) {
			// Gửi menu về Client
			sendMenu(menu(), server, clientIP, clientPort);
			// Nhận tùy chọn từ Client
			String[] arr_choose = receiveData(server).split("_");
			int choose = Integer.parseInt(arr_choose[0]);
			switch (choose) {
			case 1:
				int UCLN = GCD(a, GCD(b, GCD(c, d)));
				sendData(UCLN, -1, server, clientIP, clientPort);
				break;
			case 2:
				sendData(LCM(mangSo), -1, server, clientIP, clientPort);
				break;
			case 3:
				ArrayList<Integer> arrASC = ASC(mangSo); // Mảng sau khi sắp xếp
				sendData(arrASC.get(0), 1, server, clientIP, clientPort);
				sendData(arrASC.get(1), 2, server, clientIP, clientPort);
				sendData(arrASC.get(2), 3, server, clientIP, clientPort);
				sendData(arrASC.get(3), 4, server, clientIP, clientPort);
				break;
			case 4:
				ArrayList<Integer> arrDESC = DESC(mangSo); // Mảng sau khi sắp xếp
				sendData(arrDESC.get(0), 1, server, clientIP, clientPort);
				sendData(arrDESC.get(1), 2, server, clientIP, clientPort);
				sendData(arrDESC.get(2), 3, server, clientIP, clientPort);
				sendData(arrDESC.get(3), 4, server, clientIP, clientPort);
				break;
			case 5:
				sendData(sumOfOdd(mangSo), -1, server, clientIP, clientPort);
				break;
			case 6:
				sendData(sumOfEven(mangSo), -1, server, clientIP, clientPort);
				break;
			case 7:
				int count1 = 1;
				for (Integer item : mangSo) {
					if (soNguyenTo(item) == true) {
						sendData(item, count1, server, clientIP, clientPort);
						count1++;
					}
				}
				sendData(-1, -1, server, clientIP, clientPort);
				break;

			case 8:
				int count2 = 1;
				for (Integer item : mangSo) {
					if (soChinhPhuong(item) == true) {
						sendData(item, count2, server, clientIP, clientPort);
						count2++;
					}
				}
				sendData(-1, -1, server, clientIP, clientPort);
				break;

			case 9:
				int count3 = 1;
				for (Integer item : mangSo) {
					if (soHoanHao(item) == true) {
						sendData(item, count3, server, clientIP, clientPort);
						count3++;
					}
				}
				sendData(-1, -1, server, clientIP, clientPort);
				break;
			case 10:
				break;

			default:
				break;
			}
			if (choose == 10)
				break; // Bằng 10 thoát
		}
		server.close();
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server(15797);
		server.execute();
	}

	private String receiveData(DatagramSocket client) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket receive_DP = new DatagramPacket(temp, temp.length);
		client.receive(receive_DP);
		clientIP = receive_DP.getAddress();
		clientPort = receive_DP.getPort();
		return new String(receive_DP.getData()).trim();
	}

	private void sendData(int value, int index, DatagramSocket server, InetAddress clientIP, int clientPort)
			throws IOException {
		byte[] temp = new byte[1024];
		String str = String.valueOf(value) + "_" + index;
		temp = str.getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}

	private void sendMenu(String value, DatagramSocket server, InetAddress clientIP, int clientPort)
			throws IOException {
		byte[] temp = new byte[1024];
		temp = value.getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}

	private String menu() {
		String menu = "\n\n============MENU============\n" + "1. Tìm ước chung lớn nhất\n"
				+ "2. Tìm bội chung nhỏ nhất\n" + "3. Sắp xếp tăng dần\n" + "4. Sắp xếp giảm dần\n"
				+ "5. Tổng tất cả các số lẻ\n" + "6. Tổng tất cả các số chẵn\n" + "7. Các số nguyên tố\n"
				+ "8. Các số chính phương\n" + "9. Các số hoàn hảo\n" + "10. Thoát";
		return menu;
	}

	private int GCD(int a, int b) {
		while (a != b) {
			if (a > b)
				a = a - b;
			else
				b = b - a;
		}
		return a;
	}

	private int LCM(int[] arr) {
		int max = 0, k = 1;
		for (int i = 0; i < arr.length; i++) { // Tìm số lớn nhất trong mảng
			if (max < arr[i]) {
				max = arr[i];
			}
		}
		int temp = max;
		for (int i = 0; i < arr.length; i++) {
			while (max % arr[i] != 0) {
				k++;
				max = temp * k;
			}
		}
		return max;
	}

	private ArrayList<Integer> ASC(int[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i : arr) {
			list.add(i);
		}
		Collections.sort(list);
		return list;
	}

	private ArrayList<Integer> DESC(int[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i : arr) {
			list.add(i);
		}
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	private int sumOfOdd(int[] arr) {
		int sum = 0;
		for (int i : arr) {
			if (i % 2 == 1) {
				sum += i;
			}
		}
		return sum;
	}

	private int sumOfEven(int[] arr) {
		int sum = 0;
		for (int i : arr) {
			if (i % 2 == 0) {
				sum += i;
			}
		}
		return sum;
	}

	private boolean soNguyenTo(int n) {
		if (n < 2)
			return false;
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if ((n % i == 0) && (n != i))
				return false;
		}
		return true;
	}

	private boolean soChinhPhuong(int n) {
		int temp = (int) Math.sqrt(n);
		return (temp * temp == n);
	}

	private boolean soHoanHao(int n) {
		int tong = 0;
		for (int i = 1; i < n; i++) {
			if (n % i == 0)
				tong = tong + i;
		}
		return (tong == n);
	}

}
