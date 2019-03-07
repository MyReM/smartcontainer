package com.zsf.container.serialutils;

import com.yang.serialport.manager.SerialPortManager;
import com.zsf.container.entity.SaveNfc;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.List;


public class FindByHandleUtils {

    private SerialPort serialport;

    public Integer open(String portName){
        try {
            if (serialport != null) {
                SerialPortManager.closePort(serialport);
            }
            serialport = SerialPortManager.openPort(portName, 115200);
            return 200;
        } catch (PortInUseException e) {
            return 500;
        }
    }
    public String getEPCUid(){
        SaveNfc saveNfc = new SaveNfc();
        SerialPortManager.sendToPort(serialport, ByteUtils.hexStr2Byte("FEAA0720005221"));
        SerialPortManager.addListener(serialport, new SerialPortManager.DataAvailableListener(){

            @Override
            public void dataAvailable() {
                byte[] data = null;
                try {
                    if (serialport == null) {
                        System.out.println("串口对象为空，监听失败！");
                        SerialPortManager.closePort(serialport);
                    } else {
                        data = SerialPortManager.readFromPort(serialport);
                        if (ByteUtils.byteArrayToHexString(data).length() < 36) {
                            saveNfc.setNfc("读取失败");
                        } else if (ByteUtils.byteArrayToHexString(data).length() == 36){
                            saveNfc.setNfc(ByteUtils.byteArrayToHexString(data));
                        } else {
                            saveNfc.setNfc("读取失败");
                        }
                        SerialPortManager.closePort(serialport);
                    }
                } catch (Exception e) {
                    saveNfc.setNfc(ByteUtils.byteArrayToHexString(data));
                    // 发生读取错误时显示错误信息后退出系统
                    SerialPortManager.closePort(serialport);
                }
            }});
        try {
            // 线程睡眠0.1秒让saveNFC获取到值，必须设置，不然获取不到值
            Thread.sleep(100);
        } catch (InterruptedException e) {
            saveNfc.setNfc("读取失败");
        }
        return saveNfc.getNfc().equals("读取失败") ? "读取失败" : saveNfc.getNfc().substring(20,34);
    }
    public static void main(String args[]) {
        FindByHandleUtils findByHandleUtils = new FindByHandleUtils();
//        int status = findByHandleUtils.open("COM4");
//        if (status == 200) {
//            String data = findByHandleUtils.getEPCUid();
//            System.out.println("dataa:" + data);
//        }
       List<String> list = SerialPortManager.findPorts();
       for(String str : list) {
           System.out.println("COM:" + str);
       }
    }
}
