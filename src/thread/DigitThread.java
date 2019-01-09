package thread;

public class DigitThread extends Thread {

	@Override
	public void run() {
		for (int i = 0; i <= 9; i++) {
//			System.out.print("[" + getId() + "]" + i);
			System.out.print(i);
			try {
				Thread.sleep(1000);// cpu가 내려가서 성능이 떨어진다.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
