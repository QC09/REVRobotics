// this code tests how the encoder works along the motor
package org.firstinspires.ftc.QCTruongCode.TestingFolder;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Encoder Test ")
public class EncoderTestLocation extends OpMode {

    DcMotorEx motor;

    double ticks = 28;   // ticks per full 360° revolution — verify for your motor/gearbox
    double newTarget = 0;

    // track previous button states so actions fire once per press, not every loop cycle
    boolean lastA = false;
    boolean lastB = false;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Hardware: ", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        boolean currentA = gamepad2.a;
        boolean currentB = gamepad2.b;

        // A: turn exactly 180 degrees at 100 RPM, only on the moment it's first pressed
        if (currentA && !lastA) {
            turnToDegrees(180, 100);
        }

        // B: return to home position (0 ticks) at 80 RPM, only on the moment it's first pressed
        if (currentB && !lastB) {
            tracker();
        }

        lastA = currentA;
        lastB = currentB;

        // once the motor reaches its target, cut power so it stops actively holding
        if (!motor.isBusy()) {
            motor.setPower(0);
        }

        telemetry.addData("Gamepad A pressed?", currentA);
        telemetry.addData("Gamepad B pressed?", currentB);
        telemetry.addData("Motor Ticks: ", motor.getCurrentPosition());
        telemetry.addData("Target Ticks: ", newTarget);
        telemetry.addData("Is Busy: ", motor.isBusy());
        telemetry.addData("Current Velocity (ticks/s): ", motor.getVelocity());
        telemetry.addData("Current Velocity (RPM): ", (motor.getVelocity() / ticks) * 60.0);
        telemetry.update();
    }

    // moves the motor to a target angle (in degrees) at a specified speed (in RPM)
    public void turnToDegrees(double degrees, double rpm) {
        newTarget = (degrees / 360.0) * ticks;
        motor.setTargetPosition((int) newTarget);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double ticksPerSecond = (rpm / 60.0) * ticks;
        motor.setVelocity(ticksPerSecond);
    }

    // returns the motor to its starting position (tick 0) at 80 RPM
    public void tracker() {
        newTarget = 0;
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double ticksPerSecond = (80.0 / 60.0) * ticks;
        motor.setVelocity(ticksPerSecond);
    }
}
