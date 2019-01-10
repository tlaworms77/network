package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 * 서버 기능 정의
 * 1. 서버는 여러 클라이언트가 접속할 수 있어야한다. (다중 처리 가능, 멀티스레드 프로그램이)
 * 2. 서버는 여러 클라이언트에게 동시에 메시지를 보낼 수 있는 브로드캐스팅(Broadcasting)기능이 있어야 한다.
 * 3. EchoServer의 각 스레드는 자신의 IO Stream 객체만 사용하면 되었지만, Chat Server에서는 다른 스
 * 	    레드의 IO Stream을 사용해야 한다. ( PrintWriter 객체 )
 * 4. 닉네임을 등록하기 위한 요청, 메시지를 전달하기 위한 요청, 방을 나가기 위한 요청 등 클라이언트의 요청을
 *    구별하기 위한 프로토콜(채팅 프로토콜)을 설계해야 한다.
 * ex)
 * 	JOIM:안대혁\r\n
 * 	MESSAGE:방가^^;\r\n
 * 	QUIT\r\n
 */

public class ChatServer {
	private static final int PORT = 8000;
	
	public static void main(String[] args) {
		ServerSocket serverScoket = null;

		try {
			// 1. 서버 소켓 생성
			serverScoket = new ServerSocket();

			// 2. 바인딩 //192.168.0.73
			String localhost = InetAddress.getLocalHost().getHostAddress();
			serverScoket.bind(new InetSocketAddress(localhost, PORT));
			log("binding... " + localhost + ":" + PORT);

			// writer pool
			List<Writer> listWriters = new ArrayList<Writer>();
			
			// 3. 요청 대기
			while(true) {
				Socket socket = serverScoket.accept();
				// 수락된 소켓을 생성자에서 초기화시킨 후 Thread 생성 및 run 
				new ChatServerThread(socket, listWriters).start();
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error:" + e);
		} finally {
			try {
				if(serverScoket != null && serverScoket.isClosed() == false)
					serverScoket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	static void log(String log) {
		System.out.println(log);
	}
}
