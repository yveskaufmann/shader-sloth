package shadersloth;

import de.yvka.slothengine.engine.AppSettings;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineApp;
import de.yvka.slothengine.geometry.primitives.Cube;
import de.yvka.slothengine.geometry.primitives.Sphere;
import de.yvka.slothengine.input.InputListener;
import de.yvka.slothengine.input.event.KeyEvent;
import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.material.BasicMaterial;
import de.yvka.slothengine.material.Pass;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.renderer.FPSCounter;
import de.yvka.slothengine.renderer.RenderState;
import static de.yvka.slothengine.renderer.RenderState.*;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.camera.FreeCamera;
import de.yvka.slothengine.scene.light.PointLight;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ShaderSloth extends EngineApp implements InputListener {

    float zoomLevel;
    private BasicMaterial material;
    List<Geometry> boxes = new ArrayList<>();
    private float rotX = 0.0f;
    private Geometry room;

    @Override
    protected void prepare() {

        zoomLevel = 1.0f;

        Engine.register(new FPSCounter());
        inputManager.addListener(this);

        Random rnd = new Random();
        rnd.setSeed(System.nanoTime());

        BasicMaterial mat = new BasicMaterial();
        mat.getRenderState().setFrontFaceWinding(RenderState.FaceWinding.GL_CCW);
        mat.getRenderState().setCullFaceMode(RenderState.CullFaceMode.Back);
        mat.setParameter("sl_material.diffuse", Color.White);
        mat.setParameter("sl_material.ambient", Color.White);
        mat.setShininess(20.0f);

        Pass pass = mat.createPass();
        pass.getRenderState().enableWireframe(true).setBlendMode(RenderState.BlendFunc.Alpha);
        pass.setParameter("sl_material.diffuse", Color.White);

        pass.setEnableLightning(false);

        room = new Geometry("Room");
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
            material = new BasicMaterial();
            box.setMaterial(material);
            box.setScale(1.0f);
            box.setMesh(new Sphere(1, 20, 20));
            box.setMesh(Engine.getMesh("Rabbit.obj"));
            box.setPosition((float) (Math.cos(R * i * PI2) * radius), 0, (float) (Math.sin(R * i * PI2) * radius));
            boxes.add(box);
            room.addChild(box);

        }

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
        // scene.add(point2);
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
    }

    @Override
    public void onKeyEvent(KeyEvent keyEvent) {
        RenderState state = rendererManager.getRenderState();
        if (keyEvent.isPressed()) {
            switch (keyEvent.getKeyButton()) {
                case Esc: Engine.requestExit();
                case A: zoomLevel += 0.1; break;
                case Q: zoomLevel -= 0.1; break;
                case C:
                    if (material.getRenderState().getCullFaceMode() == RenderState.CullFaceMode.Off) {
                        material.getRenderState().setCullFaceMode(CullFaceMode.Back);
                    } else {
                        material.getRenderState().setCullFaceMode(CullFaceMode.Off);
                    }
                    rendererManager.getRenderer().applyRenderState(material.getRenderState());
                    break;
                case E: material.getRenderState().toggleWireframe(); break;
                case Up:    room.getPosition().add(0.0f, 0.2f, 0.0f); break;
                case Left:  room.getPosition().add(-0.2f, 0.0f, 0.0f); break;
                case Right: room.getPosition().add(0.2f, 0.0f, 0.0f); break;
                case Down:  room.getPosition().add(0.0f, -0.2f, 0.0f); break;
                case PageUp: boxes.forEach((box) -> box.getPosition().add(0.0f, 0.0f, -0.2f)); break;
                case PageDown: boxes.forEach((box) -> box.getPosition().add(0.0f, 0.0f, 0.2f)); break;
                case End: rotX += 0.1f; boxes.forEach((box) -> box.getRotation().rotationXYZ(rotX, -rotX, rotX)); break;
                case H: room.setVisible(! room.isVisible()); break;
            }
        }
    }

    /**
     * Run the renderer without offscreen support, exists only for
     * development and debugging purposes.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        EngineApp sandbox = new ShaderSloth();
        sandbox.start(new AppSettings());
    }



}
