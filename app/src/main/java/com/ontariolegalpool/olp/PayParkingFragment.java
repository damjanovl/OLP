package com.ontariolegalpool.olp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FightTrafficFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FightTrafficFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayParkingFragment extends Fragment implements MainActivity.OnBackPressedListener {
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

    private String pictureImagePath = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PayParkingFragment() {
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
    public static PayParkingFragment newInstance(String param1, String param2) {
        PayParkingFragment fragment = new PayParkingFragment();
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
        View view = inflater.inflate(R.layout.fragment_pay_parking, container, false);
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
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.main_frame, new PayFragment()).commit();
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

//    private File createTemporaryFile(String part, String ext) throws Exception
//    {
//        File tempDir= Environment.getExternalStorageDirectory();
//        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
//        if(!tempDir.exists())
//        {
//            tempDir.mkdirs();
//        }
//        return File.createTempFile(part, ext, tempDir);
//    }

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
                    String photoPath = getRealPathFromURI(imageUri);
                    ExifInterface ei = new ExifInterface(photoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {

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
                    if (rotatedBitmap != null) {
                        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();

                        if (!textRecognizer.isOperational()) {
                            // Note: The first time that an app using a Vision API is installed on a
                            // device, GMS will download a native libraries to the device in order to do detection.
                            // Usually this completes before the app is run for the first time.  But if that
                            // download has not yet completed, then the above call will not detect any text,
                            // barcodes, or faces.
                            // isOperational() can be used to check if the required native libraries are currently
                            // available.  The detectors will automatically become operational once the library
                            // downloads complete on device.
                            Log.w("CAMERA", "Detector dependencies are not yet available.");

                            // Check for low storage.  If there is low storage, the native library will not be
                            // downloaded, so detection will not become operational.
                            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                            boolean hasLowStorage = getActivity().registerReceiver(null, lowstorageFilter) != null;

                            if (hasLowStorage) {
                                Toast.makeText(getContext(), "Low Storage", Toast.LENGTH_LONG).
                                        show();
                                Log.w("CAMERA", "Low Storage");
                            }
                        }

                        Frame imageFrame = new Frame.Builder()
                                .setBitmap(rotatedBitmap)
                                .build();

                        SparseArray<TextBlock> items = textRecognizer.detect(imageFrame);
                        StringBuilder value = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock textBlock = items.get(items.keyAt(i));
                            List<Line> lines = (List<Line>) textBlock.getComponents();
                            for (Line line : lines) {
                                List<Element> elements = (List<Element>) line.getComponents();
                                for (Element element : elements) {
                                    String word = element.getValue();
                                    Log.i("VALUE ", word);
                                }
                            }

//                            value.append(textBlock.getValue());
//                            value.append("\n");

//                            Log.i("LINE","i= " + i + ": "+ textBlock.getValue());
                            // Do something with value
                        }
//                        Log.i("VALUE ", value.toString());
                    }
                    if (boxONE.getDrawable() == null) {
                        boxONE.setImageBitmap(rotatedBitmap);
                    } else if (boxTWO.getDrawable() == null) {
                        boxTWO.setImageBitmap(rotatedBitmap);
                    } else if (boxTHREE.getDrawable() == null) {
                        boxTHREE.setImageBitmap(rotatedBitmap);
                    } else if (boxFOUR.getDrawable() == null) {
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
