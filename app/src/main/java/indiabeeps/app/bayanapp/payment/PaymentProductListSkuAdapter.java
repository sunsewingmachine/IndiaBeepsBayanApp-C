package indiabeeps.app.bayanapp.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;
import indiabeeps.app.bayanapp.R;

import java.util.List;


public class PaymentProductListSkuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemViewHolder.OnItemSelectedListener itemSelectedListener;
    private Activity context;


    private List<SkuDetails> mPaymentProductModels;

    private static int lastSelectedPosition = 0;

    private static final int NORMAL_ITEM = 0;
    private static final int FOOTER_VIEW = 1;

    public PaymentProductListSkuAdapter(Activity context,List<SkuDetails> paymentProductModelList, ItemViewHolder.OnItemSelectedListener itemSelectedListener) {
        mPaymentProductModels = paymentProductModelList;
        this.context=context;
        this.itemSelectedListener = itemSelectedListener;
    }

    private boolean isHeader(int position) {
        return position == mPaymentProductModels.size() -1;
    }

    @Override
    public int getItemViewType(int position) {

        return isHeader(position) ? FOOTER_VIEW : NORMAL_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.payment_product_package_list_item, parent, false);
        return new ItemViewHolder(view, itemSelectedListener);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SkuDetails productModel = mPaymentProductModels.get(viewHolder.getAdapterPosition());
        ItemViewHolder holder = ((ItemViewHolder) viewHolder);
        holder.selectedProduct.setChecked(lastSelectedPosition == position);
        if (productModel.getType().equals(BillingClient.SkuType.SUBS)){
            holder.ItemName.setText(context.getString(R.string.contribute)+" "+productModel.getPrice() + " / mo");
            //holder.ItemPrice.setText(String.format("%s %s", context.getText(R.string.payment_of), productModel.getPrice()));
            holder.ItemPrice.setText(String.format("%s %s", "select to pay", productModel.getPrice() + " monthly"));
        }
        holder.mItem = productModel;
    }



    @Override
    public int getItemCount() {
        return mPaymentProductModels.size();
    }





    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        SkuDetails mItem;
        OnItemSelectedListener itemSelectedListener;

        RelativeLayout mItemLayout;
        RadioButton selectedProduct;
        TextView ItemName;
        TextView ItemPrice;

        ItemViewHolder(View v, OnItemSelectedListener itemSelected) {
            super(v);
            itemSelectedListener = itemSelected;
            mItemLayout = v.findViewById(R.id.productListItem_paymentBox);
            selectedProduct = v.findViewById(R.id.productListItem_radio);
            ItemName = v.findViewById(R.id.productListItem_name);
            ItemPrice = v.findViewById(R.id.productListItem_totalCost);

            selectedProduct.setOnClickListener(view -> {

                lastSelectedPosition = getAdapterPosition();
                itemSelectedListener.onItemSelected(mItem);
            });

            mItemLayout.setOnClickListener(view -> {

                lastSelectedPosition = getAdapterPosition();
                itemSelectedListener.onItemSelected(mItem);
            });
        }

        public interface OnItemSelectedListener {

            void onItemSelected(SkuDetails item);
        }
    }

}