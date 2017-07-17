
public class State extends Util {

  public static void main(String[] args) {
    System.out.println(new State(3));
  }

  public final int size;
  public final C[] data;

  public State(int size) {
    this.size = size;
    data = new C[1 << size];
    data[0] = C.one;
    for (int row = 1; row < (1 << size); row++) {
      data[row] = C.zero;
    }
  }

  public State(int size, C[] data) {
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

  public ProbDist collapse() {
    double[] probs = new double[data.length];
    for (int row = 0; row < data.length; row++) {
      probs[row] = Math.pow(data[row].r, 2);
    }
    return new ProbDist(size, probs);
  }

}
