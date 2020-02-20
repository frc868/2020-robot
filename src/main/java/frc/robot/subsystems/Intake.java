package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotMap;
import frc.robot.helpers.Helper;

/**
 * This is the code for the power cell intake. It initiallizes two
 * Talon motors and makes the primary follow the secondary motor. 
 * 
 * @author ai
 */ 
public class Intake {
    private static Intake instance;
    private CANSparkMax primary;

    private Intake() {
        primary = new CANSparkMax(RobotMap.Intake.MOTOR, MotorType.kBrushless);

        primary.setInverted(RobotMap.Intake.MOTOR_IS_INVERTED);
    }

    public static Intake getInstance() {
        if (instance == null) {
            return new Intake();
        }

        return instance;
    }
    
    /**
     * Sets the speed for the primary motor (secondary motor follows) 
     * @param speed the speed to set from -1 to 1
     */
    public void setSpeed(double speed) {
        primary.set(Helper.boundValue(speed, -1, 1));
    }

    /** 
     * Retrieves the speed for the primary motor (and consequently secondary)
     * @return the speed, from -1 to 1
     */
    public double getIntakeSpeed() {
        return primary.get();
    }
}