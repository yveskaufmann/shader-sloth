package eu.yvka.shadersloth.app.renderView;

import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.engine.EngineApp;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.geometry.primitives.Sphere;
import eu.yvka.slothengine.input.InputListener;
import eu.yvka.slothengine.input.event.KeyEvent;
import eu.yvka.slothengine.input.event.MouseEvent;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.material.Pass;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.renderer.FPSCounter;
import eu.yvka.slothengine.renderer.RenderState;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.camera.FreeCamera;
import eu.yvka.slothengine.scene.light.PointLight;
import javafx.geometry.Pos;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static eu.yvka.slothengine.renderer.RenderState.CullFaceMode;

public class ShaderSlothRenderer extends EngineApp implements InputListener {

	private float zoomLevel;
	private Consumer<Scene> onStartedCallback;
	private Scene activeScene;
	private Vector2f mousePos;

	public void setOnStartedCallback(Consumer<Scene> onStartedCallback) {
		this.onStartedCallback = onStartedCallback;
	}

	@Override
	protected void prepare() {
		zoomLevel = 1.0f;
		inputManager.addListener(this);
	}

	@Override
	public void update(float elapsedTime) {
		zoomLevel -= 30.0 * elapsedTime * inputManager.getMouseWheelAmount();
		scene.update(elapsedTime);
	}

	@Override
	protected void cleanUp() {
	}

	@Override
	public void onMouseEvent(MouseEvent event) {
		mousePos = new Vector2f((float) event.getX(), (float) event.getY());
	}

	@Override
	public void onKeyEvent(KeyEvent keyEvent) {
		final RenderState state = rendererManager.getRenderState();
		final Node rootNode = scene.getRootNode();

		if (keyEvent.isPressed()) {
			switch (keyEvent.getKeyButton()) {
				case A: zoomLevel += 0.1; break;
				case Q: zoomLevel -= 0.1; break;
				case Up:    rootNode.getPosition().add(0.0f, 0.2f, 0.0f); break;
				case Left:  rootNode.getPosition().add(-0.2f, 0.0f, 0.0f); break;
				case Right: rootNode.getPosition().add(0.2f, 0.0f, 0.0f); break;
				case Down:  rootNode.getPosition().add(0.0f, -0.2f, 0.0f); break;
			}
		}
	}

	public void setActiveScene(Scene activeScene) {
		Engine.runWhenReady(() -> {
			rendererManager.setScene(activeScene);
			this.scene = activeScene;
		});
	}
}
