package frc.robot.commands.gear;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.GearShift;
import frc.robot.subsystems.GearShift.Gear;

public class SwitchToLowGear extends InstantCommand {
  
  private final GearShift gearShift;

  public SwitchToLowGear(GearShift gearShift) {
    this.gearShift = gearShift;
  }

  @Override
  public void execute() {
    gearShift.setGear(Gear.LOW);
  }

}
