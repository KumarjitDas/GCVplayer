package adamas_university.soet.ece.year2017.project.final_year;

public class PythonExecutor extends Thread {
	private Process process;
	
	public void run() {
		try {
			this.process = Runtime.getRuntime().exec("python Resources/Main.py");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void kill() {
		try {
			this.process.destroy();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
