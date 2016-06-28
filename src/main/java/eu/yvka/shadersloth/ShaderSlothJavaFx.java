package eu.yvka.shadersloth;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.yvka.shadersloth.controller.ShaderSlothController;

public class ShaderSlothJavaFx extends Application {

	private final static Logger Log = LoggerFactory.getLogger(ShaderSlothJavaFx.class);
	private ShaderSlothController controller;

	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
		controller = new ShaderSlothController(primaryStage);
		controller.show();
	}
}
