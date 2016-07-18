package eu.yvka.shadersloth.app.project.loader;

import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.app.project.ProjectImpl;
import eu.yvka.shadersloth.app.project.XMLProjectReader;
import eu.yvka.shadersloth.app.project.XMLProjectWriter;
import eu.yvka.slothengine.engine.AppSettings;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.camera.FreeCamera;
import eu.yvka.slothengine.scene.light.PointLight;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import org.joml.Vector3f;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.*;;

/**
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public class XMLProjectWriterTest {

	public static File projectFile;
	private static Project testProject;

	@BeforeClass
	public static void setupProject() throws NameAlreadyInUseException {
		Engine.start(new AppSettings());
		testProject = new ProjectImpl();
		Scene scene = testProject.getScene();

		float zoomLevel = 1.0f;
		Random rnd = new Random();
		rnd.setSeed(System.nanoTime());


		Geometry room = new Geometry("Room");
		room.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
		room.setScale(10.0f);
		room.setVisible(true);
		room.setMesh(new Cube());
		scene.getRootNode().addChild(room);

		scene.getCamera().setPosition(0f, 5f, 10f);
		((FreeCamera) scene.getCamera()).setTarget(new Vector3f(10.0f, 2.0f, -10.0f));

		Geometry g = new Geometry("g");
		g.setMesh(new Cube());
		g.setPosition(-9.0f, 0.0f, 0.0f);
		g.setMaterial(new BasicMaterial());
		room.addChild(g);

		PointLight point = new PointLight("Light 1");
		point.setAttenuation(1.0f);
		point.setPosition(new Vector3f(0.0f, 5.0f, -2.0f));
		point.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
		point.setAttenuation(100.0f);
		scene.add(point);

		PointLight point2 = new PointLight("Light 2");
		point2.setAttenuation(1.0f);
		point2.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
		point2.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		point2.setAttenuation(20.0f);

		testProject.setName("test");

		projectFile = new File(testProject.getProjectFolder(), "project.xml");

	}

	@AfterClass
	public static void shutdown() {

	}


	@Test
	public void saveProject() throws Exception {
		XMLProjectWriter writer = new XMLProjectWriter();
		writer.saveProject(testProject);

		assertTrue(testProject.getProjectFolder().exists());
		assertTrue(projectFile.exists());
	}

	@Test
	public void loadProject() throws Exception {
		XMLProjectReader reader = new XMLProjectReader();
		Project loadedProject = reader.loadProject(projectFile);

		assertNotNull(loadedProject);
		assertEquals(testProject.getName(), loadedProject.getName());
		assertEquals(testProject.getProjectFolder(), loadedProject.getProjectFolder());
	}

}
