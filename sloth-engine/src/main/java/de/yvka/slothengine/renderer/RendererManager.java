package de.yvka.slothengine.renderer;


import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.material.Material;
import de.yvka.slothengine.material.MaterialParameter;
import de.yvka.slothengine.material.Pass;
import de.yvka.slothengine.material.TextureBinding;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.renderer.font.FontRenderer;
import de.yvka.slothengine.scene.camera.Camera;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.Scene;
import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.scene.Scene;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.util.List;


public class RendererManager implements EngineComponent {

	private static final Logger Log = LoggerFactory.getLogger(RendererManager.class);

	private RenderState renderState;
	private Renderer renderer;
	private FontRenderer fontRenderer;
	private boolean initialized = false;
	private Scene currentScene;

	@Override
	public void initialize() {
		if (initialized) {
			throw new IllegalArgumentException("The RenderManger is already initialized");
		}

		renderState = new RenderState();
		if (renderer == null) {
			renderer = new Lwjgl3Renderer();
			renderer.applyRenderState(renderState);
			Engine.getPrimaryWindow().updateViewportSize();
		}

		if (fontRenderer == null) {
			fontRenderer = new FontRenderer(new Font("Courier", Font.PLAIN, 32));
		}

		initialized = true;
	}

	@Override
	public void shutdown() {
		if (!initialized) {
			throw new IllegalArgumentException("The RenderManager isn't initialized");
		}

		if (renderer != null) {
			renderer.cleanUp();
		}

		initialized = false;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}


	@Override
	public void onFrameStart() {
		if (renderer != null) {
			renderer.onNewFrame();
		}
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	/**
	 * Get the render state of the active renderer.
	 *
	 * @return the state of the current active renderer.
	 */
	public RenderState getRenderState() {
		return renderState;
	}

	public void updateViewportSize(int x, int y, int width, int height) {
		if (renderer != null) {
			renderer.setViewport(x, y, width, height);
		}
	}

	public void setScene(Scene scene) {
		if (currentScene != null) {
			currentScene.setActive(false);
		}
		currentScene = scene;
	}

	public void render(float elapsedTime) {
		if (currentScene == null) return;

		renderer.setClearColor(Color.LightGrey);
		renderer.clearBuffers(true, true, true);

		//TODO: Filter Gemoetry Nodes which are not in sight
		currentScene.traverse((node) -> {
			if (node instanceof Geometry) {
				Geometry geometry = (Geometry) node;
				if (geometry.isVisible()) {
					renderGeometry((Geometry) node);
				}
			}
		});
	}

	private void renderGeometry(Geometry geometry) {
		Camera camera = currentScene.getCamera();
		Matrix4f projectionMatrix = camera.getProjectionMatrix();
		Matrix4f viewMatrix = camera.getViewMatrix();

		Matrix4f modelMatrix = geometry.getTransformMatrix();
		Matrix4f normalMatrix = new Matrix4f();
		viewMatrix.normal(normalMatrix);

		Matrix4f modelViewMatrix = new Matrix4f(viewMatrix).mul(modelMatrix);
		Matrix4f modelViewProjectionMatrix = new Matrix4f(projectionMatrix).mul(modelViewMatrix);

		Material material = geometry.getMaterial();
		List<Pass> passes = material.getPasses();
		int countOfPasses = passes.size();

		for (int i = 0; i < countOfPasses; i++) {
			Pass pass = passes.get(i);
			Pass nextPass = i > 0 ? passes.get(i - 1) : null;
			Pass prevPass = i + 1 < countOfPasses ? passes.get(i + 1) : null;

			Shader shader = pass.getShader();
			shader.getUniform("sl_cameraPosition").setValue(camera.getPosition());
			shader.getUniform("sl_cameraDirection").setValue(camera.getDirection());
			shader.getUniform("sl_projectionMatrix").setValue(projectionMatrix);
			shader.getUniform("sl_modelViewMatrix").setValue(modelViewMatrix);
			shader.getUniform("sl_viewMatrix").setValue(viewMatrix);
			shader.getUniform("sl_modelMatrix").setValue(modelMatrix);
			shader.getUniform("sl_normalMatrix").setValue(normalMatrix);
			shader.getUniform("sl_mvp").setValue(modelViewProjectionMatrix);

			pass.preparePass(prevPass);
			if (currentScene.getLightList() != null && pass.isLightningEnabled()) {
				currentScene.getLightList().passToShader(shader);
			}

			for(MaterialParameter parameter : material.getMaterialParameters().values()) {
				shader.getUniform(parameter.getName()).setValue(parameter);
			}
			try {
				renderer.setShader(shader);
			} catch (Exception ex) {
				Log.error("Failed to setup shader", ex);
			}

			for(TextureBinding tb: pass.getTextures().values()) {
				renderer.setTexture(tb.getUint(), tb.getTexture());
			}

			renderer.applyRenderState(pass.getRenderState());
			renderer.drawMesh(geometry.getMesh());
			pass.postProcessPass(nextPass);
		}
	}

}
