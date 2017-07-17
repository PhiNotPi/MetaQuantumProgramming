
public class Gate extends Util {
	public static void main(String[] args) {
		// System.out.println(new Gate(1).expand(2, 0));
		// System.out.println(H.expand(3, 0));
		// System.out.println(H.expand(3, 1));
		// System.out.println(H.expand(3, 2));
		// System.out.println(H.expand(3, 1).mult(H.expand(3, 2)));
		// System.out.println(H.expand(2, 0).mult(H.expand(2, 1)).mult(new
		// State(2)).collapse());
		// System.out.println(H);
		// System.out.println(H.expand(3, 0));
		// System.out.println(H.expand(3, 0).isSeparable(1, 2));

		Gate g0 = H.expand(3, 0).mult(H.expand(3, 1)).mult(Y.expand(3, 2));
		System.out.println(g0);
		Gate g1 = g0.isSeparable(0);
		System.out.println(g1);
		Gate g2 = g1.expand(3, 0).inv();
		System.out.println(g2);
		Gate g3 = g0.mult(g2);
		System.out.println(g3);
		System.out.println(g3.isSeparable(0));

		System.out.println(H.controlled(2, 1));
	}

	public static final Gate X = new Gate(1, new C[][] { { C.zero, C.one }, { C.one, C.zero } });
	public static final Gate Y = new Gate(1, new C[][] { { C.zero, C.ni }, { C.i, C.zero } });
	public static final Gate Z = new Gate(1, new C[][] { { C.one, C.zero }, { C.zero, C.neg } });
	public static final Gate H = new Gate(1, new C[][] { { new C(1 / Math.sqrt(2), 0), new C(1 / Math.sqrt(2), 0) },
			{ new C(1 / Math.sqrt(2), 0), new C(-1 / Math.sqrt(2), 0) } });
	public static final Gate CNot = new Gate(2, new C[][] { { C.one, C.zero, C.zero, C.zero },
			{ C.zero, C.one, C.zero, C.zero }, { C.zero, C.zero, C.zero, C.one }, { C.zero, C.zero, C.one, C.zero } });

	public final int size;
	public final C[][] data;

