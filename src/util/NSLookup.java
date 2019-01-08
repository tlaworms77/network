package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {

	public static void main(String[] args) {
		String line = "www.naver.com";
		try {
			InetAddress[] inetAddresses = 
					InetAddress.getAllByName(line);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
