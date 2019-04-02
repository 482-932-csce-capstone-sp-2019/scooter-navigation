package com.scooter.scooter_nav;

import android.Manifest;
import android.content.Context;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MicrophoneInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

// classes needed to initialize map
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeTab extends Fragment implements OnMapReadyCallback, PermissionsListener, LocationListener, MapboxMap.OnMapClickListener {



    // variables for displaying map
    private MapView mapView;
    private MapboxMap mapboxMap;

    //private static final int MY_PERMISSIONS_ACCESS_LOCATION = 1;

    private PermissionsManager permissionsManager;

    // variables for way points
    ArrayList<Marker> wayPoints;


    // variables for locating user
    private LocationComponent locationComponent;
    LocationManager lm;


    // variables for adding a marker
    private Marker destinationMarker;
    private Location currentLocation;
    private LatLng originCoord;
    private LatLng destinationCoord;

    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;


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

        if (wayPoints == null) {
            wayPoints = new ArrayList<>();
        }
        else {
            wayPoints.clear();
        }


        return rootView;
    }



    // OnMapReadyCallback
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;


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
            mapboxMap.addOnMapClickListener(this);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

            currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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
        Log.i(Utils.TAG, "location changed");
        cleanCameraJump();
    }

    public void cleanCameraJump(){
        Log.i(Utils.TAG, "onMapReady#onClick: in listener");
        if (currentLocation == null) {
            Log.i(Utils.TAG, "onMapReady#onClick: current location is null");
        }
        else {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())) // Sets the new camera position
                    .zoom(Utils.CAMERA_ZOOM) // Sets the zoom
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
            Log.e(Utils.TAG, "OnDestroy: mapView is null");

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
        else
            Log.e(Utils.TAG, "onLowMemory: mapView is null");
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(mapboxMap!=null){
            setDestination(point);
        }
        else{
            Log.e(Utils.TAG, "onMapClick: mapboxMap is null");
            return false;
        }
        return true;
    }

    // helper functions
    private void setDestination(LatLng dest) {
        Log.i(Utils.TAG, "setDestination: destination: " + dest);
        //clearDestination();
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = dest;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );

        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            if (currentLocation == null) {
                Log.i(Utils.TAG, "onMapClick: current location is null");
            }
            else {
                originCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                getRoute(originPosition, destinationPosition);
            }
        }
        else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private void clearDestination() {
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        for (Marker m : wayPoints) {
            mapboxMap.removeMarker(m);
        }
        wayPoints.clear();
        if (navigationMapRoute != null) {
            navigationMapRoute.removeRoute();
        }
        destinationCoord = null;
        navigationButton.setEnabled(false);
        //navigationButton.setVisibility(View.GONE);
    }
    private void getRoute(Point origin, Point destination) {
        NavigationRoute.Builder builder = NavigationRoute.builder(getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING);
        String routePointString = "getRoute: route points:";
        routePointString += "\n\torigin: (" + origin.latitude() + "," + origin.longitude() + ")";
        routePointString += "\n\tdestination: (" + destination.latitude() + "," + destination.longitude() + ")";
        if (wayPoints.size() > 0) {
            for (Marker m : wayPoints) {
                builder.addWaypoint(Point.fromLngLat(m.getPosition().getLongitude(),
                        m.getPosition().getLatitude()));
                routePointString += "\n\twaypoint: " + m.getPosition().getLatitude() + "," + m.getPosition().getLongitude() + ")";
            }
        }
        Log.d(Utils.TAG, routePointString);

        builder.build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(Utils.TAG, "getRoute#onResponse: No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(Utils.TAG, "getRoute#onResponse: No routes found");
                            return;
                        }

                        JSONArray legs = null;
                        try {
                            String logString = "\ngetRoute#onResponse: route";
                            JSONObject responseJSON = new JSONObject(response.body().toJson());
                            legs = responseJSON.getJSONArray("routes")
                                    .getJSONObject(0).getJSONArray("legs");
                            for (int i = 0; i < legs.length(); ++i) {
                                logString += "\n\nleg #" + i + " steps:";
                                JSONArray steps = legs.getJSONObject(i).getJSONArray("steps");
                                for (int j = 0; j < steps.length(); ++j) {
                                    JSONObject maneuver = steps.getJSONObject(j).getJSONObject("maneuver");
                                    logString += "\n" + maneuver.toString(4);
                                }
                            }
                            Log.v(Utils.TAG, logString);
                        }
                        catch (JSONException e) {
                            Log.e(Utils.TAG, "getRoute#onReponse: couldn't parse JSON");
                            Log.v(Utils.TAG, "getRoute#onResponse: " + response.body());
                        }

                        currentRoute = response.body().routes().get(0);
                        try {
                            JSONObject cr = new JSONObject(currentRoute.toJson());
                            Log.v(Utils.TAG, "getRoute#onResponse: currentRoute = " + cr.toString(2));
                        }
                        catch (JSONException e) {
                            Log.v(Utils.TAG, "getRoute#onResponse: currentRoute = " + currentRoute);
                        }

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);

                        // enable the navigation button
                        navigationButton.setEnabled(true);
                        navigationButton.setFocusable(true);
                        //navigationButton.setVisibility(View.VISIBLE);

                        // zoom the camera to the route
                        ArrayList<LatLng> points = new ArrayList<>();
                        if (legs != null) {
                            try {
                                for (int i = 0; i < legs.length(); ++i) {
                                    JSONArray steps = legs.getJSONObject(i).getJSONArray("steps");
                                    for (int j = 0; j < steps.length(); ++j) {
                                        JSONArray loc = steps.getJSONObject(j).getJSONObject("maneuver").getJSONArray("location");
                                        points.add(new LatLng(loc.getDouble(1), loc.getDouble(0)));
                                    }
                                }
                            }
                            catch (JSONException e) {
                                Log.e(Utils.TAG, "getRoute#onReponse: couldn't parse JSON");
                            }
                        }
                        LatLngBounds.Builder llbb = new LatLngBounds.Builder();
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                                llbb.includes(points).build(), 300));;
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(Utils.TAG, "getRoute#onFailureError: " + throwable.getMessage());
                    }
                });
    }
}
