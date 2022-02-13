package com.luan_g.drawerlowsiv.ui.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.constants.AppConstants;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.databinding.FragmentCameraBinding;
import com.luan_g.drawerlowsiv.ui.viewmodel.CameraViewModel;

import java.util.Objects;

public class CameraFragment extends Fragment {

    private CameraViewModel mViewModel;
    private FragmentCameraBinding binding;
    private ViewHolder mViewHolder = new ViewHolder();
    private int mFilter = 0;
    private boolean mStreamBegin = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Verifica qual a origem da chamada para a fragment - Qual a listagem desejada
        if (getArguments() != null) {
            this.mFilter = getArguments().getInt(AppConstants.FRAGMENT_FILTER);
        }

        mViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        this.mViewHolder.textStreamBegin = root.findViewById(R.id.text_stream_start);
        this.mViewHolder.imageCamera = root.findViewById(R.id.image_camera);

        this.setObservers();


        this.mViewModel.subscribeMQTTTopic(mFilter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mViewModel.endStreaming(mFilter);
        this.mStreamBegin = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mViewHolder.textStreamBegin.setText("Conectando na câmera " + (mFilter+1));
        this.mViewModel.beginStreaming(mFilter);
        new CountDownTimer(MQTTConstants.MILLIS_MESSAGE_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!mStreamBegin) {
                    mViewHolder.textStreamBegin.setText(mViewHolder.textStreamBegin.getText().toString() + " .");
                }
            }
            @Override
            public void onFinish() {
                if (!mStreamBegin) {
                    mViewHolder.textStreamBegin.setText("Tempo expirado!!");
                }
            }
        };
    }

    private void setObservers() {
        this.mViewModel.feedback.observe(getViewLifecycleOwner(), feedback -> {
            if (!feedback.isSuccesses()) {
                Snackbar.make(binding.getRoot(), feedback.getInfo(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(requireActivity().getApplicationContext(), R.color.snakeFail))
                        .show();
            }
        });
        this.mViewModel.image.observe(getViewLifecycleOwner(), bitmap -> {
            mViewHolder.imageCamera.setImageBitmap(bitmap);
            if (!mStreamBegin) {
                mStreamBegin = true;
                mViewModel.getDateTime();
            }
        });
        this.mViewModel.dateTimeText.observe(getViewLifecycleOwner(), s ->
                mViewHolder.textStreamBegin.setText(s));
    }

    private class ViewHolder {
        TextView textStreamBegin;
        ImageView imageCamera;
    }
}