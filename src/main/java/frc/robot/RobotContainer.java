package frc.robot;

import static frc.robot.Constants.OI.A_BUTTON_ID;
import static frc.robot.Constants.OI.B_BUTTON_ID;
import static frc.robot.Constants.OI.CONTROLLER_ID;
import static frc.robot.Constants.OI.LEFT_TRIGGER_AXIS_ID;
import static frc.robot.Constants.OI.RIGHT_BUMPER_ID;
import static frc.robot.Constants.OI.RIGHT_TRIGGER_AXIS_ID;
import static frc.robot.Constants.OI.X_BUTTON_ID;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.controls.DPadButton;
import frc.controls.TriggerButton;
import frc.robot.commands.arms.CloseArms;
import frc.robot.commands.arms.OpenArms;
import frc.robot.commands.arms.SpinIntakeWheels;
import frc.robot.commands.chainlift.ChainLiftJoystickControl;
import frc.robot.commands.drive.JoystickDrive;
import frc.robot.commands.folder.Fold;
import frc.robot.commands.folder.PullChainLift;
import frc.robot.commands.folder.ToggleLock;
import frc.robot.commands.folder.Unfold;
import frc.robot.commands.gear.SwitchToHighGear;
import frc.robot.commands.gear.SwitchToLowGear;
import frc.robot.subsystems.ChainLift;
import frc.robot.subsystems.CubeIntake;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Folder;
import frc.robot.subsystems.GearShift;
import frc.robot.subsystems.GearShift.Gear;

public class RobotContainer {

  private final XboxController controller = new XboxController(CONTROLLER_ID);
  private final Button switchGearButton = new JoystickButton(controller, RIGHT_BUMPER_ID);
  private final Button takeInCubeButton = new TriggerButton(controller, LEFT_TRIGGER_AXIS_ID);
  private final Button shootCubeButton = new TriggerButton(controller, RIGHT_TRIGGER_AXIS_ID);
  private final Button spinIntakeWheelsOutButton = new DPadButton(controller, DPadButton.Direction.UP);
  private final Button spinIntakeWheelsInButton = new DPadButton(controller, DPadButton.Direction.DOWN);

  private final Button toggleFoldButton = new JoystickButton(controller, X_BUTTON_ID);
  private final Button pullChainLiftBackButton = new JoystickButton(controller, B_BUTTON_ID);
  private final Button toggleLockButton = new JoystickButton(controller, A_BUTTON_ID);


  private final Drive drive = new Drive();
  private final GearShift gearShift = new GearShift();
  private final ChainLift chainLift = new ChainLift();
  private final CubeIntake cubeIntake = new CubeIntake();
  private final Folder folder = new Folder();

  
  private final Command joystickDrive = new JoystickDrive(drive, controller);
  private final Command switchToHighGear = new SwitchToHighGear(gearShift);
  private final Command switchToLowGear = new SwitchToLowGear(gearShift);
  private final Command switchGear = new ConditionalCommand(switchToHighGear, switchToLowGear, () -> gearShift.getGear() == Gear.LOW);

  private final Command chainLiftJoystickControl = new ChainLiftJoystickControl(chainLift, controller);

  private final Command openArms = new OpenArms(cubeIntake);
  private final Command closeArms = new CloseArms(cubeIntake);
  private final Command spinIntakeWheelsIn = new SpinIntakeWheels(cubeIntake, -.5);
  private final Command spinIntakeWheelsOut = new SpinIntakeWheels(cubeIntake, .5);
  private final Command shootCube = new SpinIntakeWheels(cubeIntake, .75);

  private final Command fold = new Fold(folder);
  private final Command unfold = new Unfold(folder);
  private final Command toggleFold = new ConditionalCommand(fold, unfold, folder::isLocked);
  private final Command toggleLock = new ToggleLock(folder);
  private final Command pullChainLift = new PullChainLift(folder, .75);

  private final Command autoCommand = new PrintCommand("Autonomous mode had not been made yet");

  public RobotContainer() {
    drive.setDefaultCommand(joystickDrive);
    chainLift.setDefaultCommand(chainLiftJoystickControl);
    configureButtonBindings();
  }

  private void configureButtonBindings() {
    switchGearButton.whenPressed(switchGear);

    takeInCubeButton.whileHeld(openArms.andThen(spinIntakeWheelsIn));
    takeInCubeButton.whenReleased(closeArms);
    spinIntakeWheelsOutButton.whileHeld(spinIntakeWheelsOut);
    spinIntakeWheelsInButton.whileHeld(spinIntakeWheelsIn);
    shootCubeButton.whileHeld(shootCube);

    toggleFoldButton.whenPressed(toggleFold);
    toggleLockButton.whenPressed(toggleLock);
    pullChainLiftBackButton.whileHeld(pullChainLift);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoCommand;
  }

}