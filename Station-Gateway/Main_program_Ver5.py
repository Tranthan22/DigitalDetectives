import requests
import time
import serial
import RPi.GPIO as GPIO

#Address
NODE_SENSOR_1 = "000123"
NODE_MOTOR_1 = "000223"
NODE_SENSOR_2 = "000323"
NODE_MOTOR_2 = "000423"


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


#Init Garden
##########################################################################################################
class Garden:
    def __init__(self, node_id, node_address_sensor, address_analyze_data_sensor, node_soil, node_air, node_PH, battery, node_soil_old, node_air_old, node_PH_old, battery_old, 
                                node_address_motor, address_analyze_data_motor, node_auto, bump_1, bump_2, bump_3, data_transmit_motor, bump_1_old, bump_2_old, bump_3_old, data_transmit_motor_old):
        self.node_id = node_id
#Node Sensor        
        self.node_address_sensor = node_address_sensor
        self.address_analyze_data_sensor = address_analyze_data_sensor
        self.node_soil = node_soil
        self.node_air = node_air
        self.node_PH = node_PH
        self.battery = battery
        self.node_soil_old = node_soil_old
        self.node_air_old = node_air_old
        self.node_PH_old = node_PH_old
        self.battery_old = battery_old
        
#Node Motor       
        self.node_address_motor = node_address_motor
        self.address_analyze_data_motor = address_analyze_data_motor
        self.node_auto = node_auto
        self.bump_1 = bump_1
        self.bump_2 = bump_2
        self.bump_3 = bump_3
        self.data_transmit_motor = data_transmit_motor
        self.bump_1_old = bump_1_old
        self.bump_2_old = bump_2_old
        self.bump_3_old = bump_3_old
        self.data_transmit_motor_old = data_transmit_motor_old
##########################################################################################################




#Core 1
#################################################################################################
def ascii_to_decimal(data):
    decimal_data = ""
    for char in data:      
        decimal = ord(char)
        if decimal < 10:
            decimal_data += "0" + str(decimal)
        else:
            decimal_data += str(decimal)
    return decimal_data
#################################################################################################
def receive_data():
    print("Raspberry's receiving node: ")

    try:
        s = ser.readline()
        data = s.decode()
        receive_data = ascii_to_decimal(data)[:14]
        print(receive_data)
            
    except KeyboardInterrupt:
        ser.close()
    return receive_data
################################################################################################# 
def Backup_data(data):
    with open("data.txt", "a") as file:
        file.write(data + "\n")
        print("Da luu du lieu thanh cong")
#################################################################################################      
def analyze_receive_node_s(data):
    moisture_s1 = int(data[2:5])/10
    humidity_s1 = int(data[5:8])/10
    Node_PH_s1 = int(data[8:11])/10
    Battery_s1 = round(int(data[11:14])/10)
    return moisture_s1, humidity_s1, Node_PH_s1, Battery_s1
    
def analyze_receive_node_m(data):
    Watering_engine_m1 = data[2]
    Mist_engine_m1 = data[3]
    Motor_3_m1 = data[4]
    return Watering_engine_m1, Mist_engine_m1, Motor_3_m1
#################################################################################################    



#Core 2
#########################################################################################################
def Check_receive(data):
    empty_list = ''
    if data == empty_list:
        data_check = 00
    else:
        data_check = 11
    return data_check
