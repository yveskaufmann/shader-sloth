package de.yvka.slothengine.shader.source;

import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.renderer.RendererExpception;
import de.yvka.slothengine.shader.ShaderType;
import de.yvka.slothengine.utils.IOUtils;
import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.shader.ShaderType;
import de.yvka.slothengine.utils.IOUtils;

import java.io.File;
import java.io.IOException;

public class FileShaderSource extends ShaderSource {

	private File file;
	private long lastModified;
	private String currentSource;

	public FileShaderSource(ShaderType type, String path) {
		this(type, new File(path));
	}

	public FileShaderSource(ShaderType type, File file) {
		super(type);

		if (! file.exists()) {
			throw new RendererExpception(
				"The specified shader source file %s don't exists",
				file.getAbsoluteFile()
			);
		}

		this.file = file;
		this.lastModified = 0L;
		this.currentSource = "";
	}

	@Override
	public boolean isUpdateRequired() {
		return super.isUpdateRequired() || lastModified < file.lastModified();
	}

	@Override
	public String getSource() {
		long fileTimestamp = file.lastModified();
		if (lastModified < fileTimestamp) {
			lastModified = fileTimestamp;

			try {
				this.currentSource = IOUtils.toString(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this.currentSource;
	}

	@Override
	public long lastModified() {
		return lastModified;
	}

	@Override
	public String getName() {
		return file.getAbsolutePath();
	}

	@Override
	public void deleteObject(Renderer renderer) {
		renderer.deleteShaderSource(this);
	}

	@Override
	public void resetObject() {
		enableUpdateRequired();
	}
}
