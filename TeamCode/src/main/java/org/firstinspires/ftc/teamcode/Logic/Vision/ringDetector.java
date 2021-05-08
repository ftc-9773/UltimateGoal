package org.firstinspires.ftc.teamcode.Logic.Vision;


import android.util.Log;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.*;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

//For detecting skystones.
//call init, then enable, the getPosition to get the position. To stop looking at frames. call disable.
public class ringDetector extends OpenCvPipeline {
    static final String TAG = "DETECTOR";
    Mat yCbCrChan2Mat = new Mat();
    Mat thresholdMat = new Mat();
    Mat input = new Mat();
    Mat lastFrame = new Mat();
    Mat greyscale = new Mat();
    int framesSaved = 0;

    double oneval;
    double fourval;
    List<MatOfPoint> contoursList = new ArrayList();
    static boolean DEBUG = true;

    public enum rings {unknown, zero, one, four}

    rings number = rings.unknown;

    int threshold;
    // [x,y] in terms of %width and %height
    double[] one = new double[2];
    double[] four = new double[2];
    //double[] right = new double[2];
    int width, height;
    JsonReader reader;
    boolean isblue;


    public ringDetector() {
        this(false);
    }

    public ringDetector(boolean isblue) {
        super();
        this.isblue = isblue;
        refreshJson();
    }

    public rings getPosition() {
        return number;
    }

    public void saveLastFrame(){
        Log.d(TAG, "Saved frame " + lastFrame.toString());
        saveMatToDisk(lastFrame, "saved_frame_" + framesSaved);
        framesSaved++;
    }

    public void refreshJson() {
        reader = new JsonReader("detector");

        threshold = reader.getInt("detectThreshold");
        if (isblue) {
            one[0] = reader.getDouble("blueonex");
            one[1] = reader.getDouble("blueoney");
            four[0] = reader.getDouble("bluefourx");
            four[1] = reader.getDouble("bluefoury");


        } else {
            one[0] = reader.getDouble("redonex");
            one[1] = reader.getDouble("redoney");
            four[0] = reader.getDouble("redfourx");
            four[1] = reader.getDouble("redfoury");
            Log.d(TAG, "READ: mid[0]=" + one[0] + " mid[1]=" + one[1]);
            Log.d(TAG, "READ: left[0]=" + four[0] + " left[1]" + four[1]);
        }
    }

    @Override
    public void init(Mat firstFrame) {
        saveMatToDisk(firstFrame, "saved_frames_"+framesSaved);
        framesSaved++;
    }

    //Called each new frame.
    @Override
    public Mat processFrame(Mat inputFrame) {

        Mat yCbCrChan2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Mat input;
        input = inputFrame;
        Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(yCbCrChan2Mat, greyscale, 1);
        Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2); //Flatten into 2d mat which is blue difference.

        Imgproc.threshold(yCbCrChan2Mat, thresholdMat, threshold, 255, Imgproc.THRESH_BINARY_INV);
        width = yCbCrChan2Mat.cols();
        height = yCbCrChan2Mat.rows();

        if (DEBUG) {
            Log.d(TAG, "width " + width);
            Log.d(TAG, "height " + height);
            Log.d(TAG, "thresholdmatdims " + yCbCrChan2Mat.dims());
        }

        int oney = (int) (this.one[1] * height);
        int onex = (int) (this.one[0] * width);
        int foury = (int) (this.four[1] * height);
        int fourx = (int) (this.four[0] * width);

        fourval = yCbCrChan2Mat.get(foury, fourx)[0];
        oneval = yCbCrChan2Mat.get(oney, onex)[0];

        if (DEBUG) Log.d(TAG, " len " + yCbCrChan2Mat.get(foury, fourx).length);
        if (DEBUG) Log.d(TAG, " len " + yCbCrChan2Mat.get(oney, onex).length);

        if (fourval < 100) {
            number = rings.four;
        } else if (oneval < 110) {
            number = rings.one;
        } else {
            number = rings.zero;
        }
        if (DEBUG) Log.d(TAG, "four " + fourval);
        if (DEBUG) Log.d(TAG, "one " + oneval);

        Point midPoint = new Point(onex, oney);
        Point leftPoint = new Point(fourx, foury);

        Imgproc.circle(input, midPoint, 5, new Scalar(255, 0, 0), 1);
        Imgproc.circle(input, leftPoint, 5, new Scalar(0, 255, 0), 1);
        yCbCrChan2Mat.copyTo(lastFrame);

        return input;
    }
}
