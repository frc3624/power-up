package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class JoystickDrive extends CommandBase {

  private final Drive drive;
  private final XboxController xboxController;

  public JoystickDrive(Drive drive, XboxController xboxController) {
    this.drive = drive;
    addRequirements(drive);
    this.xboxController = xboxController;
  }

  @Override
  public void execute() {
    drive.arcadeDrive(-deadzone(xboxController.getY(Hand.kLeft)), deadzone(xboxController.getX(Hand.kLeft)));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    drive.arcadeDrive(0, 0);
  }

  /*
   * If you let go of the joystick, it wont necessarily return to (0, 0). Instead,
   * the x and y may be some very small nonzero value. This is bad because the robot may move
   * even though the joystick is not being touched, so we implement something called the deadzone.
   * If the axis value is small enough, the deadzone just rounds the value to zero.
   */
  private final static double DEADZONE = .01;
  private double deadzone(double axisValue) {
    return Math.abs(axisValue) < DEADZONE ? 0 : axisValue;
  }
  
}
