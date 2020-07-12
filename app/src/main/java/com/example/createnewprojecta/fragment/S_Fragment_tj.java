package com.example.createnewprojecta.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.adapter.S_TjAdapter;
import com.example.createnewprojecta.bean.Tj;
import com.example.createnewprojecta.dialog.Fx_Dialog;
import com.example.createnewprojecta.net.VolleyLo;
import com.example.createnewprojecta.net.VolleyTo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class S_Fragment_tj extends Fragment {
    @BindView(R.id.gridView)
    GridView gridView;
    Unbinder unbinder;
    private List<Tj> mTj;
    private S_TjAdapter tjAdapter;
    private int width, screenWidth;

    public S_Fragment_tj(int width, int screenWidth) {
        this.width = width;
        this.screenWidth = screenWidth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_fragment_tj, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inview();
        huoqu();
    }

    private void inview() {
        mTj = new ArrayList<>();
    }

    private void huoqu() {
        VolleyTo volleyTo = new VolleyTo();
        volleyTo.setUrl("get_tj")
                .setJsonObject("UserName", "user1")
                .setVolleyLo(new VolleyLo() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Tj tj = gson.fromJson(jsonArray.getString(i), Tj.class);
                                mTj.add(tj);

                            }
                            setadapter();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }).start();
    }

    private void setadapter() {
        tjAdapter = new S_TjAdapter(getContext(), mTj, width);
        gridView.setAdapter(tjAdapter);
        tjAdapter.SetData(new S_TjAdapter.SetData() {
            @Override
            public void setdata(int position, String image) {
                Fx_Dialog fx_dialog = new Fx_Dialog(screenWidth, image);
                fx_dialog.show(getFragmentManager(), "");
                fx_dialog.setCancelable(false);

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
