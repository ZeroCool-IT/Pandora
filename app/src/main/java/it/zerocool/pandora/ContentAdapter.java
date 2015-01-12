package it.zerocool.pandora;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import it.zerocool.pandora.model.Cardable;

/**
 * Created by Marco on 11/01/2015.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Cardable> contentItems = Collections.emptyList();

    public ContentAdapter(Context context, List<Cardable> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.contentItems = data;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ContentAdapter.ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_content, parent, false);
        ContentViewHolder holder = new ContentViewHolder(view);
        return holder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ContentAdapter.ContentViewHolder holder, int position) {
        Cardable current = contentItems.get(position);
        holder.header.setText(current.getHeader());
        Picasso.with(context).load(Integer.parseInt(current.getImagery())).into(holder.imagery);
        ;
        //TODO retrieve image from web
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {

        return contentItems.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        ImageView imagery;

        public ContentViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.content_header);
            imagery = (ImageView) itemView.findViewById(R.id.content_imagery);
            //TODO Card button and its Click Listener
        }

    }
}
