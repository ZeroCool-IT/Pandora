package it.zerocool.pandora;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Marco on 05/01/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DrawerItem> drawerItems = Collections.emptyList();


    public DrawerAdapter(Context context, List<DrawerItem> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.drawerItems = data;

    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_row, parent, false);
        DrawerViewHolder holder = new DrawerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        DrawerItem current = drawerItems.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconID);
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }


    class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();

        }
    }

/*    private class DrawerItemClickListener implements RecyclerView.OnClickListener {

        *//**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     *//*
        @Override
        public void onClick(View v) {

        }

        private void selectItem(int position) {
            ContentFragment f = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ContentFragment.FRAG_SECTION_ID, position);
            f.setArguments(bundle);
            FragmentManager fm = f.getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            *//*recyclerView.getChildAt(position).setSelected(true);
            context.setTitle(getResources().getStringArray(R.array.drawer_list)[position]);
            mDrawerLayout.closeDrawer(recyclerView);*//*
        }
    }*/
}
