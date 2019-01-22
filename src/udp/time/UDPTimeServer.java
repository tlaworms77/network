package udp.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	private static final int PORT = 6000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;

		try {
			// 1. 소켓 생성
			socket = new DatagramSocket(PORT);

			while (true) {
				// 2. 데이터 수신(packet수신) - receive
				// --> 내용을 넣은 버퍼 필요
				DatagramPacket receivePaket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePaket);

				// 패킷안의 버퍼 데이터를 가져온다. 하지만 내용이 스트링이니 바꿔야한다.
				byte[] bufferData = receivePaket.getData();
				String message = new String(bufferData, 0, bufferData.length, "UTF-8");

				System.out.println("[server] received : reqeust Time by client!!");
				
				if ("".equals(message.trim())) {
//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS a");
					String data = format.format(new Date());

					// 3. 데이터 전송
					byte[] sendData = data.getBytes("UTF-8");
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePaket.getAddress(),
							receivePaket.getPort());
					socket.send(sendPacket);
					System.out.println("[server] send Time Message : Success!!");
				} else {
					String errStr = "잘못 요청하셨습니다.";
					byte[] sendData = errStr.getBytes("UTF-8");
					socket.send(new DatagramPacket(sendData, sendData.length, receivePaket.getAddress(),
							receivePaket.getPort()));
					System.out.println("[server] send Time Message : Fail!!");
					System.out.println("from [client] to [server] MSG:" + message + ")");
						
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 자원해제 ( 소켓이 생성 됫을 경우, 소켓이 닫혀 있지 않을 경우)
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}

}
