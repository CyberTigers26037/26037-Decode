package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.util.Device;

public class RobotConfig {
    private static boolean initialized;
    private static final String ROBOT_SERIAL_COACH_ANTHONY = "5838f408422f581c";
    private static final String ROBOT_SERIAL_COACH_ROB     = "803b7c0c22973496";
    private static final String ROBOT_SERIAL_MILLENNIUM    = "a542e45d0aef5058";

    private static String robotName = "Unknown";

    private static double carouselIntakePosition1 =   15.7;
    private static double carouselIntakePosition2 =  114.6;
    private static double carouselIntakePosition3 =  -83.8;
    private static double carouselLaunchPosition1 = -135.0;
    private static double carouselLaunchPosition2 =  -34.3;
    private static double carouselLaunchPosition3 =   63.8;

    private static double flipperParkedPosition = -4.3;
    private static double flipperRaisedPosition = 93.4;

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
            default:
                // Unknown robot
                break;
        }

        initialized = true;
    }

    private static void initMillenniumRobot() {
        robotName = "Millennium";

        // do nothing, use defaults
    }

    private static void initCoachAnthonyRobot() {
        robotName = "Coach Anthony";

        carouselIntakePosition1 =  -79;
        carouselIntakePosition2 =   20;
        carouselIntakePosition3 =  121;
        carouselLaunchPosition1 =   72;
        carouselLaunchPosition2 = -129;
        carouselLaunchPosition3 =  -26;

        flipperParkedPosition = 0;
        flipperRaisedPosition = 93;
    }

    private static void initCoachRobRobot() {
        robotName = "Coach Rob";

        // Use defaults for now...
    }
}
