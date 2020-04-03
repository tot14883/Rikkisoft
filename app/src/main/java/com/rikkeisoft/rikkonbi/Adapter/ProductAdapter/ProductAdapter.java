package com.rikkeisoft.rikkonbi.Adapter.ProductAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.UI.Category.CategoryFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable {

    private CategoryFragment mCategoryFragment;
    private List<Product> mProducts;
    private List<Product> mOriginalProduct;


    public ProductAdapter(CategoryFragment mCategoryFragment,List<Product> mProducts){
        this.mCategoryFragment = mCategoryFragment;
        this.mProducts = mProducts;
        this.mOriginalProduct = mProducts;
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_category,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
          Product product = mProducts.get(position);

          holder.nameProduct.setText(String.valueOf(product.getName()));
          holder.priceProduct.setText(String.valueOf(product.getPrice()+" ₫"));

        Picasso.get()
                .load(product.getImageUrl())
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryFragment.OnClickItem(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameProduct;
        public TextView priceProduct;
        public ImageView imgProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameProduct = (TextView)  itemView.findViewById(R.id.name_product);
            priceProduct = (TextView) itemView.findViewById(R.id.price_product);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
         }
    }
}
