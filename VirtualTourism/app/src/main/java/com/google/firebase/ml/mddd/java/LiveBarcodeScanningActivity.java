/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.ml.mddd.java;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.common.base.Objects;
import com.google.firebase.ml.mddd.R;
//import com.google.firebase.ml.md.dash;
import com.google.firebase.ml.mddd.home;
import com.google.firebase.ml.mddd.java.barcodedetection.BarcodeProcessor;
import com.google.firebase.ml.mddd.java.barcodedetection.BarcodeResultFragment;
import com.google.firebase.ml.mddd.java.camera.CameraSource;
import com.google.firebase.ml.mddd.java.camera.CameraSourcePreview;
import com.google.firebase.ml.mddd.java.camera.GraphicOverlay;
import com.google.firebase.ml.mddd.java.camera.WorkflowModel;
import com.google.firebase.ml.mddd.java.camera.WorkflowModel.WorkflowState;
import com.google.firebase.ml.mddd.java.settings.SettingsActivity;
import com.google.firebase.ml.mddd.profile;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.io.IOException;
import java.util.List;

/** Demonstrates the barcode scanning workflow using camera preview. */
public class LiveBarcodeScanningActivity extends AppCompatActivity implements OnClickListener {

  private static final String TAG = "LiveBarcodeActivity";

  private CameraSource cameraSource;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private View settingsButton;
  private View flashButton;
  private Chip promptChip;
  private AnimatorSet promptChipAnimator;
  private WorkflowModel workflowModel;
  private WorkflowState currentWorkflowState;
  LinearLayout mBottomSheet;
  TextView t1,t2;
    TextView txtviewExample;
  FirebaseVisionImage image12;
  BottomSheetBehavior mBottombehavior;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_live_barcode);
    preview = findViewById(R.id.camera_preview);
    graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    graphicOverlay.setOnClickListener(this);
    cameraSource = new CameraSource(graphicOverlay);

    promptChip = findViewById(R.id.bottom_prompt_chip);
    promptChipAnimator =
        (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
    promptChipAnimator.setTarget(promptChip);

    mBottomSheet = findViewById(R.id.bottom_sheet);
    mBottombehavior = BottomSheetBehavior.from(mBottomSheet);
    t1 = findViewById(R.id.textView);
      t2 = findViewById(R.id.textView1);
      mBottombehavior.setPeekHeight(0);

    findViewById(R.id.close_button).setOnClickListener(this);
    flashButton = findViewById(R.id.flash_button);
    flashButton.setOnClickListener(this);
    settingsButton = findViewById(R.id.settings_button);
    settingsButton.setOnClickListener(this);
//    image12 = FrameProcessorBase.helpimage;


      txtviewExample = new TextView(this);

    setUpWorkflowModel();

//    t1.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            startCameraPreview();
//           Toast.makeText(LiveBarcodeScanningActivity.this, t1.getText(), Toast.LENGTH_SHORT).show();
//        }
//    });
//      t2.setOnClickListener(new OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              if (t2.getText().toString().equals("NULL")) {
//
//              } else {
//                  startCameraPreview();
//                  Toast.makeText(LiveBarcodeScanningActivity.this, t2.getText(), Toast.LENGTH_SHORT).show();
//              }
//          }
//      });


      BottomNavigationView navView = findViewById(R.id.nav_view);
      // Passing each menu ID as a set of Ids because each
      navView.setSelectedItemId(R.id.navigation_scan);

      navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId())
            {
                  case R.id.navigation_profile :
                      startActivity(new Intent(getApplicationContext(), profile.class));
                      overridePendingTransition(0,0);
                      break;

                case R.id.navigation_home :
                      startActivity(new Intent(getApplicationContext(), home.class));
                      overridePendingTransition(0,0);
                      break;
                  case R.id.navigation_scan :
                      startActivity(new Intent(getApplicationContext(), LiveBarcodeScanningActivity.class));
                      overridePendingTransition(0,0);
                      break;
              }
              return false;
          }
      });


  }

  @Override
  protected void onResume() {
    super.onResume();

    workflowModel.markCameraFrozen();
    settingsButton.setEnabled(true);
    currentWorkflowState = WorkflowState.NOT_STARTED;
    cameraSource.setFrameProcessor(new BarcodeProcessor(graphicOverlay, workflowModel));
    workflowModel.setWorkflowState(WorkflowState.DETECTING);
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    BarcodeResultFragment.dismiss(getSupportFragmentManager());
  }

  @Override
  protected void onPause() {
    super.onPause();
    currentWorkflowState = WorkflowState.NOT_STARTED;
    stopCameraPreview();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
      cameraSource = null;
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.close_button) {
      onBackPressed();

    } else if (id == R.id.flash_button) {
      if (flashButton.isSelected()) {
        flashButton.setSelected(false);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      } else {
        flashButton.setSelected(true);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      }

    } else if (id == R.id.settings_button) {
      // Sets as disabled to prevent the user from clicking on it too fast.
      settingsButton.setEnabled(false);
      startActivity(new Intent(this, SettingsActivity.class));
    }
  }

