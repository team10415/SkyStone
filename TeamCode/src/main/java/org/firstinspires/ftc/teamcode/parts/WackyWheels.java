package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.util.glob.Shared;

import java.util.Arrays;
import java.util.List;

public class WackyWheels extends MoldovanMotors {

    public enum Side {
        LEFT, RIGHT
    }

    //the stored velocities
    private double velocityX = 0;
    private double velocityY = 0;
    private double velocityR = 0;

    private int xNegator = 1;
    /**
     * @param motors          the motors
     */
    public WackyWheels(List<DcMotor> motors) {
        super(motors);
    }

    /**
     * defines the side on which the robot is
     *
     * @param side
     */
    public void setSide(Side side) {
        if(side == Side.LEFT) {
            xNegator = 1;
        } else {
            xNegator = -1;
        }
    }

    /**
     * run the motors based on the xyr velocities
     * normalize if any motor power is too large
     */
    public void moveMecanum() {
        //calculate motor powers

        double fl = velocityR + velocityX + velocityY;
        double fr = velocityR - velocityX - velocityY;
        double bl = velocityR - velocityX + velocityY;
        double br = velocityR + velocityX - velocityY;

        Shared.telemetry.addData("fl",fl);
        Shared.telemetry.addData("fr",fr);
        Shared.telemetry.addData("bl",bl);
        Shared.telemetry.addData("br",br);

        move(Arrays.asList(fl, fr, bl, br));
    }

    /**
     * @param velocityX the x velocity
     */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * @param velocityY the y velocity
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * @param velocityR the rotational velocity
     */
    public void setVelocityR(double velocityR) {
        this.velocityR = velocityR;
    }

    /**
     * set the x and y velocities at the same time
     *
     * @param velocityX the x velocity
     * @param velocityY the y velocity
     */
    public void setVelocityXY(double velocityX, double velocityY) {
        setVelocityX(velocityX);
        setVelocityY(velocityY);
    }

    /**
     * set the x, y, and rotational velocities at the same time
     *
     * @param velocityX the x velocity
     * @param velocityY the y velocity
     * @param velocityR the rotational velocity
     */
    public void setVelocityXYR(double velocityX, double velocityY, double velocityR) {

        setVelocityX(velocityX);
        setVelocityY(velocityY);
        setVelocityR(velocityR);
    }

    /**
     * rotates amnt of degrees
     *
     * @param deg amount of rotation
     */
    public void rotate(double deg) {
        double ticks90Degs = 1;
        double numOf90Degs = deg/90;
        int newTicks       = (int)(ticks90Degs * numOf90Degs);

        int motorNum = 0;
        for(DcMotor motor : motors) {
            int ticks = motor.getCurrentPosition();

            if(motorNum % 2 == 0)
                motor.setTargetPosition(ticks+newTicks);
            else
                motor.setTargetPosition(ticks-newTicks);

            motorNum++;
        }
    }

    /**
     * amount of ticks to move by
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {

        int fl =  y+xNegator*x;
        int fr =  y-xNegator*x;
        int bl =  y-xNegator*x;
        int br =  y+xNegator*x;

        List<Integer> amnts = Arrays.asList(fl, fr, bl, br);

        int motorNum = 0;
        for(DcMotor motor : motors) {
            int ticks = motor.getCurrentPosition();

            motor.setTargetPosition(ticks+amnts.get(motorNum));

            motorNum++;
        }
    }

    public void stop() {
        setVelocityXYR(0,0,0);
        super.stop();
    }
}