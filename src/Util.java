import java.util.Arrays;

public abstract class Util {

  public static void main(String[] args) {
    System.out.println(bitstring(3, 4));
    // System.out.println(convert(2, new int[] { 0, 1 }));
    System.out.println(remBits(4, 1, 0));
  }

  public static String bitstring(int size, int val) {
    // little-endian
    String res = "";
    for (int i = 0; i < size; i++) {
      if ((val & (1 << i)) == 0) {
        res += "0";
      } else {
        res += "1";
      }
    }
    return res;
  }

  public static double epsilon = 0.0001;

  public static int convert(int full, int[] bits) {
    int res = 0;
    for (int i = 0; i < bits.length; i++) {
      if ((full & (1 << bits[i])) != 0) {
        res += 1 << i;
      }
    }
    return res;
  }

  public static int getMask(int... bits) {
    int mask = 0;
    for (int bit : bits) {
      mask += (1 << bit);
    }
    return mask;
  }

  public static int remBit(int val, int bit) {
    return ((val >> (bit + 1)) << bit) + (((1 << bit) - 1) & val);
  }

  public static int remBits(int val, int... bits) {
    bits = bits.clone();
    Arrays.sort(bits);
    for (int i = bits.length - 1; i >= 0; i--) {
      val = remBit(val, bits[i]);
    }
    return val;
  }

}
