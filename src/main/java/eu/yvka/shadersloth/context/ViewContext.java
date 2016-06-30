package eu.yvka.shadersloth.context;

import eu.yvka.shadersloth.utils.controller.AbstractController;
import eu.yvka.shadersloth.utils.controller.ControllerMeta;
import javafx.scene.Parent;

public class ViewContext<T extends AbstractController> extends AbstractContext {

	/**
	 * Property for the associated controllers.
	 */
	public static final String PROPERTY_CONTROLLER = "controllers";

	private Parent rootNode;
	private T controller;
	private ControllerMeta meta;

	public ViewContext(T controller, Parent rootNode, ControllerMeta meta) {
		this.controller = controller;
		this.rootNode = rootNode;
		this.meta = meta;
	}

	public ApplicationContext getApplicationContext() {
		return ApplicationContext.get();
	}

	public T getController() {
		return controller;
	}

	public Parent getRootNode() {
		return rootNode;
	}

	public ControllerMeta getMeta() {
		return meta;
	}
}
