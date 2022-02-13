package com.luan_g.drawerlowsiv.ui.view;

import android.os.Bundle;
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
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.databinding.FragmentHomeBinding;
import com.luan_g.drawerlowsiv.ui.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private final ViewHolder mViewHolder = new ViewHolder();
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Definição de elementos;
        this.mViewHolder.textDateTime = binding.textDatetime;
        this.mViewHolder.imageView = binding.imageDetection;

        this.setObservers();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setObservers() {
        this.mViewModel.image.observe(getViewLifecycleOwner(), bitmap -> {
            this.mViewHolder.imageView.setImageBitmap(bitmap);
            this.mViewModel.getDateTime();
        });
        this.mViewModel.dateTimeText.observe(getViewLifecycleOwner(), s -> {
            this.mViewHolder.textDateTime.setText(s);
        });
        this.mViewModel.feedback.observe(getViewLifecycleOwner(), feedback -> {
            if (!feedback.isSuccesses()) {
                Snackbar.make(binding.getRoot(), feedback.getInfo(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(requireActivity().getApplicationContext(), R.color.snakeFail))
                        .show();
            }
        });
    }

    private class ViewHolder {
        TextView textDateTime;
        ImageView imageView;
    }
}