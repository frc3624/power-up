package frc.robot.commands.folder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Folder;

public class PullChainLift extends CommandBase {

  private final Folder folder;
  private final double power;

  public PullChainLift(Folder folder, double power) {
    this.folder = folder;
    addRequirements(folder);
    this.power = power;
  }

  @Override
  public void execute() {
    folder.setPullPower(power);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
  
  @Override
  public void end(boolean interrupted) {
    folder.setPullPower(0);
  }

}
