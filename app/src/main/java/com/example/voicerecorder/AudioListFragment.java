package com.example.voicerecorder;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioListFragment extends Fragment implements Adapter.onItemListClick{

    private RecyclerView audioList;

    private MediaPlayer mediaPlayer = null;
    private List<File> files;
    private boolean isPlaying = false;
    private File fileToPlay;

    private Adapter adapter;

    public AudioListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioList = view.findViewById(R.id.audio_list_view);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File dir = new File(path);
        //files = directory.listFiles();
        files = new ArrayList<File>(Arrays.asList(dir.listFiles()));

        adapter = new Adapter(files, this);

        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(adapter);

    }

    @Override
    public void onDelete(final File file, final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete audio file");
        alert.setMessage("Are you sure you want to delete file?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath() + "/" + file.getName();
                File remove = new File(recordPath);
                remove.delete();

                files.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });

        alert.show();
    }

    @Override
    public void onPlay(File file, int position) throws FileNotFoundException {
        if(isPlaying){
            stopAudio();
        }
        else {
            fileToPlay = file;
            playAudio(fileToPlay);
        }
    }

    private void playAudio(File fileToPlay){
        // Play the audio
        isPlaying = true;

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
            }
        });

    }

    private void stopAudio(){
        // Stop audio
        isPlaying = false;
        mediaPlayer.stop();
    }
}