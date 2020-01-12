package com.englishlearning.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.englishlearning.android.R;
import com.englishlearning.android.learnMeaning;

public class CollectionFragment extends Fragment implements View.OnClickListener{
    private Button to_wordCollect;
    private Button to_listenCollect;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_collection, null);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        to_wordCollect.setOnClickListener(this);
        to_listenCollect.setOnClickListener(this);
    }
    private void initViews(View view){
        to_wordCollect=(Button)view.findViewById(R.id.to_wordCollect);
        to_listenCollect=(Button)view.findViewById(R.id.to_listenCollect);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_wordCollect:
                Intent intent = new Intent(getActivity(), learnMeaning.class);
                intent.putExtra("isCollect","1");
                startActivity(intent);
                break;
            case R.id.to_listenCollect:
                break;
        }
    }
}
