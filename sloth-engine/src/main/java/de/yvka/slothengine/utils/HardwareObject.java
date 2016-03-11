package de.yvka.slothengine.utils;

import de.yvka.slothengine.renderer.Renderer;

/**
 * A hardware object is a abstraction for objects which lives in the memory of
 * hardware devices like graphic cards.
 */
public abstract class HardwareObject {

	/**
	 * Marks a unset id of a object.
	 */
	public static final int UNSET_ID = -1;

	/**
	 * Marks a invalid id of a object
	 */
	public static final int INVALID_ID = -2;

	/**
	 * The id of the underlying hardware object.
	 */
	private int id;

	/**
	 * Marks that the hardware object must be updated by the renderer
	 * which can help to avoid unnecessary read and write operations to the hardware
	 * device.
	 */
	private boolean updateRequired;

	/**
	 * The reference to the hardware object.
	 */
	private Object objectRef;

	/**
	 * The type of the hardware object.
	 */
	private Class<?> type;

	protected HardwareObject(Class<?> type) {
		this.id = UNSET_ID;
		this.type = type;
		enableUpdateRequired();
	}


	/**
	 * The id of the underlying hardware object.
	 * Per default the id is to <code>UNSET_ID</code>
	 * and the renderer is responsible to set the id of the hardware object.
	 *
	 * @return the id of the underlying hardware object.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set the id of the underlying hardware object but only
	 * the renderer is responsible to set id of this object.
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return true if the object must be updated before it could be used by the renderer.
	 */
	public boolean isUpdateRequired() {
		return this.updateRequired;
	}

	/**
	 * Mark that this object must be updated before it could be
	 * used by the renderer.
	 */
	public void enableUpdateRequired() {
		this.updateRequired = true;
	}

	/**
	 * Mark that this object must not be updated.
	 */
	public void disableUpdateRequired() {
		this.updateRequired = false;
	}

	/**
	 * Deletes this object from the underlying hardware device by
	 * using the corresponding renderer implementation.
	 *
	 * This is required in order to remove unused objects and
	 * clean up stuff.
	 *
	 * @param renderer the corresponding renderer implementation
	 */
	public abstract void deleteObject(Renderer renderer);

	/**
	 * Reset the underlying hardware object to it's
	 * previously state.
	 *
	 * This could be necessary when the open gl context is lost
	 * for any reason for example.
	 */
	public abstract void resetObject();


}
