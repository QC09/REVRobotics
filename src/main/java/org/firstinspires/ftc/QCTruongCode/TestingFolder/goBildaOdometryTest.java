// this code is to test the odometry sensor only

package org.firstinspires.ftc.QCTruongCode.TestingFolder;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

@TeleOp(name = "goBILDA Odometry Test", group = "Linear OpMode")
public class goBildaOdometryTest extends LinearOpMode {

    private GoBildaPinpointDriver odo;

    // Filter helper: returns 0 if the value is within the deadband noise threshold
    private double applyDeadband(double value, double threshold) {
        return Math.abs(value) < threshold ? 0.0 : value;
    }

    @Override
    public void runOpMode() {
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        odo.setOffsets(-84, -168, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        telemetry.addData("Status", "Calibrating IMU... DO NOT MOVE ROBOT");
        telemetry.update();
        odo.resetPosAndIMU();
        sleep(500);

        telemetry.addData("Status", "Ready! Press Play.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            odo.update();

            Pose2D pos = odo.getPosition();

            // Apply deadbands (ignores noise under 1 mm/s or 0.5 deg/s)
            double velX = applyDeadband(odo.getVelX(DistanceUnit.MM), 1.0);
            double velY = applyDeadband(odo.getVelY(DistanceUnit.MM), 1.0);
            double velH = applyDeadband(odo.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES), 0.5);

            telemetry.addData("Device Status", odo.getDeviceStatus());
            telemetry.addData("Loop Rate", "%.0f Hz", odo.getFrequency());

            // Rounded to whole units to eliminate digit flicker
            telemetry.addLine("\n--- Position ---");
            telemetry.addData("X", "%.0f mm", pos.getX(DistanceUnit.MM));
            telemetry.addData("Y", "%.0f mm", pos.getY(DistanceUnit.MM));
            telemetry.addData("Heading", "%.0f deg", pos.getHeading(AngleUnit.DEGREES));

            telemetry.addLine("\n--- Velocity ---");
            telemetry.addData("Vel X", "%.0f mm/s", velX);
            telemetry.addData("Vel Y", "%.0f mm/s", velY);
            telemetry.addData("Vel H", "%.0f deg/s", velH);

            telemetry.addLine("\n--- Raw Encoders ---");
            telemetry.addData("Raw X Ticks", odo.getEncoderX());
            telemetry.addData("Raw Y Ticks", odo.getEncoderY());

            telemetry.update();
        }
    }
}
