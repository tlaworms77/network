package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {
	// userNickName
	private String name;
	private Socket socket;
	private List<Writer> listWriters;

	public ChatServerThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		System.out.println("ChatThread[" + Thread.currentThread().getId() + "]");

		// 1. Remote Host Information

		// 2. 스트림 얻기
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			// 3. 요청 처리
			while (true) {
				String request = br.readLine();
				System.out.println("요청처리[" + request + "]");
				if (request == null || request.equals("quit")) {
					ChatServer.log("클라이언트로 부터 연결 끊김");
					doQuit(pw);
					break;
				}

				// 4. 프로토콜 분석 - 요청명령:파라미터1:파라미터2:~~~\r\n
				String[] tokens = request.split(":");
				String protocol = tokens[0];
				String content = tokens[1];

				System.out.println("protocol : " + protocol);

				if ("join".equals(protocol)) {
					System.out.println("serverThread : join");
					doJoin(content, pw);
				} else if ("message".equals(protocol)) {
					System.out.println("serverThread : message");
					doMessage(content);
				} else if ("quit".equals(protocol)) {
					doQuit(pw);
				} else {
					ChatServer.log("에러:알수 없는 요청(" + protocol + ")");
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("1 error:" + e);
		} catch (IOException e) {
			System.out.println("2 error:" + e);
			doQuit(pw);
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("3 error:" + e);
			}
		}

	}

	private void doQuit(Writer pw) {
		removeWriter(pw);
		String data = "[" + name + "]" + "님이 퇴장 하였습니다.";
		broadcast(data);
	}

	private void removeWriter(Writer pw) {
		System.out.println("퇴장");
		synchronized (listWriters) {
			if(listWriters.contains(pw)) {
				listWriters.remove(pw);
			}
		}
	}

	private void doMessage(String message) {
		// 데이터 쓰기(전송)
		System.out.println("doMessage(name) :" + name);
		broadcast("[" + name + "]" + message);
		ChatServer.log("doMessage : Success");

	}

	private void doJoin(String nickName, PrintWriter pw) {
		// TODO Auto-generated method stub
		this.name = nickName;
		String joinMSG = "[" + nickName + "]" + "님이 참여하였습니다.";
		System.out.println("doJoin : " + joinMSG);
		broadcast(joinMSG);

		// 닉네임을 pw-pool 에 저장 입장한 유저에게도 전달되면안되니 후에 저장
		addWriter(pw);

		// ack ( 방 참여가 성공했다는 것을 클라이언트[방에 참여하려는 유저]에게 알림 )
		pw.println("join:ok");
		// pw.flush();

	}

	private void broadcast(String data) {
		synchronized (listWriters) {
			
			for (Writer writer : listWriters) {
				System.out.println("broadcase" + writer);
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				//printWriter.flush();
			}
		}
	}

	// arrayList 객체 사용시 동기화 처리
	private void addWriter(Writer pw) {
		synchronized (listWriters) {
			System.out.println("addWriter");
			listWriters.add(pw);
		}

	}

	private String ThreadLog() {
		return Thread.currentThread().getId() + "";
	}

}
