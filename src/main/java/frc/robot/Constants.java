package frc.robot;

public final class Constants {

    public final static class ChainLift {
        public final static int MASTER_MOTOR_ID = 8, SLAVE_MOTOR_ID = 11;
        public final static boolean USING_COLOR_SENSOR = true;
        public final static int LIGHT_SENSOR_DIO_ID = 0;
    }

    public final static class Drive {
        public final static int LEFT_MASTER_ID = 1, LEFT_SLAVE_ID = 3, RIGHT_MASTER_ID = 2, RIGHT_SLAVE_ID = 4;
    }

    public final static class GearShift {
        public final static int GEARSHIFT_PCM_ID = 0, GEARSHIFT_FORWARD_CHANNEL = 0, GEARSHIFT_REVERSE_CHANNEL = 1;
    }

    public final static class CubeIntake {
        public final static int LEFT_ARM_MOTOR_ID = 6, RIGHT_ARM_MOTOR_ID = 7;
        public final static int ARMS_PCM_ID = 0, ARMS_FORWARD_CHANNEL = 6, ARMS_REVERSE_CHANNEL = 7;
    }

    public final static class Folder {
        public final static int FOLD_MOTOR_ID = 5;
        public final static int PISTON_LOCK_PCM_ID = 0, PISTON_LOCK_FORWARD_CHANNEL = 4, PISTON_LOCK_REVERSE_CHANNEL = 5;
    }

    public final static class OI {
        public final static int CONTROLLER_ID = 0;
        public final static int A_BUTTON_ID = 1, B_BUTTON_ID = 2, X_BUTTON_ID = 3, Y_BUTTON_ID = 4;
        public final static int LEFT_BUMPER_ID = 5, RIGHT_BUMPER_ID = 6;
        public final static int LEFT_TRIGGER_AXIS_ID = 2, RIGHT_TRIGGER_AXIS_ID = 3;
    }

}
