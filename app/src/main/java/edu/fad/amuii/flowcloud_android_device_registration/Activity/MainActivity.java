package edu.fad.amuii.flowcloud_android_device_registration.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imgtec.flow.Flow;
import com.imgtec.flow.client.core.Client;
import com.imgtec.flow.client.core.Core;
import com.imgtec.flow.client.core.Setting;
import com.imgtec.flow.client.core.Settings;
import com.imgtec.flow.client.flowmessaging.FlowMessagingAddress;
import com.imgtec.flow.client.users.Device;

import edu.fad.amuii.flowcloud_android_device_registration.BuildConfig;
import edu.fad.amuii.flowcloud_android_device_registration.R;

/*pascual*/
/*flow */


public class MainActivity extends ActionBarActivity {

    /*pascual */
    Flow flowInstance;
    Context ct;

    static {
        System.loadLibrary("flowcore");
        System.loadLibrary("flowsip");
        System.loadLibrary("flow");

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.   onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

         ct=getApplicationContext();

        // prueba cambios desde el PC
        // prueba 2 desde el PC
        //prueba 3 desde el PC

        // Lanzar el thread que registrará el dispositivo
        new Thread(new Runnable() {
            public void run() {

                if (BuildConfig.DEBUG) {
                    Log.d("per.com", "Pascual está aquí");
                }

                String server 					= Constants.FLOW_SERVER_URL;
                String oAuth 					= Constants.FLOW_SERVER_KEY;
                String secret 					= Constants.FLOW_SERVER_SECRET;

                String hardwareType 			= "Android";
                String macAddress 				= "446D6CDAE96C";
                String serialNumber 			= "40";
                String deviceId 				="";
                String softwareVersion 			= "40";
                String deviceName 				= "XXPascualetAndroid";
                String DeviceRegistrationKey 	= "NXAYIMLCGH";


                boolean result = false;

                result = initFlowCore();
                if (result) {
                    result = setServerDetails(server, oAuth, secret);
                }
                if (result) {
                    result = registerDevice(hardwareType, macAddress, serialNumber,deviceId , softwareVersion, deviceName, DeviceRegistrationKey);
                }

                if (result) {
                    System.out.println("Device registration succeeded");
                } else {
                    System.out.println("Device registration failed");
                }
                if (result) {
                    result = getDeviceSettings();
                }

                if (result) {
                    System.out.println("Settings retrieval succeeded");
                } else {
                    System.out.println("Settings retrieval failed");
                }
                if (result) {
                    result = getDeviceAoR();
                }

                if (result) {
                    System.out.println("AoR retrieval succeeded");
                } else {
                    System.out.println("AoR retrieval failed");
                }



            System.exit(0);
                //boolean result = flowInit();

                /*flowInstance= Flow.getInstance();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        flowInstance.setAppContext(ct);

                    }
                });


                if (flowInit())//InitialiseLibFlowCore())
                {
                            if (RegisterDevice())
                            {

                                Log.d("per.com", "Pascual está aquí LoggedIn success:Enjoy Flow features\n\r");
                            }
                            else Log.d("per.com", "ha fallado el registro\n\r");


                    //de-initalise flow core
                    ShutdownLibFlowCore();
                }

                */

            }
        }
        ).start();

    }


