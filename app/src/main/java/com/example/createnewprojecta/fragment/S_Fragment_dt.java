package com.example.createnewprojecta.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.createnewprojecta.MainActivity2;
import com.example.createnewprojecta.R;
import com.example.createnewprojecta.adapter.SelectorAdapter2;
import com.example.createnewprojecta.model.ISelectImageItem;
import com.example.createnewprojecta.model.Img;
import com.example.createnewprojecta.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class S_Fragment_dt extends Fragment {
    public int screenWidth;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.floatButton)
    FloatingActionButton floatButton;
    Unbinder unbinder;
    @BindView(R.id.list)
    RecyclerView list;
    private static final int REQUEST_CODE_PREVIEW = 1;
    private static final int READ_EXTERNAL_STORAGE_CODE = 2;
    private static final String EXTRA_KEY_MAX = "max";
    private SelectorAdapter2 mAdapter;
    private List<Integer> mSelectSortPosList;
    private Context context;
    public S_Fragment_dt(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_fragment_dt, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initView();
        //initData();
    }


    private void initView() {
        mSelectSortPosList = new ArrayList<>();
        mAdapter = new SelectorAdapter2(onItemClickListener);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                list.setHasFixedSize(true);
                list.addItemDecoration(new GridSpacingItemDecoration(3, 10, false, 100, 110));
                list.setLayoutManager(layoutManager);
                list.setAdapter(mAdapter);
            }
        });
    }

    private SelectorAdapter2.OnItemClickListener onItemClickListener = new SelectorAdapter2.OnItemClickListener() {
        @Override
        public int onItemClick(ISelectImageItem item, int pos) {
            return mSelectSortPosList.size();
        }
    };




    private void initData() {
        if (permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE)) {
           // getActivity().getSupportLoaderManager().initLoader(0, null,this) ;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_CODE:
                onRequestReadExternalStorageResult(permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void onRequestReadExternalStorageResult(String[] permissions, int[] grantResults) {
        if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initData();
        } else {
            Toast.makeText(getContext(), R.string.image_selector_authorization_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean permissionGranted(String permission, int requestCode) {
        int permissionCode = ContextCompat.checkSelfPermission(getActivity(), permission);
        if (permissionCode != PackageManager.PERMISSION_GRANTED) {
            try {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }

    private static final String[] IMAGE_PROJECTION = new String[]{
            MediaStore.MediaColumns.DATA
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String select = MediaStore.Images.Media.SIZE + ">0";
        return new CursorLoader(getActivity(), uri, IMAGE_PROJECTION, select, null, MediaStore.Images.Media.DISPLAY_NAME + " DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        resetSelector();
        if (data == null || data.getCount() == 0)
            return;

        List<Img> imgs = new ArrayList<>();
        while (data.moveToNext()) {
            String path = data.getString(0);
            Img img = new Img().setPath(path);
            imgs.add(img);
        }
        mAdapter.replaceDatas(imgs);
    }

    private void resetSelector() {
        mSelectSortPosList.clear();
    }

    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PREVIEW) {
        }
    }




    private void init() {
        int h = screenWidth / 3;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
        layoutParams.width = h;
        layoutParams.height = h;
        layoutParams.leftMargin = h * h;
        image.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image, R.id.floatButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image:
                startActivity(new Intent(getContext(), MainActivity2.class));
                break;
            case R.id.floatButton:
                startActivity(new Intent(getContext(), MainActivity2.class).putExtra("width", screenWidth));
                break;
        }
    }
}
