package LoginUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import LoginTCP.DBConnection;

public class Server {
	private int port;
	private InetAddress clientIP;
	private int clientPort;

	public Server(int port) {
		this.port = port;
	}
	
	private void execute() throws IOException, SQLException {
		DatagramSocket server = new DatagramSocket(port);

		boolean flag = true;
		int result;
		
		do {
			System.out.println("Server is listening...");
			//Nhận dữ liệu từ Client
			String [] arrTemp = new String[2];
			String [] arr_a = recieveData(server).split("_");
			String [] arr_b = recieveData(server).split("_");
			
			System.out.println("Zo day");
			
			arrTemp[Integer.parseInt(arr_a[1]) - 1] = arr_a[0];
			arrTemp[Integer.parseInt(arr_b[1]) - 1] = arr_b[0];
			
			int id = Integer.parseInt(arrTemp[0]);
			String pass = arrTemp[1];
			
			Connection connect = DBConnection.getConnection();
			String query = "SELECT dbo.FN_Login(?, ?) AS result";
			PreparedStatement ps = connect.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = rs.getInt("result");
			if(result == -1) {
				sendData(-1, 1, server, clientIP, clientPort);
			}else if(result == -2) {
				sendData(-2, 1, server, clientIP, clientPort);
			}else {
				sendData(result, 1, server, clientIP, clientPort);
				flag = false;
			}	
			DBConnection.closeConnection(connect);
			
		} while (flag);
		server.close();
	}
	
	private String recieveData(DatagramSocket server) throws IOException {
		byte[] temp = new byte[1024];
		DatagramPacket recieve_Packet = new DatagramPacket(temp, temp.length);
		server.receive(recieve_Packet);
		clientIP = recieve_Packet.getAddress();
		clientPort = recieve_Packet.getPort();
		return new String(recieve_Packet.getData()).trim();
	}

	private void sendData(int value, int index, DatagramSocket server, InetAddress clientIP, int clientPort)
			throws IOException {
		byte[] temp = new byte[1024];
		String str = String.valueOf(value) + "_" + index;
		temp = str.getBytes();
		DatagramPacket send_result_Packet = new DatagramPacket(temp, temp.length, clientIP, clientPort);
		server.send(send_result_Packet);
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		Server server = new Server(15797);
		server.execute();
	}
}
