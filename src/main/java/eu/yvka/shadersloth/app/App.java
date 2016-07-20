package eu.yvka.shadersloth.app;

import eu.yvka.shadersloth.app.controllers.ShaderSlothController;
import eu.yvka.shadersloth.share.context.ApplicationContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class App extends Application {

	private final static Logger Log = LoggerFactory.getLogger(App.class);
	private ShaderSlothController controller;

	@Override
	public void init() throws Exception {

		ApplicationContext.get().init(this, () -> Collections.emptyList());
		Runtime.getRuntime().addShutdownHook(new Thread(ApplicationContext.get()::destroy));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			Log.error("Detect uncaught exception in [" + t.getName() + " Thread]", e);
			ExceptionDialog exceptionDialog = new ExceptionDialog(e);
			exceptionDialog.showAndWait();
			Platform.exit();
		});

	}

    @Override
    public void start(final Stage primaryStage) {
		controller = new ShaderSlothController(primaryStage);
		controller.show();
		controller.start();
	}

	@Override
	public void stop() throws Exception {
		controller.stop();
		ApplicationContext.get().destroy();
	}

	public static void main(String[] args) {
		Log.info("Launching ShaderSlothRenderer");
		/**
		 * Improves the poor rendering results of javafx.
		 *
		 * @see <a href="http://comments.gmane.org/gmane.comp.java.openjdk.openjfx.devel/5072">Open JavaFX development Mailing list</a>
		 */
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
		launch(args);
	}
}
