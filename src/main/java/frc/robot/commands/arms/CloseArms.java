package frc.robot.commands.arms;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CubeIntake;

public class CloseArms extends InstantCommand {
  
  private final CubeIntake cubeIntake;

  public CloseArms(CubeIntake cubeIntake) {
    this.cubeIntake = cubeIntake;
  }

  @Override
  public void execute() {
    cubeIntake.closeArms();
  }

}
