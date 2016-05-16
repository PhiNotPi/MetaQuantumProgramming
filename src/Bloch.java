public class Bloch {

  double x;
  double y;
  double z;

  public Bloch() {
    x = 0;
    y = 0;
    z = 1;
  }

  public Bloch(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  // constructs a qubit with the given probability of being "true"
  public Bloch(double p){
    this.z = p*-2+1;
  }

  public String toString() {
    return String.format("(%.3f,%.3f,%.3f)", x, y, z);
  }

  public double dist(Bloch other) {
    return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2)
        + Math.pow(z - other.z, 2));
  }
  
  // difference in probability of being true
  public double probdiff(Bloch other){
    return Math.abs(z - other.z)/2;
  }

  public Bloch rotX(double theta) {
    double nx = x;
    double ny = Math.cos(theta) * y - Math.sin(theta) * z;
    double nz = Math.sin(theta) * y + Math.cos(theta) * z;
    return new Bloch(nx, ny, nz);
  }

  public Bloch rotY(double theta) {
    double nx = Math.cos(theta) * x + Math.sin(theta) * z;
    double ny = y;
    double nz = -Math.sin(theta) * x + Math.cos(theta) * z;
    return new Bloch(nx, ny, nz);
  }

  public Bloch rotZ(double theta) {
    double nx = Math.cos(theta) * x - Math.sin(theta) * y;
    double ny = Math.sin(theta) * x + Math.cos(theta) * y;
    double nz = z;
    return new Bloch(nx, ny, nz);
  }

  public Bloch H() {
    return rotY(Math.PI / 2).rotX(Math.PI);
  }

  public Bloch X() {
    return rotX(Math.PI);
  }

  public Bloch Y() {
    return rotY(Math.PI);
  }

  public Bloch Z() {
    return rotZ(Math.PI);
  }

  public Bloch S() {
    return rotZ(Math.PI / 2);
  }

  // S-dagger
  public Bloch St() {
    return rotZ(-Math.PI / 2);
  }

  public Bloch T() {
    return rotZ(Math.PI / 4);
  }

  // T-dagger
  public Bloch Tt() {
    return rotZ(-Math.PI / 4);
  }
}
