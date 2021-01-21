public class ExampleClass {
	public void run() {
		sayHello();
		doSomething();
		sayGoodbye();
	}
	
	private void sayHello() {
		System.out.println("Hello World!");
	}
	
	protected void doSomething() {
		//Performing work here.
	}
	
	private void sayGoodbye() {
		System.out.println("Goodybe.");
	}
	
	public static final void main(String[] args) {
		ExampleClass example = new ExampleClass();
		example.run();
	}
}
