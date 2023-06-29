import requests
import time
import serial
import RPi.GPIO as GPIO
import queue


#Address
NODE_SENSOR_1 = "s1"
NODE_SENSOR_2 = "s2"
NODE_MOTOR_1 = "m1"
NODE_MOTOR_2 = "m2"

#Setup Lora 
ser = serial.Serial(
    port = '/dev/ttyS0',
    baudrate = 9600,
    parity = serial.PARITY_NONE,
    stopbits = serial.STOPBITS_ONE,
    bytesize = serial.EIGHTBITS,
    timeout = 1
)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)


GPIO.setup(16, GPIO.OUT) 
GPIO.setup(18, GPIO.OUT)
GPIO.output(16, GPIO.LOW)
GPIO.output(18, GPIO.LOW)

#Core 1
#################################################################################################
def receive_data():
    print("Raspberry's receiving: ")

    try:
        s = ser.readline()
        data = s.decode()
        print(data)
            
    except KeyboardInterrupt:
        ser.close()
    return data
################################################################################################# 
def Backup_data(data):
    with open("data.txt", "a") as file:
        file.write(data + "\n")
        print("Da luu du lieu thanh cong")
#################################################################################################      
def analyze_receive_node_s1(data):
    moisture_s1 = int(data[2:5])/10
    humidity_s1 = int(data[5:8])/10
    if data[8] == '0':
        Node_PH_s1 = int(data[9:11])/10
    else:
        Node_PH_s1 = int(data[8:11])/10
    if data[11] == '1':
        Battery_s1 = 100
    else:
        Battery_s1 = data[12:14]
    return moisture_s1, humidity_s1, Node_PH_s1, Battery_s1
    
def analyze_receive_node_m1(data):
    Watering_engine_m1 = data[2]
    Mist_engine_m1 = data[3]
    Motor_3_m1 = data[4]
    return Watering_engine_m1, Mist_engine_m1, Motor_3_m1
#################################################################################################    



#Core 2
#########################################################################################################
def Check_receive(data):
    empty_list = [''] * 16
    if data == empty_list:
        data_check = 0
    else:
        data_check = 1
    return data_check
#########################################################################################################  
def Response_data(Address, data):
    print("Raspberry's sending: ")

    data_transmit = Address + data
    print(data_transmit)
    try:
        ser.write(data_transmit.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        print("Da phan hoi ve sensor")
            
    except KeyboardInterrupt:
        ser.close()
########################################################################################################## 
def transmit_server(node_id, node_air, node_soil, node_PH, bump_1, bump_2, bump_3, battery):
    api_url = 'https://digitaldectectives.azdigi.blog/main/update_data.php'

    data = {
        'user' : 'tien',
        'node_id' : node_id,
        'node_auto' : 'false',
        'node_air' : node_air,
        'node_soil' : node_soil,
        'node_PH' : node_PH,
        'bump_1' : bump_1,
        'bump_2' : bump_2,
        'bump_3' : bump_3,
        'battery' : battery
    }

    try:
        response = requests.post(api_url, data=data)

        if response.status_code == 200:
            print('Du lieu da duoc gui len co so du lieu')
        else:
            print('Co loi xay ra khi gui du lieu')
        print(response.request.body)
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
########################################################################################################## 




#Core 3
########################################################################################################## 
def receive_server(node_id):
    api_url = 'https://digitaldectectives.azdigi.blog/main/get_status.php'


    data = {
        'user' : 'tien',
        'node_id' : node_id
    }

    try:
        response = requests.get(api_url, params=data)

        if response.status_code == 200:
            print('Du lieu da duoc gui ve thanh cong')
        else:
            print('Co loi xay ra khi lay du lieu')
        
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
        
    receive = response.json()
    node_auto = receive['node_auto']
    Watering_engine_m1 = receive['bump_1']
    Mist_engine_m1 = receive['bump_2']
    Motor_3_m1 = receive['bump_3']
    return node_auto, Watering_engine_m1, Mist_engine_m1, Motor_3_m1
##########################################################################################################



#Core 4
##########################################################################################################
def Exchange_data_transmit_motor(Address, node_soil, node_air, node_auto, bump_1, bump_2, bump_3):
    data = [''] * 16
    data[:2] = Address
    data[2] = node_auto
    if node_auto == 1:                  #Auto
        data[3:6] = node_soil*10
        data[6:9] = node_air*10
    else:                               #Manual
        data[3] = bump_1
        data[4] = bump_2
        data[5] = bump_3
    return data
##########################################################################################################
def transmit_motor(data):
    print("Raspberry's sending: ")
    print(data)
    try:
        ser.write(data.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        print("Da gui du lieu ve dong co")
    except KeyboardInterrupt:
        ser.close()
##########################################################################################################





##########################################################################################################
#main:
receive_node = [''] * 16
receive_node_2 = [''] * 16

check_data_transmit = False

while True:
    
#Core 1:
    while not check_data_transmit:
        print("Du lieu dang duoc gui toi")
        receive_node = receive_data()
        time.sleep(1)
        receive_node_2 = receive_data()
        while(receive_node_2 == receive_node):
            receive_node_2 = receive_data()
        line = ser.readline()
        if line:
              check_data_transmit = True
    try:
        Backup_data(str(receive_node))
        print(receive_node)
        Backup_data(str(receive_node_2))
        print(receive_node_2)
        if receive_node[0] == 'm':
            temp = receive_node_2
            receive_node_2 = receive_node
            receive_node = temp
        node_soil, node_air, node_PH, battery = analyze_receive_node_s1(str(receive_node))
        bump_1, bump_2, bump_3 = analyze_receive_node_m1(str(receive_node_2))
        
    #Core 2:
        Check_data = Check_receive(receive_node)
        Response_data(NODE_SENSOR_1, str(Check_data))
        transmit_server('node_01', node_air, node_soil, node_PH, bump_1, bump_2, bump_3, battery)

    #Core 3:
        node_auto, bump_1, bump_2, bump_3 = receive_server('node_01')
            
    #Core 4:
        data_transmit_motor = Exchange_data_transmit_motor(NODE_MOTOR_1,node_soil, node_air, node_auto, bump_1, bump_2, bump_3)
        transmit_motor(str(data_transmit_motor))
        time.sleep(2)
    except:
        print("Loi ham")
        check_data_transmit = False
#End
