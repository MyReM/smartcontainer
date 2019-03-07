package com.zsf.container.serialutils;

import com.zsf.smart.handler.TagManager;
import com.zsf.smart.util.LibLoader;
import com.zsf.smart.util.SmartHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RFIDHelper {

    private SmartHelper smartHelper;

    private TagManager tagManager = new TagManager() {
        @Override
        public void inventory(List<String> list) {

        }

        @Override
        public void diffrent(List<String> list) {

        }
    };
    public void openRFID() {

        try {
            smartHelper = new SmartHelper(tagManager);
            smartHelper.open(null,"COM8");
            smartHelper.inventory(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void closeRFID() {
        smartHelper.close();
    }

    public static void main(String[] args) throws InterruptedException {
        GetOutRFID getOutRFID = new GetOutRFID();
        getOutRFID.openRFID();

        TimerTask timerTask = new TimerTask() {
            public void run() {
                getOutRFID.inventory();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0L, (long) 2000);
    }
}
