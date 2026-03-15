package org.example;


import java.io.IOException;
import java.net.URISyntaxException;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;

import  org.lwjgl.opengl.GL;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL33.*;

import static org.lwjgl.system.MemoryUtil.NULL;

public class App {

	
	private long pWindow;
	private int pVao;
	private int pTime;

    public static void main(String[] args) {
        
		var app = new App();
		app.run();
		
    }
	
	public void run() {
		
		createWindow();
		int ptr = shaderProgramLinkerAndCompiler();
		objects();
		renderLoop(ptr);
		cleanup();
	}
	
	public int shaderProgramLinkerAndCompiler() {
		
		Loader loader = new Loader();
		
		String vertexSource = "";
	    String pixelSource = "";
		
		try{
		  vertexSource = loader.loadShader("/Shaders/vertex.shader");
		   pixelSource = loader.loadShader("/Shaders/pixel.shader");
		} catch(IOException | URISyntaxException e) {
			e.printStackTrace();
		}		
		
		int pVShader = glCreateShader(GL_VERTEX_SHADER);
		int pPShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(pVShader , vertexSource);
		glShaderSource(pPShader , pixelSource);
		
		glCompileShader(pVShader);
		glCompileShader(pPShader);
		
		
		//compile errors
		
		if(glGetShaderi(pVShader , GL_COMPILE_STATUS) == GL_FALSE) {
			
			System.err.println("vertex: ");
			System.err.println(glGetShaderInfoLog(pVShader));
		}
		if(glGetShaderi(pPShader , GL_COMPILE_STATUS) == GL_FALSE) {
			
			System.err.println("pixel: ");
			System.err.println(glGetShaderInfoLog(pPShader));
		}
		
		
		
		int pProgram = glCreateProgram();
		glAttachShader(pProgram , pVShader);
		glAttachShader(pProgram , pPShader);
		
		glLinkProgram(pProgram);
		glDeleteShader(pVShader);
		glDeleteShader(pPShader);
		
		
		//link
		
		if(glGetProgrami(pProgram , GL_LINK_STATUS) == GL_FALSE) {
			
			System.err.println("program: ");
			System.err.println(glGetShaderInfoLog(pProgram));
		}
		
		return pProgram;
		
	}
	
	private void objects() {
		
		float[] verticies = {
		
			0.0f , 0.5f,
			0.0f , -0.5f,
			0.5f , 0.0f
		
		};
		
		float[] vertexColor = {
			
			1.0f , 0.0f , 0.0f , 
			0.0f , 1.0f , 0.0f , 
			0.0f , 0.0f , 1.0f 
		};
		
		
		pVao = glGenVertexArrays();
		glBindVertexArray(pVao);
		
		
		int pVbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER , pVbo);
		glBufferData(GL_ARRAY_BUFFER , verticies , GL_DYNAMIC_DRAW);
			
		
		
		glVertexAttribPointer(
		
			0 , 
			2 , 
			GL_FLOAT , 
			false , 
			0 , 
			0 
			
			);
			
			glEnableVertexAttribArray(0);
			
			
			int cVbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER , cVbo);
		glBufferData(GL_ARRAY_BUFFER , vertexColor , GL_DYNAMIC_DRAW);
			
			glVertexAttribPointer(
			
			1 , 
			3 , 
			GL_FLOAT , 
			false , 
			0 , 
			0
			
			);
		
		glEnableVertexAttribArray(1);
		
		glBindVertexArray(0);
	}
	
	private void createWindow() {
		
		if(!glfwInit()) throw new RuntimeException("glfw init exception");
		
		glfwWindowHint(GLFW_VISIBLE , GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR , 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR , 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE , GLFW_OPENGL_CORE_PROFILE);
		
		pWindow = glfwCreateWindow(800 , 600 , "window" , 0 , 0);
		
		if(pWindow == NULL) throw new RuntimeException("window failed to create");
		glfwMakeContextCurrent(pWindow);
		GL.createCapabilities();
		
		glClearColor(0.0f , 0.0f , 0.0f , 1.0f);
	}
	
	private void renderLoop(int pProgram) {
		
		while(!glfwWindowShouldClose(pWindow)) {
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);
			glUseProgram(pProgram);
			
			
			float time = glfwGetTime();
			glUnifrom1f(pTime, time);
			
			
			glBindVertexArray(pVao);
			glDrawArrays(GL_TRIANGLES , 0 , 3);
			glBindVertexArray(0);
			
			
			glfwSwapBuffers(pWindow);
		}
	}
	
	private void cleanup() {
		
		glfwDestroyWindow(pWindow);
		glfwTerminate();
	}
}
