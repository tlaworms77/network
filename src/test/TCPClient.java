package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.0.73";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {
		
		Socket socket = null;
		
		try {
			//1. 소켓 생성
			socket = new Socket();

			//1-1. socket buffer size 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			
			System.out.println(
					receiveBufferSize + ":" + 
					sendBufferSize
					);
			//1-2. socket buffer size 변경
			socket.setReceiveBufferSize(1024 * 10);
			socket.setSendBufferSize(1024 * 10);
			
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			
			System.out.println(
					receiveBufferSize + ":" + 
					sendBufferSize
					);
			
			//1-3. SO_NODELAY ( Nagle Algorithm off ) --> 딜레이없이 막 보낸다는 의미
			socket.setTcpNoDelay( true );
			
			//1-4 타임아웃 ( SO_TIMEOUT ) - java [write -> IOExcetion] read -> socketTimeoutException
			socket.setSoTimeout( 1 );
			
			//2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			//3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("UTF-8"));
			
			//5. 읽기
			byte[] buffer = new byte[255];
			int readByteCount = is.read(buffer); //Blocking
			if(readByteCount == -1) {
				System.out.println("[client] closed by server");
				return;
			}
			
			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println("[client] received:" + data);

		} catch(SocketTimeoutException e){
			System.out.println("[client] timeout");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
