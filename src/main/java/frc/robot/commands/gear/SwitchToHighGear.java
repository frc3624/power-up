package frc.robot.commands.gear;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GearShift;
import frc.robot.subsystems.GearShift.Gear;

public class SwitchToHighGear extends CommandBase {
  
  private final GearShift gearShift;

  public SwitchToHighGear(GearShift gearShift) {
    this.gearShift = gearShift;
  }

  @Override
  public void execute() {
    gearShift.setGear(Gear.HIGH);
  }

}
