package chat;

import java.io.IOException;
import java.io.PrintWriter;
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
	private static final int PORT = 9999;
	
	public static void main( String[] args ) {
		List<PrintWriter> listPrintWriter =
				new ArrayList<PrintWriter>();
		
		ServerSocket serverSocket = null;
		
		try {
			//1. create server socket
			serverSocket = new ServerSocket();
			
			//1-1. set option SO_REUSEADDR 
			//     (종료후 빨리 바인딩을 하기 위해서 )
			serverSocket.setReuseAddress( true );
			
			//2. binding
			String localhost = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind( new InetSocketAddress(localhost, PORT), 5 );
			consoleLog( "binding " + localhost + ":" + PORT );
			
			while( true ) {
				//3. wating for connection
				Socket socket = serverSocket.accept();
				
				Thread thread = new ChatServerThread( socket, listPrintWriter  );
				thread.start();
			}
		} catch (IOException e) {
			consoleLog( "error:" + e );
		} finally {
			try {
				if( serverSocket != null && 
					serverSocket.isClosed() == false ){
					serverSocket.close();
				}
			}catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}
	}
	
	public static void consoleLog( String message ) {
		System.out.println( "[chat server]" + message );
	}
}