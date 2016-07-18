package eu.yvka.shadersloth.app.project;

import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.camera.Camera;
import eu.yvka.slothengine.scene.light.AmbientLight;
import eu.yvka.slothengine.scene.light.Light;
import eu.yvka.slothengine.scene.light.PointLight;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static eu.yvka.shadersloth.app.project.ProjectConstants.*;

/**
 * @author Yves Kaufmann
 * @since 16.07.2016
 */
public class XMLProjectReader implements ProjectReader {

	private static final Logger Log = LoggerFactory.getLogger(XMLProjectReader.class);

	private XMLInputFactory inputFactory;
	private XMLStreamReader reader;
	private ProjectImpl project;

	private Map<String, Light> lightMap;
	private Map<String, Material> materialMap;
	private Map<String, Geometry> geometryMap;

	private Object current = null;
	private Object parent = null;

	public XMLProjectReader() {
		lightMap = new HashMap<>();
		materialMap = new HashMap<>();
		geometryMap = new HashMap<>();
		inputFactory = XMLInputFactory.newFactory();
	}

	@Override
	public Project loadProject(File file) throws IOException {
		if (reader != null ) throw new IllegalStateException("project reading is already in progress");
		this.project = new ProjectImpl();
		try (FileReader in = new FileReader(file)) {
			reader = inputFactory.createXMLStreamReader(in);
			while (reader.hasNext()) {
				int event = reader.next();
				switch (event) {
					case XMLStreamConstants.START_ELEMENT: {
						processStartElement();
						break;
					}

					case XMLStreamConstants.END_ELEMENT: {
						processEndElement();
						break;
					}
				}
			}
 		} catch (Exception e) {
			throw new IOException("Failed to load project", e);
		}

		return this.project;
	}

	private void processStartElement() throws Exception {
		String tagName = reader.getLocalName();

		switch (tagName) {
			case PROJECT_TAG:
				project.name = readRequiredAttr(PROJECT_ATTR_NAME);
				project.projectFolder = new File(readRequiredAttr(PROJECT_ATTR_FOLDER));
				break;

			case SCENE_TAG:
					current = project.getScene();
				break;

			case CAMERA_TAG:
				String cameraType = readRequiredAttr(CLASS_ATTR);
				Camera camera = (Camera) Class.forName(cameraType).newInstance();
				project.getScene().setCamera(camera);
				current = camera;
				break;

			case NODES_TAG:

				break;

			case NODE_TAG:
				String id = readRequiredAttr(ID_ATTR);
				String classAttr = readRequiredAttr(CLASS_ATTR);
				String lightType = readAttr(LIGHT_ATTR_TYPE);

				Class<?> cls = Class.forName(classAttr);
				if (cls.isAssignableFrom(Node.class)
					|| cls.isAssignableFrom(PointLight.class)
					|| cls.isAssignableFrom(AmbientLight.class)
					|| cls.isAssignableFrom(Geometry.class)) {
					Constructor<?> constructor = cls.getConstructor(String.class);
					current = constructor.newInstance(id);
				}

				break;

			case POSITION_TAG:
				Vector3f position = readVector();
				if (current instanceof Camera) {
					((Camera) current).setPosition(position);
				}

				if (current instanceof Node) {
					((Node) current).setPosition(position);
				}
			break;

			case SCALE_TAG:
				Vector3f scale = readVector();
				if (current instanceof Node) {
					((Node) current).setScale(scale);
				}

				break;

			case ROTATION_TAG:
				Quaternionf rotation = readQuaternion();
				if (current instanceof Node) {
					((Node) current).setRotation(rotation);
				}
				break;
		}
	}



	private void processEndElement() {

	}

	private void readScene() {
	}


	private Vector3f readVector() {
		float x = readFloatAttr("x");
		float y = readFloatAttr("y");
		float z = readFloatAttr("z");
		return new Vector3f(x, y, z);
	}

	private Quaternionf readQuaternion() {
		float x = readFloatAttr("x");
		float y = readFloatAttr("y");
		float z = readFloatAttr("z");
		float w = readFloatAttr("w");
		return new Quaternionf(x, y, z, w);
	}

	private Color readColor() {
		float r = readFloatAttr("r");
		float g = readFloatAttr("g");
		float b = readFloatAttr("b");
		float a = readFloatAttr("a");
		return new Color(r, g, b, a);
	}

	private float readFloatAttr(String attr) {
		String str = readRequiredAttr(attr);
		return Float.valueOf(str);
	}

	private String readRequiredAttr(String attribute) {
		String value = readAttr(attribute);
		if (value == null) {
			throw new IllegalStateException(MessageFormat.format("The \"{0}\" tag requires a \"{1}\" attribute", reader.getLocalName(), attribute));
		}
		return value;
	}

	private String readAttr(String attribute) {
		return readAttr(attribute, null);
	}

	private String readAttr(String attribute, String alternativeValue) {
		String value = reader.getAttributeValue(null, attribute);
		if (value == null) return alternativeValue;
		return value;
	}
}
