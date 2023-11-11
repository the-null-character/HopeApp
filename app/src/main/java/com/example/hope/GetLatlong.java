package com.example.hope;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class GetLatlong {

    Context context;
    String address;
    LatLng l;

    GetLatlong(Context c, String address){
        this.context=c;
        this.address=address;
    }

    public LatLng getLatLong(){
        Geocoder coder = new Geocoder(context);
        List<Address> addressList;
        l = null;

        try {
            // May throw an IOException
            addressList = coder.getFromLocationName(address, 5);
            if (address == null) {
                return null;
            }

            Address location = addressList.get(0);
            l = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        if(l!=null) {
            return l;
        }
        return new LatLng(0,0);

    }
}
