package org.firstinspires.ftc.teamcode.Logic;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Components.actuators.Encoder;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.opencv.core.Mat;

public class DriveUtils {
    final static String TAG = "DriveUtils";
    PIDController headingPID;
    PIDController rotPID;
    PIDController drivePID;
    long[] lastEncoders;

    double trackedHeading;
    Encoder xEncoder, yEncoder;

    static private final double     COUNTS_PER_MOTOR_REV    = 288;    //
    static private final double     WHEEL_TURNS_PER_MOTOR_REV = 36.0 / 30.0;
    static private final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static private final double     ROBOT_DIAMETER_INCHES   = 7.322 * 2;
    public static final double      COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV*WHEEL_TURNS_PER_MOTOR_REV ) / (WHEEL_DIAMETER_INCHES * Math.PI);
    static private final double     INCHES_PER_ENCODER_COUNT = 2 * Math.PI / 2048;
    double rotTol, rotMinPow, rotExitSpeed;
    double minDistPow = 0.1;
    double maxTurnPower;

    public DriveUtils() {
        JsonReader reader = new JsonReader("auto");
        double rkp, rki, rkd;
        double hkp, hki, hkd;
        double dkp, dki, dkd;
        rkp = reader.getDouble("rkp");
        rki = reader.getDouble("rki");
        rkd = reader.getDouble("rkd");
        hkp = reader.getDouble("hkp");
        hki = reader.getDouble("hki");
        hkd = reader.getDouble("hkd");
        dkp = reader.getDouble("dkp");
        dki = reader.getDouble("dki");
        dkd = reader.getDouble("dkd");

         xEncoder = new Encoder("hookMotor");
        yEncoder = new Encoder("launchMotor1");

        rotExitSpeed = reader.getDouble("rotExitSpeed", 0.5);
        rotTol = reader.getDouble("rotTol", 0.1);
        rotMinPow = reader.getDouble("rotMinPow", 0.1);
        maxTurnPower = reader.getDouble("maxRotPow", 0.7);

        rotPID = new PIDController(rkp, rki, rkd);
        drivePID = new PIDController(dkp, dki, dkd);
        headingPID = new PIDController(hkp, hki, hkd);
        trackedHeading = 0;
        //Globals.drivebase.fieldCentric = true;
    }

    public void setTrackedHeading(double heading){
        trackedHeading = heading;
    }

    public void maintainHeading(double x, double y) {
        double currHeading = Globals.gyro.getHeading();
        double orientation = trackedHeading;
        //Log.d(TAG, "Target Heading: a" + orientation);
        //orientation = getClosestRightAnge(orientation); //We're always driving at right angles anyway for now.
        double error = setOnNegToPosPi(orientation - currHeading);
        //Log.d(TAG, "Error: " + error);
        Log.d(TAG, "m_Target Heading: " + orientation);
        Log.d(TAG, "m_Heading: " + currHeading);
        Log.d(TAG, "mError: " + error);
        Log.d(TAG, "m_xy " + x + "," + y);
        double correction = headingPID.getPIDCorrection(error);
        Log.d(TAG, "_MData," + headingPID.prevError + "," + headingPID.derivative + "," + correction);
        if (Math.abs(x) < minDistPow && Math.abs(x) > 0.001) x = Math.signum(x) * minDistPow;
        if (Math.abs(y) < minDistPow && Math.abs(y) > 0.001) y = Math.signum(y) * minDistPow;
        Globals.drivebase.driveSimple(x, y, correction);
    }

    public void rotateToHeading(double goalHeading) {
        final double targetAngleRad = Math.toRadians(goalHeading);
        trackedHeading = goalHeading;
        // For calculating rotational speed:
        double lastHeading;
        double currentHeading = Globals.gyro.getHeading();
        double lastTime;
        double currentTime = System.currentTimeMillis();
        double lastError = 0.0;

        // For turning PID
        double error;

        boolean firstTime = true;
        boolean forceNewReading = true;
        while (!Globals.opMode.isStopRequested()) {

            // update time and headings:
            currentHeading = Globals.gyro.getHeading(forceNewReading);
            forceNewReading = !forceNewReading;

            lastTime = currentTime;
            currentTime = System.currentTimeMillis();

            error = setOnNegToPosPi(targetAngleRad - currentHeading);
            double rotation = rotPID.getPIDCorrection(error);

            // may add this in if dt is too weak
            if (rotation > 0.0005) {
                rotation += rotMinPow;
            } else if (rotation < -0.0005) {
                rotation = -rotMinPow;
            } else {
                rotation = 0;
                //return;
            }

            if (rotation > maxTurnPower)
                rotation = maxTurnPower;
            else if (rotation < -maxTurnPower)
                rotation = -maxTurnPower;


            Globals.drivebase.driveSimple(0.0, 0, rotation);

            // Check to see if it's time to exit
            // Calculate speed
            double speed;
            if (currentTime == lastTime || firstTime) {
                speed = 0.003;
            } else {
                speed = Math.abs(error - lastError) / (currentTime - lastTime);
            }
            lastError = error;
            Log.d(TAG, "RotationSpeed: " + speed);
            Log.d(TAG, "Error: " + error);
            Log.d(TAG, "dt: " + (currentTime - lastTime));
            if (Math.abs(error) < Math.abs(rotTol) && speed < rotExitSpeed) {
                break;
            }
            firstTime = false;
        }
        Globals.drivebase.driveSimple(0, 0, 0);
        rotPID.resetPID();
    }

    private double setOnNegToPosPi(double num) {
        while (num > Math.PI) {
            num -= 2 * Math.PI;
        }
        while (num < -Math.PI) {
            num += 2 * Math.PI;
        }
        return num;
    }

    public void pidDrive(double x, double y) {
        Globals.drivebase.fieldCentric = true;
        double mag = Math.sqrt(x * x + y * y);
        double ang = Math.atan2(y, x);
        double lastTime, time;
        double vel;
//        if (x < 0){
//            ang += Math.PI;
//        }
        double error = mag;
        double speed = drivePID.getPIDCorrection(error);
        if (speed > 0.5) {
            speed = 0.5;
        } else if (speed < -0.5){
            speed = -0.5;
        }
        lastTime = System.currentTimeMillis();
        Globals.drivebase.driveSimple(Math.cos(ang) * speed, -Math.sin(ang) * speed, 0);
        lastEncoders = Globals.drivebase.getEncoders();
        Log.d("DriveUtil", "Driving " + x + " " + y + ", ang is " + ang + " and starting speed " + speed);
        //trackedHeading = Globals.gyro.getHeading();
        while (!Globals.opMode.isStopRequested()){
            error = mag - getDistTravelled();
            Log.d(TAG,  "Drive Error " + error);
            vel = getDistTravelled() / (System.currentTimeMillis() - lastTime) / 1000;
            lastTime = System.currentTimeMillis();
            if (error < 0.5 && vel < 0.5) {
                break;
            }
            speed = drivePID.getPIDCorrection(error);
            if (speed > 0.7){
                speed = 0.7;
            } else if (speed < -0.7){
                speed = -0.7;
            }
            Log.d(TAG, "Speed: " + speed);
            maintainHeading(Math.cos(ang) * speed, -Math.sin(ang) * speed);

        }
        Globals.drivebase.driveSimple(0,0,0);

    }

    public double getClosestRightAnge(double ang){
        double[] list = { - Math.PI / 2 * 3, -Math.PI / 2, 0, Math.PI / 2, Math.PI, Math.PI * 3 / 2};
        double num = ang, dist = 1000000000; //INF
        for (int i = 0; i < list.length; i++ ){
            if (Math.abs(list[i] - ang) < dist){
                num = list[i];
                dist = Math.abs(list[i] - ang);
            }
        }
        return num;
    }
    public double getDistTravelled (){
        long[] encoders = Globals.drivebase.getEncoders();
        double dy = (encoders[0] - lastEncoders[0]) + (encoders[2] - lastEncoders[2]) - (encoders[1] - lastEncoders[1]) - (encoders[3] - lastEncoders[3]);
        dy = dy / 4;
        dy = dy / (28 * 20 * 30 / 36.0) * 4 * Math.PI;
        double dx = (encoders[0] - lastEncoders[0]) - (encoders[2] - lastEncoders[2]) + (encoders[1] - lastEncoders[1]) - (encoders[3] - lastEncoders[3]);
        dx = dx / 4;
        dx = dx / (28 * 20 * 30 / 36.0) * 4 * Math.PI ;
        return Math.sqrt(dx * dx + dy * dy);
    }

}