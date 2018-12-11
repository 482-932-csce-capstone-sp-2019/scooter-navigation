package tactilebike.cse.tamu.edu.tactilebike;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.os.Handler;
import android.os.Message;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add a marker
import com.mapbox.mapboxsdk.annotations.Marker;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// classes needed to launch navigation UI

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;

public class HomeTab extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener,
        LocationEngineListener, LocationListener, MapboxMap.OnMapLongClickListener {

    private static final int MY_PERMISSIONS_ACCESS_LOCATION = 1;

    public HomeTab() {
        // Required empty public constructor
    }

    public static HomeTab newInstance() {
        HomeTab fragment = new HomeTab();
        return fragment;
    }

    // view variables
    private FloatingActionButton locationButton;
    private FloatingActionButton navigationButton;

    // variables for displaying map
    private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for locating user
    private LocationComponent locationComponent;

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

    // variables for searching
    private ConstraintLayout autocompleteFragmentHolder;
    private PlaceAutocompleteFragment autocompleteFragment;

    // route test buttons
    private FloatingActionButton route1TestButton;
    private FloatingActionButton route2TestButton;

    // variables for way points
    ArrayList<Marker> wayPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.access_token));

        super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        View rootView = inflater.inflate(R.layout.fragment_home_tab, container, false);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationButton = rootView.findViewById(R.id.location_button);
        navigationButton = rootView.findViewById(R.id.navigation_button);
        navigationButton.setEnabled(false);
        navigationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                intent.putExtra(Utils.ROUTE_ARGUMENT, currentRoute);
                startActivity(intent);
            }
        });

        autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragmentHolder = (ConstraintLayout)rootView.findViewById(R.id.place_autocomplete_fragment_holder);

        route1TestButton = rootView.findViewById(R.id.route1_button);
        route2TestButton = rootView.findViewById(R.id.route2_button);

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
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        locationComponent = mapboxMap.getLocationComponent();
        if (hasLocationAccess()) {
            locationComponent.activateLocationComponent(getContext());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.getLocationEngine().addLocationEngineListener(this);

            mapboxMap.addOnMapClickListener(this);
            mapboxMap.addOnMapLongClickListener(this);

            mapboxMap.getUiSettings().setCompassMargins(
                    0,
                    autocompleteFragmentHolder.getHeight() + autocompleteFragmentHolder.getTop(),
                    ((View)autocompleteFragmentHolder.getParent()).getWidth() - autocompleteFragmentHolder.getRight(),
                    0);

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentLocation == null) {
                        Log.i(Utils.TAG, "onMapReady#onClick: current location is null");
                    }
                    else {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())) // Sets the new camera position
                                .zoom(Utils.DEFAULT_ZOOM) // Sets the zoom
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory.
                                newCameraPosition(position), Utils.CAMERA_ANIMATE_DURATION);
                    }
                }
            });

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i(Utils.TAG, "Selected place: " + place.getName());
                    setDestination(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                }

                @Override
                public void onError(Status status) {
                    Log.i(Utils.TAG, "An error occurred: " + status);
                }
            });

            route1TestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDestination(Utils.origin1, Utils.destination1, null);
                }
            });
            route2TestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDestination(Utils.origin2, Utils.destination2, null);
                }
            });
        }
        else {
            requestLocationAccess();
        }
    }

    // OnMapClickListener
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapClick(@NonNull LatLng point){
        setDestination(point);
    }

    // OnMapLongClickListener
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapLongClick(@NonNull LatLng point) {
        if (destinationMarker == null) {
            return;
        }
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        LatLng oldDest = destinationCoord;
        destinationCoord = point;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(point)
        );
        addWayPoint(oldDest);
    }

    // helper functions
    private void setDestination(LatLng dest) {
        Log.i(Utils.TAG, "setDestination: destination: " + dest);
        clearDestination();
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = dest;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );

        if (hasLocationAccess()) {
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
            requestLocationAccess();
        }
    }

    private void setDestination(LatLng origin, LatLng dest, List<Point> wayPoints) {
        Log.i(Utils.TAG, "setDestination: origin: " + origin + ", destination: " + dest);
        clearDestination();
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = dest;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );

        if (wayPoints != null) {
            for (Point p : wayPoints) {
                addWayPoint(new LatLng(p.latitude(), p.longitude()));
            }
        }

        if (hasLocationAccess()) {
            if (currentLocation == null) {
                Log.i(Utils.TAG, "onMapClick: current location is null");
            }
            else {
                originCoord = origin;
                destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                getRoute(originPosition, destinationPosition);
            }
        }
        else {
            requestLocationAccess();
        }
    }
    private void addWayPoint(LatLng point) {

        if (destinationCoord != null) {
            wayPoints.add(mapboxMap.addMarker(new MarkerOptions()
                    .position(point)));

            if (hasLocationAccess()) {
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
                requestLocationAccess();
            }
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
        navigationButton.setVisibility(View.GONE);
	}
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
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

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.Builder builder = NavigationRoute.builder(getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_CYCLING);
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
                    navigationButton.setVisibility(View.VISIBLE);

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

    // LocationEngineListener
    @Override
    public void onConnected() {
        Log.i(Utils.TAG, "onConnected: connected to user location");
    }

    // Android event listeners
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
        else
            Log.e(Utils.TAG, "onStart: mapView is null");
        if (hasLocationAccess()) {
            if (locationComponent != null && locationComponent.getLocationEngine() != null) {
                locationComponent.getLocationEngine().requestLocationUpdates();
            }
        }
        else {
            requestLocationAccess();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
        else
            Log.e(Utils.TAG, "onResume: mapView is null");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        else
            Log.e(Utils.TAG, "onPause: mapView is null");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null)
            mapView.onStop();
        else
            Log.e(Utils.TAG, "onStop: mapView is null");
        if (locationComponent != null && locationComponent.getLocationEngine() != null) {
            locationComponent.getLocationEngine().removeLocationUpdates();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
        else
            Log.e(Utils.TAG, "onSaveInstanceState: mapView is null");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (hasLocationAccess()) {
                        // TODO: something
                    }
                    else
                    {
                        requestLocationAccess();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    boolean hasLocationAccess()
    {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    void requestLocationAccess()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_LOCATION);
    }
}
