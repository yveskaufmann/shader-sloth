package de.yvka.shadersloth.controller;

import de.yvka.slothengine.engine.AppSettings;
import de.yvka.slothengine.engine.JavaFXOffscreenSupport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import de.yvka.shadersloth.ShaderSloth;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class ShaderSlothController implements Initializable {


	@FXML private AnchorPane renderViewRoot;
	@FXML private ImageView renderView;

	/**
	 * The underlying renderer implementation
	 */
	private ShaderSloth rendererLoop;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO size should depend on parent view
		//renderView.fitWidthProperty().bind(renderViewRoot.widthProperty());
		//renderView.fitHeightProperty().bind(renderViewRoot.heightProperty());
		renderView.setFitWidth(512);
		renderView.setFitHeight(512);
	}

	/**
	 * This method will run the rendering thread.
	 * This method is adapted from: https://github.com/Spasi/LWJGL-FX/blob/master/src/lwjglfx/GUIController.java
	 *
	 * @param runningLatch
     */
	public void runRenderer(final CountDownLatch runningLatch) {
		AppSettings settings  = new AppSettings();

		settings.set(AppSettings.Width, (int) renderView.getFitWidth());
		settings.set(AppSettings.Height, (int) renderView.getFitHeight());
		settings.set(JavaFXOffscreenSupport.JAVAFX_OFFSCREEN_SUPPORT, new JavaFXOffscreenSupport(renderView, runningLatch));

		rendererLoop = new ShaderSloth();
		rendererLoop.start(settings);
	}

}
