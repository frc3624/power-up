package frc.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

/*
 * This is late CSA material, so you are welcome to just copy this class to your
 * project and use it yourself.
 * 
 * We want to start a command when one of the triggers on the xbox controller is pressed,
 * but the triggers are axes, not buttons. Therefore, we can't simply use JoystickButton.
 * We have to make our own kind of button which redefines when the button is pressed.
 * When you make a new JoystickButton(joystick, buttonNum), that button is considered
 * "pressed" when joystickButton.get() returns true. In the case of a JoystickButton,
 * get() returns joystick.getRawButton(buttonNum).
 * 
 */
public class TriggerButton extends Button {

    private final GenericHID genericHID;
    private final int axis;
    private final double threshold;

    /**
     * @param genericHID Joystick this button will be bound to
     * @param axis the axis number that will be considered the ID
     */
    public TriggerButton(GenericHID genericHID, int axis) {
        this(genericHID, axis, .5);
    }
    /**
     * 
     * @param genericHID Joystick this button will be bound to
     * @param axis the axis number that will be considered the ID
     * @param threshold How far the axis has go to before being considered "pressed"
     */
    public TriggerButton(GenericHID genericHID, int axis, double threshold) {
        this.genericHID = genericHID;
        this.axis = axis;
        this.threshold = threshold;
    }

    /*
     * The button is now considered pressed once the specified axis passes
     * the threshold 
     */
    @Override
    public boolean get() {
        return genericHID.getRawAxis(axis) > threshold;
    }

}