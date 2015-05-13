package com.liyu.itester.gyro;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Implement a simple rotation control.
 * 
 */
public class TouchSurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private CubeRenderer mRenderer;
	private float mPreviousX;
	private float mPreviousY;
	private float mPreviousZ;
	
	public TouchSurfaceView(Context context,AttributeSet attrs) {
		super(context,attrs);
		mRenderer = new CubeRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}


	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mRenderer.mAngleX = mRenderer.mAngleY = mRenderer.mAngleZ = 0f;
		return true;
	}

	public void updateGyro(float x, float y, float z) {

		float dx = x - mPreviousX;
		float dy = y - mPreviousY;
		float dz = z - mPreviousZ;

		mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
		mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
		mRenderer.mAngleZ += dz * TOUCH_SCALE_FACTOR;

		requestRender();

		mPreviousX = x;
		mPreviousY = y;
		mPreviousZ = z;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mRenderer.mAngleX = mRenderer.mAngleY = mRenderer.mAngleZ = 0f;
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * Render a cube.
	 */
	private class CubeRenderer implements GLSurfaceView.Renderer {
		public CubeRenderer() {
			mCube = new Cube();
		}

		public void onDrawFrame(GL10 gl) {
			/*
			 * Usually, the first thing one might want to do is to clear the
			 * screen. The most efficient way of doing this is to use glClear().
			 */

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			/*
			 * Now we're ready to draw some 3D objects
			 */

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -6.0f);

			gl.glRotatef(mAngleY, 1, 0, 0);
			gl.glRotatef(mAngleX, 0, 1, 0);
			gl.glRotatef(mAngleZ, 0, 0, 1);

			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

			mCube.draw(gl);
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);

			/*
			 * Set our projection matrix. This doesn't have to be done each time
			 * we draw, but usually a new projection needs to be set when the
			 * viewport is resized.
			 */

			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			/*
			 * By default, OpenGL enables features that improve quality but
			 * reduce performance. One might want to tweak that especially on
			 * software renderer.
			 */
			gl.glDisable(GL10.GL_DITHER);

			/*
			 * Some one-time OpenGL initialization can be made here probably
			 * based on features of this particular context
			 */
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glClearColor(1, 1, 1, 1);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}

		private Cube mCube;

		public float mAngleX;
		public float mAngleY;
		public float mAngleZ;
	}

	
}