#########################################################################################################
def Response_data(Address, data):
    print("Raspberry's sending sensor: ")
    data_transmit = Address + data
    data_transmit = data_transmit.replace(",","")
    data_transmit = data_transmit.replace("[","")
    data_transmit = data_transmit.replace("'","")
    data_transmit = data_transmit.replace("]","")
    data_transmit = data_transmit.replace(" ","")
    print(data_transmit)
    if len(data_transmit) % 2 != 0:
            print("Loi chuoi hex khong hop le")
            exit()
    asciiData = ""
        
    for i in range(0, len(data), 2):
            decPair = data[i:i+2]
            asciiData += chr(int(decPair))
        
    try:
        ser.write(asciiData.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        print("Da phan hoi ve sensor")
            
    except KeyboardInterrupt:
        ser.close()   
##########################################################################################################    
def Analyze_data_receive(receive_data, MyGarden):
    if receive_data[:2] == MyGarden.address_analyze_data_sensor:
        MyGarden.bump_1, MyGarden.bump_2, MyGarden.bump_3 = analyze_receive_node_m(str(receive_data))
    if receive_data[:2] == MyGarden.address_analyze_data_motor:
        MyGarden.node_soil, MyGarden.node_air, MyGarden.node_PH, MyGarden.battery = analyze_receive_node_s(str(receive_data))
        Check_data = Check_receive(receive_data)
        Response_data(MyGarden.node_address_sensor, str(Check_data))
    
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
            print('Du lieu ' + node_id + ' da duoc gui len co so du lieu')
        else:
            print('Co loi xay ra khi gui du lieu')
        print(response.request.body)
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
########################################################################################################## 
def Check_and_transmit_server(MyGarden):
    if ((MyGarden.node_air != MyGarden.node_air_old) or (MyGarden.node_soil != MyGarden.node_soil_old) or (MyGarden.node_PH != MyGarden.node_PH_old) or (MyGarden.bump_1 != MyGarden.bump_1_old) or (MyGarden.bump_2 != MyGarden.bump_2_old) or (MyGarden.bump_3 != MyGarden.bump_3_old) or (MyGarden.battery != MyGarden.battery_old)):
        transmit_server(MyGarden.node_id, MyGarden.node_air, MyGarden.node_soil, MyGarden.node_PH, MyGarden.bump_1, MyGarden.bump_2, MyGarden.bump_3, MyGarden.battery)
        MyGarden.bump_1_old = MyGarden.bump_1
        MyGarden.bump_2_old = MyGarden.bump_2
        MyGarden.bump_3_old = MyGarden.bump_3
        MyGarden.node_soil_old = MyGarden.node_soil
        MyGarden.node_air_old = MyGarden.node_air
        MyGarden.node_PH_old = MyGarden.node_PH
        MyGarden.battery_old = MyGarden.battery
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
            print('Du lieu ' + node_id + ' da duoc gui tu server ve station thanh cong')
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
def Receive_data_server(MyGarden):
    MyGarden.node_auto, MyGarden.bump_1, MyGarden.bump_2, MyGarden.bump_3 = receive_server(MyGarden.node_id) 
##########################################################################################################



#Core 4
##########################################################################################################
def Exchange_data_transmit_motor(Address, node_soil, node_air, node_auto, bump_1, bump_2, bump_3):
    data = [''] * 16
    data[:6] = Address
    data[6] = node_auto
    if node_auto == 1:                  #Auto
        data[7:10] = node_soil*10
        data[10:13] = node_air*10
    else:                               #Manual
        data[7] = bump_1
        data[8] = bump_2
        data[9] = bump_3
    return data
##########################################################################################################
def transmit_motor(data):
    print("Raspberry's sending motor: ")
    data = data.replace(",","")
    data = data.replace("[","")
    data = data.replace("'","")
    data = data.replace("]","")
    data = data.replace(" ","")
    print(data)
    if len(data) % 2 != 0:
        print("Loi chuoi hex khong hop le")
        exit()
    
    asciiData = ""
    for i in range(0, len(data), 2):
            decPair = data[i:i+2]
            asciiData += chr(int(decPair))
    try:
        ser.write(asciiData.encode())
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        print("Da gui du lieu ve dong co")
        
    except KeyboardInterrupt:
        ser.close()     
##########################################################################################################
def Check_and_transmit_motor(MyGarden):
    MyGarden.data_transmit_motor = Exchange_data_transmit_motor(MyGarden.node_address_motor, MyGarden.node_soil, MyGarden.node_air, MyGarden.node_auto, MyGarden.bump_1, MyGarden.bump_2, MyGarden.bump_3)
    if MyGarden.data_transmit_motor != MyGarden.data_transmit_motor_old:
        transmit_motor(str(MyGarden.data_transmit_motor))
        MyGarden.data_transmit_motor_old = MyGarden.data_transmit_motor
##########################################################################################################





##########################################################################################################
#main:
receive_node_check = ''
check_data_transmit = False

#Garden:
Garden_1 = Garden('node_01', NODE_SENSOR_1, '11', 60, 70, 75, 90, 65, 70, 80 ,90,
                             NODE_MOTOR_1, '12', 0, 0, 0, 0, "12345667890132", 0, 1, 0, "12345667890132")

Garden_2 = Garden('node_02', NODE_SENSOR_2, '21', 60.5, 70.5, 67.5, 90, 65, 70, 80 , 90,
                             NODE_MOTOR_2, '22', 0, 0, 0, 0,"12345667890132", 0, 1, 0, "12345667890132")
        
        
while True:

#Core 1:
    while not check_data_transmit:
        print("Dang cho du lieu tu node...")
        receive_node = receive_node_check
        receive_node = receive_data()
        time.sleep(1)
        if receive_node != receive_node_check:
            check_data_transmit = True
    try:
        Backup_data(str(receive_node))
        Analyze_data_receive(receive_node, Garden_1)
        Analyze_data_receive(receive_node, Garden_2)
            
#Core 2:
        Check_and_transmit_server(Garden_1)
        Check_and_transmit_server(Garden_2)
#Core 3:
        Receive_data_server(Garden_1)  
        Receive_data_server(Garden_2)
        
#Core 4:
        Check_and_transmit_motor(Garden_1)
        Check_and_transmit_motor(Garden_2)
       
    except:
        print("Loi ham")
    check_data_transmit = False
    print("##########################################################################################################")
#End

