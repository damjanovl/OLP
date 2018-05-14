package com.ontariolegalpool.olp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements MainActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imageView;
    private TextView firstName;
    private TextView lastName;
    private UserInformation userInformation;
    private Bitmap bmp;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String userID;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // pass
        }
    }

    @Override
    public void doBack() {
        Log.d("PROFILE_FRAG", "doBack");
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
//        fragmentManager.popBackStack("root_fragment", android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        int count = fragmentManager.getBackStackEntryCount();
        Log.d("FRAG COUNT", "count: " + count);
        if (count == 0) {
            getActivity().onBackPressed();
        }
        else {
            fragmentManager.popBackStack();
        }
//        transaction.replace(R.id.main_frame, new HomeFragment())
//                .addToBackStack("root_frag")
//                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        imageView = (ImageView) view.findViewById(R.id.profilePic);
        firstName = (TextView) view.findViewById(R.id.firstName);
        lastName = (TextView) view.findViewById(R.id.lastName);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ontariolegalpool.firebaseio.com/");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        if (MemCache.cacheExists()) {
            UserInformation userInformation = (UserInformation)MemCache.getInstance().getLru().get(userID);
            Log.d("CACHING", "User INFO: " + userInformation.getImage());
            firstName.setText(userInformation.getFirstName());
            firstName.setCursorVisible(true);
            lastName.setText(userInformation.getLastName());
            bmp = stringToBitMap(userInformation.getImage());
            imageView.setImageBitmap(bmp);
        }
        else {
            myRef.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userInformation = dataSnapshot.getValue(UserInformation.class);
                    Log.d("DOWNLOADING", "User INFO: " + userInformation.getImage());
                    firstName.setText(userInformation.getFirstName());
                    firstName.setCursorVisible(true);
                    lastName.setText(userInformation.getLastName());
                    bmp = stringToBitMap(userInformation.getImage());
                    imageView.setImageBitmap(bmp);
                    MemCache.getInstance().getLru().put(userID, userInformation);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DB FETCH", "FAILED", databaseError.toException());
                }
            });
        }

        return view;
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