	public Gate(int size) {
		this.size = size;
		this.data = new C[1 << size][1 << size];
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				if (row == col) {
					data[row][col] = C.one;
				} else {
					data[row][col] = C.zero;
				}
			}
		}
	}

	public Gate(int size, C[][] data) {
		this.size = size;
		this.data = data;
	}

	public String toString() {
		String res = new String(new char[size + 4]).replace("\0", " ");
		int colwidth = C.zero.toString().length() + 2;
		for (int col = 0; col < (1 << size); col++) {
			res += String.format("%-" + colwidth + "s", bitstring(size, col));
		}
		res += "\n";
		for (int row = 0; row < (1 << size); row++) {
			res += bitstring(size, row) + "  ";
			for (int col = 0; col < (1 << size); col++) {
				res += "  " + data[row][col];
			}
			res += "\n";
		}
		return res;
	}

	public boolean equals(Gate g) {
		if (size != g.size) {
			return false;
		}
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				if (!C.eq(data[row][col], g.data[row][col])) {
					return false;
				}
			}
		}
		return true;
	}

	public Gate expand(int fullsize, int... bits) {
		int mask = getMask(bits);
		C[][] fulldata = new C[1 << fullsize][1 << fullsize];
		for (int fullrow = 0; fullrow < (1 << fullsize); fullrow++) {
			for (int fullcol = 0; fullcol < (1 << fullsize); fullcol++) {
				if ((fullrow & ~mask) == (fullcol & ~mask)) {
					fulldata[fullrow][fullcol] = data[convert(fullrow, bits)][convert(fullcol, bits)];
				} else {
					fulldata[fullrow][fullcol] = C.zero;
				}
			}
		}
		return new Gate(fullsize, fulldata);
	}

	public Gate controlled(int fullsize, int... bits) {
		int mask = getMask(bits);
		C[][] fulldata = new C[1 << fullsize][1 << fullsize];
		for (int fullrow = 0; fullrow < (1 << fullsize); fullrow++) {
			for (int fullcol = 0; fullcol < (1 << fullsize); fullcol++) {
				if ((fullrow & ~mask) == (fullcol & ~mask)) {
					if ((fullrow & ~mask) == ((1 << (fullsize - 1)) & ~mask)) {
						fulldata[fullrow][fullcol] = data[convert(fullrow, bits)][convert(fullcol, bits)];
					} else if (fullrow == fullcol) {
						fulldata[fullrow][fullcol] = C.one;
					} else {
						fulldata[fullrow][fullcol] = C.zero;
					}
				} else {
					fulldata[fullrow][fullcol] = C.zero;
				}
			}
		}
		return new Gate(fullsize, fulldata);
	}

	public Gate mult(Gate b) {
		C[][] fulldata = new C[1 << size][1 << size];
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				fulldata[row][col] = C.zero;
				for (int i = 0; i < (1 << size); i++) {
					fulldata[row][col] = C.add(fulldata[row][col], C.mult(data[row][i], b.data[i][col]));
				}
			}
		}
		return new Gate(size, fulldata);
	}

	public State mult(State b) {
		C[] fulldata = new C[1 << size];
		for (int row = 0; row < (1 << size); row++) {
			fulldata[row] = C.zero;
			for (int i = 0; i < (1 << size); i++) {
				fulldata[row] = C.add(fulldata[row], C.mult(data[row][i], b.data[i]));
			}
		}
		return new State(size, fulldata);
	}

	public Gate isSeparable(int... bits) {
		C[][][] gs = C.makeArr(1 << (size - bits.length), 1 << bits.length, 1 << bits.length);
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				int layer = remBits(col, bits);
				int subrow = convert(row, bits);
				int subcol = convert(col, bits);
				gs[layer][subrow][subcol] = C.add(gs[layer][subrow][subcol], data[row][col]);
			}
		}
		Gate[] gates = new Gate[1 << (size - bits.length)];
		C[] norms = new C[1 << (size - bits.length)];
		for (int layer = 0; layer < gates.length; layer++) {
			gates[layer] = new Gate(bits.length, gs[layer]);
			norms[layer] = gates[layer].norm();
		}
		int firstnonzero = -1;
		for (int layer = 0; layer < gates.length; layer++) {
			if (!C.eq(norms[layer], C.zero)) {
				firstnonzero = layer;
				break;
			}
		}
		// for (Gate g : gates) {
		// System.out.println("subgate:");
		// System.out.println(g);
		// }
		if (firstnonzero == -1) {
			// not sure if should return identity or should return null
			// situation might not be possible: might imply irreversible circuit
			return new Gate(bits.length);
		}
		for (int layer = firstnonzero + 1; layer < gates.length; layer++) {
			if (!C.eq(norms[layer], C.zero)) {
				C cosang = C.div(C.re(gates[firstnonzero].dot(gates[layer])),
						C.mult(norms[firstnonzero], norms[layer]));
				if (!C.eq(C.pow(cosang, 2), C.one)) {
					return null;
				}
			}
		}
		return gates[firstnonzero].normalize();
	}

	public Gate cong() {
		C[][] fulldata = new C[1 << size][1 << size];
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				fulldata[row][col] = C.cong(data[row][col]);
			}
		}
		return new Gate(size, fulldata);
	}

	public Gate inv() {
		// ONLY for unitary matrices, like gates
		C[][] fulldata = new C[1 << size][1 << size];
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				fulldata[row][col] = C.cong(data[col][row]);
			}
		}
		return new Gate(size, fulldata);
	}

	public C dot(Gate g) {
		C res = C.zero;
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				res = C.add(res, C.mult(data[row][col], C.cong(g.data[row][col])));
			}
		}
		return res;
	}

	public C norm() {
		double res = 0;
		for (int row = 0; row < (1 << size); row++) {
			for (int col = 0; col < (1 << size); col++) {
				res += Math.pow(data[row][col].r, 2);
			}
		}
		return new C(Math.sqrt(res), 0);
	}

	public Gate normalize() {
		C[][] fulldata = new C[1 << size][1 << size];
		for (int col = 0; col < (1 << size); col++) {
			double colsum = 0;
			for (int row = 0; row < (1 << size); row++) {
				colsum += Math.pow(data[row][col].r, 2);
			}
			if (Math.abs(colsum) < epsilon) {
				return null;
			}
			C mult = new C(1 / Math.sqrt(colsum), 0);
			for (int row = 0; row < (1 << size); row++) {
				fulldata[row][col] = C.mult(data[row][col], mult);
			}
		}
		return new Gate(size, fulldata);
	}
}
