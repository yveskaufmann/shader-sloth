package eu.yvka.shadersloth.model;

import javafx.beans.value.ChangeListener;
import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static  org.mockito.Mockito.*;

public class Vector3fModelTest {

	private Vector3fModel model;

	@Before
	public void createTestInstance() {
		this.model = new Vector3fModel();
	}

	@Test
	public void testXGetterSetter() throws Exception {
		assertEquals(0.0f, model.getX(), 0.01f);
		model.setX(1.0f);
		assertEquals(1.0f, model.getX(), 0.01f);
		model.setX(2.0f);
		assertEquals(2.0f, model.getX(), 0.01f);

	}

	@Test
	public void testXPropertyChangeListener() {
		ChangeListener changeListenerMock = mock(ChangeListener.class);
		model.xProperty().addListener(changeListenerMock);
		model.setX(46.0f);
		verify(changeListenerMock).changed(model.xProperty(), 0.0f, 46.0f);
	}


	@Test
	public void testYGetterSetter() throws Exception {
		assertEquals(0.0f, model.getY(), 0.01f);
		model.setY(1.0f);
		assertEquals(1.0f, model.getY(), 0.01f);
		model.setY(2.0f);
		assertEquals(2.0f, model.getY(), 0.01f);
	}

	@Test
	public void testYPropertyChangeListener() {
		ChangeListener changeListenerMock = mock(ChangeListener.class);
		model.yProperty().addListener(changeListenerMock);
		model.setY(1.0f);
		model.setY(22.0f);
		verify(changeListenerMock).changed(model.yProperty(), 1.0f, 22.0f);
	}

	@Test
	public void testZGetterSetter() throws Exception {
		assertEquals(0.0f, model.getZ(), 0.01f);
		model.setZ(1.0f);
		assertEquals(1.0f, model.getZ(), 0.01f);
		model.setZ(2.0f);
		assertEquals(2.0f, model.getZ(), 0.01f);
	}

	@Test
	public void testZPropertyChangeListener() {
		ChangeListener changeListenerMock = mock(ChangeListener.class);
		model.zProperty().addListener(changeListenerMock);
		model.setZ(1.0f);
		model.setZ(22.0f);
		verify(changeListenerMock).changed(model.zProperty(), 1.0f, 22.0f);
	}

	@Test
	public void testToVector() {
		model.setX(1.0f);
		model.setY(0.0f);
		model.setZ(-2.0f);
		Vector3f vector3f = model.toVector3f();

		assertNotNull(vector3f);
		assertEquals(1.0f, vector3f.x, 0.01);
		assertEquals(0.0f, vector3f.y, 0.01);
		assertEquals(-2.0f, vector3f.z, 0.01);

	}

}
