package com.ontariolegalpool.olp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FightTrafficFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FightTrafficFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FightTrafficFragment extends Fragment implements MainActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton _cameraButton;
    private Button _submitButton;
    ImageView boxONE;
    ImageView boxTWO;
    ImageView boxTHREE;
    ImageView boxFOUR;
    private static final Integer PICTURE_RESULT = 1234;
    public static int REQUEST_STORAGE_PERMISSION = 122;
    private Uri imageUri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FightTrafficFragment() {
        // Required empty public constructor
    }

    @Override
    public void doBack() {
        Log.d("OTHER_FRAG", "doBack");
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_frame, new HomeFragment()).commit();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FightTrafficFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FightTrafficFragment newInstance(String param1, String param2) {
        FightTrafficFragment fragment = new FightTrafficFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fight_traffic, container, false);
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        _cameraButton = (ImageButton) view.findViewById(R.id.btn_picture);
        _cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePicture();
            }
        });

        _submitButton = (Button) view.findViewById(R.id.btn_submit);
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void TakePicture(){
        if (!checkStoragePermission()) {
            requestStoragePermission();
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Ticket");
            values.put(MediaStore.Images.Media.DESCRIPTION, "from Camera");
            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICTURE_RESULT);
        }
    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RESULT:", "requestCode: " + requestCode + "resultCode: " + resultCode + "data: " + data);
        boxONE = (ImageView) getView().findViewById(R.id.boxONE);
        boxTWO = (ImageView) getView().findViewById(R.id.boxTWO);
        boxTHREE = (ImageView) getView().findViewById(R.id.boxTHREE);
        boxFOUR = (ImageView) getView().findViewById(R.id.boxFOUR);

        if (requestCode == PICTURE_RESULT) {
            if (resultCode == -1) {
                try {
                    Log.d("URI", "image: " + imageUri);
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri);
                    Log.d("ATTEMPTING IMAGE", "thumbnail: " + thumbnail);
//                    Log.d("BOXONE", boxONE.getDrawable().toString());
                    String photoPath = getRealPathFromURI(imageUri);
                    ExifInterface ei = new ExifInterface(photoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(thumbnail, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(thumbnail, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(thumbnail, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = thumbnail;
                    }
                    if (boxONE.getDrawable() == null){
                        boxONE.setImageBitmap(rotatedBitmap);
                    }
                    else if (boxTWO.getDrawable() == null){
                        boxTWO.setImageBitmap(rotatedBitmap);
                    }
                    else if (boxTHREE.getDrawable() == null){
                        boxTHREE.setImageBitmap(rotatedBitmap);
                    }
                    else if (boxFOUR.getDrawable() == null){
                        boxFOUR.setImageBitmap(rotatedBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
