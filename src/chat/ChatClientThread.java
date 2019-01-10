package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class ChatClientThread extends Thread {
	BufferedReader br;

	public ChatClientThread(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String data = br.readLine();
				if (data == null) {
					System.out.println("채팅 서버 종료");
					break;
				}
				System.out.println(data);
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("closed by server: " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException error: " + e);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception error: " + e) ;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error33:" + e);
			}
		}
	}
}
