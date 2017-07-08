package com.example.ephraimkunz.multigametimer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by ephraimkunz on 7/1/17.
 */

public class GameCentral {
    private static GameCentral instance;
    private UUID gameUuid;
    private BluetoothLeScanner scanner;
    private BluetoothGattCallback bluetoothGattCallback;
    private List<BluetoothGatt> connected = new ArrayList<>();

    private GameCentral() {
    }

    public static GameCentral sharedInstance() {
        if (instance == null) {
            instance = new GameCentral();
        }
        return instance;
    }

    public void setupGame(UUID gameUuid, Context context) {
        this.gameUuid = gameUuid;
        scanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        setupScannerAndStartScan(scanner, gameUuid, context);
    }

    private void setupScannerAndStartScan(BluetoothLeScanner scanner, UUID gameUuid, final Context context) {
        ScanSettings settings = new ScanSettings.Builder()
                                    .build();
        List<ScanFilter> filters = new ArrayList<>(Arrays.asList(
                new ScanFilter.Builder()
                        .setServiceUuid(ParcelUuid.fromString(gameUuid.toString()))
                        .build()
        ));

        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if(newState == BluetoothProfile.STATE_CONNECTED ) {
                    Log.i("BLE", "connected to peripheral");
//                    for(int i = 0; i < connected.size(); ++i) {
//                        if(connected.get(i).getDevice().getAddress().equals(gatt.getDevice().getAddress())) {
//                            return;
//                        }
//                    }
//                    connected.add(gatt);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.i("BLE", "Disconnected from peripheral");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }
        };

        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                device.connectGatt(context, true, bluetoothGattCallback);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.i("BLE", "Batch scan results recieved");
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e("BLE", "Scan failed");
            }
        };
        scanner.startScan(filters, settings, callback);
    }
}
