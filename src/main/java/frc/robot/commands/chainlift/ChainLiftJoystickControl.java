package frc.robot.commands.chainlift;

import static frc.controls.ControlUtil.deadZone;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
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
    chainLift.set(-deadZone(xboxController.getY(Hand.kRight)));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    chainLift.set(0);
  }

}
