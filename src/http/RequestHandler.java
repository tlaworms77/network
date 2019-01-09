package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
						InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
						consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			// get IOStream
			OutputStream outputStream = socket.getOutputStream();
			// 보조스트림(보조스트림(주스트림,"엔코딩 타입"))
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			String request = null; // 헤더의 첫줄만 담을 변수. GET .....~~~
			
			// 브라우저가 연결을 끊은 경우.
			while(true) {
				String line = br.readLine();
				if(line == null) { 
					break;
				}
				
				// Header 만 읽은 경우.
				if("".equals(line)) {
					break;
				}
				
				// 헤더의 첫번째 라인만 처리
				if(request == null) {
					request = line;
				}
				
			}
			
			consoleLog(request);
			
			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) {
				responseStaticResource(outputStream, tokens[1], tokens[2]);
			} else {	// POST, DELETE
				//consoleLog("Bad request:" + request);
				response400Error(outputStream, tokens[2]);
				/*
				 * HTTP/1.0 400 Bad Request
				 * Content-Type:text/html; charset=utf-8\r\n
				 * \r\n
				 */
			}
					
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
/*			outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			outputStream.write( "\r\n".getBytes() );
			outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
*/
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}
	
	private void responseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {
		
		if("/".equals(url)) {
			url = "/index.html";
		}
		
		File file = new File("./webapp" + url);	// ./:현제프로젝트의 network 폴더
		
		//과제
		if(file.exists() == false) {
			response404Error(outputStream, protocol);
			/*
			 *  HTTP/1.0 404 File Not Found\r\n
			 *  "Content-Type: text/html; charset=utf-8\r\n"
			 */
			return;
		}
		
		// java 1.7 -> nio
		byte[] body = Files.readAllBytes(file.toPath()); // 파일의 내용을 byte[]단위로 가져온다. java.nio.~~ (1.7버전)
		String contentType = Files.probeContentType(file.toPath());
		// 응답
		// === 헤더부분 ===
		outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
		outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		// ==== 바디 부분 ====
		outputStream.write(body);
		
	}

	private void response400Error(OutputStream outputStream, String string) throws IOException {
		// TODO Auto-generated method stub
		File file = new File("./webapp/error/400.html");	// ./:현제프로젝트의 network 폴더
		byte[] body = Files.readAllBytes(file.toPath());
		
		outputStream.write((string + " 400 Bad Request\r\n").getBytes());
		outputStream.write( ("Content-Type:text/html\r\n").getBytes() );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write(body);
	}

	

	private void response404Error(OutputStream outputStream, String protocol) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		File file = new File("./webapp/error/404.html");	// ./:현제프로젝트의 network 폴더
		byte[] body = Files.readAllBytes(file.toPath());
		
		outputStream.write((protocol + " 404 File Not Found\r\n").getBytes());
		outputStream.write( ("Content-Type:text/html\r\n").getBytes() );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write(body);
	}

	public void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}
