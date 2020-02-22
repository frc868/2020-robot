package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
    private static DoubleSolenoid actuator;

    private Intake() {
        primary = new CANSparkMax(RobotMap.Intake.MOTOR, MotorType.kBrushless);

        primary.setInverted(RobotMap.Intake.MOTOR_IS_INVERTED);

        actuator = new DoubleSolenoid(RobotMap.Intake.ACTUATOR1, RobotMap.Intake.ACTUATOR2);
    }

    public static Intake getInstance() {
        if (instance == null) {
            return new Intake();
        }

        return instance;
    }
    
    /**
     * Sets the speed for the primary motor (secondary motor follows)
     *  
     * @param speed the speed to set from -1 to 1
     */
    public void setSpeed(double speed) {
        primary.set(speed);
    }

    /** 
     * Retrieves the speed for the primary motor (and consequently secondary)
     * @return the speed, from -1 to 1
     */
    public double getIntakeSpeed() {
        return primary.get();
    }

    /**
     * sets the actuator to the position it is not currently in
     * @author acr
     */
    public void toggle() {
        if(actuator.get() == Value.kForward) {
            actuator.set(Value.kReverse);
        }else if (actuator.get() == Value.kReverse) {
            actuator.set(Value.kForward);
        }
    }

    /**
     * Raise the intake.
     * @author acr
     */
    public void actuatorUp() {
        actuator.set(Value.kReverse);
    }

    /**
     * Lower the intake.
     * @author acr
     */
    public void actuatorDown() {
        actuator.set(Value.kForward);
    }
}