package tactilebike.cse.tamu.edu.tactilebike;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    final static String TAG = "tactilebike_tag";
    final static String ROUTE_ARGUMENT = "tactilebike.cse.tamu.edu.tactilebike_route";
    final static int DEFAULT_ZOOM = 15;
    final static int CAMERA_ANIMATE_DURATION = 2500;

    // navigation triggers
    final static int TURN_APPROACHING_MILESTONE_ID = 5;
    final static int TURN_NOW_MILESTONE_ID = 6;
    final static float TURN_APPROACHING_DISTANCE = 150; // in meters
    final static float TURN_NOW_DISTANCE = 10;          // in meters
    final static float TURN_THRESHOLD = 30;             // determines how sharp a turn must be
                                                        //  to trigger feedback

    // pre-defined routes
    final static LatLng origin1 = new LatLng( 30.6189843,-96.3389457);
    final static LatLng destination1 = new LatLng(30.614865782942474,-96.3381350888223);
    final static LatLng origin2 = new LatLng(30.615882074122652,-96.336676566883);
    final static LatLng destination2 = new LatLng(30.619180561411866,-96.3394362879643);
}
