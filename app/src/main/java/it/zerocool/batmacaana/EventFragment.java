package it.zerocool.batmacaana;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.shamanland.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.zerocool.batmacaana.model.Event;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class EventFragment extends Fragment implements View.OnClickListener {
    private ExpandableTextView tvDescription;
    private Event targetEvent;
    private ImageView ivEvent;
    private LinearLayout buttonLayout;
    private TextView timecardTv;
    private TextView addressTv;
    private TextView phoneTv;
    private TextView mailTv;
    private TextView linkTv;
    private TextView tagTv;
    private Button phoneActionButton;
    private Button urlActionButton;
    private Button mailActionButton;
    private Button mapActionButton;
    private FloatingActionButton floatingActionButton;
    private LinearLayout timecardLayout;
    private LinearLayout addressLayout;
    private LinearLayout phoneLayout;
    private LinearLayout mailLayout;
    private LinearLayout linkLayout;
    private LinearLayout tagLayout;
    private LinearLayout descriptionLayout;

    public EventFragment() {
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
        View layout = inflater.inflate(R.layout.fragment_event, container, false);

        //Bind widget
        buttonLayout = (LinearLayout) layout.findViewById(R.id.button_layout);
        tvDescription = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        timecardTv = (TextView) layout.findViewById(R.id.timecard_tv);
        addressTv = (TextView) layout.findViewById(R.id.address_tv);
        phoneTv = (TextView) layout.findViewById(R.id.phone_tv);
        mailTv = (TextView) layout.findViewById(R.id.mail_tv);
        linkTv = (TextView) layout.findViewById(R.id.link_tv);
        tagTv = (TextView) layout.findViewById(R.id.tag_tv);
        phoneActionButton = (Button) layout.findViewById(R.id.phoneButton);
        urlActionButton = (Button) layout.findViewById(R.id.urlButton);
        mailActionButton = (Button) layout.findViewById(R.id.mailButton);
        mapActionButton = (Button) layout.findViewById(R.id.mapButton);
        floatingActionButton = (FloatingActionButton) layout.findViewById(R.id.floatingButton);
        timecardLayout = (LinearLayout) layout.findViewById(R.id.timecard_layout);
        addressLayout = (LinearLayout) layout.findViewById(R.id.address_layout);
        phoneLayout = (LinearLayout) layout.findViewById(R.id.phone_layout);
        mailLayout = (LinearLayout) layout.findViewById(R.id.mail_layout);
        linkLayout = (LinearLayout) layout.findViewById(R.id.link_layout);
        tagLayout = (LinearLayout) layout.findViewById(R.id.tag_layout);
        descriptionLayout = (LinearLayout) layout.findViewById(R.id.description_layout);

        //Listener
        phoneActionButton.setOnClickListener(this);
        urlActionButton.setOnClickListener(this);
        mailActionButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        mapActionButton.setOnClickListener(this);

        //Args read
        Event e = ParsingUtilities.parseSingleEvent(getArguments().getString(Constraints.JSON_ARG));
        targetEvent = e;
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(e.getName());
        if (!e.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", e.getTags());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(tags);
        }

        //Load imagery and change colors
        ivEvent = (ImageView) layout.findViewById(R.id.imageView);
        LoadBitmapTask task = new LoadBitmapTask();
        task.execute(Constraints.URI_IMAGE_MEDIUM + e.getImage());

        //Fill fields
        fillFields(e);


        return layout;
    }

    private void fillFields(Event e) {
        if (e.getDescription() != null)
            tvDescription.setText(e.getDescription());
        else
            descriptionLayout.setVisibility(View.GONE);
        if (e.eventTimeToString() != null) {
            timecardTv.setText(e.eventTimeToString());
        } else {
            timecardLayout.setVisibility(View.GONE);
        }
        if (e.getContact().getAddress() != null) {
            String place = "";
            if (e.getPlace() != null) {
                place += e.getPlace() + "\n";
            }
            place += e.getContact().getAddress();
            addressTv.setText(place);
        } else {
            addressLayout.setVisibility(View.GONE);
            addressTv.setText("N/A");
        }
        if (e.getContact().getTelephone() != null) {
            phoneTv.setText(e.getContact().getTelephone());
        } else {
            phoneLayout.setVisibility(View.GONE);
            phoneTv.setText("N/A");
        }
        if (e.getContact().getEmail() != null) {
            mailTv.setText(e.getContact().getEmail());
        } else {
            mailLayout.setVisibility(View.GONE);
            mailTv.setText("N/A");
        }
        if (e.getContact().getUrl() != null) {
            linkTv.setText(e.getContact().getUrl());
        } else {
            linkLayout.setVisibility(View.GONE);
            linkTv.setText("N/A");
        }
        if (!e.getTags().isEmpty()) {
            String tags = TextUtils.join(", ", e.getTags());
            tagTv.setText(tags);
        } else
            tagLayout.setVisibility(View.GONE);

    }

    public void setBitmap(Bitmap bitmap) {
//        ivEvent.setImageBitmap(bitmap);
        Picasso.with(getActivity()).
                load(Constraints.URI_IMAGE_MEDIUM + targetEvent.getImage()).
                resize(540, 960).
                centerCrop().
                error(R.drawable.im_noimage).
                into(ivEvent);
        Palette.generateAsync(bitmap, EventPaletteListener.newInstance(this));

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
        if (v.getId() == R.id.phoneButton) {
            String phone = targetEvent.getContact().getTelephone();
            if (phone != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String uri = "tel: " + phone.trim();
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_dial_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_phone_available, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.mailButton) {
            String mail = targetEvent.getContact().getEmail();
            if (mail != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.setType("*/*");
                String[] addresses = new String[1];
                addresses[0] = targetEvent.getContact().getEmail();
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_email_available, Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.urlButton) {
            String url = targetEvent.getContact().getUrl();
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getActivity(), R.string.no_url_available, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.floatingButton) {

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, targetEvent.getName());
            if (targetEvent.getLocation() != null) {
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, targetEvent.getLocation());
            }
            if (targetEvent.getStartDate() != null) {
                GregorianCalendar start = targetEvent.getStartDate();
                if (targetEvent.getStartHour() != null) {
                    start.set(GregorianCalendar.HOUR_OF_DAY, targetEvent.getStartHour().get(Calendar.HOUR_OF_DAY));
                    start.set(GregorianCalendar.MINUTE, targetEvent.getStartHour().get(Calendar.MINUTE));
                }
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.getTimeInMillis());
                if (targetEvent.getEndDate() != null) {
                    GregorianCalendar end = targetEvent.getEndDate();
                    if (targetEvent.getEndDate() != null) {
                        end.set(Calendar.HOUR_OF_DAY, targetEvent.getEndHour().get(Calendar.HOUR_OF_DAY));
                        end.set(Calendar.MINUTE, targetEvent.getEndHour().get(Calendar.MINUTE));
                    }
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.getTimeInMillis());
                }
            }
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
            /*if (targetPlace.getLocation() != null) {
                String lat = Double.valueOf(targetPlace.getLocation().getLatitude()).toString();
                String lon = Double.valueOf(targetPlace.getLocation().getLongitude()).toString();
                String uri = "geo:0,0?q=" + lat + "," + lon + "(" + targetPlace.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            }*/


        } else if (v.getId() == R.id.mapButton) {
            String uri = "geo:0,0?q=";
            if (targetEvent.getLocation() != null || targetEvent.getContact().getAddress() != null) {
                if (targetEvent.getLocation() != null) {
                    String lat = Double.valueOf(targetEvent.getLocation().getLatitude()).toString();
                    String lon = Double.valueOf(targetEvent.getLocation().getLongitude()).toString();
                    uri += lat + "," + lon;
                } else if (targetEvent.getContact().getAddress() != null) {
                    String res = targetEvent.getContact().getAddress() + ", 00032 Carpineto Romano";
                    res = Uri.encode(res);
                    uri += res;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
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
            if (bitmap != null) {
                setBitmap(bitmap);
            } else
                Picasso.with(getActivity()).
                        load(R.drawable.im_noimage).
                        placeholder(R.drawable.im_placeholder).
                        into(ivEvent);

        }
    }

}