 boolean getDeviceAoR() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device != null && device.hasFlowMessagingAddress()) {
                FlowMessagingAddress addr = device.getFlowMessagingAddress();
                if (addr != null) {
                    String aor = addr.getAddress();
                    System.out.println("Device AoR: "+ aor);
                } else {
                    result = false;
                }
            } else {
                System.out.println("Device has no AoR");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }

    /*
    public String getInitXml(String oRoot, String oKey, String oSecret) {
        return "<?xml version=\"1.0\"?>" +
                "<Settings>" +
                "<Setting>" +
                "<Name>restApiRoot</Name>" +
                "<Value>" + oRoot + "</Value>" +
                "</Setting>" +
                "<Setting>" +
                "<Name>licenseeKey</Name>" +
                "<Value>" + oKey + "</Value>" +
                "</Setting>" +
                "<Setting>" +
                "<Name>licenseeSecret</Name>" +
                "<Value>" + oSecret + "</Value>" +
                "</Setting>" +
                "<Setting>" +
                "<Name>configDirectory</Name>" +
                "<Value>/mnt/img_messagingtest/outlinux/bin/config</Value>" +
                "</Setting>" +
                "</Settings>";
    }
*/
    boolean initFlowCore() {




        boolean result = false;

        try {
            result = Core.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            System.out.println("FlowCore Init failed");
        }
        return result;
    }


    boolean setServerDetails(String server, String oAuth, String secret) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.setServer(server, oAuth, secret);
            result = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            System.out.println("FlowCore setServer failed");
        }
        return result;
    }

    boolean registerDevice(String hardwareType, String macAddress, String serialNumber,
                           String deviceId, String softwareVersion,
                           String deviceName, String DeviceRegistrationKey) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.loginAsDevice(hardwareType, macAddress, serialNumber, deviceId, softwareVersion, deviceName, DeviceRegistrationKey);
            cli.getLoggedInDevice();
            result=true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }

        return result;
    }

    boolean getDeviceSettings() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device.hasSettings()) {
                Settings deviceSettings = device.getSettings();
                System.out.println("\n--Device settings list\n");
                for (int index = 0; index < deviceSettings.size(); index++) {
                    Setting setting = deviceSettings.get(index);
                    if (setting.hasKey() && setting.hasValue()) {
                        String key = setting.getKey();
                        String value = setting.getValue();
                        System.out.println(index+": Key = "+key+"  Value = "+value);
                    }
                }
            } else {
                System.out.println("Device has not settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }


/*
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////777

    public boolean flowInit() {

        return flowInstance.init(
                getInitXml(Constants.FLOW_SERVER_URL, Constants.FLOW_SERVER_KEY, Constants.FLOW_SERVER_SECRET));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /* Flow Cloud registering a device */
    public interface Constants {
        int MAX_SIZE=250;
        String LOG = "edu.fad.amuii.flowcloud_android_device_registration";
        String FLOW_SERVER_URL="http://ws-uat.flowworld.com";
        String FLOW_SERVER_KEY="Ph3bY5kkU4P6vmtT";
        String FLOW_SERVER_SECRET="Sd1SVBfYtGfQvUCR";

    }

  /*  String IDe;
    boolean RegisterDevice()
    {
        FlowHandler deviceHandler = new FlowHandler();
        Device device= DeviceHelper.newDevice(Core.getDefaultClient());// new Device("ANDROID");

        //device.addUsers();

        //(FlowClient_LoginAsDevice(deviceType, deviceMACAddress, deviceSerialNumber, NULL, deviceSoftwareVersion, deviceName, deviceRegistrationToken))
        device.setDeviceName("ZZ14Pascual_Device");
        device.setDeviceType("Android");
        device.setMACAddress("446D6CDAEC6C");
        device.setSerialNumber("11");
        device.setSoftwareVersion("25");
        device.setRegistrationKey("LWSBWCSHMC");


      boolean result=flowInstance.registerDevice(deviceHandler, device);


      */
/*
       IDe=device.getDeviceID();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView tt=(TextView)findViewById(R.id.texto);
                tt.setText(IDe);
            }
        });
*/
       // return true;
        /*return result;
    }
*/
 /*   boolean RegisterDevice()
    {
        FlowMemoryManager memoryManager = FlowMemoryManager_New();
        char deviceType[MAX_SIZE];
        char deviceMACAddress[MAX_SIZE];
        char deviceSerialNumber[MAX_SIZE];
        char deviceSoftwareVersion[MAX_SIZE];
        char deviceName[MAX_SIZE];
        char deviceRegistrationToken[MAX_SIZE];
        bool result = false;

        // user input for Device Type
        printf("Enter Device Type:");
        scanf("%s", deviceType);
        // user input for MAC Address
        printf("Enter Device MAC Address:");
        scanf("%s", deviceMACAddress);
        // user input for Serial Number
        printf("Enter Device Serial Number:");
        scanf("%s", deviceSerialNumber);
        // user input for software version
        printf("Enter Device Software Version:");
        scanf("%s", deviceSoftwareVersion);
        // user input for device name
        printf("Enter Device Name:");
        scanf("%s", deviceName);
        // user input for Registration token
        printf("Enter Device Registration Token:");
        scanf("%s", deviceRegistrationToken);



        if (memoryManager)
        {
            if (FlowClient_LoginAsDevice(deviceType, deviceMACAddress, deviceSerialNumber, NULL, deviceSoftwareVersion, deviceName, deviceRegistrationToken))
            {
                result = true;
                printf("Logged in as device.\n\r");
            }
            else
            {
                printf("Device Login Failed.\n\r");
                printf("ERROR: code %d\n\r", Flow_GetLastError());
            }
            //Clearing Memory
            FlowMemoryManager_Free(&memoryManager);
        }
        else
        {
            printf("Flow Could not create a FlowMemoryManager for managing memory\n\r");
        }

        return result;
    }
*/
    // Step 6: Shutdown the LibFlowCore library
    void ShutdownLibFlowCore()
    {
    //    FlowCore_Shutdown();
    }






}
