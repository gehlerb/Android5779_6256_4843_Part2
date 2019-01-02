package com.example.baruch.android5779_6256_4843_part2.model.location;

import com.example.baruch.android5779_6256_4843_part2.model.entities.AddressAndLocation;

public interface LocationHandler {

    public void getAddressAndLocation(final ActionResult action);
    public void stopTracking();

    public interface ActionResult{
        void onSuccess(AddressAndLocation addressAndLocation);
        void onFailure();
    }
}
