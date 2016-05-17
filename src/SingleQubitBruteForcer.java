public class SingleQubitBruteForcer {

  public static void main(String[] args) {
    
    // need three independent/non-trivial samples of the desired rotation
    start1 = new Bloch(1, 0, 0);
    target1 = new Bloch(Math.cos(Math.PI/8), Math.sin(Math.PI/8), 0);
    start2 = new Bloch(0, -1, 0);
    target2 = new Bloch(Math.sin(Math.PI/8), -Math.cos(Math.PI/8), 0);
    start3 = new Bloch(0, 0, 1);
    target3 = new Bloch(0, 0, 1);
    
    start1 = new Bloch(1, 0, 0);
    target1 = start1;
    start2 = new Bloch(0, 1, 0);
    target2 = start1;
    start3 = new Bloch(0, 0, 1);
    target3 = start1;
    
//    start1 = new Bloch(1, 0, 0);
//    target1 = start1.T().H().Tt().H().T().H().T().H().Tt().H().T().H().T();
//    start2 = new Bloch(0, 1, 0);
//    target2 = start2.T().H().Tt().H().T().H().T().H().Tt().H().T().H().T();
//    start3 = new Bloch(0, 0, 1);
//    target3 = start3.T().H().Tt().H().T().H().T().H().Tt().H().T().H().T();
//    
//    start1 = new Bloch(1, 0, 0);
//    target1 = start1.Tt().H().Tt().H().T().H().Tt().H().Tt().H();
//    start2 = new Bloch(0, 1, 0);
//    target2 = start2.Tt().H().Tt().H().T().H().Tt().H().Tt().H();
//    start3 = new Bloch(0, 0, 1);
//    target3 = start3.Tt().H().Tt().H().T().H().Tt().H().Tt().H();

//  start1 = new Bloch(1, 0, 0);
//  target1 = new Bloch(0, 1, 0);
//  start2 = new Bloch(0, -1, 0);
//  target2 = new Bloch(1, 0, 0);
//  start3 = new Bloch(0, 0, 1);
//  target3 = new Bloch(0, 0, 1);
    
    double bestscore = Integer.MAX_VALUE;
    for (int i = 0; i < 40; i++) {
      Path res = findRoute(i, start1, start2, start3, "");
      if (res.score < bestscore - 0.000001) {
        bestscore = res.score;
        System.out.println(i + " " + res);
        if (bestscore < 0.000001) {
          break;
        }
      }
      else {
        System.out.println(i + " -");
      }
    }
  }

  static Bloch start1;
  static Bloch target1;
  static Bloch start2;
  static Bloch target2;
  static Bloch start3;
  static Bloch target3;

  public static Path findRoute(int depth, Bloch cur1C,
      Bloch cur2C,Bloch cur3C, String pstep) {
    Path best = new Path(cur1C.dist(target1) + cur2C.dist(target2) + cur3C.dist(target3));
    if (depth > 0) {
      if (!pstep.equals("H")) {
        Path child = findRoute(depth - 1, cur1C.H(), 
            cur2C.H(),cur3C.H(), "H");
        best.best(child);
      }
      if (!pstep.equals("T") && !pstep.equals("t") && !pstep.equals("Z")) {
        Path child = findRoute(depth - 1, cur1C.T(),
            cur2C.T(),cur3C.T(), "T");
        best.best(child);

        child = findRoute(depth - 1, cur1C.Tt(),
            cur2C.Tt(), cur3C.Tt(),"t");
        best.best(child);
        
        if(!pstep.equals("S") && !pstep.equals("s")){
          child = findRoute(depth - 1, cur1C.S(),
              cur2C.S(),cur3C.S(), "S");
          best.best(child);

          child = findRoute(depth - 1, cur1C.St(), 
              cur2C.St(),cur3C.St(), "s");
          best.best(child);
          
          child = findRoute(depth - 1,  cur1C.Z(), 
              cur2C.Z(),cur3C.Z(), "Z");
          best.best(child);
        }
      }
      if (!pstep.equals("X")) {
        Path child = findRoute(depth - 1,  cur1C.X(),
            cur2C.X(),cur3C.X(), "X");
        best.best(child);
      }
      if (!pstep.equals("Y")) {
        Path child = findRoute(depth - 1, cur1C.Y(),
            cur2C.Y(),cur3C.Y(), "Y");
        best.best(child);
      }
    }
    best.add(pstep);
    return best;
  }
}
