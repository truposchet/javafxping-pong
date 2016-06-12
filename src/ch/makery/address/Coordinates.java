package ch.makery.address;

/**
 * Created by truposchet on 07.06.16.
 */
public class Coordinates {
    boolean moveRight, moveDown;
    double leftPaddleCur, leftPaddlePrev, rightPaddleCur, rightPaddlePrev;
    Coordinates(boolean moveDown, boolean moveRight, double leftPaddleCur, double leftPaddlePrev, double rightPaddleCur, double rightPaddlePrev){
        this.leftPaddleCur = leftPaddleCur;
        this.leftPaddlePrev = leftPaddlePrev;
        this.rightPaddleCur = rightPaddleCur;
        this.rightPaddlePrev = rightPaddlePrev;
        this.moveRight = moveRight;
        this.moveDown = moveDown;
    }
}
