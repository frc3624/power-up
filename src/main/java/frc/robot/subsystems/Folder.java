package frc.robot.subsystems;

import static frc.robot.Constants.Folder.FOLD_MOTOR_ID;
import static frc.robot.Constants.Folder.PISTON_LOCK_FORWARD_CHANNEL;
import static frc.robot.Constants.Folder.PISTON_LOCK_PCM_ID;
import static frc.robot.Constants.Folder.PISTON_LOCK_REVERSE_CHANNEL;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Folder extends SubsystemBase {

  private final WPI_TalonSRX pullMotor = new WPI_TalonSRX(FOLD_MOTOR_ID);
  private final DoubleSolenoid pistonLock = new DoubleSolenoid(PISTON_LOCK_PCM_ID, PISTON_LOCK_FORWARD_CHANNEL, PISTON_LOCK_REVERSE_CHANNEL);

  public Folder() {
    
  }

  /* 
   * I know that your first instinct when making this class would be to call it setMotorSpeed or setSpeed or something.
   * I specifically chose this name because it abstracts the fact that this mechanism uses a motor, and the word "speed"
   * would be entirely wrong. In this drastic case, the voltage given to the motor is not directly related to the speed
   * at which the motor turns. The word "speed" would be completely misleading.
   */
  /**
   * @param power The power at which the chain lift will be pulled with, [0, 1]
   */
  public void setPullPower(double power) {
    // The pull motor can pull either clockwise or counter clockwise, but I decided to only let it go one way to simplify things
    pullMotor.set(ControlMode.PercentOutput, Math.max(power, 0));
  }

  public void lock() {
    pistonLock.set(Value.kReverse);
  }
  public void unlock() {
    pistonLock.set(Value.kForward);
  }
  public boolean isLocked() {
    return pistonLock.get() == Value.kReverse;
  }

  /*
   * In the hinge used for folding the robot, there are torsion springs
   * https://www.mcmaster.com/torsion-springs
   * As these springs bend, the amount of force they use to resist the fold motor
   * will increase. In reaction, the fold motor will keep using more current.
   * 
   * The resisting force increases as the robot folds, and the current supplied increases as
   * the resisting force increases, therefore, the current supplied to the motor increases as the robot folds.
   * 
   * I tested it, and the current the motor uses and how far the robot is folded seems to be very consistent, which means
   * I can find out what current the motor uses when the robot is folded "all the way back", and use that as a reference 
   * to tell when the robot is all the way back.
   */
  private final static double PULL_MOTOR_CURRENT_THRESHOLD = 15;
  public boolean isPulledAllTheWayBack() {
    return pullMotor.getSupplyCurrent() > PULL_MOTOR_CURRENT_THRESHOLD;
  }
  
}
