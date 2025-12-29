package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.util.Device;

public class RobotConfig {
    private static boolean initialized;
    private static final String ROBOT_SERIAL_COACH_ANTHONY = "5838f408422f581c";
    private static final String ROBOT_SERIAL_COACH_ROB     = "803b7c0c22973496";
    private static final String ROBOT_SERIAL_MILLENNIUM    = "a542e45d0aef5058";
    private static final String ROBOT_SERIAL_OSCAR         = "ab62dbfe2c4bd160";

    private static String robotName = "Unknown";

    private static double carouselIntakePosition1 =    73.0;
    private static double carouselIntakePosition2 =   191.9;
    private static double carouselIntakePosition3 =   312.7;
    private static double carouselLaunchPosition1 =   250.1;
    private static double carouselLaunchPosition2 =     9.8;
    private static double carouselLaunchPosition3 =   128.5;

    private static double flipperParkedPosition = 173.1;
    private static double flipperRaisedPosition = 233.3;

    private static double launcherClosePosition = 139.2;
    private static double launcherParkedPosition = 139.2;
    private static double launcherFarPosition = 110.2;
    private static double launcherRaisedPosition = 110.2;

    private static double closeGoalCalcSlope = 18.85 ;
    private static double closeGoalCalcYIntercept = 1486.0;
    private static double farGoalCalcSlope = 18.85 ;
    private static double farGoalCalcYIntercept = 1486.0;

    @SuppressWarnings("unused")
    public static String getRobotName() {
        init();

        return robotName;
    }

    public static double getCarouselIntakePosition1() {
        init();

        return carouselIntakePosition1;
    }

    public static double getCarouselIntakePosition2() {
        init();

        return carouselIntakePosition2;
    }

    public static double getCarouselIntakePosition3() {
        init();

        return carouselIntakePosition3;
    }

    public static double getCarouselLaunchPosition1() {
        init();

        return carouselLaunchPosition1;
    }

    public static double getCarouselLaunchPosition2() {
        init();

        return carouselLaunchPosition2;
    }

    public static double getCarouselLaunchPosition3() {
        init();

        return carouselLaunchPosition3;
    }

    public static double getFlipperParkedPosition() {
        init();

        return flipperParkedPosition;
    }

    public static double getFlipperRaisedPosition() {
        init();

        return flipperRaisedPosition;
    }

    public static double getLauncherParkedPosition() {
        init();

        return launcherParkedPosition;
    }

    public static double getLauncherRaisedPosition() {
        init();

        return launcherRaisedPosition;
    }


    public static double getLauncherClosePosition() {
        init();

        return launcherClosePosition;
    }


    public static double getLauncherFarPosition() {
        init();

        return launcherFarPosition;
    }

    private static void init() {
        if (initialized) return;

        String robotSerialNumber = Device.getSerialNumberOrUnknown();

        switch (robotSerialNumber) {
            case ROBOT_SERIAL_COACH_ANTHONY:
                initCoachAnthonyRobot();
                break;
            case ROBOT_SERIAL_COACH_ROB:
                initCoachRobRobot();
                break;
            case ROBOT_SERIAL_MILLENNIUM:
                initMillenniumRobot();
                break;
            case ROBOT_SERIAL_OSCAR:
                initOscarRobot();
                break;
            default:
                // Unknown robot
                break;
        }

        initialized = true;
    }

    private static void initMillenniumRobot() {
        robotName = "Millennium";

        /*
        DD NOT PUT VALUES HERE!!!
        USE DEFAULTS AT TOP INSTEAD
        */
    }

    private static void initCoachAnthonyRobot() {
        robotName = "Coach Anthony";

        carouselIntakePosition1 =   78.4;
        carouselIntakePosition2 =  199.1;
        carouselIntakePosition3 =  319.0;
        carouselLaunchPosition1 =  260.5;
        carouselLaunchPosition2 =   20.7;
        carouselLaunchPosition3 =  141.5;

        flipperParkedPosition = 173.0;
        flipperRaisedPosition = 232.6;

        closeGoalCalcSlope = 9.060;
        closeGoalCalcYIntercept = 1901.0;
    }

    private static void initOscarRobot() {
        robotName = "Oscar";

        carouselIntakePosition1 =  20.62;
        carouselIntakePosition2 = 139.48;
        carouselIntakePosition3 = 259.30;
        carouselLaunchPosition1 = 198.00;
        carouselLaunchPosition2 = 80.44;
        carouselLaunchPosition3 = 320.80;

        flipperParkedPosition = 175;
        flipperRaisedPosition = 233;
    }

    private static void initCoachRobRobot() {
        robotName = "Coach Rob";

        // Use defaults for now...
    }

    public static double getCloseGoalCalcSlope() {
        return closeGoalCalcSlope;
    }

    public static double getCloseGoalCalcYIntercept() {
        return closeGoalCalcYIntercept;
    }

    public static double getFarGoalCalcSlope() {
        return farGoalCalcSlope;
    }

    public static double getFarGoalCalcYIntercept() {
        return farGoalCalcYIntercept;
    }
}
