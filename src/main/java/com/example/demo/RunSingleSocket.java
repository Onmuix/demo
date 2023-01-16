//package com.example.demo;
//
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.nuclearled.assit_package.AssitStatic;
//
//import com.nuclearled.database_package.DbDevice;
//import com.nuclearled.database_package.DbDisinfectRecord;
//import com.nuclearled.database_package.DbManager;
//import com.nuclearled.database_package.DbReceiveTimePlan;
//import com.nuclearled.database_package.DbTimePlan;
//
//import com.nuclearled.event_package.EventModeFinishStatus;
//import com.nuclearled.event_package.EventReceiveBeatTransmit;
//
//import com.nuclearled.my_thread_package.ThreadAgent;
//
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.sql.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.stream.Collectors;
//
//public class RunSingleSocket extends Thread {
//
//    private ThreadAgent mTa;
//    private DbManager mDm;
//    private SocketWifiManager mSwm;
//
//    private final String TAG = "SingleSocketManager";
//
//    private Socket mSocket;//socket对象
//    private InputStream mInputStream;//输入流
//    private OutputStream mOutputStream;//输出流
//
//
//    private final int[] socketGetIntArray = new int[1024];//接收缓存数组
//    private int startDataNum = 0;//接收数组起始位置
//    private int endDataNum = 0;//接收数组结束位置
//
//    // device id
//    private int mIntDeviceId;//(mIntDeviceType<<8 | mIntSocketAddress)
//    private int mIntDeviceType;
//    private int mIntSocketAddress;
//
//    //设备接收状态超时标志位(计时超时)
//    private int mConnectStatus;
//
//    //    private Map<>
////    private List<RunWarnErrorMessage> mListRunEw;
//    private boolean mBlRunEwExitFlag = true;
//
//    //当前设备工作模式
//    private int mIntWorkModeType;
//
//    @Override
//    public String toString() {
//        return "RunSingleSocket{" +
//                "mTa=" + mTa +
//                ", mDm=" + mDm +
//                ", mSwm=" + mSwm +
//                ", TAG='" + TAG + '\'' +
//                ", mSocket=" + mSocket +
//                ", mInputStream=" + mInputStream +
//                ", mOutputStream=" + mOutputStream +
//                ", socketGetIntArray=" + Arrays.toString(socketGetIntArray) +
//                ", startDataNum=" + startDataNum +
//                ", endDataNum=" + endDataNum +
//                ", mIntDeviceId=" + mIntDeviceId +
//                ", mIntDeviceType=" + mIntDeviceType +
//                ", mIntSocketAddress=" + mIntSocketAddress +
//                ", mConnectStatus=" + mConnectStatus +
//                ", mBlRunEwExitFlag=" + mBlRunEwExitFlag +
//                ", mIntWorkModeType=" + mIntWorkModeType +
//                '}';
//    }
//
//    /*Constant*/
//
//    //Host fall rise cmd int[]
//    public final int[] mIaCmdHfrHeartBeart = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//    public final int[] mIaCmdHfrWorkMode = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0xFF};
//    public final int[] mIaCmdHfrResetWifi = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x03, 0xFF};
//    public final int[] mIaCmdHfrBoardVersion = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x04, 0xFF};
//    public final int[] mIaCmdHfrUvLifeTime = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x05, 0x00, 0xFF};
//    public final int[] mIaCmdHfrDebugSwitch = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x06, 0x00, 0xFF};
//    public final int[] mIaCmdHfrSlowDownCalibration = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x07, 0xFF};
//    public final int[] mIaCmdHfrDistanceSensor = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0xFF};
//    public final int[] mIaCmdHfrWheelDetectionSwitch = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x08, 0x00, 0xFF};
//
//    public final int[] mIaCmdHfrAddTimePlan = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF};
//
//    public final int[] mIaCmdHfrSetTimePlan = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0xFF};
//    public final int[] mIaCmdHfrDisinfectionRecord = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0B, 0x00, 0xFF};
//    public final int[] mIaCmdHfrBallastDetection = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0C, 0x00, 0xFF};
//    public final int[] mIaCmdHfrBuzzer = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0D, 0x00, 0x00, 0xFF};
//    public final int[] mIaCmdHfrScanAuxiliary = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0E, 0xFF};
//
//    //land manaual uv light
//    public final int[] mIaCmdLmHeartBeat = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};
//    public final int[] mIaCmdLmQueryVersion = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x02, 0x00};
//    public final int[] mIaCmdLmResetWifi = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x03, 0x00};
//    public final int[] mIaCmdLmQueryUvLife = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x04, 0x00};
//    public final int[] mIaCmdLmResetUvLife = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x05, 0x00, 0x00};
//    public final int[] mIaCmdLmWorkMode = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x000, 0x00};
//    public final int[] mIaCmdLmUvCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00};
//    public final int[] mIaCmdLmAtomizationCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00};
//    public final int[] mIaCmdLmBuzzerCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00};
//    public final int[] mIaCmdLmInfraredCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0a, 0x00, 0x00};
//    public final int[] mIaCmdLmStartDisinfectionCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0b, 0x00};
//    public final int[] mIaCmdLmEndDisinfectionCtrl = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0c, 0x00};
//
//    //High power cmd int[]
//    private final int[] mIaCmdHpHeartBeart = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};
//    private final int[] mIaCmdHpUvLifeTimeQuery = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x02, 0x00};
//    private final int[] mIaCmdHpUvLifeTimeReset = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x03, 0x00};
//    private final int[] mIaCmdHpDisinfectionTime = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00};
//    private final int[] mIaCmdHpUvLight = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x05, 0x00, 0x00};
//    private final int[] mIaCmdHpAutomization = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00};
//    private final int[] mIaCmdHpBuzzer = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00};
//    private final int[] mIaCmdHpBodyInfrared = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00};
//    private final int[] mIaCmdHpOpenDisinfection = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x09, 0x00};
//    private final int[] mIaCmdHpCloseDisinfection = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x0a, 0x00};
//
//
//    public RunSingleSocket(Socket socket) {
//        mSocket = socket;
//        mTa = ThreadAgent.getmTa();
//        mDm = mTa.getmDbManager();
//        mSwm = mTa.getmSwm();
//    }
//
//    public void closeSocket() {
//        try {
//            if (mInputStream != null) {
//                mInputStream.close();
//            }
//            if (mOutputStream != null) {
//                mOutputStream.close();
//            }
//            if (mSocket != null) {
//                mSocket.close();
//                mSocket = null;
//            }
//            mBlRunEwExitFlag = false;
//
//        } catch (Exception e) {
//            AssitStatic.getmAssitStatic().collectError(e);
//        }
//    }
//
//    public void closeSocketRemove() {
//        try {
//            closeSocket();
//            Log.d(TAG, "关闭socket");
//        } catch (Exception e) {
//            AssitStatic.getmAssitStatic().collectError(e);
//            Log.d(TAG, "关闭单个socket失败");
//        } finally {
//            ThreadAgent.getmTa().getmSwm().removeListRunSingleSocket(this);
//            mSocket = null;//一定是为null
//        }
//    }
//
//    public boolean isConnectSocket() {
//        return mSocket != null && !mSocket.isClosed();
//    }
//
//    //get
//
//    //get device id
//    public int getmIntDeviceId() {
//        return mIntDeviceId;
//    }
//
//    private void pushShortTask(Runnable runTask) {
//        ThreadAgent.getmTa().pushShortTask(runTask);
//    }
//
//
//    /*receive*/
//    @Override
//    public void run() {
//        try {
//            mInputStream = mSocket.getInputStream();//获取输入流
//            mOutputStream = mSocket.getOutputStream();//获取输出流
//            while (mSocket != null) {
//                try {
//                    int inputNum = 0;//检测接收字符是否正确和接收字符测数量
//                    byte[] bufferByteData = new byte[1024];//接收缓存数据
//                    inputNum = mInputStream.read(bufferByteData);//读取socket接收数据
//                    if ((inputNum != -1) && (inputNum != 0) && (inputNum <= 512)) {//inputStateNum为0或-1都是读取出错,大于512则说明网络堵塞,不要该数据帧
//
//                        //处理接收数据的错误
//                        try {
//                            //把接收的byte数组转换成int数组
//                            Log.d("wisray_test2", "接收到数据" + inputNum);
//                            for (int i = 0; i < inputNum; i++) {
//                                socketGetIntArray[endDataNum] = ((int) bufferByteData[i]) & 0x000000ff;
//                                //判断帧开头是否0xA0, 0xA0
//                                if (socketGetIntArray[startDataNum] == 0xA0) {
//                                    endDataNum++;
//                                } else {
//                                    endDataNum = 0;
//                                    startDataNum = endDataNum;
//                                }
//
//                                if (endDataNum - startDataNum >= 2) {
//                                    if (socketGetIntArray[startDataNum + 1] != 0xA0) {
//                                        endDataNum = 0;
//                                        startDataNum = endDataNum;
//                                    }
//                                }
//                            }
//                            //当接收到的数据大于最小的6个时,进入检验
//                            if (endDataNum - startDataNum >= 6) {
//                                int tLenFrame = endDataNum - startDataNum;
//                                int[] tCheckArray = new int[tLenFrame];
//                                for (int i : socketGetIntArray) {
////                                    Log.d("TAG", "run checkSocketData01: "+i);
//                                }
//                                System.arraycopy(socketGetIntArray, startDataNum, tCheckArray, 0, tLenFrame);//提取数组
//                                for (int i1 : tCheckArray) {
////                                    Log.d("TAG", "run checkSocketData1: "+i1);
//                                }
//
//                                //校验socket数据
//                                checkSocketData(tCheckArray);
//                                if (endDataNum >= 512) {
//                                }
//                                endDataNum = 0;
//                                startDataNum = 0;
//                            }
//                        } catch (Exception e) {
//                            AssitStatic.getmAssitStatic().collectError(e);
//                            startDataNum = 0;
//                            endDataNum = startDataNum;
//                        }
//                    } else {
//
//                        if (inputNum > 512) {
//                            endDataNum = 0;
//                            startDataNum = 0;
//                        } else {
//                            closeSocketRemove();
//                        }
//                    }
//                } catch (Exception e) {
//                    AssitStatic.getmAssitStatic().collectError(e);
//                    closeSocketRemove();
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            AssitStatic.getmAssitStatic().collectError(e);
//        }
//    }
//
//    //检验socket数据
//    private void checkSocketData(int[] tCheckIntArray) throws Exception {
//
//        int checkResultInt = 0;//代表校验的数组里包含多少个正确的帧
//        int tLen = tCheckIntArray.length;//待检验数组的长度
//
//        //tLen-8:代表检验数组最少得有9个以上才进行
//        for (int i = 0; i < (tLen - 6); i++) {
//            int tLenFrame = tCheckIntArray[i + 2];//预取数据帧的长度
//            //判断数据是否正确
//            if ((tCheckIntArray[i] != 0xA0) || (tCheckIntArray[i + 1] != 0xA0) || (tLenFrame > (tLen - i - 4))) {
//                continue;
//            }
//            int checkSumInt = 0;//初始化检验和对象
//
//            //计算出校验码
//            for (int j = 0; j < (tLenFrame + 3); j++) {
////                checkSumInt = (checkSumInt + tCheckIntArray[i + j]) & 0x00FF;
//                checkSumInt = (checkSumInt ^ tCheckIntArray[i + j]) & 0x00FF;
//            }
//
//            if (checkSumInt == tCheckIntArray[tLenFrame - 1 + i + 4]) {
//                checkResultInt++;
//                Log.d(TAG, "校验成功: " + checkResultInt + "次");
//                int[] correctArray = new int[tLenFrame + 4];
//                System.arraycopy(tCheckIntArray, i, correctArray, 0, tLenFrame + 4);
//
//                for (int i1 : correctArray) {
//                    Log.d("TAG", "run checkSocketData2: " + i1);
//                }
//                cmdResponseSelect(correctArray);//校验成功,进行处理
//            }
//        }
//
//        if (checkResultInt > 0) {
//            if (endDataNum >= 512) {
//                startDataNum = 0;
//                endDataNum = 0;
//            }
//            startDataNum = endDataNum;
//        }
//    }
//
//    //解析正确帧的数据
//    //deal receive host device
//
//    private void cmdResponseSelect(int[] correctArray) throws Exception {
//
//        try {
//            mConnectStatus = 0;
//            mIntDeviceType = correctArray[3];
//            mIntSocketAddress = correctArray[4];
//
//            // 这里的|表示相加
//            int intDeviceId = (mIntDeviceType << 8) | mIntSocketAddress;
//            if (intDeviceId != mIntDeviceId) {
//                mSwm.removeSocketFromDeviceId(intDeviceId);
//                mIntDeviceId = intDeviceId;
//            }
//            int intFunctionNum = correctArray[5];
//
//            //截取接收数据码
//            int[] dataPayLoad = new int[correctArray.length - 7];
//            if (dataPayLoad.length != 0) {
//                System.arraycopy(correctArray, 6, dataPayLoad, 0, dataPayLoad.length);
//            }
//
//            if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_HOST) {
//                receiveDealHostData(intFunctionNum, dataPayLoad);//======
//                if (intFunctionNum == 3) {
//                    return;
//                }
//
//                //壁挂
//            } else if (mIntDeviceType == AttrDeviceProperty.DEVICE_TYPE_HANGING) {
//                receiveDealDeviceHangingData(intFunctionNum, dataPayLoad);
//
//            } else if (mIntDeviceType == AttrDeviceProperty.DEVICE_TYPE_AUXILIARY) {
//                receiveAuxiliaryData(intFunctionNum, dataPayLoad);
//            } else if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_LAND_MANUAL) {
//                receiveDealLandManualData(intFunctionNum, dataPayLoad);
//            } else if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER) {
//                receiveDealHighPowerData(intFunctionNum, dataPayLoad);
//            }
//
//
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//
//    }
//
//    private void receiveDealHostData(int intFunction, int[] iaPayload) throws Exception {
//        if (intFunction == 0x01) {
//
//            int intPosition = 1;
//
//            //device error num
//            int intPlErrorNum = iaPayload[intPosition];
//            intPosition++;
//
//            //接收错误的时间
//
////            if (intPlErrorNum!=0){
////
////            }else {
////                intPosition+=8;
////            }
//            int intPlErrorDateYear = iaPayload[intPosition] | (iaPayload[++intPosition] << 8);
//            intPosition++;
//            int intPlErrorDateMonth = iaPayload[intPosition];
//            if (intPlErrorDateMonth != 0) {
//                intPlErrorDateMonth = intPlErrorDateMonth - 1;
//            }
//            intPosition++;
//            int intPlErrorDateDay = iaPayload[intPosition];
//            intPosition++;
//            int intPlErrorDateHour = iaPayload[intPosition];
//            intPosition++;
//            int intPlErrorDateMinute = iaPayload[intPosition];
//            intPosition++;
//            int intPlErrorDateSecond = iaPayload[intPosition];
//
//            int intWeek = iaPayload[++intPosition];
//
//            Calendar plErrorCalendar = Calendar.getInstance();
//            plErrorCalendar.clear();
//            plErrorCalendar.set(intPlErrorDateYear, intPlErrorDateMonth, intPlErrorDateDay, intPlErrorDateHour, intPlErrorDateMinute, intPlErrorDateSecond);
//            long longPlErrorDate = plErrorCalendar.getTimeInMillis();
//
//            //提示信息
//            int intTipMessage = iaPayload[++intPosition];
//
//
//            //device current work mode
//            int intPlCurrentWorkMode = (iaPayload[++intPosition] << 8) | iaPayload[++intPosition];
//            int intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_OFFLINE;
//            if (intPlCurrentWorkMode == 0) {
//                intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STANDBY;
//            } else if (intPlCurrentWorkMode == 0x0101) {
//                intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_DISINFECTING;
//            } else if (intPlCurrentWorkMode == 0x0102) {
//                intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STOP;
//            } else if (intPlCurrentWorkMode == 0x0105) {
//                intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_SELF_TEST;
//            } else if (intPlCurrentWorkMode == 0x0106) {
//                intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_REPLACE_UV;
//            } else {
//                intTransitionCurrentWorkMode = 0xFF;
//            }
//
//            //disinfect run time
//            int intDisinfectRunTime = (iaPayload[++intPosition] * 3600 + iaPayload[++intPosition] * 60 + iaPayload[++intPosition]);
//
//            //disinfect total time
//            if (iaPayload[++intPosition] == 0xFF && iaPayload[intPosition + 1] == 0xFF) {
//                iaPayload[intPosition] = 0;
//                iaPayload[intPosition + 1] = 0;
//            }
//            int intDsinfectTotalTime = (iaPayload[intPosition] | (iaPayload[intPosition + 1] << 8)) * 60;
//
//            mSwm.refreshHostRiseFallAttrDeviceData(mIntDeviceId, intTransitionCurrentWorkMode, intDsinfectTotalTime,
//                    intDisinfectRunTime, intTipMessage, intPlErrorNum, longPlErrorDate);
//
//            //send heart beat
//            ctrlRunRiseFallWorkMode(0x01, null);
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //delivery data
//                    EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, intFunction, iaPayload));
//                }
//            }).start();
//
//
//        } else if (intFunction == 0x03) {
//            //接收完成状态
//            int intModeFinishStatus = iaPayload[0];
//            AttrSwConnectDevice attrDevice = ThreadAgent.getmTa().getmSwm().getMapAttrDeviceFromId(mIntDeviceId);
//            if (attrDevice != null) {
//                attrDevice.setIntDeviceFinishWorkStatus(intModeFinishStatus);
//            }
//            int intModeWorkType = attrDevice.getIntDeviceCurrentWorkMode();
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //传递数据
//                    EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, 0x03, new int[]{intModeFinishStatus}));
//                    //传递完成状态界面更新
//                    EventBus.getDefault().post(new EventModeFinishStatus(mIntDeviceId, intModeFinishStatus, intModeWorkType));
//                }
//            }).start();
//
//        } else if (intFunction == 0x04) {
//
//            //uv灯使用时间
//            int intUvUserHour = iaPayload[0] | (iaPayload[1] << 8);
//            DbDevice dbDevice = mDm.queryUvLifeHour(mIntDeviceId);
//            if (dbDevice == null) {
//                return;
//            }
//            int intDbUvLifeHour = dbDevice.getIntUvLifeHour();
//            if (intDbUvLifeHour != intUvUserHour) {
//                mDm.setDeviceUvTime(mIntDeviceId, 1, new int[]{intUvUserHour});
//                if (intUvUserHour == 0) {
//                    mDm.resetUvResetDate(mIntDeviceId, 0);
//                }
//            }
//
////            EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId,0x04,new int[]{intDbUvLifeHour}));
//        } else if (intFunction == 0x05) {
//            //定时消毒返回命令
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        dealTimePlanPayLoadData(iaPayload);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//        } else if (intFunction == 0x06) {
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    saveDbDisinfectionRecord(iaPayload);
//                }
//            }).start();
//        }
//    }
//
//    //存储消毒记录
//    private void saveDbDisinfectionRecord(int[] iaPayload) {
//        int intIaLength = iaPayload.length;
//        //消毒记录长度
//        int intDataLength = 9;
//        int intRecordNum = intIaLength / intDataLength;
//        DbManager dbManager = ThreadAgent.getmTa().getmDbManager();
//        String strDeviceName = dbManager.getDbDeviceName(mIntDeviceId);
//        long longRoomId = dbManager.getDeviceRoomId(mIntDeviceId);
//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//
//        for (int i = 0; i < intRecordNum; i++) {
//            calendar.clear();
//            int intYear = iaPayload[i * intDataLength] | (iaPayload[i * intDataLength + 1] << 8);
//            int intMonth = iaPayload[i * intDataLength + 2];
//            int intDay = iaPayload[i * intDataLength + 3];
//            int intHour = iaPayload[i * intDataLength + 4];
//            int intMinute = iaPayload[i * intDataLength + 5];
//            int intDisinfectionMinute = iaPayload[i * intDataLength + 6] | (iaPayload[i * intDataLength + 7] << 8);
//            int intDisinfectionStatus = iaPayload[i * intDataLength + 8];
//            calendar.set(intYear, intMonth - 1, intDay, intHour, intMinute, 0);
//            long longDateNum = calendar.getTimeInMillis();
//            boolean blExitRecord = dbManager.isExitRecordFromDeviceIdDate(mIntDeviceId, intDisinfectionMinute, longDateNum);
//
//            Calendar calendarNow = Calendar.getInstance(Locale.CHINA);
//            int intYear2 = calendarNow.get(Calendar.YEAR);
//            int intMonth2 = calendarNow.get(Calendar.MONTH);
//            //判断数据库的时间戳是否跟目前添加数据的时间戳相同 相同则不添加
////            List<DbDisinfectRecord> listStartRecord = ThreadAgent.getmTa().getmDbManager().getMonthOfDisinfectionRecord(intYear2,intMonth2);
////            for (DbDisinfectRecord dbDisinfectRecord : listStartRecord) {
////                Log.d(TAG, "saveDbDisinfectionRecord: "+dbDisinfectRecord);
////                if(dbDisinfectRecord.getmLongDisinfectData()==intDisinfectionMinute){
////                    return;
////                }
////            }
//
//            if (!blExitRecord) {
//                dbManager.saveSocketDisinfectionRecord(mIntDeviceId, strDeviceName, longRoomId,
//                        intDisinfectionMinute, intDisinfectionStatus, longDateNum);
//            }
//        }
//        ctrlRunRiseFallWorkMode(0x0B, new int[]{0x01});
//    }
//
//
//    //处理
//    private void dealTimePlanPayLoadData(int[] iaPayload) throws Exception {
//        if (mIntDeviceId == 0) {
//            return;
//        }
//        DbManager dbManager = ThreadAgent.getmTa().getmDbManager();
//        int intTpNum = iaPayload.length / 9;
//        int intTpType = 0;
//        boolean blTpChangeStatus = false;
//        if (intTpNum != 0) {
//            intTpType = iaPayload[0];
//        }
//
//        Log.d(TAG, "dealTimePlanPayLoadData: " + intTpType);
//        switch (intTpType) {
//            case 0: {
//                //查询结果
//                List<DbReceiveTimePlan> mListReceiveTp = new ArrayList<>();
//                List<DbReceiveTimePlan> mListDbReTp = dbManager.dbGetReTimePlanFromDeviceId(mIntDeviceId);
//                //app本地数据库
//                List<DbTimePlan> listPlan = ThreadAgent.getmTa().getmDbManager().dbtTpGetDbAllTimePlan();
//                List<DbTimePlan> listPlanData = new ArrayList<>();
//                for (int i = 0; i < intTpNum; i++) {
//                    DbTimePlan dbTimePlan = new DbTimePlan();
//                    DbReceiveTimePlan attrReTp = new DbReceiveTimePlan();
//                    attrReTp.setIntDeviceId(mIntDeviceId);                                  //该消毒计划所属设备id(以,隔开)
//                    attrReTp.setIntTimePlanId(iaPayload[i * 9 + 1]);                    //定时计划序号(1-10);
//                    attrReTp.setStrName(AttrDeviceProperty.getDeviceName(mIntDeviceId));//定时名称
//                    attrReTp.setIntTimePlanType(iaPayload[i * 9 + 2]);                  //定时计划类型:0:重复  1:单次
//                    attrReTp.setIntStartHour(iaPayload[i * 9 + 3]);//开始时间的小时
//                    attrReTp.setIntStartMinute(iaPayload[i * 9 + 4]);//开始时间的分钟
//                    attrReTp.setIntWeekStatus(iaPayload[i * 9 + 5]);//每周星期的选择,说明:{"周日", "周一","周二","周三","周四","周五","周六"},最低位对应"周日" : 000000'1'
//                    attrReTp.setIntDurationTimeMinute((iaPayload[i * 9 + 6] | (iaPayload[i * 9 + 7] << 8)));//消毒持续时间
//                    attrReTp.setIntSwitchPlan(iaPayload[i * 9 + 8]);//定时计划打开状态,0:代表关闭状态, 1:代表时开启状态
//                    mListReceiveTp.add(attrReTp);
//
//                    dbTimePlan.setId(0);
//                    dbTimePlan.setStrName("定时计划");
//                    dbTimePlan.setStrDeviceDeviceRoomId(String.valueOf(mIntDeviceId));
//                    dbTimePlan.setIntTimePlanId(iaPayload[i * 9 + 1]);
//                    dbTimePlan.setIntStartHour(iaPayload[i * 9 + 3]);
//                    dbTimePlan.setIntStartMinute(iaPayload[i * 9 + 4]);
//                    dbTimePlan.setIntWeekStatus(iaPayload[i * 9 + 5]);
//                    dbTimePlan.setIntDurationTime((iaPayload[i * 9 + 6] | (iaPayload[i * 9 + 7] << 8)));
//                    dbTimePlan.setIntSwitchPlan(iaPayload[i * 9 + 8]);
//                    dbTimePlan.setIntDisinfectionType(0);
//                    dbTimePlan.setIntPlanDisinfectStatus(0);
//                    listPlanData.add(dbTimePlan);
//                }
//                for (DbReceiveTimePlan attrReTp2 : mListReceiveTp) {
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getId());
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getStrName());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntStartHour());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntStartMinute());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntDurationTimeMinute());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntWeekStatus());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntSwitchPlan());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntTimePlanId());//
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: " + attrReTp2.getIntDeviceId());
//                    Log.d(TAG, "dealTimePlanPayLoadDat1: ====================================");
//                }
//                //比较
//
//                blTpChangeStatus = false;
//                //比较
//                int intDataNum = mListReceiveTp.size();
//                if (intDataNum == mListDbReTp.size()) {
//                    for (int i = 0; i < intDataNum; i++) {
//                        if (mListReceiveTp.get(i).getIntTimePlanId() != mListDbReTp.get(i).getIntTimePlanId()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntTimePlanType() != mListDbReTp.get(i).getIntTimePlanType()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntStartHour() != mListDbReTp.get(i).getIntStartHour()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntStartMinute() != mListDbReTp.get(i).getIntStartMinute()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntWeekStatus() != mListDbReTp.get(i).getIntWeekStatus()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntDurationTimeMinute() != mListDbReTp.get(i).getIntDurationTimeMinute()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                        if (mListReceiveTp.get(i).getIntSwitchPlan() != mListDbReTp.get(i).getIntSwitchPlan()) {
//                            blTpChangeStatus = true;
//                            break;
//                        }
//                    }
//                } else {
//                    blTpChangeStatus = true;
//                }
//                if (blTpChangeStatus) {
//                    mDm.dbClearAndAddListReTpData(mIntDeviceId, mListReceiveTp);
//                    List<DbReceiveTimePlan> dbReceiveTimePlans = mDm.selectDbTimePlanAll();
//                    List<DbTimePlan> timePlanList = new ArrayList<>();
//
//                    for (DbTimePlan dbTimePlan : mDm.dbTimingPlanCount()) {
//                        for (DbReceiveTimePlan dbReceiveTimePlan : dbReceiveTimePlans) {
//                            if (dbTimePlan.getIntWeekStatus() == dbReceiveTimePlan.getIntWeekStatus()
//                                    && dbTimePlan.getIntStartHour() == dbReceiveTimePlan.getIntStartHour()
//                                    && dbTimePlan.getIntStartMinute() == dbReceiveTimePlan.getIntStartMinute()) {
//                                DbTimePlan dbTimePlan1 = new DbTimePlan();
//                                for (int i = 0; i <= dbReceiveTimePlans.size(); i++)
//                                    dbTimePlan1.setId(i);
//                                dbTimePlan1.setStrName(dbTimePlan.getStrName());
//                                dbTimePlan1.setIntStartHour(dbTimePlan.getIntStartHour());
//                                dbTimePlan1.setIntStartMinute(dbTimePlan.getIntStartMinute());
//                                dbTimePlan1.setIntDurationTime(dbTimePlan.getIntDurationTime());
//                                dbTimePlan1.setIntWeekStatus(dbTimePlan.getIntWeekStatus());
//                                dbTimePlan1.setIntSwitchPlan(dbTimePlan.getIntSwitchPlan());
//                                dbTimePlan1.setIntDisinfectionType(dbTimePlan.getIntDisinfectionType());
//                                dbTimePlan1.setStrDeviceDeviceRoomId(dbTimePlan.getStrDeviceDeviceRoomId());
//                                dbTimePlan1.setIntPlanDisinfectStatus(dbTimePlan.getIntPlanDisinfectStatus());
//                                dbTimePlan1.setIntTimePlanId(dbReceiveTimePlan.getIntTimePlanId());
//                                Log.d(TAG, "dealTimePlanPayLoadDataID: " + dbReceiveTimePlan.getIntTimePlanId());
//                                timePlanList.add(dbTimePlan1);
//                            }
//                        }
//                    }
//
//                    //重后的数据
//                    List<DbTimePlan> newTimePlanList = delRepeat(timePlanList);
//                    Log.d(TAG, "timePlanList1: " + newTimePlanList);
//
//                    mDm.clearDbTimePlan();
//                    mDm.dbAddListReTpData(timePlanList);
//                }
//                List<DbTimePlan> listPlan2 = ThreadAgent.getmTa().getmDbManager().dbtTpGetDbAllTimePlan();
//                for (DbTimePlan dbTimePlan : listPlan2) {
//                    Log.d(TAG, "dealTimePlanPayLoadData: " + dbTimePlan);
//                }
//                Log.d(TAG, "dealTimePlanPayLoadData 机器数据库: " + mDm.selectDbTimePlanAll().size());
//                Log.d(TAG, "dealTimePlanPayLoadData 本地数据库: " + mDm.dbTimingPlanCount().size());
//                //eg
//                Log.d(TAG, "dealTimePlanPayLoadData:count " + mDm.selectDbTimePlanAll().size());
//                Log.d(TAG, "dealTimePlanPayLoadData:count " + mDm.dbTimingPlanCount().size());
//                if (mDm.dbTimingPlanCount().size() == 0 && mDm.selectDbTimePlanAll().size() != 0) {
//                    mDm.dbAddListReTpData(listPlanData);
//                }
//                for (DbReceiveTimePlan dbTimePlan : mDm.selectDbTimePlanAll()) {
//                    Log.d(TAG, "dealTimePlanPayLoadData2:  " + dbTimePlan);
//                }
//            }
//
//
//            case 5:
////                List<DbReceiveTimePlan> dbReceiveTimePlans = mDm.selectDbTimePlanAll();
//
//            case 7:
//                break;
//            case 8:
//                break;
//            default: {
//                ctrlRunRiseFallWorkMode(0x0A, new int[]{0x00, 0x00});
//            }
//        }
//    }
//
//
//    //deal receive hanging device
//    private void receiveDealDeviceHangingData(int intFunction, int[] iaPayload) throws Exception {
//        try {
//            switch (intFunction) {
//                case 0x01: {
////                    //heart beat
////                    int intErrorNum = iaPayload[1];
////
////                    //device current work mode
////                    int intDeviceCurrentWorkMode = iaPayload[26];
////                    int intTransitionDeviceCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STANDBY;
////                    switch (intDeviceCurrentWorkMode) {
////                        case 0x00: {
////                            intTransitionDeviceCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STANDBY;
////                        }
////                        break;
////                        case 0x01: {
////                            intTransitionDeviceCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STOP;
////                        }
////                        break;
////                        case 0x02: {
////                            intTransitionDeviceCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_DISINFECTING;
////                        }
////                        break;
////                    }
////                    //disinfect run time
//////                    int intDisinfectRunTime = iaPayload[4] * 3600 + iaPayload[5] * 60 + iaPayload[6];
//
//                    int int1 = iaPayload[0];
//
//                }
//                case 0x02: {
//
//                }
//                case 0x03: {
//
//                }
//                case 0x04: {
//
//                }
//                break;
//            }
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }
//
//    //接收辅灯数据
//    private void receiveAuxiliaryData(int intFunction, int[] iaPayload) {
//        switch (intFunction) {
//            case 0x01: {
//
//                //device error num
//                int intPlErrorNum = iaPayload[1];
//
//                //device current work mode
//                int intPlCurrentWorkMode = (iaPayload[2] << 8) | iaPayload[3];
//                int intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_OFFLINE;
//                if (intPlCurrentWorkMode == 0) {
//                    intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STANDBY;
//                } else if (intPlCurrentWorkMode == 0x0101) {
//                    intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_DISINFECTING;
//                } else if (intPlCurrentWorkMode == 0x0102) {
//                    intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_STOP;
//                } else if (intPlCurrentWorkMode == 0x0105) {
//                    intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_SELF_TEST;
//                } else if (intPlCurrentWorkMode == 0x0106) {
//                    intTransitionCurrentWorkMode = AttrDeviceProperty.DEVICE_ACTION_STATUS_REPLACE_UV;
//                } else {
//                    intTransitionCurrentWorkMode = 0xFF;
//                }
//
//                //disinfect run time
//                int intDisinfectRunTime = iaPayload[4] * 3600 + iaPayload[5] * 60 + iaPayload[6];
//
//                //disinfect total time
//                if (iaPayload[7] == 0xFF && iaPayload[8] == 0xFF) {
//                    iaPayload[7] = 0;
//                    iaPayload[8] = 0;
//                }
//                int intDsinfectTotalTime = (iaPayload[7] | (iaPayload[8] << 8)) * 60;
//
//                mSwm.refreshAttrAuxiliaryStatus(mIntDeviceId, intTransitionCurrentWorkMode, intDsinfectTotalTime,
//                        intDisinfectRunTime, 0, intPlErrorNum);
//
//                //send heart beat
//                ctrlRunAuxiliaryWorkMode(0x01, null);
//                //delivery data
//                EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, intFunction, iaPayload));
//
//            }
//            break;
//            case 0x02: {
//                //verison
//            }
//            break;
//            case 0x03: {
//                //work finish status
//                //接收完成状态
//                int intModeFinishStatus = iaPayload[0];
//                AttrSwConnectDevice attrDevice = ThreadAgent.getmTa().getmSwm().getMapAttrDeviceFromId(mIntDeviceId);
//                if (attrDevice != null) {
//                    switch (intModeFinishStatus) {
//                        case 0: {
//                            attrDevice.setIntDeviceFinishWorkStatus(1);
//                        }
//                        break;
//                        default: {
//                            attrDevice.setIntDeviceFinishWorkStatus(2);
//                        }
//                    }
//                }
//
//                EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, 0x03, new int[]{intModeFinishStatus}));
//            }
//            break;
//            case 0x04: {
//                //uv灯使用时间
//                int intUvUserHour = iaPayload[0] | (iaPayload[1] << 8);
//                DbDevice dbDevice = mDm.queryUvLifeHour(mIntDeviceId);
//                if (dbDevice == null) {
//                    return;
//                }
//                int intDbUvLifeHour = dbDevice.getIntUvLifeHour();
//                if (intDbUvLifeHour != intUvUserHour) {
//                    mDm.setDeviceUvTime(mIntDeviceId, 1, new int[]{intUvUserHour});
//                    if (intUvUserHour == 0) {
//                        mDm.resetUvResetDate(mIntDeviceId, 0);
//                    }
//                }
//            }
//            break;
//            default: {
//                break;
//            }
//        }
//
//    }
//
//    //deal receive land manual device
//    private void receiveDealLandManualData(int intFunction, int[] iaPayload) {
//        try {
//            switch (intFunction) {
//                case 1: {
//                    int intRunDisinfectionSec = iaPayload[11] * 3600 + iaPayload[12] * 60 + iaPayload[13];
//                    int intTotalDisinfectionSec = (iaPayload[14] | (iaPayload[15] << 8)) * 60;
//
//                    int intWarnMessage = 0;
//                    boolean blHumanInfraredResut = false;
//
//                    int intTransitionCurrentWorkMode = iaPayload[11] == 0x01 ? 0x01 : 0x00;
//                    if (intTransitionCurrentWorkMode == 0x01) {
//                        blHumanInfraredResut = iaPayload[7] == 1 | iaPayload[8] == 1 | iaPayload[9] == 1 | iaPayload[10] == 1;
//                    }
//
//                    if (blHumanInfraredResut) {
//                        intWarnMessage = 2;
//                    }
//
//                    //select light
//                    int intLightNum = iaPayload[16];
//                    if (intLightNum == 0x03) {
//                        intLightNum = 0;
//                    } else if (intLightNum == 0x02) {
//                        intLightNum = 1;
//                    } else if (intLightNum == 0x01) {
//                        intLightNum = 2;
//                    }
//
//                    //send heart beat
//                    ctrlRunLandManualWorkMode(0x01, null);
//
//                    mSwm.refreshAttrLandManualStatus(mIntDeviceId, intTransitionCurrentWorkMode, intTotalDisinfectionSec,
//                            intRunDisinfectionSec, intWarnMessage, 0, intLightNum);
//
//                    //delivery data
//                    EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, intFunction, iaPayload));
//
//                }
//                break;
//                case 3: {
//                    int intUvLeftLifeHour = iaPayload[0] | (iaPayload[1] << 8);
//                    int intUvRightLifeHour = iaPayload[2] | (iaPayload[3] << 8);
//                    DbDevice dbDevice = mDm.queryUvLifeHour(mIntDeviceId);
//                    if (dbDevice == null) {
//                        return;
//                    }
//                    int intDbUvLeftLifeHour = dbDevice.getIntUvLifeHour();
//                    int intDbUvRightLife = dbDevice.getIntUvLifeHourSec();
//                    if (intUvLeftLifeHour != intDbUvLeftLifeHour) {
//                        mDm.setDeviceUvTime(mIntDeviceId, 1, new int[]{intUvLeftLifeHour});
//                    }
//                    if (intUvRightLifeHour != intDbUvRightLife) {
//                        mDm.setDeviceUvTime(mIntDeviceId, 2, new int[]{intUvRightLifeHour});
//                    }
//                }
//                break;
//                default: {
//                    //null
//                }
//                break;
//            }
//        } catch (Exception e) {
//            AssitStatic.getmAssitStatic().collectError(e);
//        }
//    }
//
//
//    //deal receive high power device
//    private void receiveDealHighPowerData(int intFunction, int[] iaPayload) {
//        try {
//            if (intFunction == 0x01) {
//
//                int intDsinfectTotalTime = (iaPayload[8] | (iaPayload[9] << 8)) * 60;
//                int intDisinfectRunTime = iaPayload[5] * 3600 + iaPayload[6] * 60 + iaPayload[7];
//
//                int intCurrentWorkMode = iaPayload[10];
//                int intTransitionCurrentWorkMode = 0;
//                if (intCurrentWorkMode == 1) {
//                    intTransitionCurrentWorkMode = 1;
//                }
//
//                int intManageticField = iaPayload[14] | (iaPayload[15] << 8);
//                int intWarnMessage = 0;
//                if (intManageticField > 1000) {
//                    intWarnMessage = SocketWifiManager.HP_WM_MAGNETIC_OVERSTRENGTH;
//                }
//
//                ctrlRunHighPowerWorkMode(0x01, null);
//                EventBus.getDefault().post(new EventReceiveBeatTransmit(mIntDeviceId, intFunction, iaPayload));
//
//                // refresh attr
//                mSwm.refreshAttrHighPowerStatus(mIntDeviceId, intTransitionCurrentWorkMode, intDsinfectTotalTime, intDisinfectRunTime, intWarnMessage, 0);
//
//            } else if (intFunction == 0x02) {
//                int intUvUserHour = iaPayload[0] | (iaPayload[1] << 8);
//                DbDevice dbDevice = mDm.queryUvLifeHour(mIntDeviceId);
//                if (dbDevice == null) {
//                    return;
//                }
//                int intDbUvLifeHour = dbDevice.getIntUvLifeHour();
//                if (intDbUvLifeHour != intUvUserHour) {
//                    mDm.setDeviceUvTime(mIntDeviceId, 1, new int[]{intUvUserHour});
//                }
//            }
//        } catch (Exception e) {
//            AssitStatic.getmAssitStatic().collectError(e);
//        }
//    }
//
//    /* send */
//
//    //检验数组数据,返回数组
//    private void checkSumArray(@NonNull int[] checkDataArray) {
//
//        checkDataArray[2] = checkDataArray.length - 4;
//        int tCheckData = 0;//初始化检验和
//        checkDataArray[3] = mIntDeviceType;
//        checkDataArray[4] = mIntSocketAddress;
//        for (int i = 0; i < checkDataArray.length - 1; i++) {
//            tCheckData = (tCheckData ^ checkDataArray[i]) & 0x00ff;
//        }
//        checkDataArray[checkDataArray.length - 1] = tCheckData;
//    }
//
//    /**
//     * 当前线程，发送socket数据，接收int[]类型
//     *
//     * @param sendArray：int[]类型数据
//     */
//    private void sendSocketIntData(int[] sendArray) {
//        try {
//            if (mSocket != null) {
//                int[] checkArray = new int[sendArray.length];
//                System.arraycopy(sendArray, 0, checkArray, 0, checkArray.length);
//                //给设备字段赋值
//                checkSumArray(checkArray);
//                byte[] dealArray = new byte[checkArray.length];
//
//                for (int i = 0; i < checkArray.length; i++) {
//                    dealArray[i] = (byte) checkArray[i];
//                }
//                mOutputStream.write(dealArray);
//                mOutputStream.flush();
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "sendSocketIntData: 报错");
//            AssitStatic.getmAssitStatic().collectError(e);
//            closeSocketRemove();
//        }
//    }
//
//    /*all device ctrl*/
//
//    //disinfection : Cat:ctrl all type
//    public void swCatOpenDisinfection(int intMinute) {
//        switch (mIntDeviceType) {
//            case SocketWifiManager.DEVICE_TYPE_HOST: {
//                ctrlRunRiseFallWorkMode(0x02, new int[]{0x01, 0x01, intMinute & 0xFF, intMinute >> 8});
//            }
//            break;
//            case AttrDeviceProperty.DEVICE_TYPE_AUXILIARY: {
//                ctrlRunAuxiliaryWorkMode(0x02, new int[]{0x01, 0x01, intMinute & 0xFF, intMinute >> 8});
//            }
//            break;
//            case SocketWifiManager.DEVICE_TYPE_LAND_MANUAL: {
//                ctrlRunLandManualWorkMode(0x06, new int[]{(intMinute & 0xFF), (intMinute >> 8)});
//            }
//            break;
//            case SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER: {
//                ctrlRunHighPowerWorkMode(0x04, new int[]{(intMinute & 0xFF), (intMinute >> 8)});
//                ctrlRunHighPowerWorkMode(0x09, null);
//            }
//            break;
//        }
//    }
//
//    public void swCatOpenDisinfection(int intMinute, int intLightResult) {
//        switch (mIntDeviceType) {
//            case SocketWifiManager.DEVICE_TYPE_HOST: {
//                ctrlRunRiseFallWorkMode(0x02, new int[]{0x01, 0x01, intMinute & 0xFF, intMinute >> 8});
//            }
//            break;
//            case SocketWifiManager.DEVICE_TYPE_LAND_MANUAL: {
//                ctrlRunLandManualWorkMode(0x06, new int[]{(intMinute & 0xFF), (intMinute >> 8), intLightResult});
//            }
//            break;
//            case SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER: {
//                ctrlRunHighPowerWorkMode(0x04, new int[]{(intMinute & 0xFF), (intMinute >> 8)});
//                ctrlRunHighPowerWorkMode(0x09, null);
//            }
//            break;
//
//        }
//
//    }
//
//    public void swCatCloseDisinfection() {
//        if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_HOST) {
//            ctrlRunRiseFallWorkMode(0x02, new int[]{0x01, 0x02, 0xFF, 0xFF});
//        } else if (mIntDeviceType == AttrDeviceProperty.DEVICE_TYPE_AUXILIARY) {
//            ctrlRunAuxiliaryWorkMode(0x02, new int[]{0x01, 0x02, 0xFF, 0xFF});
//        } else if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER) {
//            ctrlRunHighPowerWorkMode(0x0a, null);
//        }
//    }
//
//    /**
//     * @param intFunction:0:query 1:reset uv life
//     */
//    public void swCatQueryUvLifeTime(int intFunction) {
//        switch (intFunction) {
//            case 0x00: {
//                if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_HOST) {
//                    ctrlRunRiseFallWorkMode(0x05, new int[]{0x00});
//                } else if (mIntDeviceType == AttrDeviceProperty.DEVICE_TYPE_AUXILIARY) {
//                    ctrlRunAuxiliaryWorkMode(0x05, new int[]{0x00});
//                } else if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER) {
//                    ctrlRunHighPowerWorkMode(0x02, null);
//                }
//            }
//            break;
//            case 0x01: {
//                if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_HOST) {
//                    ctrlRunRiseFallWorkMode(0x05, new int[]{0x01});
//                } else if (mIntDeviceType == AttrDeviceProperty.DEVICE_TYPE_AUXILIARY) {
//                    ctrlRunAuxiliaryWorkMode(0x05, new int[]{0x01});
//                } else if (mIntDeviceType == SocketWifiManager.DEVICE_TYPE_LAND_HIGH_POWER) {
//                    ctrlRunHighPowerWorkMode(0x03, null);
//                }
//            }
//            break;
//
//
//        }
//
//    }
//
//
//    /**
//     * repalce
//     *
//     * @param blFlag
//     */
//    public void swCatReplaceUvLight(boolean blFlag) {
//        switch (mIntDeviceType) {
//            case AttrDeviceProperty.DEVICE_TYPE_HOST: {
//                if (blFlag) {
//                    ctrlRunRiseFallWorkMode(0x02, new int[]{0x01, 0x06, 0xFF, 0XFF});
//                } else {
//                    swCatCloseDisinfection();
//                }
//            }
//        }
//
//    }
//
//
//    /*hsot rise fall*/
//
//    public void ctrlRunRiseFallWorkMode(int intWorkMode, int[] iaData) {
//        RunHostRiseFallWorkMode runHostRiseFallWorkMode = new RunHostRiseFallWorkMode();
//        runHostRiseFallWorkMode.setIntWorkMode(intWorkMode);
//        runHostRiseFallWorkMode.setIaData(iaData);
//        pushShortTask(runHostRiseFallWorkMode);
//    }
//
//    private class RunHostRiseFallWorkMode implements Runnable {
//
//        private int intWorkMode;
//        private int[] iaData;
//
//        public void setIntWorkMode(int intWorkMode) {
//            this.intWorkMode = intWorkMode;
//        }
//
//        public void setIaData(int[] iaData) {
//            this.iaData = iaData;
//        }
//
//        @Override
//        public void run() {
//            try {
//                AttrSwConnectDevice attrDevice = mSwm.getMapAttrDeviceFromId(mIntDeviceId);
//                Log.d("TAG", "run attrDevice: " + attrDevice);
//                if (attrDevice == null) {
//                    return;
//                }
//
//                int[] iaSendLoad = null;
//                Log.d(TAG, "run: " + intWorkMode);
//                if (intWorkMode == 0x01) {
//
//                    Calendar nowCalendar = Calendar.getInstance();
//                    int intYear = nowCalendar.get(Calendar.YEAR);
//                    int intMonth = nowCalendar.get(Calendar.MONTH) + 1;
//                    int intDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
//                    int intHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
//                    int intMinute = nowCalendar.get(Calendar.MINUTE);
//                    int intSecond = nowCalendar.get(Calendar.SECOND);
//                    int intWeek = nowCalendar.get(Calendar.DAY_OF_WEEK);
//
////                    public final int[] mIaCmdHfrHeartBeart = {0xA0, 0xA0, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//
//                    //heart beat
//                    iaSendLoad = mIaCmdHfrHeartBeart;
//                    int intPosition = 6;
//                    iaSendLoad[intPosition] = attrDevice.isBlConnectStatus() ? 0 : 1;
//                    iaSendLoad[++intPosition] = 0xFF;
//                    iaSendLoad[++intPosition] = 0xFF;
//                    iaSendLoad[++intPosition] = 0xFF;
//                    iaSendLoad[++intPosition] = attrDevice.isBlDebugFlag() ? 1 : 0;
//
//                    iaSendLoad[++intPosition] = intYear & 0xFF;
//                    iaSendLoad[++intPosition] = (intYear >> 8);
//                    iaSendLoad[++intPosition] = intMonth;
//                    iaSendLoad[++intPosition] = intDay;
//                    iaSendLoad[++intPosition] = intHour;
//                    iaSendLoad[++intPosition] = intMinute;
//                    iaSendLoad[++intPosition] = intSecond;
//                    iaSendLoad[++intPosition] = intWeek;
//
//                } else if (intWorkMode == 0x02) {
//                    //work mode
//                    attrDevice.setIntDeviceFinishWorkStatus(0);
//
//                    iaSendLoad = mIaCmdHfrWorkMode;
//                    iaSendLoad[6] = iaData[0];//type 1:总控;2:主机;3~6:辅灯
//                    iaSendLoad[7] = iaData[1];//
//                    iaSendLoad[9] = iaData[3];
//                    iaSendLoad[8] = iaData[2];
//
//                } else if (intWorkMode == 0x03) {
//                    //reset wifi
//                    iaSendLoad = mIaCmdHfrResetWifi;
//                } else if (intWorkMode == 0x04) {
//                    //get version
//                    iaSendLoad = mIaCmdHfrBoardVersion;
//                } else if (intWorkMode == 0x05) {
//                    //uv life time
//                    iaSendLoad = mIaCmdHfrUvLifeTime;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x06) {
//                    //debug switch(nonuse)
//                    iaSendLoad = mIaCmdHfrDebugSwitch;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x07) {
//                    //设置距离传感器
//                    iaSendLoad = new int[mIaCmdHfrDistanceSensor.length];
//                    System.arraycopy(mIaCmdHfrDistanceSensor, 0, iaSendLoad, 0, mIaCmdHfrDistanceSensor.length);
//                    iaSendLoad[6] = iaData[0];
//                    iaSendLoad[7] = iaData[1];
//                    iaSendLoad[8] = iaData[2];
//                } else if (intWorkMode == 0x08) {
//                    //设置
//                    iaSendLoad = new int[mIaCmdHfrWheelDetectionSwitch.length];
//                    System.arraycopy(mIaCmdHfrWheelDetectionSwitch, 0, iaSendLoad, 0, mIaCmdHfrWheelDetectionSwitch.length);
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x09) {
//                    //tiem plan add
//                    iaSendLoad = new int[mIaCmdHfrAddTimePlan.length];
//                    System.arraycopy(mIaCmdHfrAddTimePlan, 0, iaSendLoad, 0, mIaCmdHfrAddTimePlan.length);
//                    iaSendLoad[6] = iaData[0];
//                    iaSendLoad[7] = iaData[1];
//                    iaSendLoad[8] = iaData[2];
//                    iaSendLoad[9] = iaData[3];
//                    iaSendLoad[10] = iaData[4];
//                    iaSendLoad[11] = iaData[5];
//                    iaSendLoad[12] = iaData[6];
//                    iaSendLoad[13] = iaData[7];
//                    iaSendLoad[14] = iaData[8];
//                } else if (intWorkMode == 0x0A) {
//                    //tiem plan check and delete
//                    iaSendLoad = new int[mIaCmdHfrSetTimePlan.length];
//                    System.arraycopy(mIaCmdHfrSetTimePlan, 0, iaSendLoad, 0, mIaCmdHfrSetTimePlan.length);
//                    iaSendLoad[6] = iaData[0];
//                    iaSendLoad[7] = iaData[1];
//                } else if (intWorkMode == 0x0B) {
//                    //tiem plan check and delete
//                    iaSendLoad = new int[mIaCmdHfrDisinfectionRecord.length];
//                    System.arraycopy(mIaCmdHfrDisinfectionRecord, 0, iaSendLoad, 0, mIaCmdHfrDisinfectionRecord.length);
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x0C) {
//                    //tiem plan check and delete
//                    iaSendLoad = new int[mIaCmdHfrBallastDetection.length];
//                    System.arraycopy(mIaCmdHfrBallastDetection, 0, iaSendLoad, 0, mIaCmdHfrBallastDetection.length);
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x0D) {
//                    //tiem plan check and delete
//                    iaSendLoad = new int[mIaCmdHfrBuzzer.length];
//                    System.arraycopy(mIaCmdHfrBuzzer, 0, iaSendLoad, 0, mIaCmdHfrBuzzer.length);
//                    iaSendLoad[6] = iaData[0];
//                    iaSendLoad[7] = iaData[1];
//                } else if (intWorkMode == 0x0E) {
//                    //tiem plan check and delete
//                    iaSendLoad = new int[mIaCmdHfrScanAuxiliary.length];
//                    System.arraycopy(mIaCmdHfrScanAuxiliary, 0, iaSendLoad, 0, mIaCmdHfrScanAuxiliary.length);
//                }
//                if (iaSendLoad != null) {
//                    sendSocketIntData(iaSendLoad);
////                    for (int i = 0; i < iaSendLoad.length; i++) {
////                        Log.d(TAG, "run:11111111111111   "+iaSendLoad[i]);
////                    }
//                }
//                Log.d("wisray", Thread.currentThread().getName());
//            } catch (Exception e) {
//                AssitStatic.getmAssitStatic().collectError(e);
//            }
//        }
//    }
//
//
//    /*Auxiliary*/
//    public void ctrlRunAuxiliaryWorkMode(int intWorkMode, int[] iaData) {
//
//        RunAuxiliarayWorkMode runHAuxiliarayWorkMode = new RunAuxiliarayWorkMode();
//        runHAuxiliarayWorkMode.setIntWorkMode(intWorkMode);
//        runHAuxiliarayWorkMode.setIaData(iaData);
//        pushShortTask(runHAuxiliarayWorkMode);
//
//    }
//
//    private class RunAuxiliarayWorkMode implements Runnable {
//
//        private int intWorkMode;
//        private int[] iaData;
//
//        public void setIntWorkMode(int intWorkMode) {
//            this.intWorkMode = intWorkMode;
//        }
//
//        public void setIaData(int[] iaData) {
//            this.iaData = iaData;
//        }
//
//        @Override
//        public void run() {
//
//            AttrSwConnectDevice attrDevice = mSwm.getMapAttrDeviceFromId(mIntDeviceId);
//            if (attrDevice == null) {
//                return;
//            }
//
//            int[] iaSendLoad = null;
//            switch (intWorkMode) {
//                case 0x01: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryHeartBeat.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryHeartBeat, 0, iaSendLoad, 0, intArrayLength);
//                    iaSendLoad[6] = attrDevice.isBlConnectStatus() ? 0 : 1;
//                    iaSendLoad[7] = 0xFF;
//                    iaSendLoad[8] = 0xFF;
//                    iaSendLoad[9] = 0xFF;
//                    iaSendLoad[10] = attrDevice.isBlDebugFlag() ? 1 : 0;
//                }
//                break;
//                case 0x02: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryWorkMode.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryWorkMode, 0, iaSendLoad, 0, intArrayLength);
//
//                    attrDevice.setIntDeviceFinishWorkStatus(0);
//
//                    iaSendLoad = mIaCmdHfrWorkMode;
//                    iaSendLoad[6] = iaData[0];//type 1:总控;2:主机;3~6:辅灯
//                    iaSendLoad[7] = iaData[1];//
//                    iaSendLoad[8] = iaData[2];
//                    iaSendLoad[9] = iaData[3];
//                }
//                break;
//                case 0x03: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryWifiReset.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryWifiReset, 0, iaSendLoad, 0, intArrayLength);
//                }
//                break;
//                case 0x04: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryVersion.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryVersion, 0, iaSendLoad, 0, intArrayLength);
//                }
//                break;
//                case 0x05: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryUvLifeTime.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryUvLifeTime, 0, iaSendLoad, 0, intArrayLength);
//                    iaSendLoad[6] = iaData[0];
//                }
//                break;
//                case 0x06: {
//                    int intArrayLength = SocketWifiManager.mIaCmdThrAuxiliaryDebug.length;
//                    iaSendLoad = new int[intArrayLength];
//                    System.arraycopy(SocketWifiManager.mIaCmdThrAuxiliaryDebug, 0, iaSendLoad, 0, intArrayLength);
//                    iaSendLoad[6] = iaData[0];
//                }
//                break;
//            }
//
//            if (iaSendLoad != null) {
//                sendSocketIntData(iaSendLoad);
//            }
//            Log.d("wisray", Thread.currentThread().getName());
//        }
//    }
//
//
//    /*LandManual*/
//    public void ctrlRunLandManualWorkMode(int intWorkMode, int[] iaData) {
//        RunLandManualWorkMode runLandManualWorkMode = new RunLandManualWorkMode();
//        runLandManualWorkMode.setIntWorkMode(intWorkMode);
//        runLandManualWorkMode.setIaData(iaData);
//        pushShortTask(runLandManualWorkMode);
//    }
//
//    private class RunLandManualWorkMode implements Runnable {
//
//        private int intWorkMode;
//        private int[] iaData;
//
//        public void setIaData(int[] iaData) {
//            this.iaData = iaData;
//        }
//
//        public void setIntWorkMode(int intWorkMode) {
//            this.intWorkMode = intWorkMode;
//        }
//
//        @Override
//        public void run() {
//            try {
//                AttrSwConnectDevice attrDevice = mSwm.getMapAttrDeviceFromId(mIntDeviceId);
//                if (attrDevice == null) {
//                    return;
//                }
//                int[] iaSendLoad = null;
//                switch (intWorkMode) {
//                    case 0x01: {
//                        iaSendLoad = mIaCmdLmHeartBeat;
//                        iaSendLoad[6] = attrDevice.isBlConnectStatus() ? 0 : 1;
//                        iaSendLoad[7] = 0xFF;
//                        iaSendLoad[8] = 0xFF;
//                        iaSendLoad[9] = 0xFF;
//                    }
//                    break;
//                    case 0x02: {
//                        iaSendLoad = mIaCmdLmQueryVersion;
//                    }
//                    break;
//                    case 0x03: {
//                        iaSendLoad = mIaCmdLmResetWifi;
//                    }
//                    break;
//                    case 0x04: {
//                        iaSendLoad = mIaCmdLmQueryUvLife;
//                    }
//                    break;
//                    case 0x05: {
//                        iaSendLoad = mIaCmdLmResetUvLife;
//                        iaSendLoad[6] = iaData[0];
//                    }
//                    break;
//                    case 0x06: {
//                        iaSendLoad = mIaCmdLmWorkMode;
//                        iaSendLoad[6] = iaData[0];
//                        iaSendLoad[7] = iaData[1];
//                        iaSendLoad[8] = iaData[2];
//                    }
//                    break;
//                    case 0x07: {
//                        iaSendLoad = mIaCmdLmUvCtrl;
//                        iaSendLoad[6] = iaData[0];
//                        iaSendLoad[7] = iaData[1];
//                    }
//                    break;
//                    case 0x08: {
//                        iaSendLoad = mIaCmdLmAtomizationCtrl;
//                        iaSendLoad[6] = iaData[0];
//                    }
//                    break;
//                    case 0x09: {
//                        iaSendLoad = mIaCmdLmBuzzerCtrl;
//                        iaSendLoad[6] = iaData[0];
//                    }
//                    break;
//                    case 0x0a: {
//                        iaSendLoad = mIaCmdLmInfraredCtrl;
//                        iaSendLoad[6] = iaData[0];
//                    }
//                    break;
//                    case 0x0b: {
//                        iaSendLoad = mIaCmdLmStartDisinfectionCtrl;
//                    }
//                    break;
//                    case 0x0c: {
//                        iaSendLoad = mIaCmdLmEndDisinfectionCtrl;
//                    }
//                    break;
//                    default: {
//                        break;
//                    }
//                }
//
//                if (iaSendLoad != null) {
//                    sendSocketIntData(iaSendLoad);
//                }
//
//            } catch (Exception e) {
//                AssitStatic.getmAssitStatic().collectError(e);
//            }
//
//        }
//    }
//
//
//    /*High Power*/
//    public void ctrlRunHighPowerWorkMode(int intWorkMode, int[] iaData) {
//        RunHighPowerWorkMode runHighPowerWorkMode = new RunHighPowerWorkMode();
//        runHighPowerWorkMode.setIntWorkMode(intWorkMode);
//        runHighPowerWorkMode.setIaData(iaData);
//        pushShortTask(runHighPowerWorkMode);
//    }
//
//
//    private class RunHighPowerWorkMode implements Runnable {
//
//        private int intWorkMode;
//        private int[] iaData;
//
//        public void setIntWorkMode(int intWorkMode) {
//            this.intWorkMode = intWorkMode;
//        }
//
//        public void setIaData(int[] iaData) {
//            this.iaData = iaData;
//        }
//
//        @Override
//        public void run() {
//            try {
//                AttrSwConnectDevice attrDevice = mSwm.getMapAttrDeviceFromId(mIntDeviceId);
//                if (attrDevice == null) {
//                    return;
//                }
//
//                int[] iaSendLoad = null;
//                if (intWorkMode == 0x01) {
//                    iaSendLoad = mIaCmdHpHeartBeart;
//                    iaSendLoad[6] = attrDevice.isBlConnectStatus() ? 0 : 1;
//                    iaSendLoad[7] = 0xFF;
//                    iaSendLoad[8] = 0xFF;
//                    iaSendLoad[9] = 0xFF;
//                } else if (intWorkMode == 0x02) {
//                    iaSendLoad = mIaCmdHpUvLifeTimeQuery;
//                } else if (intWorkMode == 0x03) {
//                    iaSendLoad = mIaCmdHpUvLifeTimeReset;
//                } else if (intWorkMode == 0x04) {
//                    iaSendLoad = mIaCmdHpDisinfectionTime;
//                    iaSendLoad[6] = iaData[0];
//                    iaSendLoad[7] = iaData[1];
//                } else if (intWorkMode == 0x05) {
//                    iaSendLoad = mIaCmdHpUvLight;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x06) {
//                    iaSendLoad = mIaCmdHpAutomization;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x07) {
//                    iaSendLoad = mIaCmdHpBuzzer;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x08) {
//                    iaSendLoad = mIaCmdHpBodyInfrared;
//                    iaSendLoad[6] = iaData[0];
//                } else if (intWorkMode == 0x09) {
//                    iaSendLoad = mIaCmdHpOpenDisinfection;
//                } else if (intWorkMode == 0x0a) {
//                    iaSendLoad = mIaCmdHpCloseDisinfection;
//                }
//
//                if (iaSendLoad != null) {
//                    sendSocketIntData(iaSendLoad);
//                }
//            } catch (Exception e) {
//                AssitStatic.getmAssitStatic().collectError(e);
//            }
//
//        }
//    }
//
//    /**
//     * 去重
//     */
//
//    public static List<DbTimePlan> delRepeat(List<DbTimePlan> dbTimePlanList) {
////        final boolean sta = null != dbTimePlanList && dbTimePlanList.size() > 0;
////         List timePlanList = new ArrayList();
////         if (sta){
////             HashSet set = new HashSet();
////             set.addAll(dbTimePlanList);
////             timePlanList.addAll(set);
////         }
////         return timePlanList;
//
//
//        List<DbTimePlan> newTimePlan = new ArrayList<>();
//        int[] id = new int[9];
//
////        int a = 0;
////        for (int i = 1; i <= dbTimePlanList.size(); i++) {
////            for (DbTimePlan dbTimePlan : dbTimePlanList) {
////                if (i == dbTimePlan.getIntTimePlanId()) {
////                    id[i] = (int) dbTimePlan.getId();
////                    if (id[i] == dbTimePlan.getId()){
////                        Log.d("TAG", "delRepeat id: "+id[i]);
////                    }
////                    Log.d("TAG", "delRepeat2: "+i);
////                    Log.d("TAG", "delRepeat2: "+dbTimePlan.getIntTimePlanId());
////                    Log.d("TAG", "delRepeat2: "+id[i]);
////
////
//////                    if(id[i] == dbTimePlan.getIntTimePlanId()){
//////                        break;
//////                    }else{
//////                        newTimePlan.add(dbTimePlan);
//////                        a++;
//////                        Log.d("TAG", "delRepeat3: "+dbTimePlan);
//////                        if(a>=i)
//////                            break;
//////                    }
////                }
////            }
////        }
//        return dbTimePlanList;
//    }
//}
