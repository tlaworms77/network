package udp.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

import udp.UDPEchoServer;

public class UDPTimeClient {

	private static final String SERVER_IP = "192.168.0.73";

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		Scanner scanner = null;
		DatagramSocket socket = null;

		try {
			// 1. 키보드 연결
			scanner = new Scanner(System.in);

			// 2. 소켓 생성
			socket = new DatagramSocket();
			while (true) {
				// 3. 사용자 입력 받음
				System.out.print(">>");
				String message = scanner.nextLine();

				if ("quit".equals(message)) {
					break;
				}

				// 4. 메시지 전송
				byte[] data = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(data, data.length,
						new InetSocketAddress(SERVER_IP, UDPEchoServer.PORT));
				socket.send(sendPacket);

				// 5. 메시지 수신
				DatagramPacket receivePacket = 
						new DatagramPacket(new byte[UDPEchoServer.BUFFER_SIZE], UDPEchoServer.BUFFER_SIZE);
				
//				synchronized (socket) {
//					socket.receive(receivePacket);
//				}
				socket.receive(receivePacket);
				
				message = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("<<" + message);
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
			if (socket != null && socket.isClosed() == false)
				socket.close();
		}

	}

}
