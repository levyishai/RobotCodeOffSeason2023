// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.trigon.robot;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.trigon.robot.commands.Commands;
import frc.trigon.robot.constants.AutonomousConstants;
import frc.trigon.robot.constants.CommandsConstants;
import frc.trigon.robot.constants.OperatorConstants;
import frc.trigon.robot.subsystems.arm.Arm;
import frc.trigon.robot.subsystems.arm.ArmConstants;
import frc.trigon.robot.subsystems.collector.Collector;
import frc.trigon.robot.subsystems.collector.CollectorConstants;
import frc.trigon.robot.subsystems.poseestimator.PoseEstimator;
import frc.trigon.robot.subsystems.roller.Roller;
import frc.trigon.robot.subsystems.sideshooter.SideShooter;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class RobotContainer {
    public static final LoggedDashboardChooser<String> MATCH_START_AUTO_NAME_CHOOSER = new LoggedDashboardChooser<>("autoChooser");
    public static final PoseEstimator POSE_ESTIMATOR = PoseEstimator.getInstance();
    private final Arm arm = Arm.getInstance();
    private final SideShooter sideShooter = SideShooter.getInstance();
    private final Collector collector = Collector.getInstance();
    private final Roller roller = Roller.getInstance();
//    private final Swerve swerve = Swerve.getInstance();

    public RobotContainer() {
        bindCommands();
        configureMatchStartAutoChooser();
    }

    /**
     * @return the autonomous command
     */
    public CommandBase getAutonomousCommand() {
        if (MATCH_START_AUTO_NAME_CHOOSER.get() == null)
            return null;

        return Commands.getMatchStartAuto(MATCH_START_AUTO_NAME_CHOOSER.get());
    }

    private void bindCommands() {
        bindDefaultCommands();
        bindControllerCommands();
        bindSetters();
    }

    private void bindControllerCommands() {
        OperatorConstants.FULL_COLLECTION_FROM_LEFT_SUBSTATION_TRIGGER.whileTrue(Commands.getFullCollectionFromLeftSubstationCommand());
        OperatorConstants.FULL_COLLECTION_FROM_RIGHT_SUBSTATION_TRIGGER.whileTrue(Commands.getFullCollectionFromRightSubstationCommand());
        OperatorConstants.FULL_CONE_PLACING_TRIGGER.whileTrue(Commands.getFullConePlacingCommand());
//        OperatorConstants.FULL_CUBE_SHOOTING_TRIGGER.whileTrue(Commands.getFullCubeShootingCommand());

        OperatorConstants.RESET_HEADING_TRIGGER.onTrue(CommandsConstants.RESET_HEADING_COMMAND);
        OperatorConstants.PRELOAD_CURRENT_MATCH_START_AUTO_TRIGGER.onTrue(Commands.getPreloadCurrentMatchStartAutoCommand());

        OperatorConstants.TOGGLE_FIELD_AND_SHOOTER_RELATIVE_DRIVE_TRIGGER.onTrue(Commands.getToggleFieldAndShooterRelativeDriveCommand());
        OperatorConstants.MANUAL_DRIVE_WITH_TURN_TO_COMMUNITY_TRIGGER.whileTrue(CommandsConstants.MANUAL_DRIVE_WITH_TURN_TO_COMMUNITY_COMMAND);
        OperatorConstants.MANUAL_DRIVE_WITH_TURN_TO_SHOOTING_ANGLE_TRIGGER.whileTrue(CommandsConstants.MANUAL_DRIVE_WITH_TURN_TO_SHOOTING_ANGLE_COMMAND);

        OperatorConstants.STANDING_CONE_COLLECTION_TRIGGER.whileTrue(Commands.getStandingConeCollectionCommand());
        OperatorConstants.NON_ASSISTED_CUBE_COLLECTION_TRIGGER.whileTrue(Commands.getNonAssistedCubeCollectionCommand());
//        OperatorConstants.ASSISTED_CUBE_COLLECTION_TRIGGER.whileTrue(RobotConstants.ROBOT_TYPE == RobotConstants.RobotType.SIMULATION ? CommandsConstants.ASSISTED_CUBE_COLLECTION_FOR_SIMULATION : CommandsConstants.ASSISTED_CUBE_COLLECTION);
//
        OperatorConstants.SHOOT_CUBE_TO_HIGH_LEVEL_TRIGGER.whileTrue(Commands.getShootCubeToHighLevelCommand());
        OperatorConstants.SHOOT_CUBE_TO_MIDDLE_LEVEL_TRIGGER.whileTrue(Commands.getShootCubeToMiddleLevelCommand());
        OperatorConstants.SHOOT_CUBE_TO_HYBRID_LEVEL_TRIGGER.whileTrue(Commands.getShootCubeToHybridLevelCommand());

        OperatorConstants.PLACE_CONE_AT_HIGH_LEVEL_TRIGGER.whileTrue(Commands.getPlaceConeAtHighLevelCommand());
        OperatorConstants.PLACE_CONE_AT_MIDDLE_LEVEL_TRIGGER.whileTrue(Commands.getPlaceConeAtMiddleLevelCommand());
        OperatorConstants.PLACE_CONE_AT_HYBRID_LEVEL_TRIGGER.whileTrue(Commands.getPlaceConeAtHybridLevelCommand());

        OperatorConstants.TOGGLE_BRAKE_MODE_TRIGGER.onTrue(CommandsConstants.COAST_COMMAND);
        OperatorConstants.TOGGLE_BRAKE_MODE_TRIGGER.onFalse(CommandsConstants.BRAKE_COMMAND);

        OperatorConstants.EJECT_TRIGGER.whileTrue(Collector.getInstance().getSetTargetStateCommand(CollectorConstants.CollectorState.EJECT));
    }

    private void bindDefaultCommands() {
        arm.setDefaultState(ArmConstants.ArmState.DEFAULT);
        sideShooter.setDefaultCommand(CommandsConstants.GO_TO_DEFAULT_SIDE_SHOOTER_STATE_COMMAND);
        collector.setDefaultCommand(CommandsConstants.STOP_COLLECTOR_COMMAND);
        roller.setDefaultCommand(roller.getFullCloseCommand());
//        swerve.setDefaultCommand(CommandsConstants.FIELD_RELATIVE_DRIVE_COMMAND);
    }

    private void bindSetters() {
        OperatorConstants.SET_GRID_NUMBER_TO_1_TRIGGER.onTrue(CommandsConstants.SET_GRID_NUMBER_TO_1_COMMAND);
        OperatorConstants.SET_GRID_NUMBER_TO_2_TRIGGER.onTrue(CommandsConstants.SET_GRID_NUMBER_TO_2_COMMAND);
        OperatorConstants.SET_GRID_NUMBER_TO_3_TRIGGER.onTrue(CommandsConstants.SET_GRID_NUMBER_TO_3_COMMAND);

        OperatorConstants.SET_COLUMN_TO_LEFT_TRIGGER.onTrue(CommandsConstants.SET_COLUMN_TO_LEFT_COMMAND);
        OperatorConstants.SET_COLUMN_TO_RIGHT_TRIGGER.onTrue(CommandsConstants.SET_COLUMN_TO_RIGHT_COMMAND);

        OperatorConstants.SET_LEVEL_TO_HIGH_TRIGGER.onTrue(CommandsConstants.SET_LEVEL_TO_HIGH_COMMAND);
        OperatorConstants.SET_LEVEL_TO_MIDDLE_TRIGGER.onTrue(CommandsConstants.SET_LEVEL_TO_MIDDLE_COMMAND);
    }

    private void configureMatchStartAutoChooser() {
        MATCH_START_AUTO_NAME_CHOOSER.addDefaultOption("None", null);

        for (String currentPathName : AutonomousConstants.AUTONOMOUS_PATHS_NAMES) {
            if (currentPathName.contains("Red") || currentPathName.contains("Blue"))
                currentPathName = currentPathName.replace("Red", "ALLIANCE").replace("Blue", "ALLIANCE");
            MATCH_START_AUTO_NAME_CHOOSER.addOption(currentPathName, currentPathName);
        }
    }
}
