package frc.robot.commands.chainlift;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.ChainLift;

/*
 * I made this command because I want something a lot more reliable than "If I start at the bottom
 * and set the speed of the chain lift to .6 for 1 second, it will go to the height I want"
 * This command will also be a demonstration on how to make your own PID controller.
 * http://robotsforroboticists.com/pid-control/
 * I like this resource because not only does it tell you what a PID is in a simple way, but
 * it also tells you how to do it in code.
 */
public class MoveChainLiftToPosition extends CommandBase {

  private final ChainLift chainLift;
  private final double targetPosition, allowedError;

  /**
   * @param chainLift The chain lift subsystem
   * @param targetPosition What position the chain lift will be at the end of the command, in feet. Target will be constrained to bounds of the chain lift
   */
  public MoveChainLiftToPosition(ChainLift chainLift, double targetPosition) {
    this(chainLift, targetPosition, .01);
  }
  /**
   * @param chainLift The chain lift subsystem
   * @param targetPosition What position the chain lift will be at the end of the command, in feet. Target will be constrained to bounds of the chain lift
   * @param allowedError The maximum difference between the position of the chain lift and the target position that the command will consider "at the position" 
   */
  public MoveChainLiftToPosition(ChainLift chainLift, double targetPosition, double allowedError) {
    this.chainLift = chainLift;
    addRequirements(this.chainLift);
    // Hover over the method clamp to see what it does
    this.targetPosition = MathUtil.clamp(targetPosition, ChainLift.MIN_HEIGHT_FEET, ChainLift.MAX_HEIGHT_FEET);
    this.allowedError = MathUtil.clamp(allowedError, 0, ChainLift.MAX_HEIGHT_FEET - ChainLift.MIN_HEIGHT_FEET);
  }

  @Override
  public void initialize() {
    // Need to reset all of the variables every time this command is run
    integral = 0;
    priorError = targetPosition - chainLift.getPosition();
    beforeTime = System.currentTimeMillis();
  }

  private final static double kP = 1, kI = 0, kD = 0;

  private double integral;
  private double priorError;

  private long beforeTime;

  @Override
  public void execute() {
    // For Integral and Derivative, we need delta time
    long currentTime = System.currentTimeMillis();
    double deltaTime = (currentTime - beforeTime) / 1000.0;
    beforeTime = currentTime;

    double error = targetPosition - chainLift.getPosition(); // Error, AKA P

    /* 
     * The integral is a Riemann sum where Δt is the time between when this method is called.
     * It really should always be .05s, but I calculate delta time myself in case it isn't.
     * The internet tells me to use a right riemann sum, but I can actually make a trapezoidal
     * riemann sum since we have priorError
     */ 
    // integral += error * deltaTime;
    integral += ((error + priorError) / 2) * deltaTime;

    double derivative = (error - priorError) / deltaTime; // Slope formula

    priorError = error;
    
    double output = kP*error + kI*integral + kD*derivative;
    /*
     * The target value is a position, which means that 
     */
    chainLift.set(output);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(targetPosition - chainLift.getPosition()) < allowedError;
  }

  @Override
  public void end(boolean interrupted) {
    chainLift.set(0);
  }

}
