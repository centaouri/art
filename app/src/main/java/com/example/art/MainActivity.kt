package com.example.art

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_BT = 1
    private val REQUEST_LOCATION_PERMISSION = 2
    private val REQUEST_BLUETOOTH_ADMIN_PERMISSION = 3
    private var isButtonClicked = false // 버튼 클릭 상태를 저장하기 위한 변수
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectButton: Button = findViewById(R.id.connectButton)
        connectButton.setOnClickListener {
            if (!isButtonClicked) {
                // 버튼이 한 번 클릭되었을 때의 동작
                connectButton.text = "CHECK!"
                isButtonClicked = true
                checkBluetoothPermission()
            } else {
                // 버튼이 두 번째 클릭되었을 때의 동작 (다른 액티비티로 이동)
                val intent = Intent(this@MainActivity, MainActivity2::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkBluetoothPermission() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            // 기기가 블루투스를 지원하지 않는 경우 처리
            // Toast 메시지 출력
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            discoverDevices()
        }
    }

    private fun discoverDevices() {
        // BluetoothAdmin 권한 확인
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_ADMIN_PERMISSION
            )
            return
        }

        val deviceList: ListView = findViewById(R.id.deviceListView)
        val devices: MutableList<BluetoothDevice> = mutableListOf()
        val deviceNames: MutableList<String> = mutableListOf()
        val deviceAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
        deviceList.adapter = deviceAdapter

        val pairedDevices = bluetoothAdapter.bondedDevices

        pairedDevices.forEach { device ->
            devices.add(device)
            deviceNames.add("${device.name}\n${device.address}")
        }

        deviceList.setOnItemClickListener { _, _, position, _ ->
            // 선택한 기기에 대한 페어링 작업 수행
            val selectedDevice: BluetoothDevice = devices[position]
            establishConnection(selectedDevice)
        }

        // 리스트뷰를 보이도록 visiblity를 visible로 변경
        deviceList.visibility = View.VISIBLE
    }

    private fun establishConnection(device: BluetoothDevice) {
        // BLUETOOTH_ADMIN 권한 확인
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_ADMIN_PERMISSION
            )
            return
        }

        val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        socket?.use { bluetoothSocket ->
            try {
                // 소켓을 통한 연결 시도
                bluetoothSocket.connect()
                // 연결이 성공했을 때의 동작
                Toast.makeText(this, "연결 되었습니다.", Toast.LENGTH_SHORT).show()
                // 연결이 성공하면 원하는 작업 수행
                // 예: 데이터 송수신 또는 작업 수행
            } catch (e: IOException) {
                // 연결이 실패했을 때의 동작
                Toast.makeText(this, "연결 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_BLUETOOTH_ADMIN_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    discoverDevices()
                } else {
                    // BluetoothAdmin 권한이 거부된 경우 처리
                    // 예: Toast 메시지 출력 or 다른 액션 수행
                    Toast.makeText(this, "원활한 이용을 위해 필요합니다.", Toast.LENGTH_SHORT).show()
                    // 다시 권한 요청
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                        REQUEST_BLUETOOTH_ADMIN_PERMISSION
                    )
                }
            }
            // 다른 권한에 대한 처리 등
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}





