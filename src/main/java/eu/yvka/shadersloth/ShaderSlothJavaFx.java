package eu.yvka.shadersloth;

import eu.yvka.shadersloth.context.ApplicationContext;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.yvka.shadersloth.controller.ShaderSlothController;

import java.util.Collections;

public class ShaderSlothJavaFx extends Application {

	private final static Logger Log = LoggerFactory.getLogger(ShaderSlothJavaFx.class);
	private ShaderSlothController controller;

	@Override
	public void init() throws Exception {
		ApplicationContext.get().init(this, () -> Collections.emptyList());
		Runtime.getRuntime().addShutdownHook(new Thread(ApplicationContext.get()::destroy));
		Thread.setDefaultUncaughtExceptionHandler((t, e) ->
			Log.error("Detect uncaught exception in [" + t.getName() + " Thread]", e));

	}

    @Override
    public void start(final Stage primaryStage) {
		controller = new ShaderSlothController(primaryStage);
		controller.show();
	}

	@Override
	public void stop() throws Exception {
		ApplicationContext.get().destroy();
	}

	public static void main(String[] args) {
		Log.info("Launching ShaderSloth");
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