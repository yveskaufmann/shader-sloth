package de.yvka.slothengine.renderer;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.shader.source.ShaderSource;
import de.yvka.slothengine.texture.Texture;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.utils.HardwareObject;
import de.yvka.slothengine.utils.HardwareObjectManager;
import de.yvka.slothengine.shader.*;
import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.shader.*;
import de.yvka.slothengine.shader.source.ShaderSource;
import de.yvka.slothengine.texture.Texture;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.utils.HardwareObject;
import de.yvka.slothengine.utils.HardwareObjectManager;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public class Lwjgl3Renderer implements Renderer {

	private static final Logger Log = LoggerFactory.getLogger(Lwjgl3Renderer.class);

	private HardwareObjectManager objectManager;
	private RenderContext ctx;
	private GLCapabilities caps;

	public Lwjgl3Renderer() {
		initialize();
	}

	private void initialize() {
		objectManager = new HardwareObjectManager();
		ctx = new RenderContext();
		caps = GL.getCapabilities();
	}

	@Override
	public void setClearColor(Color clearColor) {
		glClearColor(
			clearColor.getRed(),
			clearColor.getGreen(),
			clearColor.getBlue(),
			clearColor.getAlpha()
		);
	}

	@Override
	public void clearBuffers(boolean color, boolean depth, boolean stencil) {
		int mask = 0;

		if (color) {
			mask |= GL_COLOR_BUFFER_BIT;
		}

		if (depth) {
			mask |= GL_DEPTH_BUFFER_BIT;
		}

		if (stencil) {
			mask |= GL_STENCIL_BUFFER_BIT;
		}

		glClear(mask);

	}

	@Override
	public void setViewport(int x, int y, int width, int height) {

		if (x != ctx.viewPortX || y != ctx.viewportY || width != ctx.viewPortWidth || height != ctx.viewPortHeight)  {
			ctx.viewPortX = x;
			ctx.viewportY = y;
			ctx.viewPortWidth = width;
			ctx.viewPortHeight = height;
			glViewport(x, y, width, height);
		}

	}

	@Override
	public void setDepthRange(float near, float far) {
		glDepthRange(near, far);
	}

	@Override
	public void setClipRect(int x, int y, int width, int height) {
		if ( !ctx.scissorTest ) {
			ctx.scissorTest = true;
			glEnable(GL_SCISSOR_TEST);
		}

		if (x != ctx.scissorX || y != ctx.scissorY|| width != ctx.scissorWidth || height != ctx.scissorHeight ) {
			ctx.scissorX = x;
			ctx.scissorY = y;
			ctx.scissorWidth = width;
			ctx.scissorHeight = height;
			glScissor(x, y, width, height);
		}
	}

	@Override
	public void clearClipRect() {
		if (ctx.scissorTest) {
			ctx.scissorTest = false;
			glDisable(GL_SCISSOR_TEST);

			ctx.scissorX = 0;
			ctx.scissorY = 0;
			ctx.scissorWidth = 0;
			ctx.scissorHeight = 0;
		}
	}


	@Override
	public void setShader(Shader shader) {
		assert shader != null;

		if (shader.isUpdateRequired()) {
			updateShaderData(shader);
		}

		bindShaderProgram(shader);
		updateShaderUniforms(shader);
	}

	private boolean updateShaderData(Shader shader) {
		int id  = shader.getId();

		if (id == HardwareObject.UNSET_ID) {
			id = glCreateProgram();
			if (id == 0) {
				Log.error("Failed to create a new ShaderProgram: invalid ShaderProgram id ("+ id +") received.");
				throw new RendererExpception("Failed to create a new ShaderProgram");
			}
			shader.setId(id);
			objectManager.register(shader);
		}
		for (ShaderSource source : shader.getShaderSources()) {
			if (source.isUpdateRequired()) {
				if (! updateShaderSource(source)) {
					shader.disableUpdateRequired();
					return false;
				}
			}
			glAttachShader(id, source.getId());
		}


		boolean linkSuccess;
		String infoLog;

		glLinkProgram(id);
		linkSuccess = glGetProgrami(id, GL_LINK_STATUS) == GL_TRUE;
		infoLog = glGetProgramInfoLog(shader.getId());
		if (linkSuccess) {
			Log.debug("Shader link success");
			shader.disableUpdateRequired();
			shader.setValid(true);
			resetUniformLocation(shader);

		} else {
			Log.error("Failed to link shader program {}\n{}", shader, infoLog);
			shader.setValid(false);
		}

		return linkSuccess;

	}


	private boolean updateShaderSource(ShaderSource source) {
		int id = source.getId();
		if (id == HardwareObject.UNSET_ID) {
			id = glCreateShader(toShaderTypeConstant(source.getType()));
			if (id == 0) {
				throw new RendererExpception("Failed to created shader object invalid id received: %d", id);
			}
			source.setId(id);
			objectManager.register(source);
		}

		glShaderSource(id, source.getSource());
		glCompileShader(id);

		boolean compileSuccess = glGetShaderi(id, GL_COMPILE_STATUS) == GL_TRUE;
		if (!compileSuccess) {
			String infoLog = glGetShaderInfoLog(id);
			Log.error("Failed to compile shader {}\n{}", source.toString(), infoLog);
		} else {
			Log.info("Success to compile shader {}", source.toString());
		}
		source.disableUpdateRequired();

		return compileSuccess;

	}

	private void updateShaderUniforms(Shader shader) {
		for (Uniform uniform : shader.getUniforms()) {
			if (uniform.isUpdateRequired()) {
				updateShaderUniform(shader, uniform);
			}
		}
	}

	private void resetUniformLocation(Shader shader) {
		for (Uniform uniform : shader.getUniforms()) {
			uniform.reset();
		}
	}


	private void updateShaderUniform(Shader shader, Uniform uniform) {
		int location = uniform.getLocation();
		ShaderVariable.VariableType type = uniform.getType();

		if (location == ShaderVariable.LOCATION_UNKNOWN) {
			updateUniformLocation(shader, uniform);
			if (uniform.getLocation() == ShaderVariable.LOCATION_NOT_FOUND) {
				// Ensures that and not found uniform variable are handled only once
				uniform.disableUpdateRequired();
				return;
			}
			location = uniform.getLocation();
		}

		if (type == null) {
			Log.error("The {} of the shader {} must have a assigned type.", uniform, shader);
			uniform.disableUpdateRequired();
			return;
		}

		Object value = uniform.getValue();
		FloatBuffer fb;
		switch (type) {
			case Float:
				float f1 = (float) value;
				glUniform1f(location, f1);
				break;
			case Float2:
				float[] fv2 = (float[]) value;
				glUniform2f(location, fv2[0], fv2[1]);
				break;
			case Float3:
				float[] fv3 = (float[]) value;
				glUniform3f(location, fv3[0], fv3[1], fv3[2]);
				break;
			case Float4:
				float[] fv4 = (float[]) value;
				glUniform4f(location, fv4[0], fv4[1], fv4[2], fv4[3]);
				break;
			case Int:
				int int1 = (int) value;
				glUniform1i(location, int1);
				break;
			case Int2:
				int[] int2 = (int[]) value;
				glUniform2i(location, int2[0], int2[1]);
				break;
			case Int3:
				int[] int3 = (int[]) value;
				glUniform3i(location, int3[0], int3[1], int3[2]);
				break;
			case Int4:
				int[] int4 = (int[]) value;
				glUniform4i(location, int4[0], int4[1], int4[2], int4[3]);
				break;
			case Matrix3x3:
				fb = (FloatBuffer) value;
				glUniformMatrix3fv(location, false, fb);
				break;
			case Matrix4x4:
				fb = (FloatBuffer) value;
				glUniformMatrix4fv(location, false, fb);
				break;
		}

		uniform.disableUpdateRequired();
	}

	private void updateUniformLocation(Shader shader, ShaderVariable shaderVariable) {
		String name = shaderVariable.getName();
		int location = glGetUniformLocation(shader.getId(), name);
		if (location == ShaderVariable.LOCATION_NOT_FOUND) {
			Log.warn("Could not find the uniform variable {} in the shader {}", name, shader);
		}
		shaderVariable.setLocation(location);
	}

	private void bindShaderProgram(Shader shader) {
		int shaderId = shader.getId();
		if (ctx.boundShader == null || ctx.boundShader.getId() != shaderId || shader.isUpdateRequired()) {
			if (shader.isValid()) {
				glUseProgram(shaderId);
				ctx.boundShader = shader;
			}
		}
	}

	private int toShaderTypeConstant(ShaderType type) {
		switch (type) {
			case VERTEX:
				return GL_VERTEX_SHADER;
			case FRAGMENT:
				return GL_FRAGMENT_SHADER;
			case GEOMETRY:
				return GL_GEOMETRY_SHADER;
			case COMPUTE:
				return GL_COMPUTE_SHADER;
		}
		throw new IllegalArgumentException("Invalid Shader type specified");
	}

	@Override
	public void deleteShader(Shader shader) {
		if (shader.getId() == HardwareObject.UNSET_ID) {
			Log.warn("Shader Program is not uploaded to GPU, cannot be deleted");
			return;
		}

		for (ShaderSource source : shader.getShaderSources()) {
			if (source.getId() != HardwareObject.UNSET_ID && source.getId() >= 0) {
				glDetachShader(shader.getId(), source.getId());
				deleteShaderSource(source);
			}
		}

		glDeleteProgram(shader.getId());
		shader.resetObject();
	}

	@Override
	public void deleteShaderSource(ShaderSource source) {
		if (source.getId() == HardwareObject.UNSET_ID) {
			Log.warn("Shader is not uploaded to GPU, cannot be deleted");
			return;
		}
		glDeleteShader(source.getId());
		source.enableUpdateRequired();
		source.resetObject();
	}

	@Override
	public void setTexture(int unit, Texture texture) {
		if (texture != null && texture.isUpdateRequired()) {
			updateTextureData(texture, unit);
		}

		bindTexture(texture, unit);
	}

	private void updateTextureData(Texture texture, int unit) {
		boolean created = false;
		int id = texture.getId();
		glGetError();

		if (id == HardwareObject.UNSET_ID) {
			id = glGenTextures();
			texture.setId(id);
			objectManager.register(texture);
			created = true;
		}

		int target = bindTexture(texture, unit);

		// Only upload the image if one is set
		ByteBuffer imageBuffer = null;
		if (texture.getImage() != null) {
			Image image = texture.getImage();
			imageBuffer = image.getReader().getBuffer();
			imageBuffer.flip();
		}

		glTexParameteri(target, GL_TEXTURE_BASE_LEVEL, 0);

		if (created) {
			glTexImage2D(target, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
			glGenerateMipmap(target);
		}

		if (imageBuffer != null && texture.isUpdateRequired()) {
			glTexSubImage2D(target, 0, 0, 0, texture.getWidth(), texture.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

			if (texture.getMinFilter().requiresMipMaps()) {
				if (caps.OpenGL30) {
					glGenerateMipmap(target);
				}
			}
		}


		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, convertToMagFilter(texture.getMagFilter()));
		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, convertToMinFilter(texture.getMinFilter()));

		switch (texture.getType()) {
			case DIMENSION_3D:
				glTexParameteri(target, GL_TEXTURE_WRAP_R, convertToWrapMode(texture.getWrapMode(Texture.TextureAxis.R)));
			case DIMENSION_2D:
				glTexParameteri(target, GL_TEXTURE_WRAP_T, convertToWrapMode(texture.getWrapMode(Texture.TextureAxis.T)));
			case DIMENSION_1D:
				glTexParameteri(target, GL_TEXTURE_WRAP_S, convertToWrapMode(texture.getWrapMode(Texture.TextureAxis.S)));
		}

		if (texture.getAnisotropicLevel() >= 1.0) {
			if (caps.GL_EXT_texture_filter_anisotropic) {
				glTexParameterf(target, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, texture.getAnisotropicLevel());
			}
		}

		texture.disableUpdateRequired();
	}

	private int convertToWrapMode(Texture.WrapMode wrapMode) {
		switch (wrapMode) {
			case Repeast: return GL_REPEAT;
			case ClampEDGE: return GL_CLAMP_TO_EDGE;
			case ClampBORDER: return GL_CLAMP_TO_BORDER;
			case MirroredRepeat: return GL_MIRRORED_REPEAT;
		}

		throw new IllegalArgumentException("Unsupported WrapMode specified" + wrapMode);
	}

	private int convertToMinFilter(Texture.MinFilter minFilter) {
		switch (minFilter) {
			case NearestNeighbour: return GL_NEAREST;
			case Bilinear: return GL_LINEAR;
			case Trilinear: return GL_LINEAR_MIPMAP_LINEAR;
			case LINEAR_MIPMAP_NEAREST: return GL_LINEAR_MIPMAP_NEAREST;
			case NEAREST_MIPMAP_LINEAR: return GL_NEAREST_MIPMAP_LINEAR;
			case NEAREST_MIPMAP_NEAREST: return GL_NEAREST_MIPMAP_NEAREST;
		}

		throw new IllegalArgumentException("Unsupported MinFilter specified" + minFilter);
	}

	private int convertToMagFilter(Texture.MagFilter magFilter) {
		switch (magFilter) {
			case Bilinear: return GL_LINEAR;
			case NearestNeighbour: return GL_NEAREST;
		}
		throw new IllegalArgumentException("Unsupported MagFilter specified" + magFilter);
	}

	private int bindTexture(Texture texture, int unit) {

		unit = Math.max(0, unit);
		if (unit > GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS - 1) {
			String description = "Texture unit limit reached";
			Log.error("{} must be between {} and {}", description, 0, GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
			throw new RendererExpception(description);
		}

		int id = texture.getId();
		if(ctx.activeTextureUnit != unit) {
			glActiveTexture(GL_TEXTURE0 + unit);
			ctx.activeTextureUnit = unit;
		}

		int target = convertTypeToTarget(texture.getType());
		if (ctx.boundTextures[unit] != id) {
			glBindTexture(target, id);
			ctx.boundTextures[unit] = id;
		}

		return target;
	}

	private int convertTypeToTarget(Texture.Dimension type) {
		switch (type) {
			case DIMENSION_1D: return GL_TEXTURE_1D;
			case DIMENSION_2D: return GL_TEXTURE_2D;
			case DIMENSION_3D: return GL_TEXTURE_3D;
			default: throw new RendererExpception("Unsupported texture type detected: {}", type.name());
		}
	}

	@Override
	public void deleteTexture(Texture texture) {
		int id = texture.getId();
		glDeleteTextures(id);
		texture.resetObject();
		deleteImage(texture.getImage());
	}

	@Override
	public void deleteImage(Image image) {

	}

	@Override
	public void drawMesh(Mesh mesh) {
		if (mesh.getVertexCount() <= 0) {
			return;
		}

		if (mesh.getLineWidth() != ctx.lineWith) {
			if (mesh.getLineWidth() <= .0f) throw new RendererExpception("Line width must be greater than zero");
			glLineWidth(mesh.getLineWidth());
			ctx.lineWith = mesh.getLineWidth();
		}

		if (mesh.getPointSize() != ctx.pointSize) {
			if (mesh.getPointSize() <= .0f) throw new RendererExpception("Point size must be greater than zero");
			glPointSize(mesh.getPointSize());
			ctx.pointSize = mesh.getPointSize();
		}

		renderMesh(mesh);

	}

	private void renderMesh(Mesh mesh) {
		if (ctx.boundShader == null) {
			throw new RendererExpception("In order to render a mesh a shader must first bound to the renderer");
		}

		VertexBuffer interleavedBuffer = mesh.getBuffer(VertexBuffer.Type.Interleaved);
		if (interleavedBuffer != null && interleavedBuffer.isUpdateRequired()) {
			updateBuffer(interleavedBuffer);
		}


		for (VertexBuffer buffer : mesh.getBuffers()) {
			VertexBuffer.Type type = buffer.getType();
			if (VertexBuffer.Type.Index.equals(type) ||
				VertexBuffer.Type.Interleaved.equals(type) ||
				VertexBuffer.Type.CpuOnly.equals(type)) continue;

			setVertexAttributes(buffer, interleavedBuffer);
		}




		VertexBuffer indices = mesh.getBuffer(VertexBuffer.Type.Index);
		if (indices != null) {
			drawTrianglesWithIndices(indices, mesh);
		} else {
			glDrawArrays(convertToMode(mesh.getMode()), 0, mesh.getVertexCount());
		}
		clearVertexAttributes();
	}




	private void drawTrianglesWithIndices(VertexBuffer indices, Mesh mesh) {
		if (indices.getType() != VertexBuffer.Type.Index) {
			throw new IllegalArgumentException("An index buffer is required for the indices parameter");
		}

		updateBuffer(indices);
		glDrawElements(
			convertToMode(mesh.getMode()),
			indices.getBuffer().limit(),
			convertToFormat(indices.getPointer().getFormat()),
			0
		);

	}

	private void clearVertexAttributes() {
		if (ctx.boundShader != null) {
			for(Attribute attribute : ctx.boundShader.getAttributes()) {
				int location = attribute.getLocation();
				if (location >= 0) {
					glDisableVertexAttribArray(attribute.getLocation());
				}
			}
		}
	}


	private void setVertexAttributes(VertexBuffer buffer, VertexBuffer interleavedBuffer) {
		assert ctx.boundShader != null;

		Attribute attribute = ctx.boundShader.getAttribute(buffer.getType());
		int location = attribute.getLocation();

		if (location == Attribute.LOCATION_UNKNOWN && attribute.isUpdateRequired()) {
			attribute.bindName(buffer);

			if (attribute.getName() == null) {
				throw new RendererExpception("An attribute requires a name, please consider to set a name for each attribute");
			}

			location = glGetAttribLocation(ctx.boundShader.getId(), attribute.getName());

			if ( location < 0 ) {
				Log.warn("The attribute {} isn't an active attribute in the shader {}.\nThe attribute could not be bounded to the shader.", attribute.getName(), ctx.boundShader.getShaderName());
				attribute.disableUpdateRequired();
				return;
			}

			attribute.setLocation(location);
		}

		// When invalid location lets ignore this attribute for now.
		if (location < 0) return;

		if (interleavedBuffer == null) {
			updateBuffer(buffer);
		} else {
			updateBuffer(interleavedBuffer);
		}

		glEnableVertexAttribArray(location);
		glVertexAttribPointer(
			location,
			buffer.getPointer().getComponents(),
			convertToFormat(buffer.getPointer().getFormat()),
			buffer.getPointer().getNormalized(),
			buffer.getPointer().getStride(),
			buffer.getPointer().getOffset());
	}



	private int convertToMode(Mesh.Mode mode) {
		switch (mode) {
			case POINTS: return GL_POINT;
			case LINES: return GL_LINES;
			case LINE_LOOP: return GL_LINE_LOOP;
			case LINE_STRIP: return GL_LINE_STRIP;
			case TRIANGLES: return GL_TRIANGLES;
			case TRIANGLE_FAN: return GL_TRIANGLE_FAN;
			case TRIANGLE_STRIP: return GL_TRIANGLE_STRIP;
			case QUADS: return GL_QUADS;
			case QUAD_STRIP: return GL_QUAD_STRIP;
			default:
				throw new IllegalArgumentException(
					String.format("Unsupported mesh mode specified %s", mode.name())
				);
		}
	}

	private int convertToFormat(VertexAttributePointer.Format format) {
		switch (format) {
			case Byte: return GL_BYTE;
			case Int: return GL_INT;
			case Short: return GL_SHORT;
			case Float: return  GL_FLOAT;
			case Double: return  GL_DOUBLE;
			case Unsigned_Int: return GL_UNSIGNED_INT;
			case Unsigned_Short: return GL_UNSIGNED_SHORT;
			case Unsingned_Byte: return GL_UNSIGNED_BYTE;
			default:
				throw new IllegalArgumentException(String.format("Unsupported format specified %s", format));

		}
	}


	@Override
	public void updateBuffer(VertexBuffer buffer) {
		int target;
		int bufferId = buffer.getId();
		boolean bufferCreated  = false;

		if (bufferId == HardwareObject.UNSET_ID) {
			bufferId = glGenBuffers();
			if (bufferId == GL_INVALID_VALUE) {
				throw new RendererExpception("Invalid buffer object name returned, creation of buffer object failed");
			}
			buffer.setId(bufferId);
			bufferCreated = true;
			objectManager.register(buffer);
		}


		if (buffer.getType() == VertexBuffer.Type.Index) {
			target = GL_ELEMENT_ARRAY_BUFFER;
			if (ctx.boundElementArrayVboBuffer != bufferId) {
				glBindBuffer(target, bufferId);
				ctx.boundElementArrayVboBuffer = bufferId;
			}
		} else {
			target = GL_ARRAY_BUFFER;
			if (ctx.boundVboBuffer != bufferId) {
				glBindBuffer(target, bufferId);
				ctx.boundVboBuffer = bufferId;
			}
		}

		buffer.getBuffer().rewind();
		int usage = convertToUsageConstant(buffer.getUsage());
		if (bufferCreated) {
			switch (buffer.getPointer().getFormat()) {
				case Byte:
				case Unsingned_Byte:
					glBufferData(target, (ByteBuffer) buffer.getBuffer(), usage);
					break;
				case Short:
				case Unsigned_Short:
					glBufferData(target, (ShortBuffer) buffer.getBuffer(), usage);
					break;
				case Int:
				case Unsigned_Int:
					glBufferData(target, (IntBuffer) buffer.getBuffer(), usage);
					break;
				case Float:
					glBufferData(target, (FloatBuffer) buffer.getBuffer(), usage);
					break;
				case Double:
					glBufferData(target, (DoubleBuffer) buffer.getBuffer(), usage);
					break;
				default:
					throw new RendererExpception("Unknown buffer format");

			}
		} else if(buffer.isUpdateRequired()) {
			switch (buffer.getPointer().getFormat()) {
				case Byte:
				case Unsingned_Byte:
					glBufferSubData(target, 0,(ByteBuffer) buffer.getBuffer());
					break;
				case Short:
				case Unsigned_Short:
					glBufferSubData(target, 0,(ShortBuffer) buffer.getBuffer());
					break;
				case Int:
				case Unsigned_Int:
					glBufferSubData(target, 0, (IntBuffer) buffer.getBuffer());
					break;
				case Float:
					glBufferSubData(target, 0,  (FloatBuffer) buffer.getBuffer());
					break;
				case Double:
					glBufferSubData(target, 0,  (DoubleBuffer) buffer.getBuffer());
					break;
				default:
					throw new RendererExpception("Unknown buffer format");

			}
		}
		buffer.disableUpdateRequired();
	}

	private int convertToUsageConstant(VertexBuffer.Usage usage) {
		switch (usage) {
			case DYNAMIC_COPY: return GL_DYNAMIC_COPY;
			case DYNAMIC_DRAW: return GL_DYNAMIC_DRAW;
			case DYNMAIC_READ: return GL_DYNAMIC_READ;
			case STATIC_COPY:  return GL_STATIC_COPY;
			case STATIC_DRAW:  return GL_STATIC_DRAW;
			case STATIC_READ:  return GL_STATIC_READ;
			case STREAM_COPY:  return GL_STREAM_COPY;
			case STREAM_DRAW:  return GL_STATIC_DRAW;
			case STREAM_READ:  return GL_STATIC_READ;
			default:
				throw new RendererExpception("Unrecognized usage specified.");
		}

	}

	@Override
	public void deleteBuffer(VertexBuffer buffer) {
		int id = buffer.getId();
		if (id != HardwareObject.UNSET_ID) {
			glDeleteBuffers(id);
			buffer.resetObject();
		}

	}

	@Override
	public void applyRenderState(RenderState state) {

		if (state.isWireframe() && !ctx.wireframe) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			ctx.wireframe = true;
		} else if (!state.isWireframe() && ctx.wireframe) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			ctx.wireframe = false;
		}

		if (state.getDepthTestMode() != ctx.depthTestMode) {
			if (toggleEnable(GL_DEPTH_TEST, state.getDepthTestMode() != RenderState.TestFunc.Off)) {
				glDepthFunc(toTestFunction(state.getDepthTestMode()));
			}
		}

		if (state.getCullFaceMode() != ctx.cullFaceMode) {
			if (toggleEnable(GL_CULL_FACE, state.getCullFaceMode() != RenderState.CullFaceMode.Off)) {
				glCullFace(toCullFaceMode(state.getCullFaceMode()));
			}
			ctx.cullFaceMode = state.getCullFaceMode();
		}

		if (state.getFrontFaceWinding() != ctx.frontFaceWinding) {
			int winding = (state.getFrontFaceWinding() == RenderState.FaceWinding.GL_CCW) ? GL_CCW : GL_CW;
			glFrontFace(winding);
			ctx.frontFaceWinding = state.getFrontFaceWinding();
		}

		if (state.getBlendMode() != ctx.blendMode) {
			switch (state.getBlendMode()) {
				case Off:
					glDisable(GL_BLEND);
					break;
				case Default:
					glEnable(GL_BLEND);
					glBlendFunc(GL_ONE, GL_ZERO);
					break;
				case Additive:
					glEnable(GL_BLEND);
					glBlendFunc(GL_ONE, GL_ONE);
					break;
				case Alpha:
					glEnable(GL_BLEND);
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					break;
				case Color:
					glEnable(GL_BLEND);
					glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);

			}
			ctx.blendMode = state.getBlendMode();
		}

		if (state.getPointSize() != ctx.pointSize) {
			glPointSize(state.getPointSize());
			ctx.pointSize = state.getLineWidth();
		}

		if (toggleEnable(GL_LINE_SMOOTH, state.isSmoothLinesEnabled() != ctx.lineSmooth)) {
			ctx.lineSmooth = state.isSmoothLinesEnabled();
		}

		if (state.getLineWidth() != ctx.lineWith) {
			glLineWidth(state.getLineWidth());
			ctx.lineWith = state.getLineWidth();
		}
	}

	private boolean toggleEnable(int cap, boolean enable) {
		if (enable) {
			glEnable(cap);
		} else {
			glDisable(cap);
		}
		return enable;
	}

	private int toTestFunction(RenderState.TestFunc testFunc) {
		switch (testFunc) {
			case Always: return GL_ALWAYS;
			case Equal: return GL_EQUAL;
			case Greater: return GL_GREATER;
			case GreaterOrEqual: return GL_GEQUAL;
			case Less: return GL_LESS;
			case LessOrEqual: return GL_LEQUAL;
			case Never: return GL_NEVER;
			case NotEqual: return GL_NOTEQUAL;
			default : throw new IllegalArgumentException("Unsupported test function specified");
		}
	}

	private int toCullFaceMode(RenderState.CullFaceMode cullFaceMode) {
		switch (cullFaceMode) {
			case Back: return GL_BACK;
			case Front: return GL_FRONT;
			case FrontAndBack: return GL_FRONT_AND_BACK;
			case Off: throw new IllegalArgumentException("Unsupported cull face mode specified");
		}
		return 0;
	}

	@Override
	public void invalidateState() {
		ctx.reset();
	}

	@Override
	public void cleanUp() {
		invalidateState();
		objectManager.deleteAllObjects();
		Log.info("Clean up all renderer resources");
	}

	@Override
	public void resetGLObjects() {
		objectManager.resetAllObjects();
	}

	@Override
	public void onNewFrame() {
		objectManager.deleteAllUnused();
	}
}
