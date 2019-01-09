package menuTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Server {
	private int port;
	private int a;
	private int b;
	private int c;
	private int d;

	public Server(int port) {
		this.port = port;
	}
	
	private void execute() throws IOException{
		System.out.println("Server is listening...");
		//Cấu hình Server
		ServerSocket server = new ServerSocket(port);
		Socket socket = server.accept();
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		//Nhận dữ liệu từ Client
		a = dis.readInt();
		b = dis.readInt();
		c = dis.readInt();
		d = dis.readInt();
		int [] arr = {a,b,c,d};
		
		while(true) {
			//Gửi menu về Client
			dos.writeUTF(menu());
			//Nhận tùy chọn menu
			int choose = dis.readInt();
			switch (choose) {
			case 1:
				int UCLN = GCD(arr[0], GCD(arr[1], GCD(arr[2], arr[3])));
				dos.writeInt(UCLN);
				break;
			case 2:
				dos.writeInt(LCM(arr));
				break;
			case 3:
				ArrayList<Integer> arrASC = ASC(arr);	//Mảng sau khi sắp xếp
				dos.writeInt(arrASC.get(0));
				dos.writeInt(arrASC.get(1));
				dos.writeInt(arrASC.get(2));
				dos.writeInt(arrASC.get(3));
				break;
			case 4:
				ArrayList<Integer> arrDESC = DESC(arr);	//Mảng sau khi sắp xếp
				dos.writeInt(arrDESC.get(0));
				dos.writeInt(arrDESC.get(1));
				dos.writeInt(arrDESC.get(2));
				dos.writeInt(arrDESC.get(3));
				break;
			case 5:
				dos.writeInt(sumOfOdd(arr));
				break;
			case 6:
				dos.writeInt(sumOfEven(arr));
				break;
			case 7:
				for (Integer item : arr) {
					if(soNguyenTo(item) == true) {
						dos.writeInt(item);
					}
				}
				dos.writeInt(-1);	//Đánh dấu kết thúc
				break;

			case 8:
				for (Integer item : arr) {
					if(soChinhPhuong(item) == true) {
						dos.writeInt(item);
					}
				}
				dos.writeInt(-1);	//Đánh dấu kết thúc
				break;

			case 9:
				for (Integer item : arr) {
					if(soHoanHao(item) == true) {
						dos.writeInt(item);
					}
				}
				dos.writeInt(-1);	//Đánh dấu kết thúc
				break;
			case 10:
				break;

			default:
				break;
			}
			if(choose == 10) break;
		}
		dis.close();
		dos.close();
		server.close();
	}
	private String menu() {
		String menu = "\n\n============MENU============\n"
					+ "1. Tìm ước chung lớn nhất\n" 
					+ "2. Tìm bội chung nhỏ nhất\n" 
					+ "3. Sắp xếp tăng dần\n"
					+ "4. Sắp xếp giảm dần\n" 
					+ "5. Tổng tất cả các số lẻ\n" 
					+ "6. Tổng tất cả các số chẵn\n"
					+ "7. Các số nguyên tố\n"
					+ "8. Các số chính phương\n" 
					+ "9. Các số hoàn hảo\n" 
					+ "10. Thoát";
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
	
	private int LCM(int [] arr)
	{
		int max = 0, k = 1;
        for (int i = 0; i < arr.length; i++) {	//Tìm số lớn nhất trong mảng
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
	
	private ArrayList<Integer> ASC (int [] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i : arr) {
			list.add(i);
		}
		Collections.sort(list);
		return list;
	}
	
	private ArrayList<Integer> DESC (int [] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i : arr) {
			list.add(i);
		}
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	private int sumOfOdd(int [] arr) {
		int sum = 0;
		for (int i : arr) {
			if(i % 2 == 1) {
				sum += i;
			}
		}
		return sum;
	}
	
	private int sumOfEven(int [] arr) {
		int sum = 0;
		for (int i : arr) {
			if(i % 2 == 0) {
				sum += i;
			}
		}
		return sum;
	}
	
	private boolean soNguyenTo(int n) {
		if(n < 2) return false;
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if((n%i == 0) && (n != i)) return false;
		}
		return true;
	}
	
	private boolean soChinhPhuong(int n) {
		int temp = (int)Math.sqrt(n);
		return (temp*temp == n);
	}
	
	private boolean soHoanHao(int n) {
		int tong = 0;
        for (int i = 1; i < n ; i ++){
            if ( n % i == 0 ) tong = tong + i;
        }
        return (tong == n);
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(15797);
		server.execute();
	}
	
}
