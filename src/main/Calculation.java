package main;

import java.util.ArrayList;

public abstract class Calculation {

	public static final double dt = Math.pow(10, -3);
	protected double t = 0;
	public Vector M;
	public Vector dM;
	protected ArrayList<Vector> array = new ArrayList<>();
	protected PeriodCounter pc = new PeriodCounter();

	public static final double sigma = 0 * Math.pow(10, 18);
	public static final double nu = 0 * Math.pow(10, 18);//TODO find AHE koeff
	private static final double alpha0 = 1 * Math.pow(10, -2);

	public static final boolean IS_CONDUCTICE_LLG = true;


	protected static final double R = Math.pow(10, -5);
	protected static final double V = (4/3) * Math.PI * Math.pow(R, 3);
	protected static final double mu1 = 1;
	private static final double mu2 = 1;
	private static final double c = 3 * Math.pow(10, 10);
	private static final double gamma = 1.76 * Math.pow(10, 7);
	protected static final double modM = Math.pow(10, 4) / (2 * Math.PI);
	protected static final double Ha = 5 * Math.pow(10, 4);

	protected static final double kappa = 3 * mu2 / (mu1 + 2 * mu2);

	protected static final double ksi = getKsi();

	protected static final double taoSigma = getTaoSigma();
	protected static final double taoAnomal = getTaoAnomal();
	protected static final double ALPHA = alpha0 + getAlphaSigma();




	protected static final double constant = -1 / (Math.pow(1 - ksi, 2) + Math.pow(ALPHA, 2));


	protected double omega;
	public double getOmega() {
		return omega;
	}

	public abstract Vector getHeff(Vector M, double t);
	public abstract Vector getDerHeff(Vector M, double t);

	public void run() {
		update();
		wait(300d);
		while (true) {
			iteration();
			pc.update(this);
			if (pc.isOver())
				break;
		}
		for (Vector vector : pc.list)
			array.add(vector);
	}

	public void run(double waitingTime, double workingTime) {
		update();
		wait(waitingTime);
		while (true) {
			iteration();
			pc.update(this);
			array.add(new Vector(M));
			if (t >  waitingTime + workingTime)
				break;
		}
	}

	public double getEnergy() {
		return pc.getEnergy();
	}

	public Vector getM_aver() {
		return pc.getM_aver();
	}
	
	protected void wait(double waitingTime) {
		while (t < waitingTime) 
			iteration();
	}
	
	protected abstract void iteration();

	public ArrayList<Vector> getArray() {
		return array;
	}

	private static double getTaoSigma() {
		double answer = 4 * Math.PI * sigma * kappa * kappa * R * R / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}

	private static double getTaoAnomal() {
		double answer = 4 * Math.PI * nu * kappa * kappa * R * R / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}
	
	private static  double getAlphaSigma() {
		return 8 * Math.PI / 3 * modM / Ha * getTaoSigma();
	}

	private static  double getKsi() {
		return 8 * Math.PI / 3 * modM / Ha * getTaoAnomal();
	}

	protected void setBeginningLocation(Vector vector) {
		M = vector;
	}

	protected void update() {
		pc.externalReset(this);
		array = new ArrayList<>();
		t = 0;
	}
}
