package com.example.baruch.android5779_6256_4843_part2.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.backend.Backend;
import com.example.baruch.android5779_6256_4843_part2.model.backend.BackendFactory;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentDriver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class HistoryListFragment extends Fragment {
    private View view;
    private List<Ride> rieds;
    private Backend backend;
    private RecyclerView rvRieds;
    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_history_list, container, false) ;
        rieds = new ArrayList<Ride>();
        rvRieds = (RecyclerView) view.findViewById(R.id.rvRidesHIstoryList);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        searchView=(SearchView) view.findViewById(R.id.search_name);
        backend = BackendFactory.getBackend();
        AccessContact();
        RidesManagerActivity activity = (RidesManagerActivity) getActivity();

        final HistoryRideAdapter adapter = new HistoryRideAdapter(rieds);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Refresh().execute();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  adapter.getFilter().filter(newText);
                  return false;
              }
          });

        rvRieds.setAdapter(adapter);
        rvRieds.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvRieds.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL);
        rvRieds.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new HistoryRideAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(final View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Add " +rieds.get(position).getClientFirstName()+' ' +
                    rieds.get(position).getClientLastName()+" to contacts?");

                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveContact(view,rieds.get(position));
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                saveContact(view,rieds.get(position));

            }
        });

        backend.notifyRidesListByDriverKey(new Backend.NotifyDataChange<Ride>() {
            @Override
            public void OnDataChanged(Ride ride) {
                for (int i =0 ;i < rieds.size();++i){
                    if(ride.getKey().equals( rieds.get(i).getKey())){
                        rieds.set(i,ride);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onDataAdded(Ride ride) {
                rieds.add(0,ride);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDataRemoved(Ride ride) {
                for (int i =0 ;i < rieds.size();++i){
                    if(ride.getKey().equals( rieds.get(i).getKey())){
                        rieds.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Exception exception) {
            }
        }, CurrentDriver.getDriver().getId());

        return view;
    }

    private class Refresh extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            swipeContainer.setRefreshing(result);
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveContact(View view, Ride ride) {
        //Create a new contact entry!
        String szFullname = ride.getClientFirstName()+" "+ride.getClientLastName();
        //https://developer.android.com/reference/android/provider/ContactsContract.RawContacts
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        //INSERT NAME
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, szFullname) // Name of the person
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ride.getClientFirstName()) // Name of the person
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ride.getClientLastName()) // Name of the person
                .build());

        //INSERT MOBILE
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, ride.getClientTelephone()) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()); //

        //INSERT EMAIL
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, ride.getClientEmail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build()); //

      /*//INSERT ADDRESS: FULL, STREET, CITY, REGION, POSTCODE, COUNTRY
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, m_szAddress)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, m_szStreet)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, m_szCity)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, m_szState)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, m_szZip)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, m_szCountry)
                //.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK)
                .build());*/

        //INSERT NOTE
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, "RideTaxi")
                .build()); //

        Uri newContactUri = null;
        //PUSH EVERYTHING TO CONTACTS
        try
        {
            ContentProviderResult[] res = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (res!=null && res[0]!=null) {
                newContactUri = res[0].uri;
            }
        }
        catch (RemoteException e)
        {
            newContactUri = null;
        }
        catch (OperationApplicationException e)
        {
            newContactUri = null;
        }
    }
    /* From Android 6.0 Marshmallow,
    the application will not be granted any permissions at installation time.
    Instead, the application has to ask the user for permissions one-by-one at runtime
    with an alert message. The developer has to call for it manually.*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact()
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}

