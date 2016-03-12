package de.yvka.shadersloth.utils;

import de.yvka.shadersloth.I18N.I18N;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

public abstract class AbstractController {

	/**
	 * Abstract base logger
	 */
	private Logger Log = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * The FXML file which is corresponding to this controller
	 */
	private URL fxmlFile;

	/**
	 * The current local Resource Bundle
	 */
	private ResourceBundle resourceBundle;

	/**
	 * The root element of this controller
	 */
	protected Parent root;

	public AbstractController(URL location) {
		this(location, I18N.getResourceBundle());
	}

	public AbstractController(URL location, ResourceBundle resources) {
		this.fxmlFile = location;
		this.resourceBundle = resources;
	}

	/**
	 * Retrieves the root element.
	 *
	 * @return the root element
     */
	public Parent getRoot() {
		if (this.root == null) {
			loadFXML();
		}
		return this.root;
	}

	protected final void setRoot(Parent root) {
		assert this.root == null;
		this.root = root;
	}

	/**
	 * Load the corresponding fxml file which contains the
	 * view of this controller.
	 */
	protected void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(fxmlFile);
		loader.setResources(resourceBundle);
		loader.setCharset(Charset.forName("UTF-8"));
		try {
			setRoot(loader.load());
			onFxmlLoaded();
		} catch (IOException e) {
			Log.debug("Loader.getController : " + loader.getController());
			Log.debug("Loader.getLocation : " + loader.getLocation());
			throw new RuntimeException("Failed to load " + fxmlFile.getFile(), e);
		}
	}

	/**
	 * Called when the fxml file was loaded
	 */
	abstract protected void onFxmlLoaded();

	/**
	 * @return the assigned resource bundle
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @return the location of the corresponding fxml file.
	 */
	public URL getLocation() {
		return fxmlFile;
	}
}
