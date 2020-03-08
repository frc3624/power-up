package frc.robot.subsystems;

import static frc.robot.Constants.GearShift.GEARSHIFT_FORWARD_CHANNEL;
import static frc.robot.Constants.GearShift.GEARSHIFT_PCM_ID;
import static frc.robot.Constants.GearShift.GEARSHIFT_REVERSE_CHANNEL;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GearShift extends SubsystemBase {

  /*
   * You should already be well acquainted with enums, since Value - which you used to set
   * the state of the DoubleSolenoid - is an enum
   * http://tutorials.jenkov.com/java/enums.html
   */
  public enum Gear {
    HIGH(Value.kReverse),
    LOW(Value.kForward);
    private Value dState;
    Gear(Value doubleSolenoidState) {
      dState = doubleSolenoidState;
    }
    public Value getSolenoidState() {
      return dState;
    }
  }

  private final DoubleSolenoid gearPiston = new DoubleSolenoid(GEARSHIFT_PCM_ID, GEARSHIFT_FORWARD_CHANNEL, GEARSHIFT_REVERSE_CHANNEL);
  
  private Gear gear;

  public GearShift() {
    setGear(Gear.HIGH);
  }

  public void setGear(Gear gear) {
    this.gear = gear;
    gearPiston.set(this.gear.getSolenoidState());
  }
  public Gear getGear() {
    return gear;
  }

}
