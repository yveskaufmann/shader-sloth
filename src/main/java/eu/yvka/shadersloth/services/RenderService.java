package eu.yvka.shadersloth.services;

import eu.yvka.shadersloth.ShaderSlothRenderer;
import eu.yvka.slothengine.engine.AppSettings;
import eu.yvka.slothengine.engine.JavaFXOffscreenSupport;
import eu.yvka.slothengine.scene.Scene;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * A service which is responsible to render
 * the rendering preview view.
 *
 * @Author Yves Kaufmann
 * @since 30.06.2016
 */
public class RenderService extends Service<Void> {

	private static final Logger Log = LoggerFactory.getLogger(RenderService.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private final CountDownLatch runningLatch = new CountDownLatch(1);
	private final ImageView renderView;
	private ShaderSlothRenderer rendererLoop;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public RenderService(ImageView renderView) {
		this.renderView = renderView;
		setExecutor(Executors.newSingleThreadExecutor(runnable -> {
			Thread thread = new Thread(runnable);
			thread.setName("Renderer Thread");
			thread.setDaemon(true);
			thread.setPriority(Thread.MAX_PRIORITY);
			return thread;
		}));
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@Override
	protected void cancelled() {
		runningLatch.countDown();
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				AppSettings settings  = new AppSettings();

				settings.set(AppSettings.Width, (int) renderView.getFitWidth());
				settings.set(AppSettings.Height, (int) renderView.getFitHeight());
				settings.set(JavaFXOffscreenSupport.JAVAFX_OFFSCREEN_SUPPORT, new JavaFXOffscreenSupport(renderView, runningLatch));

				rendererLoop = new ShaderSlothRenderer();
				rendererLoop.setOnStartedCallback(scene -> setScene(scene));
				try {
					rendererLoop.start(settings);
				} catch (Exception ex) {
					Log.error("Detected a exception in the renderer", ex);
					cancel(false);
					setException(ex);
				}
				return null;
			}
		};
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<Scene> scene;

	/**
	 * Specifies which scene should be rendered by the renderer.
     */
	public ObjectProperty<Scene> sceneProperty() {
		if (scene == null) {
			scene = new SimpleObjectProperty<>(this, "scene");
		}
		return scene;
	}

	public Scene getScene() {
		return scene == null ? null : scene.get();
	}

	public void setScene(Scene sceneToRender) {
		sceneProperty().setValue(sceneToRender);
	}
}
