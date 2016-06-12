package ch.makery.address;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.Random;

/**
 * Game Main class
 */
public class PinPong  {
    private static final Object criticalZone = new Object();
    private static Scene scene;
    private EngineThread pongAnimation = new EngineThread();
    private RightPlayer rightPlayer = new RightPlayer();
    private ReplayRead replayRead = new ReplayRead();
    private ReplayWrite replayWrite = new ReplayWrite();
    private RightPaddle rightPaddleThr = new RightPaddle();
    private LeftPaddle leftPaddleThr = new LeftPaddle();
    private DoubleProperty centerX = new SimpleDoubleProperty();
    private DoubleProperty centerY = new SimpleDoubleProperty();
    private DoubleProperty leftPaddleY = new SimpleDoubleProperty();
    private DoubleProperty rightPaddleY = new SimpleDoubleProperty();
    private Circle ball;
    private Rectangle leftPaddle;
    private Rectangle rightPaddle;
    private Rectangle topWall;
    private Rectangle rightWall;
    private Rectangle leftWall;
    private Rectangle bottomWall;
    private boolean movingRight;
    private boolean movingDown;
    private Main father;
    private BitSet keyboardBitSet = new BitSet();
    private KeyCode upKey = KeyCode.K;
    private KeyCode downKey = KeyCode.M;
    private KeyCode feFive = KeyCode.F5;
    private int leftWallgoal=0;
    private int rightWallgoal=0;
    private double leftPaddlePrev;
    private double rightPaddlePrev;

    /**
     * Values initialize method
     */
    private void initialize() {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(10+1);
        if (number%2==0)
            movingRight=true;
        else movingRight=false;
        number = rnd.nextInt(10+1);
        if (number%2==0)
            movingDown=false;
        else movingDown=true;
        centerX.setValue(350);
        centerY.setValue(300);
        leftPaddleY.setValue(265);
        rightPaddleY.setValue(265);
    }

    /**
     * Check collision method
     */
    private void checkForCollision() {
        if (ball.intersects(rightWall.getBoundsInLocal())) {
            rightWallgoal++;
            initialize();
        }
        else if (
        ball.intersects(leftWall.getBoundsInLocal())){
            leftWallgoal++;
            initialize();
        }
        else if (ball.intersects(bottomWall.getBoundsInLocal()) ||
                ball.intersects(topWall.getBoundsInLocal())) {
            movingDown = !movingDown;
        }
        else if(leftWallgoal ==3 || rightWallgoal==3)Platform.exit();
    }

    /**
     * Thread exit method
     */
    void threadsExit() {
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        pongAnimation.interrupt();
        rightPlayer.interrupt();
        replayRead.interrupt();
        replayWrite.interrupt();
        leftPaddleThr.interrupt();
        rightPaddleThr.interrupt();
        try {
            rightPaddleThr.join();
            leftPaddleThr.join();
            pongAnimation.join();
            rightPlayer.join();
            replayRead.join();
            replayWrite.join();
        } catch (InterruptedException ex) {
            System.out.println("KJGORJGOR");
        }
    }

