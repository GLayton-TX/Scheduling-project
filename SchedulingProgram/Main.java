package SchedulingProgram;

import SchedulingProgram.Utilities.SqlDbHelper;
import SchedulingProgram.View_Controller.LogInScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_Controller/LandingPage.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_Controller/Testing.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_Controller/LogInScreen.fxml")); // actual start screen
        primaryStage.setTitle("Log in");
        if (System.getProperty("user.language").equals("fr")) primaryStage.setTitle("Connexion");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) throws Exception {
        SqlDbHelper.openConnection();

        launch(args);

        SqlDbHelper.closeConnection();

    }
}
