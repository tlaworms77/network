package thread;

public class MutithreadEx01 {

	public static void main(String[] args) {
		// 싱글 스레드 - main
		/*for (char c = 'a'; c <= 'z'; c++) {
			System.out.print(c);
		}
		*/
		
		Thread digitThread = new DigitThread();
		
		digitThread.start();
		
		for (char c = 'a'; c <= 'z'; c++) {
//			System.out.print("[" + Thread.currentThread().getId() + "]" + c);
			System.out.print(c);
			try {
				Thread.sleep(1000);// cpu가 내려가서 성능이 떨어진다.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 스레드가 뺏길 수 잇다.
		System.out.println("main 끝");
	}

}
