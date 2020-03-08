package frc.robot.subsystems;

import static frc.robot.Constants.ChainLift.LIGHT_SENSOR_DIO_ID;
import static frc.robot.Constants.ChainLift.MASTER_MOTOR_ID;
import static frc.robot.Constants.ChainLift.SLAVE_MOTOR_ID;
import static frc.robot.Constants.ChainLift.USING_COLOR_SENSOR;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The chain lift is the mechanism that makes the ladder move up and down
 * https://www.youtube.com/watch?v=Sm-cxv0QsuM
 */
public class ChainLift extends SubsystemBase {

  private final static int MAX_HEIGHT_TICKS = 27050;
  private final static int MIN_HEIGHT_TICKS = 0;

  // For converting the position of the encoder into the height of the ladder
  // I measured this by setting the encoder position to 0, make the ladder go up 1 foot, and see the new encoder position
  private final static double FEET_PER_ENCODER_TICK = 1.0/4400;

  // The encoder constants are private while these feet constants are public because I want to hide the fact that 

  public final static double MAX_HEIGHT_FEET = MAX_HEIGHT_TICKS * FEET_PER_ENCODER_TICK;
  public final static double MIN_HEIGHT_FEET = MIN_HEIGHT_TICKS * FEET_PER_ENCODER_TICK;

  /*
   * The sprocket that works the chain is run by two motors driving one shaft.
   * Since they both drive the same thing, we make make one master and one slave
   * so we make sure both are going at the same speed and in the same direction.
   * 
   * The master motor should be the one with the encoder
   */
  private final WPI_TalonSRX masterChainMotor = new WPI_TalonSRX(MASTER_MOTOR_ID);
  private final WPI_TalonSRX slaveChainMotor = new WPI_TalonSRX(SLAVE_MOTOR_ID);

  public ChainLift() {
    slaveChainMotor.set(ControlMode.Follower, MASTER_MOTOR_ID);
    /*
     * Inverted the motors so the ladder going up is the positive direction
     * There's no real reason to do this besides making it easier cognitively
     */
    masterChainMotor.setInverted(true);
    slaveChainMotor.setInverted(true);
    /* 
     * The positive direction of the encoder is in the opposite direction the motor moves if 
     * you give it a positive voltage. We need to make sure they are the same
     */
    masterChainMotor.setSensorPhase(true);
    // The chain lift should be at its 0 position (bottomed out) when the robot is turned on
    calibrateEncoder();
  }

  /*
   * This is where the code gets a little bit weird.
   * You can't let the ladder go too far up or too far down. If it goes too far down, the
   * chain will break. If it goes too far up, the chain will break. We don't want the chain to break.
   * We're going to use the fact that the motors that drive the chain lift are private in order to stop the
   * motor from breaking. 
   */
  private double desiredSpeed = 0; // Speed should be 0 when the robot turns on
  /**
   * Sets the speed of the chain lift, but the speed will be 0
   * if the speed will cause the ladder to go out of bounds
   * @param speed percent voltage of chain lift motor.
   */
  public void set(double speed) {
    desiredSpeed = speed;
  }

  /*
   * This code stops the motor if it is trying to go past one of its bounds
   * 
   * "Why didn't I just check in set()?"
   * I had a very specific circumstance in mind when I wrote this code.
   * Imagine the ladder is at the top, and we call chainLift.set(-1) once, and never again.
   * desiredSpeed < 0 && !this.isAtLowest() == false, so it will set the speed of the motor to -1.
   * chainLift.set() isn't being called when it reaches the bottom, so it won't set the speed to 0
   * once this.isAtLowest() == true. masterChainMotor.get() will remain -1 and it will break.
   */
  @Override
  public void periodic() {
    if (desiredSpeed > 0 && !this.isAtHighest())
      masterChainMotor.set(ControlMode.PercentOutput, desiredSpeed);
    else if (desiredSpeed < 0 && !this.isAtLowest() && isInSlowZone())
      masterChainMotor.set(ControlMode.PercentOutput, Math.max(desiredSpeed, -.3));
    else if (desiredSpeed < 0 && !this.isAtLowest())
      masterChainMotor.set(ControlMode.PercentOutput, desiredSpeed);
    else
      masterChainMotor.set(ControlMode.PercentOutput, 0);
    
    calibrateEncoder();
  }

  /**
   * @return position of the ladder in feet
   */
  public double getPosition() {
    return getEncoderPosition() * FEET_PER_ENCODER_TICK;
  }

  private final static double ALLOWED_ERROR = 0.05; // feet

  public boolean isAtLowest() {
    return bottomLimitSwitchTriggered() || getPosition() <= MIN_HEIGHT_FEET + ALLOWED_ERROR;
  }
  
