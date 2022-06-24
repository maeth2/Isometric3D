package util;

import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import main.Window;

public class BufferHelper {
	private static List<Integer> rbos = new ArrayList<Integer>();
	private static List<Integer> fbos = new ArrayList<Integer>();

	/**
	 * Create an OpenGL FBO
	 * @return		FBO ID
	 */
	private static int generateFBO() {
		int id = glGenFramebuffers();
		fbos.add(id);
		return id;
	}
	
	/**
	 * Create an OpenGL RBO
	 * @return		RBO ID
	 */
	private static int generateRBO() {
		int id = glGenRenderbuffers();
		rbos.add(id);
		return id;
	}
	
	/**
	 * Binds frame buffer
	 * 
	 * @param fbo			FBO ID
	 * @param width			Width of frame
	 * @param height		Height of frame
	 */
	public static void bindFrameBuffer(int fbo, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, width, height);
	}
	
	/**
	 * Unbinds frame buffer
	 * 
	 * @param fbo			FBO ID
	 * @param width			Width of frame
	 * @param height		Height of frame
	 */
	public static void unbindFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Window.WIDTH, Window.HEIGHT + 100);
	}
	
	/**
	 * Creates a Frame Buffer 
	 * @return			Frame Buffer ID
	 */
	public static int createFrameBuffer(int width, int height) {
		int id = generateFBO();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		createDepthBufferAttachment(width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return id;
	}
	
	/**
	 * Creates a depth buffer attachment
	 * @return			Depth Buffer ID
	 */
	private static int createDepthBufferAttachment(int width, int height) {
		int id = generateRBO();
		glBindRenderbuffer(GL_RENDERBUFFER, id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, id);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		return id;
	}
	
	/**
	 * Cleans up all FBOs and RBOs from memory
	 */
	public static void dispose() {
		for(int fbo : fbos) {
			glDeleteFramebuffers(fbo);
		}
		for(int rbo : rbos) {
			glDeleteRenderbuffers(rbo);
		}
	}
}
