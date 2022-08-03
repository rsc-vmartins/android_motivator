package com.joythis.android.motivator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String STAMP = "@MainActivity";
    /*
    discussion about String[] (static arrays) vs. ArrayList<String>
    discussion strings in XML (better) vs. strings in Java (recompilation req)
     */
    //ArrayList<String> mAlPositiveMsgs; //e.g. if the app was to support user defined messages
    //ArrayList<String> mAlNegativeMsgs;
    String[] mAlPositiveMsgs;
    String[] mAlNegativeMsgs;

    Context mContext; //permanent access to the Activity's context
    SeekBar mSbProbPositiveMsgs;
    Button mBtnGetMsg;
    TextView mTvMsgs;
    AmUtil mUtil; //to bridge to the tools @AmUtil

    //to have a working ListView we'll also need a "data source" and an "adapter"
    ListView mLvMsgs; //null - initial value set @init (called on onCreate)
    //data src - these will be the messages presented to the user
    ArrayList<String> mAlMsgs;
    ArrayAdapter<String> mAd;//Adapter

    void displayMessage (String pStr){
        if (mTvMsgs!=null){
            //v1 - a TextView is used to display the message
            String strAll = mTvMsgs.getText().toString();
            mTvMsgs.setText(pStr+"\n"+strAll);
        }//if
        if (mLvMsgs!=null){
            //v2 - a ListView is used to display the message
            //TODO: display the new message in the ListView
            mAlMsgs.add(0, pStr); //stack push - insert @head - @top
            //mAlMsgs.add(pStr); //message appears at the end
            mAd.notifyDataSetChanged();
        }//if
    }//displayMessage

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //this (beter)
            String strInItem = parent.getItemAtPosition(position).toString();
            //or this
            TextView tv = (TextView)view;
            strInItem = tv.getText().toString();

            String strReady =
                    String.format("@pos %d : %s", position, strInItem);

            mUtil.fb(strReady);
        }//onItemClick
    };//mItemClickListener

    View.OnClickListener mBtnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            pay attention to the user's probability of positive msg
             */
            int iUserProbability = mSbProbPositiveMsgs.getProgress(); //0-100
            int iProb = AmUtil.randomInt(0, 100);
            boolean bPositiveMsg = iProb <= iUserProbability;
            boolean bCaution = mAlPositiveMsgs!=null && mAlNegativeMsgs!=null
                    && mAlNegativeMsgs.length>0
                    && mAlPositiveMsgs.length>0;

            if (bCaution){
                if (bPositiveMsg){
                    //?
                    int idxRandom = AmUtil.randomInt(
                            0
                            ,
                            mAlPositiveMsgs.length-1
                    );
                    String strPositive = mAlPositiveMsgs[idxRandom];
                    displayMessage(strPositive);
                    /*
                    //display!
                    String strAll =
                        strPositive + "\n" + mTvMsgs.getText().toString();
                    mTvMsgs.setText(strAll);
                     */
                }//if
                else{
                    //?
                    int idxRandom = AmUtil.randomInt(
                            0
                            ,
                            mAlNegativeMsgs.length-1
                    );
                    String strNegative = mAlNegativeMsgs[idxRandom];
                    displayMessage(strNegative);
                    /*
                    //display!
                    String strAll = strNegative + "\n" +
                        mTvMsgs.getText().toString();
                    mTvMsgs.setText(strAll);
                     */
                }//else
            }//if bCaution
        }//onClick
    };//mBtnClickHandler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.rl_motivator_v1);
        setContentView(R.layout.rl_motivator_v2);

        init();
    }//onCreate

    void init(){
        //1 - bindings
        mContext = this;
        mUtil = new AmUtil(this);
        mAlPositiveMsgs = this.getResources().getStringArray(R.array.saPositiveMsgs);
        /*
        for (String s: fromXML){
            mAlPositiveMsgs.add(s);
        }//for
         */
        mAlNegativeMsgs = getResources().getStringArray(R.array.saNegativeMsgs);

        mSbProbPositiveMsgs = findViewById(R.id.idSbProbPositiveMsgs);
        mBtnGetMsg = findViewById(R.id.idBtnGetMsg);
        mTvMsgs = findViewById(R.id.idTvMsgs); //null @ v2 ; ok @ v1
        mLvMsgs = findViewById(R.id.idLvMsgs); //ok @ v2 ; null @ v1
        if (mLvMsgs!=null){
            mAlMsgs = new ArrayList<String>(); //no strings, empty ArrayList
            mAd = new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_list_item_1,//each String/Message will appear inside this layout
                mAlMsgs//data source
            );
            mLvMsgs.setAdapter(mAd); //the ListView gets bound to the mAd
        }//if


        View[] mAllRelevantObjects =
                {mSbProbPositiveMsgs, mBtnGetMsg /*, mTvMsgs*/};
        //static method
        boolean bQualityControlResult = AmUtil.qualityControl(
            mAllRelevantObjects
        );
        //if (!bQualityControlResult){
        if (bQualityControlResult==false){
            //will happen @v2
            Log.e(STAMP, getResources().getString(R.string.strQCFail));
            finish();
        }//if

        mUtil.fb("Hello!");

        //2 - behaviors
        //without this the Button would "do nothing"
        mBtnGetMsg.setOnClickListener(mBtnClickHandler);
        //without the following code, list items would "do nothing"
        mLvMsgs.setOnItemClickListener(mItemClickListener);
    }//init
}//MainActivity