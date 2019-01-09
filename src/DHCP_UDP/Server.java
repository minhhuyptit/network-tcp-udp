package DHCP_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {
	private int port;
	private InetAddress clientIP;
	private int clientPort;
	public static Map<Data, Integer> listSK;

	public Server(int port) {
		this.port = port;
	}

	private void execute() throws IOException {
		// Tạo ServerSocket
		DatagramSocket server = new DatagramSocket(port);
		System.out.println("Server is listening...");
		
		ClientAlive clientAlive = new ClientAlive();
		clientAlive.start();

		while (true) {
			String data = recieveData(server);
			if (data.equals("alive")) {
				for (Map.Entry<Data, Integer> item : listSK.entrySet()) {
					InetAddress hostTemp = item.getKey().getHost();
					int portTemp = item.getKey().getPort();
					if (hostTemp.equals(clientIP) && portTemp == clientPort) {
						item.getKey().setTimeRemain(0);
						System.out.println(clientIP + " " + clientPort + " alive");
					}
				}
			} else if (checkExistClient(clientIP, clientPort) == false) { // Lần đầu tiên 1 Client nào đó kết nối
				sendData(menu(), server, clientIP, clientPort);
				// Lưu lại địa chỉ Client lần đầu kết nối
				Data dataTemp = new Data(clientIP, clientPort);
				listSK.put(dataTemp, -1);
				System.out.println("Đã kết nối với " + clientIP + " " + clientPort);
			} else {
				String[] arrData = data.split("_");
				int key = Integer.parseInt(arrData[1]);
				int value = Integer.parseInt(arrData[0]);
				switch (key) {
				case 1:
					while (true) {
						int numberRand = (int) (Math.random() * 255);
						if (listSK.containsValue(numberRand) == false) {
							// Gửi DHCP về cho Client
							sendData(numberRand, server, clientIP, clientPort);
							// Cập nhật lại DHCP của Client đó
							for (Map.Entry<Data, Integer> item : listSK.entrySet()) {
								InetAddress hostTemp = item.getKey().getHost();
								int portTemp = item.getKey().getPort();
								if (hostTemp.equals(clientIP) && portTemp == clientPort) {
									item.setValue(numberRand);
								}
							}
							break;
						}
					}
					break;
				case 2:
					if (value < 0 || value > 255) {
						// Gửi -1 báo hiệu DHCP not in range
						sendData(-1, server, clientIP, clientPort);
					} else if (listSK.containsValue(value)) {
						// Gửi -2 báo hiệu DHCP đã được sử dụng
						sendData(-2, server, clientIP, clientPort);
					} else {
						// Gửi -3 báo hiệu DHCP hợp lệ
						sendData(-3, server, clientIP, clientPort);
						// Cập nhật lại DHCP của Client đó
						for (Map.Entry<Data, Integer> item : listSK.entrySet()) {
							InetAddress hostTemp = item.getKey().getHost();
							int portTemp = item.getKey().getPort();
							if (hostTemp.equals(clientIP) && portTemp == clientPort) {
								item.setValue(value);
							}
						}
					}
					break;

				case 3:
					for (Data item : listSK.keySet()) {
						if (item.getHost().equals(clientIP) && item.getPort() == clientPort) {
							listSK.remove(item);
							System.out.println("Đã ngắt kết nối với " + clientIP + " " + clientPort);
						}
					}
					break;

				default:
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Server.listSK = new HashMap<Data, Integer>();
		Server server = new Server(15797);
		server.execute();
	}

	private String menu() {
		return "1.Cấp tự động\n2.Cấp bằng tay\n3.Thoát";
	}

	private String recieveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		clientIP = recieve_Packet.getAddress();
		clientPort = recieve_Packet.getPort();
		return new String(recieve_Packet.getData()).trim();
	}

	private void sendData(String value, DatagramSocket server, InetAddress clientIP, int clientPort)
			throws IOException {
		byte[] temp = new byte[1024];
		temp = value.getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}

	private void sendData(int value, DatagramSocket server, InetAddress clientIP, int clientPort) throws IOException {
		byte[] temp = new byte[1024];
		temp = String.valueOf(value).getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}

	private boolean checkExistClient(InetAddress clientIP, int clientPort) {
		for (Data item : listSK.keySet()) {
			if (item.getHost().equals(clientIP) && item.getPort() == clientPort) {
				return true;
			}
		}
		return false;
	}
}

class Data {
	private InetAddress host;
	private int port;
	private int timeRemain;

	public Data(InetAddress host, int port) {
		this.host = host;
		this.port = port;
		this.timeRemain = 0;
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeRemain() {
		return timeRemain;
	}

	public void setTimeRemain(int timeRemain) {
		this.timeRemain = timeRemain;
	}
}

class ClientAlive extends Thread{
	@Override
	public void run() {
		while(true) {
			try {
				for (Map.Entry<Data, Integer> item : Server.listSK.entrySet()) {
					int timeRemain = item.getKey().getTimeRemain();
					if(timeRemain > 10) {		//Nếu quá 10s Client sẽ xóa Client đó trong danh sách
						Server.listSK.remove(item);
					}else {
						item.getKey().setTimeRemain(timeRemain+1); //Nếu chưa quá 10s thì thời gian của Client đang duyệt tăng 1s
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
