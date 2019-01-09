package thread;

public class AlphabeticThread extends Thread {
	@Override
	public void run() {
		for (char c = 'a'; c <= 'z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);// cpu가 내려가서 성능이 떨어진다.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
