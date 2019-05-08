package com.ragshion.ayosekolah.mapbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.ragshion.ayosekolah.R;

import org.json.JSONObject;

/**
 * Use the place picker functionality inside of the Places Plugin, to show UI for
 * choosing a map location. Once selected, return to the previous location with a
 * CarmenFeature to extract information from for whatever use that you want.
 */

public class PlaceSelectionPluginActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 5678;
  private TextView selectedLocationTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_selection);
    selectedLocationTextView = findViewById(R.id.selected_location_info_textview);
    Mapbox.getInstance(this, "sk.eyJ1IjoicmFnc2hpb24iLCJhIjoiY2p1dXVzdzR5MGVneTRlbm9reDNxMXFseCJ9.xehhCJkskfVTk_2XmCZk5w");


    goToPickerActivity();
  }

  /**
   * Set up the PlacePickerOptions and startActivityForResult
   */
  private void goToPickerActivity() {
    startActivityForResult(
      new PlacePicker.IntentBuilder()
        .accessToken(getString(R.string.access_token))
        .placeOptions(PlacePickerOptions.builder()
          .statingCameraPosition(new CameraPosition.Builder()
            .target(new LatLng(40.7544, -73.9862)).zoom(16).build())
          .build())
        .build(this), REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_CANCELED) {
      // Show the button and set the OnClickListener()
      Button goToPickerActivityButton = findViewById(R.id.go_to_picker_button);
      goToPickerActivityButton.setVisibility(View.VISIBLE);
      goToPickerActivityButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          goToPickerActivity();
        }
      });
    } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      // Retrieve the information from the selected location's CarmenFeature
      CarmenFeature carmenFeature = PlacePicker.getPlace(data);
      Toast.makeText(this, carmenFeature.geometry().toJson(), Toast.LENGTH_SHORT).show();
        selectedLocationTextView.setText(carmenFeature.geometry().toJson());

        try{
          JSONObject jsonRESULTS = new JSONObject(carmenFeature.geometry().toJson());
          String[] separated = jsonRESULTS.getString("coordinates").split(",");
          StringBuilder builder;
          builder = new StringBuilder( separated[0]);
          String lat = builder.deleteCharAt(0).toString();
          builder = new StringBuilder(separated[1]);
          String lng = builder.deleteCharAt(separated[1].length() - 1).toString();

          selectedLocationTextView.setText("Latitude : "+lat+" Longitude : "+lng);





        }catch (Exception e){

        }

    }
  }
}
