package de.yvka.slothengine.utils;

import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.yvka.slothengine.engine.Engine;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

public class HardwareObjectManager {

	/**
	 * Logger of this class
	 */
	private static final Logger Log = LoggerFactory.getLogger(HardwareObjectManager.class);

	/**
	 * A reference to a object which is capable to count
	 * the usages of a object. A can determine if the object
	 * is already in use.
	 */
	private class ObjectReference extends SoftReference<HardwareObject> {
		HardwareObject object;

		ObjectReference(HardwareObject hardwareObject) {
			super(hardwareObject, referenceQueue);
			object = hardwareObject;
		}

		public HardwareObject getRef() {
			return object;
		}
	}

	/**
	 * List of all registered hardware objects
	 */
	private List<ObjectReference> referenceList;

	/**
	 * The Reference Queue which contains references
	 * which are no longer strongly accessible.
	 */
	private ReferenceQueue<HardwareObject> referenceQueue;

	/**
	 * Creates the a Hardware ObjectManager to keep
	 * track of hardware ids in order to clean all
	 * hardware objects after they are not longer used.
	 */
	public HardwareObjectManager() {
		referenceList = new LinkedList<>();
		referenceQueue = new ReferenceQueue<>();
	}

	/**
	 * Registers a hardware object in order to keep track
	 * of it.
	 *
	 * @param object the hardware object which should be registered.
	 */
	public void register(HardwareObject object) {
		ObjectReference reference = new ObjectReference(object);
		referenceList.add(reference);

		if (Log.isDebugEnabled()) {
			Log.debug("Registered: {}", object);
		}
	}

	/**
	 * Delete all hardware objects
	 */
	public void deleteAllObjects() {
		for(ObjectReference reference : referenceList) {
			HardwareObject obj = reference.getRef();
			if (obj != null) {
				obj.resetObject();
				obj.deleteObject(Engine.getCurrentRenderer());
			}
			if (Log.isDebugEnabled()) {
				Log.debug("Delete Object: {}", reference.getRef());
			}
		}
		referenceList.clear();
	}

	/**
	 * Reset all hardware objects
	 */
	public void resetAllObjects() {
		for(ObjectReference reference : referenceList) {
			reference.getRef().resetObject();
			if (Log.isDebugEnabled()) {
				Log.debug("Reset Object: {}", reference.getRef());
			}
		}
	}

	/**
	 * Delete all softly accessible referent objects.
	 */
	public void deleteAllUnused() {
		int deletedObjects = 0;
		ObjectReference ref = null;
		while((ref = (ObjectReference) referenceQueue.poll()) != null) {
			if (ref.get() == null) {
				break;
			}

			referenceList.remove(ref);
			Renderer currentRenderer = Engine.getCurrentRenderer();
			ref.get().deleteObject(currentRenderer);
			deletedObjects++;
		}

		if (deletedObjects > 0 && Log.isDebugEnabled()) {
			Log.debug("{0} hardware objects were deleted", deletedObjects);
		}
	}








}