    /**
     * Create game window method
     * @param father Application descriptor
     * @return scene Game window
     */
    Scene getScene(Main father) {
        this.father = father;
        initialize();
        Pane root = new Pane();
        scene = new Scene(root, 700, 600, Color.DARKSLATEGRAY);
        ball = new Circle(10.0);
        ball.setFill(Color.BLACK);
        ball.setVisible(true);
        ball.setCenterY(centerY.getValue());
        ball.setCenterX(centerX.getValue());
        topWall = new Rectangle(0, 0, 700, 1);
        leftWall = new Rectangle(0, 0, 1, 600);
        rightWall = new Rectangle(700, 0, 1, 600);
        bottomWall = new Rectangle(0, 600, 700, 1);
        leftPaddle = new Rectangle();
        leftPaddle.setX(20);
        leftPaddle.setY(leftPaddleY.getValue());
        leftPaddle.setHeight(70);
        leftPaddle.setWidth(10);
        leftPaddle.setFill(Color.LIGHTBLUE);
        rightPaddle = new Rectangle();
        rightPaddle.setX(670);
        rightPaddle.setY(rightPaddleY.getValue());
        rightPaddle.setHeight(70);
        rightPaddle.setWidth(10);
        rightPaddle.setFill(Color.LIGHTBLUE);
        MenuButtons.MenuItem game = new MenuButtons.MenuItem("Game");
        MenuButtons.MenuItem exitGame= new MenuButtons.MenuItem("Exit");
        MenuButtons.MenuItem replayButton = new MenuButtons.MenuItem("Replay");
        MenuButtons.MenuItem savedButton = new MenuButtons.MenuItem("Saves");
        MenuButtons.MenuItem sort = new MenuButtons.MenuItem("Sort");
        MenuButtons.MenuItem stat = new MenuButtons.MenuItem("Statistics");
        MenuButtons.SubMenu mainMenu = new MenuButtons.SubMenu(
                game, replayButton, savedButton, sort, stat, exitGame
                );
        MenuButtons.MenuItem brainBut = new MenuButtons.MenuItem("Brain");
        MenuButtons.MenuItem autoplayButton = new MenuButtons.MenuItem("Autoplay");
        MenuButtons.MenuItem backButton = new MenuButtons.MenuItem("Back");
        MenuButtons.SubMenu gameMenu = new MenuButtons.SubMenu(
                brainBut, autoplayButton, backButton
        );
        MenuButtons.MenuItem easyBut = new MenuButtons.MenuItem("Easy");
        MenuButtons.MenuItem mediumBut = new MenuButtons.MenuItem("Medium");
        MenuButtons.MenuItem hardBut = new MenuButtons.MenuItem("Hard");
        MenuButtons.MenuItem backButton1 = new MenuButtons.MenuItem("Back");
        MenuButtons.SubMenu difficultyMenu = new MenuButtons.SubMenu(
                easyBut, mediumBut, hardBut, backButton1
        );
        MenuButtons.MenuBox menuBoox = new MenuButtons.MenuBox(mainMenu);
        backButton.setOnMouseClicked(event -> menuBoox.setSubMenu(mainMenu));
        brainBut.setOnMouseClicked(event -> menuBoox.setSubMenu(difficultyMenu));
        backButton1.setOnMouseClicked(event -> menuBoox.setSubMenu(gameMenu));
        game.setOnMouseClicked(event -> menuBoox.setSubMenu(gameMenu));
        exitGame.setOnMouseClicked(event -> Platform.exit());
        sort.setOnMouseClicked(event -> {
            CreateCareer a = new CreateCareer();
            a.filesSort();
        });
        stat.setOnMouseClicked(event -> {
            GoalsCreate a = new GoalsCreate();
            a.goalsStat();
        });
        easyBut.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            pongAnimation.setDifficulty(1);
            rightPlayer.start();
            leftPaddleThr.start();
            leftPaddleThr.setDifficulty(4);
            pongAnimation.start();
            replayWrite.start();
        });
        mediumBut.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            pongAnimation.setDifficulty(1);
            rightPlayer.start();
            leftPaddleThr.start();
            leftPaddleThr.setDifficulty(3);
            pongAnimation.start();
            replayWrite.start();
        });
        hardBut.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            pongAnimation.setDifficulty(1);
            rightPlayer.start();
            leftPaddleThr.start();
            leftPaddleThr.setDifficulty(2);
            pongAnimation.start();
            replayWrite.start();
        });
        savedButton.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            String fileName = String.valueOf(file);
            try {DataInputStream dos = new DataInputStream(new FileInputStream(fileName));
                ball.setCenterX(dos.readDouble());
                ball.setCenterY(dos.readDouble());
                rightPaddle.setY(dos.readDouble());
                leftPaddle.setY(dos.readDouble());
                rightWallgoal = dos.readInt();
                leftWallgoal = dos.readInt();
            }catch (IOException ex){}
            pongAnimation.start();
            leftPaddleThr.setDifficulty(2);
            leftPaddleThr.start();
            rightPlayer.start();
            replayWrite.start();
        });
        replayButton.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            String fileName = String.valueOf(file);
            replayRead.setFileName(fileName);
            replayRead.start();
        });
        autoplayButton.setOnMouseClicked(event -> {
            menuBoox.setVisible(false);
            pongAnimation.setDifficulty(1);
            pongAnimation.start();
            leftPaddleThr.setDifficulty(2);
            leftPaddleThr.start();
            rightPaddleThr.setDifficulty(2);
            rightPaddleThr.start();
            replayWrite.start();
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        root.getChildren().addAll(ball, topWall, leftPaddle, leftWall,
                rightPaddle, rightWall, bottomWall, menuBoox
                );
        return scene;
    }

    /**
     * Replay Writer thread
     */
    private class ReplayWrite extends Thread {
        public void run() {
            Double[] buff=new Double[4];
            try {
                Date date = new Date() ;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("/home/truposchet/Документы/pong/Replays/"
                        + dateFormat.format(date) + ".bin"));
                do {
                    synchronized (criticalZone) {
                        buff[0] = ball.getCenterX();
                        buff[1] = ball.getCenterY();
                        buff[2] = rightPaddle.getY();
                        buff[3] = leftPaddle.getY();
                    }
                    for (Double i : buff) {
                        dos.writeDouble(i);
                    }
                    try {
                        sleep(17);
                    } catch (InterruptedException ex) {
                        return;
                    }
                } while (true);
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Class which create elements for Scala Pseudo Notation
     */

    public void PsevdoNot(){
        ArrayList<Coordinates>  coordArr = new ArrayList<>();
        ScalaNotationPrinter snp = new ScalaNotationPrinter();
        Coordinates coordinates = new Coordinates(movingDown, movingRight, leftPaddle.getY(),
                leftPaddlePrev, rightPaddle.getY(), rightPaddlePrev);
        coordArr.add(coordinates);
        snp.notPrint(coordinates);
    }

    /**
     * Replay reader thread
     */
    private class ReplayRead extends Thread {
        private String fileName;
        void setFileName(String fileName1){this.fileName = fileName1;}
        public void run() {
            try {

                DataInputStream dos = new DataInputStream(new FileInputStream(fileName));
                do {
                    synchronized (criticalZone) {
                        ball.setCenterX(dos.readDouble());
                        ball.setCenterY(dos.readDouble());
                        rightPaddle.setY(dos.readDouble());
                        leftPaddle.setY(dos.readDouble());
                    }
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        return;
                    }
                } while (dos.available() != 0);
                if (dos.available() == 0) {
                    father.forceKill();
                }
            } catch (IOException ex) {
            }
        }
    }


    /**
     * Game Engine thread
     */
    private class EngineThread extends Thread {
        private final int minFrequency = 34;
        private int difficulty = 1;

        void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public void run() {
            while (true) {
                checkForCollision();
                PsevdoNot();
                int horzPixels = movingRight ? 6 : -6;
                int vertPixels = movingDown ? 6 : -6;
                ball.setCenterX(centerX.getValue());
                ball.setCenterY(centerY.getValue());
                centerX.setValue(centerX.getValue() + horzPixels);
                centerY.setValue(centerY.getValue() + vertPixels);
                if (ball.intersects(rightPaddle.getBoundsInParent()) && movingRight) {
                    movingRight = !movingRight;
                }
                synchronized (criticalZone) {
                    ball.setCenterX(centerX.getValue());
                    ball.setCenterY(centerY.getValue());
                }
                try {
                    sleep(minFrequency*difficulty);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }

    /**
     * Left Paddle Thread class
     */
    private class LeftPaddle extends Thread {
        private final int minFrequency = 30;
        private int difficulty = 4;

        void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public void run() {
            while (true) {
                if (ball.intersects(leftPaddle.getBoundsInParent()) && !movingRight) {
                    movingRight = !movingRight;
                }

                if ((ball.getCenterY() - 35) > leftPaddle.getY() && leftPaddle.getBoundsInParent().getMaxY() < 593) {
                    leftPaddlePrev = leftPaddle.getY();
                    leftPaddleY.setValue(leftPaddleY.getValue() + 8);
                    synchronized (criticalZone){
                    leftPaddle.setY(leftPaddleY.getValue());}
                } else {
                    if ((ball.getCenterY() - 35) < leftPaddle.getY() && leftPaddle.getBoundsInParent().getMinY() > 7) {
                        leftPaddlePrev = leftPaddle.getY();
                        leftPaddleY.setValue(leftPaddleY.getValue() - 8);
                        synchronized (criticalZone) {
                            leftPaddle.setY(leftPaddleY.getValue());
                        }
                    }
                }

                try {
                    sleep(minFrequency*difficulty);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }

    /**
     * Right Paddle bot
     */

    private class RightPaddle extends Thread {
        private final int minFrequency = 28;
        private int difficulty = 4;

        void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public void run() {
            while (true) {
                if (ball.intersects(rightPaddle.getBoundsInParent()) && movingRight) {
                    movingRight = !movingRight;
                }

                if ((ball.getCenterY() - 35) > rightPaddle.getY() && rightPaddle.getBoundsInParent().getMaxY() < 593) {
                    rightPaddlePrev = rightPaddle.getY();
                    rightPaddleY.setValue(rightPaddleY.getValue() + 7);
                    rightPaddle.setY(rightPaddleY.getValue());
                } else if ((ball.getCenterY() - 35) < rightPaddle.getY() && rightPaddle.getBoundsInParent().getMinY() > 7) {
                    rightPaddlePrev = rightPaddle.getY();
                    rightPaddleY.setValue(rightPaddleY.getValue() - 7);
                    rightPaddle.setY(rightPaddleY.getValue());
                }

                try {
                    sleep(minFrequency*difficulty);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }


    /**
     * Control player thread
     */
    private class RightPlayer extends Thread {
        public void run() {
            while (true) {
                if (isMoveDown()) {
                    synchronized (criticalZone) {
                        if (rightPaddle.getBoundsInParent().getMinY() <= 7 ||
                                (!rightPaddle.getBoundsInParent().intersects(topWall.getBoundsInLocal())
                                        && rightPaddle.getBoundsInParent().getMaxY() < 593)) {
                                    rightPaddlePrev = rightPaddle.getY();
                                    rightPaddleY.setValue(rightPaddleY.getValue() + 14);
                                    rightPaddle.setY(rightPaddleY.getValue());
                                }
                    }
                } else if (isMoveUp()) {
                    synchronized (criticalZone){
                        if (rightPaddle.getBoundsInParent().getMaxY() >= 593 ||
                                (!rightPaddle.getBoundsInParent().intersects(bottomWall.getBoundsInLocal())
                                        && rightPaddle.getBoundsInParent().getMinY() > 7)) {
                                    rightPaddlePrev = rightPaddle.getY();
                                    rightPaddleY.setValue(rightPaddleY.getValue() - 14);
                                    rightPaddle.setY(rightPaddleY.getValue());
                                }
                    }
                } else if (isFeFive()) {

                    Date date = new Date() ;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

                    try {
                        DataOutputStream dos = new DataOutputStream(new FileOutputStream("/home/truposchet/Документы/pong/Saves/"
                                + dateFormat.format(date) + ".bin"));
                        dos.writeDouble(ball.getCenterX());
                        dos.writeDouble(ball.getCenterY());
                        dos.writeDouble(rightPaddle.getY());
                        dos.writeDouble(leftPaddle.getY());
                        dos.writeInt(rightWallgoal);
                        dos.writeInt(leftWallgoal);
                    }catch (IOException ex) {
                    }
                }
                try {
                    sleep(50);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }

    private EventHandler<KeyEvent> keyPressedEventHandler = event -> {

        keyboardBitSet.set(event.getCode().ordinal(), true);

    };

    private EventHandler<KeyEvent> keyReleasedEventHandler = event -> {

        keyboardBitSet.set(event.getCode().ordinal(), false);

    };

    /**
     * Class up key controller
     * @return true or false
     */
    private boolean isMoveUp() {
        return keyboardBitSet.get(upKey.ordinal()) && !keyboardBitSet.get(downKey.ordinal());
    }

    /**
     * Class down key controller
     * @return true or false
     */
    private boolean isMoveDown() {return keyboardBitSet.get(downKey.ordinal()) && !keyboardBitSet.get(upKey.ordinal());}

    /**
     * Class save key controller
     * @return true or false
     */
    private boolean isFeFive() {return keyboardBitSet.get(feFive.ordinal());}
}