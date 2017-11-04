package com.example.ephraimkunz.multigametimer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by ephraimkunz on 7/1/17.
 */

public class GamePeripheral {
    private static GamePeripheral instance;
    private String gameId;
    private BluetoothGattServer mGattServer;
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback advertiseCallback;
    private GameSetupPeripheralDelegate gameSetupDelegate;
    private GamePlayPeripheralDelegate gamePlayDelegate;

    private GamePeripheral() {

    }

    public static GamePeripheral sharedInstance() {
        if (instance == null) {
            instance = new GamePeripheral();
        }
        return instance;
    }

    public void advertiseForGameId(String gameId, Context context) {
        this.gameId = gameId;
        UUID gameUuid = Constants.uuidFromGameId(gameId);
        createGattServer(gameUuid, context);
        createAdvertisement(gameUuid);
    }

    public void setGameSetupDelegate(GameSetupPeripheralDelegate delegate) {
        gameSetupDelegate = delegate;
    }

    public void setGamePlayDelegate(GamePlayPeripheralDelegate delegate) {
        gamePlayDelegate = delegate;
    }

    private void createAdvertisement(UUID gameUuid) {
        // Create advertisement data
        boolean mae = BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported();
        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                //.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
               // .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
               // .setConnectable(true)
                .build();
        AdvertiseData data = new AdvertiseData.Builder()
                .addServiceUuid(new ParcelUuid(gameUuid))
                .build();
        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.i( "BLE", "Advertising onStartSuccess");
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
            }
        };

        advertiser.startAdvertising(settings, data, advertiseCallback);
    }

    private void createGattServer(UUID gameUuid, Context context) {
        // Create GATT service: https://stackoverflow.com/questions/37181843/android-using-bluetoothgattserver
        BluetoothGattServerCallback serverCallback = new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i("BLE", "State connected");
                } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                    Log.i("BLE", "State connecting");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.i("BLE", "State disconnected");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    Log.i("BLE", "State disconnecting");
                }
            }

            @Override
            public void onServiceAdded(int status, BluetoothGattService service) {
                super.onServiceAdded(status, service);
            }

            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                if (characteristic.getUuid().equals(Constants.PlayerNameCharacteristic)) {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, BluetoothAdapter.getDefaultAdapter().getName().getBytes(StandardCharsets.US_ASCII));
                }
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                if(characteristic.getUuid().toString().equals(Constants.StartPlayCharacteristic.toString())) {
                    String rawString = new String(value, StandardCharsets.US_ASCII);
                    String[] splitted = rawString.split(":");
                    if(gameSetupDelegate != null) {
                        gameSetupDelegate.gameDidStart(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
                    }
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
                }

                advertiser.stopAdvertising(advertiseCallback);
            }

            @Override
            public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
                super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            }

            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            }

            @Override
            public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
                super.onExecuteWrite(device, requestId, execute);
            }

            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {
                super.onNotificationSent(device, status);
            }

            @Override
            public void onMtuChanged(BluetoothDevice device, int mtu) {
                super.onMtuChanged(device, mtu);
            }
        };

        BluetoothGattService service = new BluetoothGattService(gameUuid, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic startPlayChar = new BluetoothGattCharacteristic(
                Constants.StartPlayCharacteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        BluetoothGattCharacteristic isPlayerTurnChar = new BluetoothGattCharacteristic(
                Constants.IsPlayerTurnCharacteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);
        BluetoothGattCharacteristic isPausedChar = new BluetoothGattCharacteristic(
                Constants.IsPausedCharacteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);
        BluetoothGattCharacteristic isTimeExpiredChar = new BluetoothGattCharacteristic(
                Constants.IsPlayerTimeExpiredCharacteristic,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);
        BluetoothGattCharacteristic playerName = new BluetoothGattCharacteristic(
                Constants.PlayerNameCharacteristic,
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);

        service.addCharacteristic(startPlayChar);
        service.addCharacteristic(isPlayerTurnChar);
        service.addCharacteristic(isPausedChar);
        service.addCharacteristic(isTimeExpiredChar);
        service.addCharacteristic(playerName);

        BluetoothManager manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        mGattServer = manager.openGattServer(context, serverCallback);
        mGattServer.addService(service);
    }
}