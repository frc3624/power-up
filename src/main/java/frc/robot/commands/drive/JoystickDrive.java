package frc.robot.commands.drive;

import static frc.controls.ControlUtil.deadZone;

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
    drive.arcadeDrive(-deadZone(xboxController.getY(Hand.kLeft)), deadZone(xboxController.getX(Hand.kLeft)));
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    drive.arcadeDrive(0, 0);
  }
  
}
