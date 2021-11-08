package drawingbot;

import javafx.application.Preloader;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.animation.Animation;
import javafx.geometry.Insets;

public class SplashPreloader {
	
	private Label progressLabel;
	private Stage primaryStage;
	private Stage secondaryStage;
	private SimpleStringProperty lastNotification;
	private FadeTransition fadeaway;
	volatile private boolean vanishedFlag=false;
	public void start(){
		StackPane stackPane=new StackPane();
		try{
			primaryStage=new Stage();
		} catch(Exception e){
			e.printStackTrace();
		}
		ImageView imageView = new ImageView(SplashPreloader.class.getResource("/images/splash.png").toString());
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(800);
		progressLabel = new Label();
		stackPane.getChildren().addAll(imageView, progressLabel);
		stackPane.setAlignment(progressLabel, Pos.BOTTOM_LEFT);
		stackPane.setMargin(progressLabel, new Insets(0,0,50,100));
		stackPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0);");
		FadeTransition fadein = new FadeTransition(Duration.millis(3000),stackPane);
		fadeaway = new FadeTransition(Duration.millis(3000),stackPane);
		fadein.setFromValue(0.0);
		fadein.setToValue(1.0);
		fadeaway.setFromValue(1.0);
		fadeaway.setToValue(0.0);
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setOpacity(0);
		secondaryStage=new Stage();
		secondaryStage.initOwner(primaryStage);
		secondaryStage.initStyle(StageStyle.TRANSPARENT);
		Scene scene=new Scene(stackPane);
		scene.setFill(Color.TRANSPARENT);
		secondaryStage.setScene(scene);
		lastNotification=new SimpleStringProperty();
		progressLabel.textProperty().bind(lastNotification);
		progressLabel.setTextFill(Color.WHITE);
		fadeaway.setOnFinished(e->{vanishedFlag=true; primaryStage.hide();});
		primaryStage.setAlwaysOnTop(true);
		fadein.play();
		Platform.runLater( ()->{primaryStage.show(); secondaryStage.show();} );
	}
	public void notify(String s){
		Platform.runLater(()->{
		lastNotification.set(s);
		Platform.requestNextPulse();
		});
	}
	public void vanish(){
		Platform.runLater(()->fadeaway.play());
		while(!vanishedFlag);
	}
}