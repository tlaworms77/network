package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "192.168.0.73";
	private static final int PORT = 8000;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner sc = null;
		Socket socket = null;

		try {

			// 1. 키보드 연결ㄹ
			sc = new Scanner(System.in);

			// 2. socket 생성
			socket = new Socket();

			// 3. socket 연결
			socket.connect(new InetSocketAddress(SERVER_IP, PORT));

			// 4. reader/writer 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			// 5. join 프로토콜
			System.out.print("닉네임>>");
			String nickName = sc.nextLine();
			pw.println("join:" + nickName);
			//pw.flush();

			//6. ChatClientReceivedThread 시작
			new ChatClientThread(br).start();
			
			//7. 키보드 입력 처리
			while(true) {
				
				String input = sc.nextLine();
				
				if("quit".equals(input) == true) {
					//8.quit 프로토콜 처리
					pw.println("quit");
					//pw.flush();
					break;
				}else {
					//9. 메시지 처리
					//System.out.println("[" + Thread.currentThread().getId() + "] " + nickName + "+ sendmessage");
					pw.println("message:" + input);
				}
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("1 error:" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("2 error:" + e);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("3 error:" + e);
		} finally {
			try {
				if(sc != null) {
					sc.close();
				}
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error:" + e);
			}
		}

	}

}
