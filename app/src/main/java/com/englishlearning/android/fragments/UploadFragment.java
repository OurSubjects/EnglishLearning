package com.englishlearning.android.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.englishlearning.android.R;
import com.englishlearning.android.activities.BookListActivity;
import com.englishlearning.android.activities.ListenMainActivity;

public class UploadFragment  extends Fragment implements View.OnClickListener{
    private RelativeLayout to_wordUpload;
    private RelativeLayout to_listenUpload;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_upload, null);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        to_wordUpload.setOnClickListener(this);
        to_listenUpload.setOnClickListener(this);
    }
    private void initViews(View view){
        to_wordUpload=(RelativeLayout)view.findViewById(R.id.to_wordUpload);
        to_listenUpload=(RelativeLayout)view.findViewById(R.id.to_listenUpload);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.to_wordUpload:
                intent=new Intent(getActivity(), BookListActivity.class);
                intent.putExtra("bookChangeFlag","2");
                startActivity(intent);
                break;
            case R.id.to_listenUpload:
                startActivity(new Intent(getActivity(), ListenMainActivity.class));
                break;
        }
    }

}