//  public void createTextView(String lmark){
//
//           // int a =10;
//
//          LinearLayout.LayoutParams paramsExample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,4.0f);
//          txtviewExample.setId(View.generateViewId());
//          //txtviewExample.setBackgroundResource(R.color.black);
//          txtviewExample.setGravity(Gravity.CENTER);
//          txtviewExample.setTextColor(getResources().getColor(android.R.color.black));
//         // paramsExample.setMargins(20, 20, 20, 20);
//          txtviewExample.setPadding(0, 16, 0, 16);
//          //txtviewExample.setTextSize(40);
//          txtviewExample.setText(lmark);
//
//          //if I comment out the following line, this TextView displays.
//          //if I leave it in, it doesn't display.
//          txtviewExample.setLayoutParams(paramsExample);
//          mBottomSheet.addView(txtviewExample);
//  }

  private void startCameraPreview() {
    if (!workflowModel.isCameraLive() && cameraSource != null) {
      try {
        workflowModel.markCameraLive();
        preview.start(cameraSource);
      } catch (IOException e) {
        Log.e(TAG, "Failed to start camera preview!", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  private void stopCameraPreview() {
    if (workflowModel.isCameraLive()) {
      workflowModel.markCameraFrozen();
      flashButton.setSelected(false);
      preview.stop();
    }
  }

  private void extraceck()
  {
      //added code
      FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance()
              .getVisionCloudLandmarkDetector();

      Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image12)
              .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLandmark>>() {
                  @Override
                  public void onSuccess(List<FirebaseVisionCloudLandmark> firebaseVisionCloudLandmarks) {
                      // Task completed successfully
                      // ...
                      for (FirebaseVisionCloudLandmark landmark : firebaseVisionCloudLandmarks) {

                          Rect bounds = landmark.getBoundingBox();
                          String landmarkName = landmark.getLandmark();
                          String entityId = landmark.getEntityId();
                          float confidence = landmark.getConfidence();
                          //if(confidence>.5)
                          //{
                          Toast.makeText(LiveBarcodeScanningActivity.this, landmarkName, Toast.LENGTH_SHORT).show();

                          Toast.makeText(LiveBarcodeScanningActivity.this, Double.toString(confidence), Toast.LENGTH_SHORT).show();
                          //}

                          // Multiple locations are possible, e.g., the location of the depicted
                          // landmark and the location the picture was taken.
                          for (FirebaseVisionLatLng loc : landmark.getLocations()) {
                              double latitude = loc.getLatitude();
                              double longitude = loc.getLongitude();
                          }
                      }
                  }
              });

      //added ends
  }

  private void setUpWorkflowModel() {
    workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

    // Observes the workflow state changes, if happens, update the overlay view indicators and
    // camera preview state.
    workflowModel.workflowState.observe(
        this,
        workflowState -> {
          if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
            return;
          }

          currentWorkflowState = workflowState;
          Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

          boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

          switch (workflowState) {
            case DETECTING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_point_at_a_barcode);
              startCameraPreview();
              break;
            case CONFIRMING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_move_camera_closer);
              startCameraPreview();
              break;
            case SEARCHING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_searching);
              stopCameraPreview();
              break;
            case DETECTED:
            case SEARCHED:
                t1.setText(BarcodeProcessor.s[0]);
               t2.setText(BarcodeProcessor.s[1]);
                if(t2.getText().toString().equals(""))
                {
                    t2.setText("NULL");
                   // createTextView(BarcodeProcessor.s[1]);
                }
                mBottombehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                promptChip.setVisibility(View.GONE);
              stopCameraPreview();
            //  extraceck();
              break;
            default:
              promptChip.setVisibility(View.GONE);
              break;
          }

          boolean shouldPlayPromptChipEnteringAnimation =
              wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
          if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
            promptChipAnimator.start();
          }
        });


    workflowModel.detectedBarcode.observe(
        this,
        barcode -> {
          if (barcode != null) {
            //ArrayList<BarcodeField> barcodeFieldList = new ArrayList<>();
              //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
           // barcodeFieldList.add(new BarcodeField("Landmark name", barcode.getLandmark()));
            //BarcodeResultFragment.show(getSupportFragmentManager(), barcodeFieldList);
          }
        });
  }
}
