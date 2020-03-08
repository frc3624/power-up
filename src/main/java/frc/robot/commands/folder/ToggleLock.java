package frc.robot.commands.folder;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Folder;

public class ToggleLock extends InstantCommand {

  private final Folder folder;

  public ToggleLock(Folder folder) {
    this.folder = folder;
    addRequirements(folder);
  }

  @Override
  public void execute() {
    if (folder.isLocked())
      folder.unlock();
    else
      folder.lock();
  }
}
