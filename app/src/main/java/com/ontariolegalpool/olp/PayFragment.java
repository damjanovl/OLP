package com.ontariolegalpool.olp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment implements MainActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button _payButton;
    EditText _paymentText;

    private static double PROCESS_FEE = 1.50;
    private static double PROFIT_MARGIN = 1.10; //10 percent per transaction

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PayFragment() {
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
     * @return A new instance of fragment PayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayFragment newInstance(String param1, String param2) {
        PayFragment fragment = new PayFragment();
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
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        _payButton = (Button) view.findViewById(R.id.btn_pay);
        _paymentText = (EditText) view.findViewById(R.id.payment);

        _paymentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("PAYMENT", "Editable: " + s.toString());
                if (s.toString().endsWith(".")){
                    double amount = Double.parseDouble(s.toString()
                            .substring(0, s.toString().length() -1 ));
                    amount = (amount * PROFIT_MARGIN) + PROCESS_FEE;
                    amount = Math.round(amount * 100.0) / 100.0;
                    _payButton.setText("PAY $" + amount);
                }else {
                    double amount = Double.parseDouble(s.toString());
                    amount = (amount * PROFIT_MARGIN) + PROCESS_FEE;
                    amount = Math.round(amount * 100.0) / 100.0;
                    _payButton.setText("PAY $" + amount);
                }
            }
        });

        _payButton.setOnClickListener(new View.OnClickListener() {
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
