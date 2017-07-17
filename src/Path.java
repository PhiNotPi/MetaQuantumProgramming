
public class Path {

	String desc;
	double score;

	public Path(double s) {
		desc = "";
		score = s;
	}

	public void add(String s) {
		desc = s + desc;
	}

	public void best(Path other) {
		if (other.score < score || (other.score == score && other.desc.length() < desc.length())) {
			this.score = other.score;
			this.desc = other.desc;
		}
	}

	public String toString() {
		return "[" + desc + ":" + score + "]";
	}

}