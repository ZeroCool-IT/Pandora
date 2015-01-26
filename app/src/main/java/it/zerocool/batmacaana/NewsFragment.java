package it.zerocool.batmacaana;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import it.zerocool.batmacaana.model.News;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class NewsFragment extends Fragment implements View.OnClickListener {

    private ExpandableTextView tvBody;
    private News targetNews;
    private ImageView ivNews;
    private LinearLayout buttonLayout;
    private TextView linkTv;
    private TextView tagTv;
    private Button urlActionButton;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout bodyLayout;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvBody = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        bodyLayout = (LinearLayout) layout.findViewById(R.id.description_layout);
        ivNews = (ImageView) layout.findViewById(R.id.imageView);


        //Listener
        urlActionButton.setOnClickListener(this);
        ivNews.setOnClickListener(this);


        //Args read
        News n = ParsingUtilities.parseSingleNews(getArguments().getString(Constraints.JSON_ARG));
        targetNews = n;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(n.getTitle());
        if (n.getDateToString() != null) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(n.getDateToString());
        }

        //Load imagery and change colors
        LoadBitmapTask task = new LoadBitmapTask();
        task.execute(Constraints.URI_IMAGE_MEDIUM + n.getImage());

        //Fill fields
        fillFields(n);


        return layout;
    }

    private void fillFields(News n) {
        if (n.getBody() != null)
            tvBody.setText(n.getBody());
        else
            bodyLayout.setVisibility(View.GONE);

        if (n.getUrl() != null) {
            linkTv.setText(n.getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
            linkTv.setText("N/A");
        }
        if (!n.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", n.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);

    }

    public void setBitmap(Bitmap bitmap) {
//        ivEvent.setImageBitmap(bitmap);
        Picasso.with(getActivity()).
                load(Constraints.URI_IMAGE_MEDIUM + targetNews.getImage()).
                error(R.drawable.im_noimage).
                into(ivNews);
        Palette.generateAsync(bitmap, NewsPaletteListener.newInstance(this));

    }

    public void setPalette(Palette palette) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(palette.getDarkVibrantColor(R.color.primaryColor));
        }
        buttonLayout.setBackgroundColor(palette.getLightMutedColor(R.color.primaryColor));

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.urlButton) {
            String url = targetNews.getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.imageView) {
            Intent intent = new Intent(getActivity(), FullscreenActivity.class);
            intent.putExtra(Constraints.IMAGE, targetNews.getImage());
            startActivity(intent);
        }
    }

    private class LoadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            return RequestUtilities.downloadBitmap(params[0]);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param bitmap The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                setBitmap(bitmap);
            } else
                Picasso.with(getActivity()).
                        load(R.drawable.im_noimage).
                        placeholder(R.drawable.im_placeholder).
                        into(ivNews);

        }
    }


}
