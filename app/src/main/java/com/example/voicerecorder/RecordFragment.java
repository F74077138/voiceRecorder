package com.example.voicerecorder;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;

    private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView initialTxt;
    private Chronometer timer;

    private RippleBackground rippleBackground;

    private boolean isRecording = false;
    private final String recordPermission = Manifest.permission.RECORD_AUDIO;
    private final int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        listBtn = view.findViewById(R.id.listBtn);
        recordBtn = view.findViewById(R.id.recordBtn);

        listBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);

        initialTxt = view.findViewById(R.id.initialTxt);
        timer = view.findViewById(R.id.timer);

        rippleBackground = (RippleBackground) view.findViewById(R.id.content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recordBtn:
                if(isRecording){
                    // Stop recording
                    isRecording = false;
                    stopRecording();
                    rippleBackground.stopRippleAnimation();
                }
                else{
                    // Start recording
                    if(verifyPermissions()){
                        isRecording = true;
                        startRecording();
                        rippleBackground.startRippleAnimation();
                    }
                }
                break;
            case R.id.listBtn:
                navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                break;
        }
    }

    public void startRecording(){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        recordFile = format.format(now) + ".3gp";

        initialTxt.setText("RECORDING...");

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopRecording(){
        timer.stop();

        initialTxt.setText("PRESS TO START RECORDING");

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public boolean verifyPermissions(){
        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

}