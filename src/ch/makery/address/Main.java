package ch.makery.address;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


public class Main extends Application {
    private PinPong pinPong;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Application start method
     * @param stage root stage
     */

    public void start(Stage stage)  {
        pinPong = new PinPong();
        stage.setScene(pinPong.getScene(this));
        stage.setResizable(false);
        stage.setTitle("Game");
        stage.show();
    }

    /**
     * Thread stop method
     */


    public void stop() {
        pinPong.threadsExit();
    }

    /**
    * Interface for close application from non gui thread
    * */
    public void forceKill() {
        Platform.exit();
    }

}