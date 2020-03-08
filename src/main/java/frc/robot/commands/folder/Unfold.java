package frc.robot.commands.folder;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Folder;

public class Unfold extends InstantCommand {
  
  private final Folder folder;

  public Unfold(Folder folder) {
    this.folder = folder;
  }

  @Override
  public void execute() {
    folder.setPullPower(0);
    folder.unlock();
  }

}
