package template.shop.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import template.shop.R;
import template.shop.adapter.CartListAdapter;
import template.shop.data.GlobalVariable;
import template.shop.model.ItemModel;
import template.shop.widget.DividerItemDecoration;

public class CartFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private CartListAdapter mAdapter;
    private TextView item_total, price_total;
    private LinearLayout lyt_notfound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_shop_fragment_cart, null);

        item_total = (TextView) view.findViewById(R.id.item_total);
        price_total = (TextView) view.findViewById(R.id.price_total);
        lyt_notfound = (LinearLayout) view.findViewById(R.id.lyt_notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //set data and list adapter
        mAdapter = new CartListAdapter(getActivity(), GlobalVariable.getCart());
        recyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new CartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ItemModel obj) {
                dialogCartAction(obj, position);
            }
        });

        ((Button) view.findViewById(R.id.bt_checkout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getItemCount() != 0) {
                    checkoutConfirmation();
                }
            }
        });

        setTotalPrice();

        if (mAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }
        return view;
    }

    private void setTotalPrice() {
        item_total.setText(" - " + GlobalVariable.getCartItemTotal() + " Items");
        price_total.setText(" $ " + GlobalVariable.getCartPriceTotal());
    }

    private void dialogCartAction(final ItemModel model, final int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.app_shop_dialog_cart_option);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((TextView) dialog.findViewById(R.id.title)).setText(model.getName());
        final TextView qty = (TextView) dialog.findViewById(R.id.quantity);
        qty.setText(model.getTotal() + "");
        ((ImageView) dialog.findViewById(R.id.img_decrease)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getTotal() > 1) {
                    model.setTotal(model.getTotal() - 1);
                    qty.setText(model.getTotal() + "");
                }
            }
        });
        ((ImageView) dialog.findViewById(R.id.img_increase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setTotal(model.getTotal() + 1);
                qty.setText(model.getTotal() + "");
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.updateItemTotal(model);
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.removeCart(model);
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void checkoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Checkout Confirmation");
        builder.setMessage("Are you sure continue to checkout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GlobalVariable.clearCart();
                mAdapter.notifyDataSetChanged();
                Snackbar.make(view, "Checkout success", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}
