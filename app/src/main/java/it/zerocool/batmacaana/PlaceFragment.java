package it.zerocool.batmacaana;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamanland.fab.FloatingActionButton;

import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;

import static android.os.Build.VERSION;


public class PlaceFragment extends Fragment implements View.OnClickListener {


    private com.ms.square.android.expandabletextview.ExpandableTextView tvDescription;
    private Place targetPlace;
    private ImageView ivPlace;
    private LinearLayout buttonLayout;
    private TextView timecardTv;
    private TextView addressTv;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView linkTv;
    private Button phoneActionButton;
    private Button urlActionButton;
    private Button mailActionButton;
    private FloatingActionButton floatingActionButton;

    public PlaceFragment() {
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
        View layout = inflater.inflate(R.layout.fragment_place, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (com.ms.square.android.expandabletextview.ExpandableTextView) layout.findViewById(R.id.description_tv);
        timecardTv = (TextView) layout.findViewById(R.id.timecard_tv);
        addressTv = (TextView) layout.findViewById(R.id.address_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        phoneActionButton = (Button) layout.findViewById(R.id.phoneButton);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        mailActionButton = (Button) layout.findViewById(R.id.mailButton);
        floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);

        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);

        //Args read
        Place p = ParsingUtilities.parseSinglePlace(getArguments().getString(Constraints.JSON_ARG));
        targetPlace = p;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(p.getName());

        //Load imagery and change colors
        ivPlace = (ImageView) layout.findViewById(R.id.imageView);
        LoadBitmapTask task = new LoadBitmapTask();
        task.execute("http://www.ilmiositodemo.altervista.org/app/images/big/" + p.getImage());

        //Fill fields
        fillFields(p);


        return layout;
    }

    private void fillFields(Place p) {
        tvDescription.setText(p.getDescription());
        timecardTv.setText(p.getTimeCard().toString());
        if (p.getContact().getAddress() != null) {
            addressTv.setText(p.getContact().getAddress());
        } else {
            addressTv.setText("N/A");
        }
        if (p.getContact().getTelephone() != null) {
            phoneTv.setText(p.getContact().getTelephone());
        } else {
            phoneTv.setText("N/A");
        }
        if (p.getContact().getEmail() != null) {
            mailTv.setText(p.getContact().getEmail());
        } else {
            mailTv.setText("N/A");
        }
        if (p.getContact().getUrl() != null) {
            linkTv.setText(p.getContact().getUrl());
        } else {
            linkTv.setText("N/A");
        }

    }

    public void setBitmap(Bitmap bitmap) {
        ivPlace.setImageBitmap(bitmap);
        Palette.generateAsync(bitmap, PaletteListener.newInstance(this));

    }

    public void setPalette(Palette palette) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantColor(R.color.primaryColor)));
        if (VERSION.SDK_INT >= 21) {
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
        if (v.getId() == R.id.phoneButton) {
            String phone = targetPlace.getContact().getTelephone();
            if (phone != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String uri = "tel: " + phone.trim();
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else
                Toast.makeText(getActivity(), R.string.no_phone_available, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.mailButton) {
            String mail = targetPlace.getContact().getEmail();
            if (mail != null) {
                /*Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra(Intent.EXTRA_EMAIL, mail);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Send Email"));*/

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.setType("*/*");
                String[] addresses = new String[1];
                addresses[0] = targetPlace.getContact().getEmail();
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intent.putExtra(Intent.EXTRA_STREAM, attachment);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else
                Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.urlButton) {
            String url = targetPlace.getContact().getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.floatingButton) {
            if (targetPlace.getLocation() != null) {
                String lat = Double.valueOf(targetPlace.getLocation().getLatitude()).toString();
                String lon = Double.valueOf(targetPlace.getLocation().getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + "," + lon + "(" + targetPlace.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }


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
            setBitmap(bitmap);
        }
    }


}
