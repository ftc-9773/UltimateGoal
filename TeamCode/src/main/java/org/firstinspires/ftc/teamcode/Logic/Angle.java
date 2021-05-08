package org.firstinspires.ftc.teamcode.Logic;

public class Angle {
    double internal_angle_radians; // Unbounded
    boolean negative;

    public Angle(double val){
        internal_angle_radians = val;
        negative = val < 0;
    }

    public double getRealValue(){
        return internal_angle_radians;
    }

    public double difference(double ang){
        return differance(new Angle(ang));
    }

    public double differance(Angle ang){
        return 0;
    }

    public double setOnNegPosPI(){
        double output = internal_angle_radians;
        while (output > Math.PI) {
            output -= 2 * Math.PI;
        }
        while (output < -Math.PI) {
            output += 2 * Math.PI;
        }
        return output;
    }

    public double getDegreesValue(){
        return internal_angle_radians * 180 / Math.PI;
    }
}
