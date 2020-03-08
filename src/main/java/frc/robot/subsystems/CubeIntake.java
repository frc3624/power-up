package frc.robot.subsystems;

import static frc.robot.Constants.CubeIntake.ARMS_FORWARD_CHANNEL;
import static frc.robot.Constants.CubeIntake.ARMS_PCM_ID;
import static frc.robot.Constants.CubeIntake.ARMS_REVERSE_CHANNEL;
import static frc.robot.Constants.CubeIntake.LEFT_ARM_MOTOR_ID;
import static frc.robot.Constants.CubeIntake.RIGHT_ARM_MOTOR_ID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CubeIntake extends SubsystemBase {
  
  /* 
   * While it would be unusual for the 2 motors to have different speeds, I don't feel
   * it is appropriate to make them master/slave as they aren't driving the same shaft
   */
  private final WPI_TalonSRX leftWheelsMotor = new WPI_TalonSRX(LEFT_ARM_MOTOR_ID);
  private final WPI_TalonSRX rightWheelsMotor = new WPI_TalonSRX(RIGHT_ARM_MOTOR_ID);
  
  private final DoubleSolenoid armPistons = new DoubleSolenoid(ARMS_PCM_ID, ARMS_FORWARD_CHANNEL, ARMS_REVERSE_CHANNEL);

  public CubeIntake() {
    closeArms();
  }

  public void openArms() {
    armPistons.set(Value.kForward);
  }
  public void closeArms() {
    armPistons.set(Value.kReverse);
  }

  public boolean armsAreOpen() {
    return armPistons.get() == Value.kForward;
  }

  /*
   * I set the speed of the gripper wheels to 0 once the cube is all the way back
   * in order to stop the intake wheels (which are actually called compliant wheels)
   * from breaking.
   * https://www.andymark.com/products/4-in-compliant-wheel-1-2-in-hex-bore-35a-durometer
   * The green part is soft and rubbery, but the black plastic on the inside is hard and brittle.
   * If something stops the green part of the wheel from rotating (such as friction between the box and the wheel),
   * but the black inside part keeps trying to rotate, the black part is going to break.
   * The only time the green part is stopped from rotating is when the cube is all the way back and the wheels
   * try to push it back further.
   * 
   * I want to stop the motors when the cube goes all the way to the back of the intake.
   * 
   * When the wheels are free spinning, they have very little resistance and use very little current.
   * When the intake grabs a cube, the cube creates some resistance, so there will be an increase in current - C. 
   * When it hits the back of the intake, it will case a spike in resistance and a spike in current - up to a maximum of Cmax.
   * 
   * I can tell when the cube goes all the way back by looking for that spike in current.
   */

  /**
   * @param speed percent voltage of the arms [-1, 1], 1 is outwards
   */
  public void spinArms(double speed) {
    if (speed > 0)
      cubeIsFurthestBack = false;
    if (!cubeIsFurthestBack()) {
      leftWheelsMotor.set(ControlMode.PercentOutput, speed);
      rightWheelsMotor.set(ControlMode.PercentOutput, speed);
    }
  }

  /* 
   * Once the cube is all the way back, the motors are shut off and current drops to 0.
   * I save that the cube went all the way back because the current wont properly reflect that
   * the cube is all the way back once the current drops.
   */
  private boolean cubeIsFurthestBack = false;
  @Override
  public void periodic() {
    if (cubeIsFurthestBack()) {
      leftWheelsMotor.set(0);
      rightWheelsMotor.set(0);
      cubeIsFurthestBack = true;
    }
  }

  public boolean cubeIsFurthestBack() {
    return cubeIsFurthestBack || wheelsOverCurrent();
  }

  private final static double OVER_CURRENT_THRESHOLD = 7;
  private boolean wheelsOverCurrent() {
    return leftWheelsMotor.getSupplyCurrent() > OVER_CURRENT_THRESHOLD 
        || rightWheelsMotor.getSupplyCurrent() > OVER_CURRENT_THRESHOLD;
  }

}
