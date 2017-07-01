package com.example.ephraimkunz.multigametimer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ephraimkunz.multigametimer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoinGameFragment.JoinGameFragmentListener} interface
 * to handle interaction events.
 * Use the {@link JoinGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinGameFragment extends Fragment {
    private JoinGameFragmentListener mListener;

    public JoinGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JoinGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinGameFragment newInstance() {
        JoinGameFragment fragment = new JoinGameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_join_game, container, false);

        final Button joinGame = (Button)inflatedView.findViewById(R.id.joinGameButtonWithGameId);
        final EditText editText = (EditText)inflatedView.findViewById(R.id.joinGameEditText);

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameId = editText.getText().toString();
                gameJoinTapped(gameId);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 3) {
                    joinGame.setEnabled(true);
                } else {
                    joinGame.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return inflatedView;
    }

    public void gameJoinTapped(String gameId) {
        if (mListener != null) {
            mListener.onGameJoinTapped(gameId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JoinGameFragmentListener) {
            mListener = (JoinGameFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface JoinGameFragmentListener {
        void onGameJoinTapped(String gameId);
    }
}
