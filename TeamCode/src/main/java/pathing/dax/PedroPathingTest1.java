package pathing.dax;

public class PedroPathingTest1 {
}

/*public static class Paths {

  public PathChain ToLaunchingPosition;
  public PathChain Path2;
  public PathChain Path3;
  public PathChain Path4;
  public PathChain Path5;

  public Paths(Follower follower) {
    ToLaunchingPosition = follower
      .pathBuilder()
      .addPath(
        new BezierLine(new Pose(42.500, 8.000), new Pose(59.000, 83.000))
      )
      .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(128))
      .build();

    Path2 = follower
      .pathBuilder()
      .addPath(
        new BezierLine(new Pose(59.000, 83.000), new Pose(40.000, 84.000))
      )
      .setLinearHeadingInterpolation(Math.toRadians(128), Math.toRadians(180))
      .build();

    Path3 = follower
      .pathBuilder()
      .addPath(
        new BezierLine(new Pose(40.000, 84.000), new Pose(24.000, 84.000))
      )
      .setConstantHeadingInterpolation(Math.toRadians(180))
      .build();

    Path4 = follower
      .pathBuilder()
      .addPath(
        new BezierLine(new Pose(24.000, 84.000), new Pose(59.000, 83.000))
      )
      .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(128))
      .build();

    Path5 = follower
      .pathBuilder()
      .addPath(
        new BezierLine(new Pose(59.000, 83.000), new Pose(41.000, 60.000))
      )
      .setLinearHeadingInterpolation(Math.toRadians(128), Math.toRadians(180))
      .build();
  }
}

 */