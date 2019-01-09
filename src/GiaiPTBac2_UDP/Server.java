package GiaiPTBac2_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
	private int port;
	private InetAddress clientIP;
	private int clientPort;
	public static ArrayList<Data> listSK;

	public Server(int port) {
		this.port = port;
	}

	private void execute() throws IOException {
		// Tạo Server Socket
		DatagramSocket server = new DatagramSocket(port);
		System.out.println("Server is listening....");
		while (true) {
			String[] arrData = recieveData(server).split("_");
			int key = Integer.parseInt(arrData[1]);
			int value = Integer.parseInt(arrData[0]);
			if (checkClientExist(clientIP, clientPort) == false) { // Client này kết nối lần đầu
				Data data = new Data(clientIP, clientPort);
				// Gán dữ liệu vào
				if (key == 1) {
					data.setA(value);
				} else if (key == 2) {
					data.setB(value);
				} else if (key == 3) {
					data.setC(value);
				}
				listSK.add(data);
			} else { // Đã tồn tại
				for (Data item : listSK) {
					if (item.getHost().equals(clientIP) && item.getPort() == clientPort) {
						// Gán dữ liệu vào
						if (key == 1) {
							item.setA(value);
						} else if (key == 2) {
							item.setB(value);
						} else if (key == 3) {
							item.setC(value);
						}
					}

					// Kiểm tra xem Client này đã đưa đủ dữ liệu thì giải PT
					if(item.getCount() == 3) {
						giaiPTBac2(server, item);
						//Sau khi tính toán cũng xóa Client trong danh sách ra
						listSK.remove(item);
						break;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server.listSK = new ArrayList<>();
		Server server = new Server(15797);
		server.execute();
	}
	
	private void giaiPTBac2(DatagramSocket server, Data data) throws IOException {
		/*
		 * -1: vô nghiệm	-2: vô số nghiệm	-3: 1 nghiệm kép	-4: 2 nghiệm
		 * */
		int a = data.getA();
		int b = data.getB();
		int c = data.getC();
		
		//Xử lý và gửi về Client
		if(a == 0) {
			if(b == 0) {
				if(c == 0) {	//Phương trình vô số nghiệm
					sendData(0, -2, server, clientIP, clientPort);
				}else {			//Phương trình vô nghiệm
					sendData(0, -1, server, clientIP, clientPort);
				}
			}else {	//Phương trình có 1 nghiệm
				sendData((-c/b), -3, server, clientIP, clientPort);
			}
			return;
		}
		
		double delta = (b*b) - (4*a*c);
		double x1;
		double x2;
		if(delta > 0) {			//Phương trình có 2 nghiệm
			x1 = (double) ((-b + Math.sqrt(delta)) / (2 * a));
			x2 = (double) ((-b - Math.sqrt(delta)) / (2 * a));
			sendData(x1, -4, server, clientIP, clientPort);
			sendData(x2, -4, server, clientIP, clientPort);
		}else if(delta == 0) {	//Phương trình có 1 nghiệm
			x1 = (-b / (2 * a));
			sendData(x1, -3, server, clientIP, clientPort);
		}else {					//Phương trình vô nghiệm
			sendData(0, -1, server, clientIP, clientPort);
		}
		
	}
	
	private void sendData(double value, int index, DatagramSocket server, InetAddress clientIP, int clientPort) throws IOException {
		byte[] temp = new byte[1024];
		String str = String.valueOf(value) + "_" + index;
		temp = str.getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}

	private String recieveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		clientIP = recieve_Packet.getAddress();
		clientPort = recieve_Packet.getPort();
		return new String(recieve_Packet.getData()).trim();
	}

	private boolean checkClientExist(InetAddress clientIP, int clientPort) {
		for (Data item : listSK) {
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
	private int a;
	private int b;
	private int c;
	private int count;

	public Data(InetAddress host, int port) {
		this.host = host;
		this.port = port;
		this.a = 0;
		this.b = 0;
		this.c = 0;
		this.count = 0;
	}

	public InetAddress getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setA(int a) {
		this.a = a;
		this.count++;
	}

	public void setB(int b) {
		this.b = b;
		this.count++;
	}

	public void setC(int c) {
		this.c = c;
		this.count++;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
