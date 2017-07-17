public class C extends Util {

	public static final C i = new C(0, 1);
	public static final C ni = new C(0, -1);
	public static final C one = new C(1, 0);
	public static final C neg = new C(-1, 0);
	public static final C zero = new C(0, 0);

	public final double x;
	public final double y;
	public final double r;
	public final double o;

	public C() {
		super();
		this.x = 0;
		this.y = 0;
		this.r = 0;
		this.o = 0;
	}

	public C(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.r = Math.hypot(x, y);
		this.o = Math.atan2(y, x);
	}

	public C(double r, double o, boolean b) {
		super();
		this.r = r;
		this.o = o;
		this.x = r * Math.cos(o);
		this.y = r * Math.sin(o);
	}

	public static boolean eq(C a, C b) {
		if (Math.abs(a.x - b.x) < epsilon && Math.abs(a.y - b.y) < epsilon) {
			return true;
		}
		return false;
	}

	public static C add(C a, C b) {
		return new C(a.x + b.x, a.y + b.y);
	}

	public static C add(C... arr) {
		if (arr.length == 0) {
			return C.zero;
		} else {
			C total = arr[0];
			for (int i = 1; i < arr.length; i++) {
				total = C.add(total, arr[i]);
			}
			return total;
		}
	}

	public static C sub(C a, C b) {
		return new C(a.x - b.x, a.y - b.y);
	}

	public static C mult(C a, C b) {
		return new C(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
	}

	public static C mult(C... arr) {
		if (arr.length == 0) {
			return C.one;
		} else {
			C total = arr[0];
			for (int i = 1; i < arr.length; i++) {
				total = C.mult(total, arr[i]);
			}
			return total;
		}
	}

	public static C pow(C a, int b) {
		if (b <= 0) {
			return C.one;
		} else {
			C res = a;
			for (int i = 1; i < b; i++) {
				res = C.mult(res, a);
			}
			return res;
		}
	}

	public static C cong(C a) {
		return new C(a.x, -1 * a.y);
	}

	public static C re(C a) {
		return new C(a.x, 0);
	}

	public static C div(C a, C b) {
		C cong = cong(b);
		C num = C.mult(a, cong);
		C den = C.mult(b, cong); // real
		return new C(num.x / den.x, num.y / den.x);
	}

	public static C log(C a) {
		return new C(Math.log(a.r), a.o);
	}

	public static C exp(C a) {
		return new C(Math.exp(a.x) * Math.cos(a.y), Math.exp(a.x) * Math.sin(a.y));
	}

	public static C acot(C a) {
		return C.mult(new C(0, -0.5), C.log(C.div(C.add(a, i), C.sub(a, i))));
	}

	public static C acos(C z) {
		C sqdif = C.sub(one, C.mult(z, z));
		C exponent = new C(0, sqdif.o / 2);
		C factor = new C(0, Math.sqrt(sqdif.r));
		return C.mult(ni, C.log(C.add(z, C.mult(factor, C.exp(exponent)))));
	}

	public String toString() {
		String res = "<";
		if (Math.abs(x) >= epsilon) {
			res += String.format("%5.2f", x);
		} else {
			res += String.format("%5s", "");
		}
		res += ",";
		if (Math.abs(y) >= epsilon) {
			res += String.format("%5.2f", y);
		} else {
			res += String.format("%5s", "");
		}
		res += ">";
		return res;
	}

	public static C v(C w, C x, C y) {
		return C.div(C.add(w, y), C.add(x, x));
	}

	public static C dd(C w, C x, C y) {
		return C.sub(C.add(w, y), C.add(x, x));
	}

	public static C[] makeArr(int a) {
		C[] res = new C[a];
		for (int i = 0; i < a; i++) {
			res[i] = C.zero;
		}
		return res;
	}

	public static C[][] makeArr(int a, int b) {
		C[][] res = new C[a][b];
		for (int i = 0; i < a; i++) {
			res[i] = makeArr(b);
		}
		return res;
	}

	public static C[][][] makeArr(int a, int b, int c) {
		C[][][] res = new C[a][b][c];
		for (int i = 0; i < a; i++) {
			res[i] = makeArr(b, c);
		}
		return res;
	}

}
