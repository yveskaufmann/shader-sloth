package eu.yvka.shadersloth.app.project;

import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.camera.Camera;
import eu.yvka.slothengine.scene.light.AmbientLight;
import eu.yvka.slothengine.scene.light.Light;
import eu.yvka.slothengine.scene.light.LightList;
import eu.yvka.slothengine.scene.light.PointLight;
import eu.yvka.slothengine.texture.Texture;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static eu.yvka.shadersloth.app.project.ProjectConstants.*;

/**
 *
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public class XMLProjectWriter implements ProjectWriter {

	private static final Logger Log = LoggerFactory.getLogger(XMLProjectWriter.class);

	private XMLOutputFactory outputFactory;
	private XMLStreamWriter writer;

	public XMLProjectWriter() {
		try {
			outputFactory = XMLOutputFactory.newFactory();
		} catch (FactoryConfigurationError ex) {
			throw new IllegalStateException("Failed to initiate xml writer factory", ex);
		}
	}


	@Override
	public void saveProject(final Project project) throws IOException {

		if (writer != null ) throw new IllegalStateException("project writing is already in progress");

		final File projectFolder = project.getProjectFolder();
		final File projectFile = new File(projectFolder, ProjectReader.DEFAULT_PROJECT_FILE);

		try (FileOutputStream fo = new FileOutputStream(projectFile)) {
			writer = outputFactory.createXMLStreamWriter(fo, "UTF-8");
			// writer =  new IndentingXMLStreamWriter(writer);

			writer.writeStartDocument();
			writeProject(project);
			writer.writeEndDocument();

			writer.flush();
			writer.close();
		} catch (IOException ex) {
			Log.error("Failed to save project file " + projectFile.getAbsolutePath(), ex);
			throw ex;
		} catch (XMLStreamException ex) {
			Log.error("Failed to write project xml file", ex);
			throw new IOException("failed to write project xml file", ex);
		} finally {
			writer = null;
		}
	}

	private void writeProject(final Project project) throws XMLStreamException {
		writer.writeStartElement(ProjectConstants.PROJECT_TAG);
		writer.writeAttribute(PROJECT_ATTR_NAME, project.getName());
		writer.writeAttribute(PROJECT_ATTR_FOLDER, project.getProjectFolder().getAbsolutePath());
		writeScene(project.getScene());
		writeMaterials(project.getMaterials());
		writeTextures(project.getTextures());
		writer.writeEndElement();
	}

	private void writeScene(final Scene scene) throws XMLStreamException {
		writer.writeStartElement(SCENE_TAG);
		writeCamera(scene.getCamera());
		writeLights(scene.getLightList());
		writeNodes(scene.getRootNode());
		writer.writeEndElement();
	}

	private void writeMaterials(List<Material> materials) {

		// TODO: write materials
	}

	private void writeTextures(List<Texture> textures) {
		// TODO: write textures
	}

	private void writeCamera(final Camera camera) throws XMLStreamException {
		writer.writeStartElement(CAMERA_TAG);
		writer.writeAttribute(CLASS_ATTR, camera.getClass().getName());

		writeVectorTag(POSITION_TAG, camera.getPosition());
		writeVectorTag(DIRECTION_TAG, camera.getDirection());

		writer.writeEndElement();
	}

	private void writeLights(LightList lightList) throws XMLStreamException {
		writer.writeStartElement(LIGHTS_TAG);
		for(Light light : lightList) {
			writeLight(light);
		}
		writer.writeEndElement();
	}

	private void writeLight(Light light) throws XMLStreamException {
		writer.writeStartElement(LIGHT_TAG);
		writeNodeAttr(light);
		writeLightAttr(light);
		writeNodeTags(light);
		writeLightTags(light);
		writer.writeEndElement();
	}

	private void writeLightAttr(Light light) throws XMLStreamException {
		writer.writeAttribute(LIGHT_ATTR_TYPE, light.getType().name());
	}

	private void writeLightTags(Light light) throws XMLStreamException {
		writeNumberTag(LIGHT_ATTENTION_TAG, light.getAttenuation());
		writeColorTag(COLOR_TAG, light.getColor());

		if (light instanceof PointLight) {
			// todo: point light handling
		} else if (light instanceof AmbientLight) {
			// todo: ambient light handling
		}
	}

	private void writeNodes(Node rootNode) throws XMLStreamException {
		writer.writeStartElement(NODES_TAG);
		writeNode(rootNode);
		writer.writeEndElement();
	}

	private void writeNode(Node node) throws XMLStreamException {
		assert node != null;

		writer.writeStartElement(NODE_TAG);
		writeNodeAttr(node);

		if (node instanceof Light) {
			final Light light = (Light) node;
			writeLightAttr(light);
			writeLightTags(light);
		}

		if (node instanceof Geometry) {
			final Geometry geometry = (Geometry) node;
			writeBooleanAttribute("visible", geometry.isVisible());
			// todo: geometry persistence
			// todo: material persistence
		}

		writeNodeTags(node);

		List<Node> children = node.getChildren();
		if (! children.isEmpty()) {
			writer.writeStartElement(CHILDREN_TAG);
			for (Node child: children) {
				writeNode(child);
			}
			writer.writeEndElement();
		}
		writer.writeEndElement();

	}

	private void writeNodeTags(Node node) throws XMLStreamException {
		writeVectorTag(POSITION_TAG, node.getPosition());
		writeVectorTag(SCALE_TAG, node.getScale());
		writeQuaternionTag(ROTATION_TAG, node.getRotation());
	}

	private void writeNodeAttr(Node node) throws XMLStreamException {
		writer.writeAttribute(ID_ATTR, node.getId());
		writer.writeAttribute(CLASS_ATTR,  node.getClass().getName());
	}

	private void writeVectorTag(String tagName, final Vector3f position) throws XMLStreamException {
		assert position != null;
		writer.writeStartElement(tagName);
		writeNumberAttribute(VECTOR_ATTR_X, position.x);
		writeNumberAttribute(VECTOR_ATTR_Y, position.y);
		writeNumberAttribute(VECTOR_ATTR_Z, position.z);
		writer.writeEndElement();
	}

	private void writeColorTag(String tagName, Color color) throws XMLStreamException {
		assert color != null;
		writer.writeStartElement(tagName);
		writeNumberAttribute(COLOR_ATTR_R, color.getRed());
		writeNumberAttribute(COLOR_ATTR_G, color.getGreen());
		writeNumberAttribute(COLOR_ATTR_B, color.getBlue());
		writeNumberAttribute(COLOR_ATTR_A, color.getAlpha());
		writer.writeEndElement();
	}

	private void writeQuaternionTag(String tagName, Quaternionf rotation) throws XMLStreamException {
		assert rotation != null;
		writer.writeStartElement(tagName);
		writeNumberAttribute(VECTOR_ATTR_X, rotation.x);
		writeNumberAttribute(VECTOR_ATTR_Y, rotation.y);
		writeNumberAttribute(VECTOR_ATTR_Z, rotation.z);
		writeNumberAttribute(VECTOR_ATTR_W, rotation.w);
		writer.writeEndElement();
	}

	private void writeNumberTag(String tagName, Number number) throws XMLStreamException {
		writer.writeStartElement(tagName);
		writeNumberAttribute(VALUE_ATTR, number);
		writer.writeEndElement();
	}

	private void writeNumberAttribute(String name, Number number) throws XMLStreamException {
		String numberString = toString(number);
		writer.writeAttribute(name, numberString);
	}

	private void writeBooleanAttribute(String attrName, boolean visible) throws XMLStreamException {
		String booleanString = Boolean.toString(visible);
		writer.writeAttribute(attrName, booleanString);
	}

	private String toString(Number number) {
		float numVal = number.byteValue();
		return Float.toString(numVal);
	}
}
