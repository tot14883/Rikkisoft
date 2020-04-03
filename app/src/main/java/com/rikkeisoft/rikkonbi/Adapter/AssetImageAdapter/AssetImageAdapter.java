package com.rikkeisoft.rikkonbi.Adapter.AssetImageAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.UI.Home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AssetImageAdapter extends BaseAdapter {

    private HomeFragment mHomeFragment;
    private List<PiSignage> mAssetArrayList;

    public AssetImageAdapter(HomeFragment mHomeFragment, List<PiSignage> mAssetArrayList){
        this.mHomeFragment = mHomeFragment;
        this.mAssetArrayList =  mAssetArrayList;
    }


    @Override
    public int getCount() {
        return mAssetArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PiSignage piSignage = mAssetArrayList.get(position);


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_image_ad,null);
        ImageView Img_ad = (ImageView) view.findViewById(R.id.iv_auto_image_slider1);
        Img_ad.setClipToOutline(true);

        Picasso.get()
                .load(piSignage.getFileUrl())
                .into(Img_ad);

        return view;
    }
}
