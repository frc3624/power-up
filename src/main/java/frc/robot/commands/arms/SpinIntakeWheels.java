package frc.robot.commands.arms;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CubeIntake;

public class SpinIntakeWheels extends CommandBase {

  private final CubeIntake cubeIntake;
  private double speed;

  public SpinIntakeWheels(CubeIntake cubeIntake, double speed) {
    this.cubeIntake = cubeIntake;
    addRequirements(this.cubeIntake);
    this.speed = speed;
  }

  @Override
  public void initialize() {
    cubeIntake.spinArms(speed);
  }

  @Override
  public boolean isFinished() {
    return cubeIntake.cubeIsFurthestBack();
  }

  @Override
  public void end(boolean interrupted) {
    cubeIntake.spinArms(0);
  }

}
