package nz.co.spaceapp.library.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LocationManager implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	protected static final String TAG = "LocationManager";
    private static final int TWO_MINUTES = 2 * 60 * 1000;

    private static volatile LocationManager mInstance = null;

	private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    
	/*
	 * Constants for location update parameters
	 */
	public static final int MILLISECONDS_PER_SECOND = 1000;

	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

	// A fast ceiling of update intervals, used when the app is visible
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private Dialog errorDialog;
	private boolean hasGooglePlayServices = true;
	private SensorManager mSensorManager;
	private final Set<OnChangedListener> mListeners;
    private final List<LocationChangedData> mLocationChangedListeners;
    private LocationEnabledCallBack mFirstLocationCallback;
    private final float[] mRotationMatrix;
    private final float[] mRemapRotationMatrix;
    private final float[] mOrientation;

    private boolean mTracking = false;
    private float mHeading;
    private float mPitch;
    private GeomagneticField mGeomagneticField;
    private boolean mHasInterference;
    private Context mContext;
    private List<LocationEnabledCallBack> mLocationCallbacks = new ArrayList<>();
    private boolean mIsRunning = false;
    private Location mLocation = null;
    private boolean mIsConnected = false;

    private SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mHasInterference = (accuracy < SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
                notifyAccuracyChanged();
            }
            if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                if (accuracy != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
                    notifyAccuracyChanged();
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // Get the current heading from the sensor, then notify the listeners of the
                // change.
                SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
                SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRemapRotationMatrix);
                SensorManager.getOrientation(mRemapRotationMatrix, mOrientation);

                // Store the pitch (used to display a message indicating that the user's head
                // angle is too steep to produce reliable results.
                mPitch = (float) Math.toDegrees(mOrientation[1]);

                // Convert the heading (which is relative to magnetic north) to one that is
                // relative to true north, using the user's current location to compute this.
                float magneticHeading = (float) Math.toDegrees(mOrientation[0]);
                float a = computeTrueNorth(magneticHeading) - 90;
                float b = 360.0f;

                mHeading = (a % b + b) % b;

                notifyOrientationChanged();
            }
        }
    };

    public LocationManager() {
		mRotationMatrix = new float[16];
		mRemapRotationMatrix = new float[16];
        mOrientation = new float[9];
        mListeners = new LinkedHashSet<>();
        mLocationChangedListeners = Collections.synchronizedList(new ArrayList<LocationChangedData>());
	}
	
	public static LocationManager getInstance() {
		if (mInstance == null) {
			synchronized (LocationManager.class) {
				if (mInstance == null) {
					mInstance = new LocationManager();
				}
			}
		}
		return mInstance;
	}

    public void registerLocationListener(Context context, LocationEnabledCallBack callBack) {
        if (!mLocationCallbacks.contains(callBack))
            mLocationCallbacks.add(callBack);
        if (!mIsRunning)
            start(context, callBack);
        else
            callBack.onConnected();
    }

    public boolean isLocationEnabled() {
        android.location.LocationManager manager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    private void start(Context context, LocationEnabledCallBack callBack) {

        if (mIsRunning)
            return;

        mFirstLocationCallback = callBack;
        mContext = context;

        if (!isLocationEnabled())
            buildAlertMessageNoGps(mContext);
        mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(5000);

		/*
		 * Create a new location client, using the enclosing class to
		 * handle callbacks.
		 */
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mIsRunning = true;
    }

    private void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        mFirstLocationCallback.onConnectionFailure();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void startRotationSensor(Context context) {
        if (mTracking)
            return;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        initSensorManager();
    }

    private void stopRotationSensor() {
        mSensorManager.unregisterListener(mSensorListener);
    }

	private void initSensorManager() {
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);

        // The rotation vector sensor doesn't give us accuracy updates, so we observe the
        // magnetic field sensor solely for those.
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        mTracking = true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult((Activity)mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST);

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */

			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			// If no resolution is available, display a dialog to the user with the error.
			hasGooglePlayServices = false;
			showErrorDialog(connectionResult.getErrorCode());
		}
	}



    @Override
    public void onConnected(Bundle bundle) {
        hasGooglePlayServices = true;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mIsConnected = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (isBetterLocation(location, mLocation)) {
                mLocation = location;

                synchronized (mLocationChangedListeners) {

                    for (LocationChangedData data : mLocationChangedListeners) {
                        if (data.getLastLocation() == null)
                            data.setLastLocation(mLocation);
                        if (mLocation.distanceTo(data.getLastLocation()) > data.getDistance()) {
                            data.getCallback().onSignificantLocationChanged();
                            data.setLastLocation(mLocation);
                        }
                    }
                    updateGeomagneticField();
                }
                if (!mIsConnected) {
                    mFirstLocationCallback.onConnected();
                    mIsConnected = true;
                }
            }
        }
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    /**
     * Get the current Location
     */
    public Location getLocation() {
        return mLocation;
    }

    /**
     * Adds a listener that will be notified when the user's orientation changes.
     */
    public void registerOnOrientationChangedListener(Context context, OnChangedListener listener) {
        mListeners.add(listener);

        if (!mTracking)
            startRotationSensor(context);
    }

	/**
     * Removes a listener from the list of those that will be notified when the user's location changes.
     */
    public void unregisterOnOrientationChangedListener(OnChangedListener listener) {
        mListeners.remove(listener);

        if (mListeners.isEmpty() && mTracking)
            stopRotationSensor();
    }

    /**
     * Adds a listener that will be notified when the user's location or orientation changes.
     */
    public void registerOnLocationChangedListener(OnSignificantLocationChangedListener listener, double distance) {
        LocationChangedData locationChangedData = new LocationChangedData(listener, distance, mLocation);
        mLocationChangedListeners.add(locationChangedData);
    }

    /**
     * Removes a listener from the list of those that will be notified when the user's location changes.
     */
    public void unregisterOnLocationChangedListener(OnSignificantLocationChangedListener listener) {
        synchronized (mLocationChangedListeners) {
            Iterator i = mLocationChangedListeners.iterator();

            while (i.hasNext()) {
                LocationChangedData data = (LocationChangedData) i.next();

                if (data.getCallback() == listener) {
                    i.remove();
                    break;
                }
            }
        }
    }

	public void unregisterLocationListener(LocationEnabledCallBack listener) {
        if (mLocationCallbacks.contains(listener))
            mLocationCallbacks.remove(listener);

        if (mLocationCallbacks.isEmpty() && mIsRunning) {
            mIsRunning = false;
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
                mIsConnected = false;
            }
        }
	}

	/**
	 * Show a dialog returned by Google Play services for the
	 * connection error code
	 *
	 * @param errorCode An error code returned from onConnectionFailed
	 */
	private void showErrorDialog(int errorCode) {

		if (errorDialog == null) {
			// Get the error dialog from Google Play services
			errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, (Activity)mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {

				errorDialog.setOnKeyListener(new OnKeyListener(){
					@Override
					public boolean onKey ( DialogInterface dialog , int keyCode , KeyEvent event ){
						if (keyCode == KeyEvent.KEYCODE_BACK){
							errorDialog.dismiss();
							errorDialog = null;
							return true;
						}
						return false;
					}
				});
				errorDialog.show();

				// Create a new DialogFragment in which to show the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();

				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				errorFragment.setCancelable(true);
				// Show the error dialog in the DialogFragment
				errorFragment.show(((Activity)mContext).getFragmentManager(), "Kea");
			}
		}
	}

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public String getGeoPosition() {
        if (mLocation == null)
            return null;
        return mLocation.getLatitude() + ";" + mLocation.getLongitude() + ";" + mLocation.getAltitude() + " epu=" + mLocation.getAccuracy() + " hdn=" + mHeading + " spd=" + mLocation.getSpeed();
    }

    /**
	 * Define a DialogFragment to display the error dialog generated in
	 * showErrorDialog.
	 */
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 *
		 * @param dialog An error dialog
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	public boolean hasGooglePlayServices(){
		return hasGooglePlayServices;
	}

	public boolean hasInterference() {
        return mHasInterference;
    }
	
	/**
     * Gets the user's current heading, in degrees. The result is guaranteed to be between 0 and
     * 360.
     *
     * @return the user's current heading, in degrees
     */
    public float getHeading() {
        return mHeading;
    }
	
	/**
     * Gets the user's current pitch (head tilt angle), in degrees. The result is guaranteed to be
     * between -90 and 90.
     *
     * @return the user's current pitch angle, in degrees
     */
    public float getPitch() {
        return mPitch;
    }
    
    /**
     * Updates the cached instance of the geomagnetic field after a location change.
     */
    private void updateGeomagneticField() {
        mGeomagneticField = new GeomagneticField((float) mLocation.getLatitude(), (float) mLocation.getLongitude(), (float)mLocation.getAltitude(), mLocation.getTime());
    }

    /**
         * Use the magnetic field to compute true (geographic) north from the specified heading
         * relative to magnetic north.
         *
         * @param heading the heading (in degrees) relative to magnetic north
         * @return the heading (in degrees) relative to true north
         */
    private float computeTrueNorth(float heading) {
        if (mGeomagneticField != null) {
            return heading + mGeomagneticField.getDeclination();
        } else {
            return heading;
        }
    }
    
    /**
     * Notifies all listeners that the compass's accuracy has changed.
     */
    private void notifyAccuracyChanged() {
        for (OnChangedListener listener : mListeners) {
            listener.onAccuracyChanged(this);
        }
    }
    
    /**
     * Notifies all listeners that the user's orientation has changed.
     */
    private void notifyOrientationChanged() {
        for (OnChangedListener listener : mListeners) {
            listener.onOrientationChanged(this);
        }
    }

    private class LocationChangedData {
        private final OnSignificantLocationChangedListener mListener;
        private final double mDistance;
        private Location mLocation;

        public LocationChangedData(OnSignificantLocationChangedListener listener, double distance, Location location) {
            mListener = listener;
            mDistance = distance;
            mLocation = location;
        }

        public Location getLastLocation() {
            return mLocation;
        }

        public void setLastLocation(Location location) {
            mLocation = location;
        }

        public double getDistance() {
            return mDistance;
        }

        public OnSignificantLocationChangedListener getCallback() {
            return mListener;
        }
    }


    public interface LocationEnabledCallBack {
        public void onConnectionFailure();
        public void onConnected();
    }

    public interface OnSignificantLocationChangedListener {
        public void onSignificantLocationChanged();
    }

    public interface OnChangedListener {
        public void onOrientationChanged(LocationManager orientationManager);
        public void onAccuracyChanged(LocationManager orientationManager);
    }
}
