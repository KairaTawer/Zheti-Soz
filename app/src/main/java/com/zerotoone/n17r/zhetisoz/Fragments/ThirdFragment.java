package com.zerotoone.n17r.zhetisoz.Fragments;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.zerotoone.n17r.zhetisoz.Activities.MainActivity;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.List;

public class ThirdFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TableRow linkRate = (TableRow) view.findViewById(R.id.link_rate);
        TableRow linkEmail = (TableRow) view.findViewById(R.id.link_email);
        TableRow linkInstagram = (TableRow) view.findViewById(R.id.link_instagram);
        TableRow linkFacebook = (TableRow) view.findViewById(R.id.link_facebook);

        linkFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Zheti Söz қосымшасы арқылы ағылшын деңгеіңді жетілдір, сілтеме арқылы жазып ал! \n"
                                        + "");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        linkInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent
                        .putExtra(
                                Intent.EXTRA_STREAM,
                                Uri.parse("android.resource://com.zerotoone.n17r.zhetisoz/drawable/frame/"));
                shareIntent.setPackage("com.instagram.android");
                try {
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view,"Instagram тіркелмеген",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        linkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("kairatawer@gmail.com"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "kairatawer@gmail.com" });
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Жеті сөз");
                startActivity(sendIntent);
            }
        });

        linkRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}