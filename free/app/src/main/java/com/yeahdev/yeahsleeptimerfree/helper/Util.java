package com.yeahdev.yeahsleeptimerfree.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;

import com.yeahdev.yeahsleeptimerfree.R;


public class Util {

    public static void buildFirstLoadDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.app_name));
        //
        WebView wv = new WebView(context);
        wv.loadData(context.getString(R.string.firststart_dialog), "text/html", "utf-8");
        //
        dialog.setView(wv);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //
        dialog.show();
    }

    public static void buildShare(Context context) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        //
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.shareBody));
        //
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void buildFeedback(Context context) {
        Intent email = new Intent(Intent.ACTION_SEND);
        //
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"yeahdev@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.fbSubject));
        email.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.fbBody));
        //
        context.startActivity(Intent.createChooser(email, "Send Feedback:"));
    }

    public static void navToPlaystoreEntry(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.yeahdev.yeahsleeptimerfree")));
    }

    public static void navToAllPlaystoreApps(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=yeahdev")));
    }

    public static String convertValueToDoubleZeroString(int value) {
        if (value >= 0 && value < 10) {
            return String.valueOf("0" + value);
        } else {
            return String.valueOf(value);
        }
    }

    public static int[] getCardColors(Context context) {
        int[] colors = new int[6];
        colors[0] = ContextCompat.getColor(context, R.color.cardDarkRed);
        colors[1] = ContextCompat.getColor(context, R.color.cardMiddleRed);
        colors[2] = ContextCompat.getColor(context, R.color.cardRed);
        colors[3] = ContextCompat.getColor(context, R.color.cardGreen);
        colors[4] = ContextCompat.getColor(context, R.color.cardMiddleGreen);
        colors[5] = ContextCompat.getColor(context, R.color.cardDarkGreen);
        return colors;
    }

    public static int[] getCardColorsReverse(Context context) {
        int[] colors = new int[6];
        colors[0] = ContextCompat.getColor(context, R.color.cardDarkGreen);
        colors[1] = ContextCompat.getColor(context, R.color.cardMiddleGreen);
        colors[2] = ContextCompat.getColor(context, R.color.cardGreen);
        colors[3] = ContextCompat.getColor(context, R.color.cardRed);
        colors[4] = ContextCompat.getColor(context, R.color.cardMiddleRed);
        colors[5] = ContextCompat.getColor(context, R.color.cardDarkRed);
        return colors;
    }
}
