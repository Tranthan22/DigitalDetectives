import requests
import time
import serial
import RPi.GPIO as GPIO
import threading
import queue
from Crypto.Cipher import AES


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

key = bytes([0x00, 0x01, 0x02, 0x03,
             0x04, 0x05, 0x06, 0x07,
             0x08, 0x09, 0x0A, 0x0B,
             0x0C, 0x0D, 0x0E, 0x0F])

iv = bytes([0x10, 0x11, 0x12, 0x13,
            0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B,
            0x1C, 0x1D, 0x1E, 0x1F])

#Core 1
#################################################################################################
def receive_data():                                     #Nhan du lieu khong ma hoa
    print("Raspberry's receiving: ")

    try:
        s = ser.readline()
        data = s.decode()
        print(data)
            
    except KeyboardInterrupt:
        ser.close()
    return data
################################################################################################# 
def decode_data_receive():                              #Nhan du lieu da ma hoa va giai ma
    cipher = AES.new(key, AES.MODE_CBC, iv)

    print("Raspberry's receiving: ")

    for i in range (2):
        ciphertext = ser.read(16)
        decrypted_data = cipher.decrypt(ciphertext)

    try: 
        ciphertext = ser.read(16)
        decrypted_data = cipher.decrypt(ciphertext)
        time.sleep(1)
        ser.flush()
        time.sleep(1)
            
    except KeyboardInterrupt:
        ser.close()
    return decrypted_data
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
#################################################################################################
def Check_receive(data):
    empty_list = [''] * 16
    if data == empty_list:
        data_check = 0
    else:
        data_check = 1
    return data_check
##########################################################################################################  
def pad_array(array):
    padding_length = 14 - len(array)
    padded_array = array + [''] * padding_length
    return padded_array
##########################################################################################################
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
def encode_data_transmit(data):
    cipher = AES.new(key, AES.MODE_CBC, iv)
    print("Raspberry's sending: ")

    try:
        encrypted_data = cipher.encrypt(data.encode())
        ser.write(encrypted_data)
        ser.flush()
        time.sleep(1)
            
    except KeyboardInterrupt:
        ser.close()        
##########################################################################################################







##############################################################################################################
#Cau hinh function:
data_queue = queue.Queue()
data_transmit_motor_old = "1321324343423215"

receive_node = [''] * 16
receive_node_2 = [''] * 16
check_data_transmit = False

def Thread_1():
    while not check_data_transmit:
        print("Du lieu duoc gui toi")
        receive_node = receive_data()
        time.sleep(1)
        receive_node_2 = receive_data()
        line = ser.readline()
        if line:
            check_data_transmit = True 
    Backup_data(str(receive_node))
    print(receive_node)
    Backup_data(str(receive_node_2))
    print(receive_node_2)
    if receive_node[0] == 'm':
        temp = receive_node_2
        receive_node_2 = receive_node
        receive_node = temp
    data_queue.put(analyze_receive_node_s1(receive_node))
    data_queue.put(analyze_receive_node_m1(receive_node))
    data_queue.put(receive_node)
    
def Thread_2():  
    time.sleep(2)   
    node_soil, node_air, node_PH, battery, bump_1, bump_2, bump_3, receive_node1 = data_queue.get()
    Check_data = Check_receive(receive_node)
    Response_data(NODE_SENSOR_1, pad_array(str(Check_data)))
    transmit_server('node_01', node_air, node_soil, node_PH, bump_1, bump_2, bump_3, battery)
    data_queue.put(node_soil, node_air)
        
def Thread_3():
    data_queue.put(receive_server('node_01'))
        
def Thread_4(): 
    data_transmit_motor = Exchange_data_transmit_motor(NODE_MOTOR_1, data_queue.get())
    if str(data_transmit_motor) != data_transmit_motor_old:
        transmit_motor(str(data_transmit_motor))
        data_transmit_motor_old = str(data_transmit_motor)

#main:
threading_1 = threading.Thread(target=Thread_1)
threading_2 = threading.Thread(target=Thread_2)
threading_3 = threading.Thread(target=Thread_3)
threading_4 = threading.Thread(target=Thread_4)

while True:
    threading_1.start()
    threading_2.start()
    threading_3.start()
    threading_4.start()

    threading_1.join()
    threading_2.join()
    threading_3.join()
    threading_4.join()