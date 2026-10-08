// code I copied from somewhere to test the camera

package org.firstinspires.ftc.QCTruongCode.TestingFolder;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp(name = "Camera Test OpMode", group = "Test")
public class CameraTest extends LinearOpMode {

    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        // Build the bare-minimum VisionPortal stream using the configured webcam name
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // Must match configuration name
                .build();

        // Check if the camera is opening/streaming
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Camera Status", visionPortal.getCameraState().toString());
            telemetry.addData("Action Required", "Check Driver Station menu for 'Camera Stream'");
            telemetry.update();
            sleep(50);
        }

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Running. Camera is streaming.");
            telemetry.addData("FPS", visionPortal.getFps());
            telemetry.update();
            sleep(100);
        }

        // Clean up and close the stream
        visionPortal.close();
    }
}
