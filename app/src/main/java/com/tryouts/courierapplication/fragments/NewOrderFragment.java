package com.tryouts.courierapplication.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tryouts.courierapplication.R;
import com.tryouts.courierapplication.presenters.NewOrderPresenter;


public class NewOrderFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private NewOrderPresenter mPresenter;
    private GoogleMap mGoogleMap;
    private TextView mDistanceView;
    private Button mCalculateButton;
    private Button mCallButton;
    private Button mAdditionalButton;
    private TextView placesFrom;
    private TextView placesTo;
    private Intent mOnActivityResultIntent;


    public NewOrderFragment() {
    }

    public static Fragment newInstance() {
        return new NewOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_neworder, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPresenter = new NewOrderPresenter(this);
        mPresenter.setGMapsUrl(getString(R.string.google_maps_key));
        mPresenter.sendFragmentActivityToInteractor();

        //  Toast.makeText(this,mPresenter.)
        mDistanceView = (TextView) view.findViewById(R.id.neworder_textview_summary);
        mCalculateButton = (Button) view.findViewById(R.id.neworder_button_calculate);
        mAdditionalButton = (Button) view.findViewById(R.id.neworder_button_additional);
        mCallButton = (Button) view.findViewById(R.id.neworder_button_call);
        placesFrom = (TextView) view.findViewById(R.id.place_autocomplete_from);
        placesTo = (TextView) view.findViewById(R.id.place_autocomplete_to);
        mMapView = (MapView) view.findViewById(R.id.neworder_map);
        mMapView.onCreate(savedInstanceState);

        mPresenter.onMapAsyncCall();
        setListeners();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOnActivityResultIntent = data;
        try {
            mPresenter.onActivityResultCalled(requestCode, resultCode);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    public Intent getOnActivityResultIntent() {
        return mOnActivityResultIntent;
    }

    public void setDistanceView(String distance) {
        mDistanceView.setText(getString(R.string.neworder_summary_start) + " " + distance +
                getString(R.string.neworder_summary_mid));
    }

    public void setFromText(String address) {
        placesFrom.setText(address);
    }

    public void setToText(String address) {
        placesTo.setText(address);
    }

    public void moveCameraTo(double lat, double lng) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(10)
                        .build()));
    }

    // STARTING AGAIN FFS

    private void setListeners() {
        placesFrom.setOnClickListener(v -> {
            try {
                mPresenter.createListenerForPlaceAutocompleteCall(1);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
        });
        placesTo.setOnClickListener(v -> {
            try {
                mPresenter.createListenerForPlaceAutocompleteCall(2);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
        });
        mAdditionalButton.setOnClickListener(v -> mPresenter.createListenerForAdditionalServices());
        mCalculateButton.setOnClickListener(v -> mPresenter.createListenerForCalculate());
        mCallButton.setOnClickListener(v -> mPresenter.createListenerForCall());
    }

    public FragmentActivity getParentActivity() {
        return getActivity();
    }

    public void mapAsyncCall() {
        mMapView.getMapAsync(this);
    }

    public void setMapClear() {
        mGoogleMap.clear();
    }

    public void onMapPolylineAdded(PolylineOptions polylineOptions) {
        mGoogleMap.addPolyline(polylineOptions);
    }

    public void animateCamera(CameraUpdate cu) {
        mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(cu, 1000, null);
    }

    public void makeToast(String message) {
        Toast toast = Toast.makeText(getContext(),
                message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showAlertDialog(AlertDialog.Builder builder) {
        builder.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
      /*  Location location = mGoogleMap.getMyLocation();
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
       // mGoogleMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Toast.makeText(getContext(), lat+""+lon,Toast.LENGTH_LONG).show();*/
        mPresenter.setMapView();
//        Toast.makeText(getContext(), (int) mGoogleMap.getMapType(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public final void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public final void onPause() {
        super.onPause();
        mMapView.onPause();
    }

}
