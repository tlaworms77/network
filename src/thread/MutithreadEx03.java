package thread;

public class MutithreadEx03 {

	public static void main(String[] args) {
		
		Thread thread1 = new AlphabeticThread();
		Thread thread2 = new DigitThread();
		//Runnable runnable = new UppercaseAlphabeticRunnableImpl();
		Thread thread3 = new Thread(new UppercaseAlphabeticRunnableImpl());
		
		
		thread1.start();
		thread2.start();
		thread3.start();
//		new Thread(runnable).start();
//		new Thread(new UppercaseAlphabeticRunnableImpl()).start();
		
	}

}
