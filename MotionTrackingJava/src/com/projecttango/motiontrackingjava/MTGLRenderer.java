/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projecttango.motiontrackingjava;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.projecttango.tangoutils.Renderer;
import com.projecttango.tangoutils.renderables.CameraFrustumAndAxis;
import com.projecttango.tangoutils.renderables.CameraFrustum;
import com.projecttango.tangoutils.renderables.Grid;
import com.projecttango.tangoutils.renderables.Trajectory;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

/**
 * OpenGL rendering class for the Motion Tracking API sample. This class
 * managers the objects visible in the OpenGL view which are the
 * {@link CameraFrustum}, {@link CameraFrustumAndAxis}, {@link Trajectory}, and
 * the {@link Grid}. These objects are implemented in the TangoUtils library in
 * the package {@link com.projecttango.tangoutils.renderables}.
 * 
 * This class receives also handles the user-selected camera view, which can be
 * 1st person, 3rd person, or top-down.
 */
public class MTGLRenderer extends Renderer implements GLSurfaceView.Renderer {

    private Trajectory mTrajectory;
    private CameraFrustum mCameraFrustum;
    private CameraFrustumAndAxis mCameraFrustumAndAxis;
    private Grid mFloorGrid;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set background color and enable depth testing
        GLES20.glClearColor(1f, 1f, 1f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // resetModelMatCalculator();
        mCameraFrustum = new CameraFrustum();
        mFloorGrid = new Grid();
        mCameraFrustumAndAxis = new CameraFrustumAndAxis();
        mTrajectory = new Trajectory(3);

        // Construct the initial view matrix
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, 5f, 5f, 5f, 0f, 0f, 0f, 0f, 1f, 0f);
        mCameraFrustumAndAxis.setModelMatrix(getModelMatCalculator()
                .getModelMatrix());

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mCameraAspect = (float) width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, THIRD_PERSON_FOV,
                mCameraAspect, CAMERA_NEAR, CAMERA_FAR);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mTrajectory.draw(getViewMatrix(), mProjectionMatrix);
        mFloorGrid.draw(getViewMatrix(), mProjectionMatrix);
        mCameraFrustumAndAxis.draw(getViewMatrix(), mProjectionMatrix);
    }

    public CameraFrustum getCameraFrustum() {
        return mCameraFrustum;
    }

    public CameraFrustumAndAxis getCameraFrustumAndAxis() {
        return mCameraFrustumAndAxis;
    }

    public Trajectory getTrajectory() {
        return mTrajectory;
    }

}
