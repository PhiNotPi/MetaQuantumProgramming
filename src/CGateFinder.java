public class CGateFinder {

  public static void main(String[] args) {

//    globalbest = new Path(Integer.MAX_VALUE);
    
    // need three independent/non-trivial samples of the desired rotation
    start1 = new Bloch(1, 0, 0);
    target1 = start1.T().Z();
    start2 = new Bloch(0, 1, 0);
    target2 = start2.T().Z();
    start3 = new Bloch(0, 0, 1);
    target3 = start3.T().Z();

    // start1 = new Bloch(1, 0, 0);
    // target1 = new Bloch(0, 1, 0);
    // start2 = new Bloch(0, -1, 0);
    // target2 = new Bloch(1, 0, 0);

    double bestscore = Integer.MAX_VALUE;
    for (int i = 0; i < 40; i++) {
      Path res = findRoute(i, start1, start1, start2, start2, start3, start3,
          "", 2);
//      findRouteG(i, start1, start1, start2, start2, start3, start3, "", 3, "");
//      Path res = globalbest;
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

  public static Path findRoute(int depth, Bloch cur1, Bloch cur1C, Bloch cur2,
      Bloch cur2C, Bloch cur3, Bloch cur3C, String pstep, int maxC) {
    Path best = new Path(cur1.dist(start1) + cur1C.dist(target1)
        + cur2.dist(start2) + cur2C.dist(target2) + cur3.dist(start3)
        + cur3C.dist(target3));
    if (depth > 0) {
      if (!pstep.equals("C") && maxC > 0) {
        Path child = findRoute(depth - 1, cur1, cur1C.X(), cur2, cur2C.X(),
            cur3, cur3C.X(), "C", maxC-1);
        best.best(child);
      }
      if (!pstep.equals("H")) {
        Path child = findRoute(depth - 1, cur1.H(), cur1C.H(), cur2.H(),
            cur2C.H(), cur3.H(), cur3C.H(), "H", maxC);
        best.best(child);
      }
      if (!pstep.equals("T") && !pstep.equals("t") && !pstep.equals("Z")) {
        Path child = findRoute(depth - 1, cur1.T(), cur1C.T(), cur2.T(),
            cur2C.T(), cur3.T(), cur3C.T(), "T", maxC);
        best.best(child);

        child = findRoute(depth - 1, cur1.Tt(), cur1C.Tt(), cur2.Tt(),
            cur2C.Tt(), cur3.Tt(), cur3C.Tt(), "t", maxC);
        best.best(child);

//        if (!pstep.equals("S") && !pstep.equals("s")) {
//          child = findRoute(depth - 1, cur1.S(), cur1C.S(), cur2.S(),
//              cur2C.S(), cur3.S(), cur3C.S(), "S", maxC);
//          best.best(child);
//
//          child = findRoute(depth - 1, cur1.St(), cur1C.St(), cur2.St(),
//              cur2C.St(), cur3.St(), cur3C.St(), "s", maxC);
//          best.best(child);
//
//          child = findRoute(depth - 1, cur1.Z(), cur1C.Z(), cur2.Z(),
//              cur2C.Z(), cur3.Z(), cur3C.Z(), "Z", maxC);
//          best.best(child);
//        }
      }
//      if (!pstep.equals("X")) {
//        Path child = findRoute(depth - 1, cur1.X(), cur1C.X(), cur2.X(),
//            cur2C.X(), cur3.X(), cur3C.X(), "X", maxC);
//        best.best(child);
//      }
//      if (!pstep.equals("Y")) {
//        Path child = findRoute(depth - 1, cur1.Y(), cur1C.Y(), cur2.Y(),
//            cur2C.Y(), cur3.Y(), cur3C.Y(), "Y", maxC);
//        best.best(child);
//      }
    }
    best.add(pstep);
    return best;
  }
  
  
  static Path globalbest;
  public static void findRouteG(int depth, Bloch cur1, Bloch cur1C, Bloch cur2,
      Bloch cur2C, Bloch cur3, Bloch cur3C, String pstep, int maxC, String path) {
    path += pstep;
    Path curpath = new Path(cur1.dist(start1) + cur1C.dist(target1)
        + cur2.dist(start2) + cur2C.dist(target2) + cur3.dist(start3)
        + cur3C.dist(target3));
    curpath.desc = path;
    globalbest.best(curpath);
    if (depth > 0) {
      if (!pstep.equals("C") && maxC > 0) {
        findRouteG(depth - 1, cur1, cur1C.X(), cur2, cur2C.X(),
            cur3, cur3C.X(), "C", maxC-1, path);
      }
      if (!pstep.equals("H")) {
        findRouteG(depth - 1, cur1.H(), cur1C.H(), cur2.H(),
            cur2C.H(), cur3.H(), cur3C.H(), "H", maxC, path);
      }
      if (!pstep.equals("T") && !pstep.equals("t") && !pstep.equals("Z")) {
        findRouteG(depth - 1, cur1.T(), cur1C.T(), cur2.T(),
            cur2C.T(), cur3.T(), cur3C.T(), "T", maxC, path);

        findRouteG(depth - 1, cur1.Tt(), cur1C.Tt(), cur2.Tt(),
            cur2C.Tt(), cur3.Tt(), cur3C.Tt(), "t", maxC, path);

//        if (!pstep.equals("S") && !pstep.equals("s")) {
//          findRouteG(depth - 1, cur1.S(), cur1C.S(), cur2.S(),
//              cur2C.S(), cur3.S(), cur3C.S(), "S", maxC, path);
//
//          findRouteG(depth - 1, cur1.St(), cur1C.St(), cur2.St(),
//              cur2C.St(), cur3.St(), cur3C.St(), "s", maxC, path);
//
//          findRouteG(depth - 1, cur1.Z(), cur1C.Z(), cur2.Z(),
//              cur2C.Z(), cur3.Z(), cur3C.Z(), "Z", maxC, path);
//        }
      }
//      if (!pstep.equals("X")) {
//        findRouteG(depth - 1, cur1.X(), cur1C.X(), cur2.X(),
//            cur2C.X(), cur3.X(), cur3C.X(), "X", maxC, path);
//      }
//      if (!pstep.equals("Y")) {
//        findRouteG(depth - 1, cur1.Y(), cur1C.Y(), cur2.Y(),
//            cur2C.Y(), cur3.Y(), cur3C.Y(), "Y", maxC, path);
//      }
    }
  }

  // public static Path findRoutePre(int depth, Bloch cur, String pstep) {
  // Path best = findRouteMid(depth,cur,"C");
  // if (depth > 0) {
  // if (!pstep.equals("H")) {
  // Path child = findRoutePre(depth - 1, cur.H(), "H");
  // best.best(child);
  // }
  // if (pstep.equals("H") || pstep.equals("S") || pstep.equals("")) {
  // Path child = findRoutePre(depth - 1, cur.T(), "T");
  // best.best(child);
  // }
  // if (pstep.equals("H") || pstep.equals("s") || pstep.equals("")) {
  // Path child = findRoutePre(depth - 1, cur.Tt(), "t");
  // best.best(child);
  // }
  // }
  // best.add(pstep);
  // return best;
  // }
  //
  // public static Path findRouteMid(int depth, Bloch cur, String pstep){
  // Path res = findRoutePost(depth,cur,cur.X(),"");
  // res.add("C");
  // return res;
  // }
  //
  // public static Path findRoutePost(int depth, Bloch cur, Bloch curC, String
  // pstep) {
  // Path best = new Path(cur.dist(start1) + curC.dist(target1));
  // if (depth > 0) {
  // if (!pstep.equals("H")) {
  // Path child = findRoutePost(depth - 1, cur.H(), curC.H(), "H");
  // best.best(child);
  // }
  // if (pstep.equals("H") || pstep.equals("S") || pstep.equals("")) {
  // Path child = findRoutePost(depth - 1, cur.T(), curC.T(), "T");
  // best.best(child);
  // }
  // if (pstep.equals("H") || pstep.equals("t") || pstep.equals("")) {
  // Path child = findRoutePost(depth - 1, cur.Tt(), curC.Tt(), "t");
  // best.best(child);
  // }
  // }
  // best.add(pstep);
  // return best;
  // }

}
