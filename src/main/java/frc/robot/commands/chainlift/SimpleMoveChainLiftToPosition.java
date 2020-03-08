package frc.robot.commands.chainlift;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ChainLift;

/*
 * This version of the command just uses the setPosition method of
 * ChainLift instead of implementing our own PID loop
 */
public class SimpleMoveChainLiftToPosition extends CommandBase {

  private final ChainLift chainlift;
  private final double targetPosition;

  public SimpleMoveChainLiftToPosition(ChainLift chainLift, double targetPosition) {
    this.chainlift = chainLift;
    addRequirements(chainLift);
    this.targetPosition = targetPosition;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    
  }
}
