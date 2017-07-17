
public class ProbDist extends Util {

	public static void main(String[] args) {
		System.out.println(new ProbDist(3));
	}

	public final int size;
	public final double[] data;

	public ProbDist(int size) {
		this.size = size;
		data = new double[1 << size];
		data[0] = 1;
	}

	public ProbDist(int size, double[] data) {
		this.size = size;
		this.data = data;
	}

	public String toString() {
		String res = "";
		for (int row = 0; row < data.length; row++) {
			res += bitstring(size, row) + "   " + data[row] + "\n";
		}
		return res;
	}

}
