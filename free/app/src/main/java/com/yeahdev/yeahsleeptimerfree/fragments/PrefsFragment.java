package com.yeahdev.yeahsleeptimerfree.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.BuildConfig;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yeahdev.inappbilling.util.IabHelper;
import com.yeahdev.inappbilling.util.IabResult;
import com.yeahdev.inappbilling.util.K;
import com.yeahdev.inappbilling.util.Purchase;
import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.helper.Util;


public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private IabHelper mHelper;
    private Preference btnOpenFirstLoadPopUp, btnIABTest, btnIAB1, btnIAB2, btnIAB3, linkToPlayStore, linkToPSOtherApps, btnSendFeedback;

    public PrefsFragment() {}
    public static PrefsFragment newInstance() { return new PrefsFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //
        String base64EncodedPublicKey = K.getApplicationKey();
        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(K.IabDebugTag, "IAB Setup failed!");
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.d(K.IabDebugTag, "IAB Setup success!");
                        mHelper.enableDebugLogging(true, K.IabDebugTag);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prefs, container, false);

        initPreferences();
        setupListener();

        return v;
    }
    /**
     *
     */
    private void initPreferences() {
        btnOpenFirstLoadPopUp = findPreference("btnOpenHowToPref");
        btnIABTest = findPreference("btnOpenInAppBillingTestPref");
        btnIAB1 = findPreference("btnOpenInAppBillingSup1Pref");
        btnIAB2 = findPreference("btnOpenInAppBillingSup2Pref");
        btnIAB3 = findPreference("btnOpenInAppBillingSup3Pref");
        linkToPlayStore = findPreference("linkToPlayStorePref");
        linkToPSOtherApps = findPreference("linkToPlayStoreOtherAppsPref");
        btnSendFeedback = findPreference("btnSendFeedbackEmailPref");
    }
    /**
     *
     */
    private void setupListener() {
        btnOpenFirstLoadPopUp.setOnPreferenceClickListener(this);
        btnIABTest.setOnPreferenceClickListener(this);
        btnIAB1.setOnPreferenceClickListener(this);
        btnIAB2.setOnPreferenceClickListener(this);
        btnIAB3.setOnPreferenceClickListener(this);
        linkToPlayStore.setOnPreferenceClickListener(this);
        linkToPSOtherApps.setOnPreferenceClickListener(this);
        btnSendFeedback.setOnPreferenceClickListener(this);
    }

    /**
     * BEGIN - IAB METHODS
     */
    public void openIAB(int iabLevel) {
        if (mHelper != null) {
            mHelper.flagEndAsync();
            switch (iabLevel) {
                case 0:
                    mHelper.launchPurchaseFlow(getActivity(), K.ITEM_SKU_Testing, 100011, mPurchaseFinishedListener, K.PurchasedTestToken);
                    break;
                case 1:
                    mHelper.launchPurchaseFlow(getActivity(), K.ITEM_SKU_Support_Small, 100021, mPurchaseFinishedListener, K.PurchasedSupportSmallToken);
                    break;
                case 2:
                    mHelper.launchPurchaseFlow(getActivity(), K.ITEM_SKU_Support_Mid, 100031, mPurchaseFinishedListener, K.PurchasedSupportMidToken);
                    break;
                case 3:
                    mHelper.launchPurchaseFlow(getActivity(), K.ITEM_SKU_Support_High, 100041, mPurchaseFinishedListener, K.PurchasedSupportHighToken);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     *
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Toast.makeText(getActivity(), "Thanks for supporting my work!. You have already buy the Item.", Toast.LENGTH_SHORT).show();
            } else if (purchase.getSku().equals(K.ITEM_SKU_Testing)) {
                Toast.makeText(getActivity(), "Thanks for Testing.", Toast.LENGTH_SHORT).show();
            } else if (purchase.getSku().equals(K.ITEM_SKU_Support_Small)) {
                Toast.makeText(getActivity(), "Thanks for Supporting my Work - Small!", Toast.LENGTH_SHORT).show();
            } else if (purchase.getSku().equals(K.ITEM_SKU_Support_Mid)) {
                Toast.makeText(getActivity(), "Thanks for Supporting my Work - Mid!", Toast.LENGTH_SHORT).show();
            } else if (purchase.getSku().equals(K.ITEM_SKU_Support_High)) {
                Toast.makeText(getActivity(), "Thanks for Supporting my Work - High!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No valid Item!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     *
     */
    public void disposeMHelper() {
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
    /**
     * END - IAB METHODS
     */

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "btnOpenHowToPref":
                Util.buildFirstLoadDialog(getActivity());
                break;
            case "btnOpenInAppBillingTestPref":
                openIAB(0);
                break;
            case "btnOpenInAppBillingSup1Pref":
                openIAB(1);
                break;
            case "btnOpenInAppBillingSup2Pref":
                openIAB(2);
                break;
            case "btnOpenInAppBillingSup3Pref":
                openIAB(3);
                break;
            case "linkToPlayStorePref":
                Util.navToPlaystoreEntry(getActivity());
                break;
            case "linkToPlayStoreOtherAppsPref":
                Util.navToAllPlaystoreApps(getActivity());
                break;
            case "btnSendFeedbackEmailPref":
                Util.buildFeedback(getActivity());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposeMHelper();
    }
}
