package eu.yvka.shadersloth.app.project;

import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.geometry.primitives.Sphere;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.material.Pass;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.renderer.RenderState;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.camera.FreeCamera;
import eu.yvka.slothengine.scene.light.PointLight;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author Yves Kaufmann
 * @since 17.07.2016
 */
public class DummyProject extends ProjectImpl {

	private BasicMaterial material;


	public DummyProject() {
		super();
		List<Geometry> boxes = new ArrayList<>();
		final Scene scene = getScene();
		Random rnd = new Random();
		rnd.setSeed(System.nanoTime());

		BasicMaterial mat = new BasicMaterial();
		mat.getRenderState().setFrontFaceWinding(RenderState.FaceWinding.GL_CCW);
		mat.getRenderState().setCullFaceMode(RenderState.CullFaceMode.Back);
		mat.setParameter("sl_material.diffuse", Color.White);
		mat.setParameter("sl_material.ambient", Color.White);
		mat.setShininess(20.0f);
		getMaterials().add(mat);

		Pass pass = mat.createPass();
		pass.getRenderState().setWireframe(true);
		pass.getRenderState().setBlendMode(RenderState.BlendFunc.Alpha);
		pass.setParameter("sl_material.diffuse", Color.White);

		pass.setEnableLightning(false);

		Geometry room = new Geometry("Room");
		room.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
		room.setScale(10.0f);
		room.setVisible(true);
		room.setMesh(new Cube());
		room.setMaterial(mat);
		scene.getRootNode().addChild(room);

		scene.getCamera().setPosition(0f, 5f, 10f);
		((FreeCamera)scene.getCamera()).setTarget(new Vector3f(10.0f, 2.0f, -10.0f));

		float radius = 2.0f;
		int boxCount = 5;
		float PI2 = (float) (Math.PI * 2);
		float R = (1.0f / boxCount);
		for (int i = 0; i <= boxCount; i++) {
			Geometry box = new Geometry("Box " + i);
			box.setMaterial(mat);
			box.setScale(1.0f);
			box.setMesh(new Sphere(1, 20, 20));
			box.setMesh(Engine.getMeshFromAssets("Rabbit.obj"));
			box.setPosition((float) (Math.cos(R * i * PI2) * radius), 0, (float) (Math.sin(R * i * PI2) * radius));
			boxes.add(box);
			room.addChild(box);

		}

		Geometry g = new Geometry("g");
		g.setMesh(new Cube());
		g.setPosition(-9.0f, 0.0f, 0.0f);
		g.setMaterial(mat);
		room.addChild(g);

		PointLight point = new PointLight("Light 1");
		point.setAttenuation(1.0f);
		point.setPosition(new Vector3f(0.0f, 5.0f, -2.0f));
		point.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
		point.setAttenuation(100.0f);
		getScene().add(point);

		PointLight point2 = new PointLight("Light 2");
		point2.setAttenuation(1.0f);
		point2.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
		point2.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		point2.setAttenuation(20.0f);
		getScene().add(point2);
	}
}
