package frc.controls;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;

/*
 * See TriggerButton for explanation for what I am doing
 */
public class DPadButton extends Button {

    public enum Direction {
		UP(0),
		UP_LEFT(45),
		RIGHT(90),
		DOWN_RIGHT(135),
		DOWN(180),
		DOWN_LEFT(225),
		LEFT(270),
		UP_RIGHT(315);
		private int angle;
		Direction(int angle) {
			this.angle = angle;
		}
		/**
		 * @return an angle representative of the cardinal direction, going clockwise
		 */
		public int getAngle() {
			return angle;
		}
	}


    private final XboxController controller;
    private final int pov;
    private final Direction direction;

    /**
     * @param controller the Xbox controller this button will be bound to
     * @param axis the axis number that will be considered the ID
     */
    public DPadButton(XboxController controller, Direction direction) {
        this(controller, 0, direction);
    }

    /**
     * @param controller the Xbox controller this button will be bound to
     * @param pov POV index
     * @param direction Direction on the DPad
     */
    public DPadButton(XboxController controller, int pov, Direction direction) {
        this.controller = controller;
        this.pov = pov;
        this.direction = direction;
    }

    /*
     * The button is now considered pressed if the direction of the POV is the
     * same as the direction of this button.
     */
    @Override
    public boolean get() {
        return controller.getPOV(pov) == direction.getAngle();
    }

}