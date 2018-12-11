package tactilebike.cse.tamu.edu.tactilebike;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.StepManeuver;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.RouteListener;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.milestone.StepMilestone;
import com.mapbox.services.android.navigation.v5.milestone.Trigger;
import com.mapbox.services.android.navigation.v5.milestone.TriggerProperty;
import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback,
        MilestoneEventListener, RouteListener, NavigationListener, NavigationEventListener {

    // variables for navigation
    private DirectionsRoute currentRoute;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();
        currentRoute = (DirectionsRoute)intent.getSerializableExtra(Utils.ROUTE_ARGUMENT);
        navigationView = findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
    }

    // OnNavigationReadyCallback
    @Override
    public void onNavigationReady(boolean isRunning) {
        Log.i(Utils.TAG, "onNavigationReady: isRunning = " + isRunning);
        ArrayList<Milestone> milestones = new ArrayList<>();
        // approaching turn milestone
        milestones.add(new StepMilestone.Builder()
                .setIdentifier(Utils.TURN_APPROACHING_MILESTONE_ID)
                .setTrigger(
                        Trigger.lte(TriggerProperty.STEP_DISTANCE_REMAINING_METERS,
                                Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(this).getString("turn_approaching_distance",
                                        "" + Utils.TURN_APPROACHING_DISTANCE)))
                ).build());
        // turn now milestone
        milestones.add(new StepMilestone.Builder()
                .setIdentifier(Utils.TURN_NOW_MILESTONE_ID)
                .setTrigger(
                        Trigger.lte(TriggerProperty.STEP_DISTANCE_REMAINING_METERS,
                                Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(this).getString("turn_now_distance",
                                        "" + Utils.TURN_NOW_DISTANCE)))
                ).build());
        NavigationViewOptions options = NavigationViewOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("simulate_route", false))
                .milestoneEventListener(this)
                .routeListener(this)
                .navigationListener(this)
                .milestones(milestones)
                .build();
        navigationView.startNavigation(options);
        sendToArduino(Constants.ROUTE_START);
    }

    // MilestoneEventListener
    @Override
    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
        if (milestone != null) {
            Log.d(Utils.TAG, milestoneDebug(routeProgress, instruction, milestone));
        }

        float turnApproachingDistance = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(this)
                .getString("turn_approaching_distance", "" + Utils.TURN_APPROACHING_DISTANCE));

        StepManeuver currManeuver = routeProgress.currentLegProgress().currentStep().maneuver();
        StepManeuver nextManeuver = routeProgress.currentLegProgress().upComingStep().maneuver();
        if ((currManeuver != null && nextManeuver != null) &&
                (Math.abs(nextManeuver.bearingBefore() - currManeuver.bearingAfter()) >= Utils.TURN_THRESHOLD ||
                Math.abs(nextManeuver.bearingAfter() - nextManeuver.bearingBefore()) >= Utils.TURN_THRESHOLD)) {
            if (nextManeuver.type().contains("turn")) {
                if (nextManeuver.modifier().contains("right")) {
                    if (milestone.getIdentifier() == Utils.TURN_APPROACHING_MILESTONE_ID &&
                            routeProgress.currentLegProgress().currentStep().distance() >= 1.05 * turnApproachingDistance) {
                        Toast.makeText(this, "right turn approaching", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.RIGHT_TURN_APPROACHING);
                    }
                    else if (milestone.getIdentifier() == Utils.TURN_NOW_MILESTONE_ID) {
                        Toast.makeText(this, "turn right", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.RIGHT_TURN);
                    }
                }
                else if (nextManeuver.modifier().contains("left")) {
                    if (milestone.getIdentifier() == Utils.TURN_APPROACHING_MILESTONE_ID &&
                            routeProgress.currentLegProgress().currentStep().distance() >= 1.05 * turnApproachingDistance) {
                        Toast.makeText(this, "left turn approaching", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.LEFT_TURN_APPROACHING);
                    }
                    else if (milestone.getIdentifier() == Utils.TURN_NOW_MILESTONE_ID) {
                        Toast.makeText(this, "turn left", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.LEFT_TURN);
                    }
                }
            }
            else if (nextManeuver.modifier() != null) {
                if (nextManeuver.modifier().contains("slight right")) {
                    if (milestone.getIdentifier() == Utils.TURN_APPROACHING_MILESTONE_ID &&
                            routeProgress.currentLegProgress().currentStep().distance() >= 1.05 * turnApproachingDistance) {
                        Toast.makeText(this, "slight right turn approaching", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.RIGHT_TURN_APPROACHING);
                    }
                    else if (milestone.getIdentifier() == Utils.TURN_NOW_MILESTONE_ID) {
                        Toast.makeText(this, "turn slightly right", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.RIGHT_TURN);
                    }
                }
                else if (nextManeuver.modifier().contains("slight left")) {
                    if (milestone.getIdentifier() == Utils.TURN_APPROACHING_MILESTONE_ID &&
                            routeProgress.currentLegProgress().currentStep().distance() >= 1.05 * turnApproachingDistance) {
                        Toast.makeText(this, "slight left turn approaching", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.LEFT_TURN_APPROACHING);
                    }
                    else if (milestone.getIdentifier() == Utils.TURN_NOW_MILESTONE_ID) {
                        Toast.makeText(this, "turn slightly left", Toast.LENGTH_SHORT).show();
                        sendToArduino(Constants.LEFT_TURN);
                    }
                }
            }
        }
    }

    private void sendToArduino(int command) {
        if (SettingsTab.mChatService != null) {
            byte[] out = {(byte)command};
            SettingsTab.mChatService.write(out);
        }
        else {
            Log.e(Utils.TAG, "sendToArduino: app not connected to Arduino");
        }
    }

    private String milestoneDebug(RouteProgress routeProgress, String instruction, Milestone milestone) {
        String logString = "onMilestoneEvent:";
        if (milestone != null) {
            logString += "\n\tmilestone ID: " + milestone.getIdentifier();
        }
        if (routeProgress != null) {
            logString += "\n\tnext step name: " + routeProgress.currentLegProgress().upComingStep().name();
            logString += "\n\tnext step distance: " + routeProgress.currentLegProgress().upComingStep().distance();
            StepManeuver currManeuver = routeProgress.currentLegProgress().currentStep().maneuver();
            StepManeuver nextManeuver = routeProgress.currentLegProgress().upComingStep().maneuver();
            logString += "\n\tnext maneuver:\n\t\tinstruction: " + nextManeuver.instruction() +
                    "\n\t\tmodifier: " + nextManeuver.modifier() + "\n\t\ttype: " + nextManeuver.type() +
                    "\n\t\tbearing before: " + nextManeuver.bearingBefore() +
                    "\n\t\tbearing after: " + nextManeuver.bearingAfter();
            logString += "\n\tbearing change: " + Math.abs(nextManeuver.bearingBefore() - currManeuver.bearingAfter());
        }
        if (instruction.length() > 0) {
            logString += "\n\tinstruction: " + instruction;
        }
        return logString;
    }

    // RouteListener
    @Override
    public boolean allowRerouteFrom(Point offRoutePoint) {
        Log.d(Utils.TAG, "allowRerouteFrom: point: " + offRoutePoint);
        return true;
    }

    @Override
    public void onArrival() {
        Log.d(Utils.TAG, "onArrival");
        Toast.makeText(this, "onArrival: arrived at destination", Toast.LENGTH_LONG).show();
        sendToArduino(Constants.ARRIVAL);
    }

    @Override
    public void onFailedReroute(String errorMessage) {
        Log.e(Utils.TAG, "onFailedReroute: " + errorMessage);
        Toast.makeText(this, "onFailedReroute: failed to reroute: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOffRoute(Point offRoutePoint) {
        Log.d(Utils.TAG, "onOffRoute: point: " + offRoutePoint);
    }

    @Override
    public void onRerouteAlong(DirectionsRoute directionsRoute) {
        Log.d(Utils.TAG, "onRerouteAlong");
        Toast.makeText(this, "rerouting", Toast.LENGTH_LONG).show();
    }

    // NavigationListener
    @Override
    public void onCancelNavigation() {
        Log.d(Utils.TAG, "onCancelNavigation");
        onNavigationFinished();
    }

    @Override
    public void onNavigationFinished() {
        Log.d(Utils.TAG, "onNavigationFinished");
        // End the navigation session
        if (navigationView != null) {
            navigationView.stopNavigation();
            Log.i(Utils.TAG, "onNavigationFinished: stopped navigation view");
        }
        else {
            Log.e(Utils.TAG, "onNavigationFinished: navigation view is null");
        }
        Intent intent = new Intent(this, Entry.class);
        startActivity(intent);
    }

    @Override
    public void onNavigationRunning() {
        Log.d(Utils.TAG, "onNavigationRunning");
    }

    // NavigationEventListener
    @Override
    public void onRunning(boolean running) {
        Log.d(Utils.TAG, "onRunning: running: " + running);
    }

    // Android callbacks
    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }
}
