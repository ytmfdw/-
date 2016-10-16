package qf.com.qf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import qf.com.qf.bean.KaoQinBean;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class KaoQinAdapter extends RecyclerView.Adapter<KaoQinAdapter.KaoQinViewHolder> {

    List<KaoQinBean> data;

    LayoutInflater inflater;

    public KaoQinAdapter(Context context, List<KaoQinBean> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public KaoQinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new KaoQinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KaoQinViewHolder holder, int position) {
        holder.tv_date.setText(data.get(position).kq_date);
        holder.tv_address.setText(data.get(position).kq_time + data.get(position).kq_address);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class KaoQinViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_address;

        public KaoQinViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(android.R.id.text1);
            tv_address = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }
}
