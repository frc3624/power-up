package frc.robot.commands.folder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Folder;

public class Fold extends CommandBase {
  
  private final Folder folder;

  public Fold(Folder folder) {
    this.folder = folder;
  }

  @Override
  public void initialize() {
    folder.setPullPower(.5);
  }

  @Override
  public boolean isFinished() {
    return folder.isPulledAllTheWayBack();
  }

  @Override
  public void end(boolean interrupted) {
    folder.setPullPower(0);
    folder.lock();
  }

}
