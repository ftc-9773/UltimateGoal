package org.firstinspires.ftc.teamcode.Utilities;
/**
 * Class determines whether a button is pressed once or held
 * @author robocracy
 */
public class Button {
    private boolean currVal; //Current state of the button (On || Off)
    private boolean lastVal; //Previous state of the button

    public Button() {
        this.currVal = this.lastVal = false;
    }

    /**
     * Updates the state of the button
     * @param newButtonValue boolean determining the new state of the button
     * */
    public void recordNewValue(boolean newButtonValue) {
        this.lastVal = this.currVal;
        this.currVal = newButtonValue;
    }
    /**
     * Determine whether the button was off and is now on (it has been pressed)
     * @return boolean
     * */
    public boolean isJustOn() { return (this.currVal && ! this.lastVal); }

    /**
     * Determine whether the button was on and is now off (it has been released)
     * @return boolean
     * */
    public boolean isJustOff() { return (! this.currVal && this.lastVal); }

    /**
     * Determines if the button is being pressed
     * @return state of the button
     * */
    public boolean isOn() { return this.currVal; }
    /**
     * Determines if the button is not being pressed
     * @return state of button
     * */
    public boolean isOff() { return ! this.currVal; }
}