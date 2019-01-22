package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	public static final int PORT = 6000;
	public static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {

		DatagramSocket socket = null;
		try {
			// 1. socket 생성
			socket = new DatagramSocket(PORT);
			
			while (true) {
			
				// 2. 데이터 수신(packet수신) - receive
				// --> 내용을 넣은 버퍼 필요
				DatagramPacket receivePacket =
						new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);
				
				// 패킷안의 버퍼 데이터를 가져온다. 하지만 내용이 스트링이니 바꿔야한다.
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				String message = new String(data, 0, length, "UTF-8");
				
				System.out.println("[server] received:" + message);
				
				// 3. 데이터 전송
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, sendData.length,
								receivePacket.getAddress(), receivePacket.getPort());
				socket.send(sendPacket);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 소켓이 생성 되있을 때와 닫혀있는 경우
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

}
