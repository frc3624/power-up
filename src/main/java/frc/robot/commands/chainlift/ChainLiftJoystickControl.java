package frc.robot.commands.chainlift;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ChainLift;

public class ChainLiftJoystickControl extends CommandBase {

  private final ChainLift chainLift;
  private final XboxController xboxController;

  public ChainLiftJoystickControl(ChainLift chainLift, XboxController xboxController) {
    this.chainLift = chainLift;
    addRequirements(chainLift);
    this.xboxController = xboxController;
  }

  @Override
  public void execute() {
    chainLift.set(-deadzone(xboxController.getY(Hand.kRight)));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    chainLift.set(0);
  }

  // See JoystickDrive for information on deadzones
  private final static double DEADZONE = .01;
  private double deadzone(double axisValue) {
    return Math.abs(axisValue) < DEADZONE ? 0 : axisValue;
  }

}
