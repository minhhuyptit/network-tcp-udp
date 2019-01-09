package LoginTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
	private int port;

	public Server(int port) {
		this.port = port;
	}
	
	private void execute() throws IOException, SQLException {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Server is listening....");
		Socket socket = server.accept();
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		int id = 0;
		String pass = "";
		boolean flag = true;
		int result = 0;
		do {
			id = dis.readInt();
			pass = dis.readUTF();
			Connection connect = DBConnection.getConnection();
			String query = "SELECT dbo.FN_Login(?, ?) AS result";
			PreparedStatement ps = connect.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = rs.getInt("result");
			if(result == -1) {
				dos.writeInt(-1);
			}else if(result == -2) {
				dos.writeInt(-2);
			}else {
				dos.writeInt(result);
				flag = false;
			}	
			DBConnection.closeConnection(connect);
		} while (flag);
		dis.close();
		dos.close();
		socket.close();
		server.close();
	}
	public static void main(String[] args) throws IOException, SQLException {
		Server server = new Server(15797);
		server.execute();

	}
	
}
