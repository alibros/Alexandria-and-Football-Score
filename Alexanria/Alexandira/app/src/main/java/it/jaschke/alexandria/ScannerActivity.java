package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Ali on 26/07/15.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    public static final String BARCODE_BUNDLE_KEY = "barCode";
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the scannerView and assign it as Activity's ContentView
        scannerView = new ZXingScannerView(this);

        //Activity implements the Result Handler - See below
        scannerView.setResultHandler(this);
        setContentView(scannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(BARCODE_BUNDLE_KEY, result.getText());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