  public boolean isAtHighest() {
    return getPosition() >= MAX_HEIGHT_FEET - ALLOWED_ERROR;
  }

  /*
 	 * "The ladder slowed down right before it bottomed out so the light sensor has time to register"
 	 * - Jordan D Dobstaff, Team Captain (2019 - 2020)
   */
  private final static double UPPER_SLOW_ZONE_BOUND = 1;
  private boolean isInSlowZone() {
    return getPosition() <= UPPER_SLOW_ZONE_BOUND;
  }

  /*
   * The robot might be using either a light sensor or a color sensor. Originally, it used
   * a light sensor but I put a color sensor on it so kids can learn how to use it for Infinite Recharge.
   * 
   * The chain may break if the ladder goes down its "0 position" on the way down, so it is critical that
   * we don't do that. We originally just used the encoder, set the bottomed out position of the ladder
   * as the 0 position on the encoder, and then stopped the motor from going past that, but they felt that
   * wasn't enough. In addition to the encoder, they also added a light sensor. There's a tape measure on
   * the ladder with a square black piece of tape on it. When the ladder is at the 0 position, the black tape is
   * in front of the light sensor, and when the ladder is raised, the tape measure gets pulled and the yellow
   * part of the tape measure goes in front of the robot. The light sensor returns "true" when it sees black, and
   * "false" otherwise. It is dedicated to differentiating black from other things, so it's a lot better than the
   * general purpose color sensor. 
   */
  private final DigitalInput lightSensor = new DigitalInput(LIGHT_SENSOR_DIO_ID);
  
  private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
  private final ColorMatch colorMatcher = new ColorMatch();
  // I pointed the color sensor at these colors while printing out the RGB, and recorded them here
  private final Color kYellowTarget = ColorMatch.makeColor(0.321045, 0.56274, 0.11621);
  private final Color kBlackTarget = ColorMatch.makeColor(0.249, 0.464, 0.285);

  {// Think of this as an extension of the constructor (this is an "instance initializer"). I used this because I want to keep my related code close together
    colorMatcher.addColorMatch(kYellowTarget);
    colorMatcher.addColorMatch(kBlackTarget);
  }
  private final static boolean USE_COLOR_SENSOR = USING_COLOR_SENSOR;
  protected boolean bottomLimitSwitchTriggered() {
    if (USE_COLOR_SENSOR) {
      Color currentlySeenColor = colorSensor.getColor();
      /*
       * The color sensor only tells us the RGB value of what it is seeing, it doesn't explicitly
       * tell us if it sees Yellow or Black. We cant simply use R1 == R2, G1 == G2, B1 == B2 because
       * even if you're looking at the same color, it may vary a little bit due to lighting or error with 
       * the sensor.
       * 
       * How color matcher works (very interesting, not important for you to know):
       * If you wanted to see if a point (x1, y1) was more similar to (x, y) than (x2, y2), then you would
       * find the distance (using the distance formula) between (x1, y1) and (x, y) and compare it to the 
       * distance between (x2, y2) and (x, y). The same concept is being applied here, but the RBG value
       * is being treated as a 3D point, (x, y, z). You can find the distance between 2 3D points
       * with the formula sqrt(x^2 + y^2 + z^2).
       */
      Color closestColor = colorMatcher.matchClosestColor(currentlySeenColor).color;
      /*
       * We need to find out if the color we are looking at is closer to yellow or black. We added the yellow
       * and black Color objects to colorMatcher. If we give colorMatcher some RGB value, it will find out
       * which color that RBG value is most similar to and return the reference to that Color object. If it
       * doesn't think it's any of the colors you added to it, it returns null
       */
      if (closestColor == kYellowTarget) {
        return false;
      } else if (closestColor == kBlackTarget || closestColor == null) { // I want to be safe and stop if it is null.
        return true;
      } else { // There should never be a case where it returns anything else, but I'm returning true anyways
        return true;
      }
    } else {
      return lightSensor.get();
    }
  }
  protected int getEncoderPosition() {
    return masterChainMotor.getSelectedSensorPosition();
  }

  /*
   * Encoder drift is a thing. The encoder only updates so often, so once you exceed 6600RPM with the specific encoder we use
   * in the chain lift, it will begin to drift (meaning that what it reads may not reflect reality).
   * The chain lift will never exceed 6600RPM or 110 rotations per second, but I did this just because I can.
   * We use the light sensor as a reference, so whenever the ladder bottoms out, it will reset the motor back to 0
   * and get rid of whatever drift it accumulated.
   * 
   * Drift from Fortnite
   */
  private void calibrateEncoder() {
    if (bottomLimitSwitchTriggered()) // I dont use isAtLowest() because isAtLowest() uses the encoder
      masterChainMotor.setSelectedSensorPosition(0);
  }

}
