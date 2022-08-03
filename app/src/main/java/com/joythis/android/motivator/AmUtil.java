package com.joythis.android.motivator;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class AmUtil {
    public final static String STAMP = "@AmUtil";

    Activity mA; //AmUtil is servicing to Activity mA

    public AmUtil (Activity pA){
        this.mA = pA;
    }//AmUtil

    //fb - feedback
    public void fb(String pMsg){
        Toast t = Toast.makeText(
            this.mA,
            pMsg,
            Toast.LENGTH_LONG
        );
        t.show();
    }//fb

    public static boolean qualityControl(
        Object[] pCol
    ){
        for (Object o : pCol){
            if (o==null){
                return false;
            }
        }//for
        return true;
    }//qualityControl

    public static int randomInt(int pMin, int pMax){
        Random r = new Random();
        int iAmplitude =
            Math.max(pMin, pMax) - Math.min(pMin, pMax) + 1;
        int iJump = r.nextInt(iAmplitude);
        int iDestination = Math.min(pMin, pMax) + iJump;
        return iDestination;
    }//randomInt
}//AmUtil
