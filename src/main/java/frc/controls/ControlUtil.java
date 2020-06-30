package frc.controls;

public final class ControlUtil {

    private final static double DEAD_ZONE = .01;
    public final static double deadZone(double axisValue) {
        return Math.abs(axisValue) < DEAD_ZONE ? 0 : axisValue;
    }

}
