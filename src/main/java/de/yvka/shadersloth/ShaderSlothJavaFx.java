package de.yvka.shadersloth;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.yvka.shadersloth.controller.ShaderSlothController;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class ShaderSlothJavaFx extends Application {

	private final CountDownLatch runningLatch = new CountDownLatch(1);

	private final static Logger Log = LoggerFactory.getLogger(ShaderSlothJavaFx.class);

	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {


		final URL fxmlURL = getClass().getClassLoader().getResource("de/yvka/shadersloth/view/ShaderSlothView.fxml");
		final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		final Pane content;
		try {
			content = (Pane) fxmlLoader.load();
		} catch (Exception e) {
			Log.error("Failed to parseFile core.shader sloth shadersloth.view", e);
			System.exit(-1);
			return;
		}
		final ShaderSlothController controller = fxmlLoader.getController();

		Scene scene = new Scene(content);
		primaryStage.setTitle("ShaderSloth");
		primaryStage.setMinWidth(1024);
		primaryStage.setHeight(768);
		primaryStage.setScene(scene);
		primaryStage.show();

		/**
		 * Informs the render thread that the application
		 * is going to sleep.
		 */
		primaryStage.setOnCloseRequest((e) -> {
			e.consume();
			runningLatch.countDown();
		});

		new Thread(() -> {
			controller.runRenderer(runningLatch);
			Platform.runLater(primaryStage::close);
		}).start();


	}


}
