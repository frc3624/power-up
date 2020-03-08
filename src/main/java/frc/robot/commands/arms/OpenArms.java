package frc.robot.commands.arms;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CubeIntake;

public class OpenArms extends InstantCommand {

  private final CubeIntake cubeIntake;

  public OpenArms(CubeIntake cubeIntake) {
    this.cubeIntake = cubeIntake;
  }

  @Override
  public void execute() {
    cubeIntake.openArms();
  }

}
