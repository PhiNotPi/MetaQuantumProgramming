public class SingleQubitBruteForcer {

  public static void main(String[] args) {
    double bestscore = Integer.MAX_VALUE;
    for(int i = 0; i < 40; i++){
      Path res = findRoute(
          i, // max depth
          // starting position
          new Bloch(0, 0, 1),
          // ending position
          new Bloch(Math.cos(Math.PI/8), Math.sin(Math.PI/8), 0),
          "");
      // Path res = findRoute(i, new Bloch(8./11));
      if(res.score < bestscore - 0.000001){
        bestscore = res.score;
        System.out.println(i + " " + res);
        if(bestscore < 0.000001){
          break;
        }
      }
    }
  }

  public static Path findRoute(int depth, Bloch cur, Bloch target, String pstep) {
    Path best = new Path(cur.dist(target));
    // Path best = new Path(cur.probdiff(target));
    if (depth > 0) {
      if (!pstep.equals("H")) {
        Path child = findRoute(depth - 1, cur.H(), target, "H");
        best.best(child);
      }
      if (pstep.equals("H") || pstep.equals("S")) {
        Path child = findRoute(depth - 1, cur.T(), target, "T");
        best.best(child);
      }
      if (pstep.equals("H") || pstep.equals("t")) {
        Path child = findRoute(depth - 1, cur.Tt(), target, "t");
        best.best(child);
      }
      // for blazing-fast (but slightly sub-optimal results)
      // comment out all the options below here
//      if (pstep.equals("H")) {
//        Path child = findRoute(depth - 1, cur.S(), target, "S");
//        best.best(child);
//      }
//      if (pstep.equals("H")) {
//        Path child = findRoute(depth - 1, cur.St(), target, "s");
//        best.best(child);
//      }
//      if (pstep.equals("H")) {
//        Path child = findRoute(depth - 1, cur.X(), target, "X");
//        best.best(child);
//      }
//      if (!pstep.equals("Y")) {
//        Path child = findRoute(depth - 1, cur.Y(), target, "Y");
//        best.best(child);
//      }
//      if (!pstep.equals("Z")) {
//        Path child = findRoute(depth - 1, cur.Z(), target, "Z");
//        best.best(child);
//      }
    }
    best.add(pstep);
    return best;
  }

}
