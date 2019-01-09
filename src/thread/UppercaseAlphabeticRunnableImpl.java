package thread;

public class UppercaseAlphabeticRunnableImpl extends UppercaseAlphabetic implements Runnable {

	// Runnable -> Thread 클래스가 아니라서 스레드클래스를 사용하는게 아니다.
	@Override
	public void run() {
		print();
	}

}
