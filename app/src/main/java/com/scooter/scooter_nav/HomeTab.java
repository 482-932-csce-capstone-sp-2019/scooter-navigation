package com.scooter.scooter_nav;

import android.Manifest;
import android.content.Context;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.List;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

// classes needed to initialize map
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.location.LocationComponent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.widget.Toast;




public class HomeTab extends Fragment implements OnMapReadyCallback, PermissionsListener, LocationListener {


    public final int CAMERA_ZOOM = 15;

    // variables for displaying map
    private MapView mapView;
    private MapboxMap mapboxMap;

    private static final int MY_PERMISSIONS_ACCESS_LOCATION = 1;

    // variables for locating user
    private LocationComponent locationComponent;
    LocationManager lm;
    private PermissionsManager permissionsManager;

    private Location currentLocation;

    // view variables
    private FloatingActionButton locationButton;
    private FloatingActionButton navigationButton;

    public static HomeTab newInstance() {
        return new HomeTab();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("scooter_app", "onCreateView: starting view");
        Mapbox.getInstance(getContext(), getString(R.string.access_token));
        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        View rootView = inflater.inflate(R.layout.home_tab_fragment, container, false);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        lm = (LocationManager)this.getContext().getSystemService(Context.LOCATION_SERVICE);


        locationButton = rootView.findViewById(R.id.location_button);
        navigationButton = rootView.findViewById(R.id.navigation_button);
        navigationButton.setEnabled(false);
//        navigationButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), NavigationActivity.class);
//                intent.putExtra(Utils.ROUTE_ARGUMENT, currentRoute);
//                startActivity(intent);
//            }
//        });




        return rootView;
    }



    // OnMapReadyCallback
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

        currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("scooter_app", "onMapReady#onClick: in listener");
                cleanCameraJump();
            }
        });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(getContext(), loadedMapStyle);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            //finish();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.i("scooter_app", "location changed");
        cleanCameraJump();
    }

    public void cleanCameraJump(){
        Log.i("scooter_app", "onMapReady#onClick: in listener");
        if (currentLocation == null) {
            Log.i("scooter_app", "onMapReady#onClick: current location is null");
        }
        else {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())) // Sets the new camera position
                    .zoom(CAMERA_ZOOM) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.animateCamera(CameraUpdateFactory.
                    newCameraPosition(position), 2500);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    // Android event listeners
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
        else
            Log.e("scooter_app", "onStart: mapView is null");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
        else
            Log.e("scooter_app", "onResume: mapView is null");
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        else
            Log.e("scooter_app", "onPause: mapView is null");
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null)
            mapView.onStop();
        else
            Log.e("scooter_app", "onStop: mapView is null");
//        if (locationComponent != null && locationComponent.getLocationEngine() != null) {
//            locationComponent.getLocationEngine().removeLocationUpdates();
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
        else
            Log.e("scooter_app", "onSaveInstanceState: mapView is null");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
        else
            Log.e("scooter_app", "OnDestroy: mapView is null");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null)
            mapView.onDestroy();
        else
            Log.e("scooter_app", "OnDestroy: mapView is null");

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
        else
            Log.e("scooter_app", "onLowMemory: mapView is null");
    }

    boolean hasLocationAccess()
    {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
