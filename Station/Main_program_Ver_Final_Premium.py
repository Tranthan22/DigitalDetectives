import os
import sys
import pygame

import RPi.GPIO as GPIO
import serial
import requests

import time
import datetime
import threading

from Crypto.Cipher import AES
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding

###############################################################################################################################################################
#Interface
main_program_running = False
show_login_interface = True  # Flag to control the display of the login interface
User = ''

def login(user, password):
    global main_program_running, show_login_interface, User
    api_url = 'https://digitaldetectives.top/App/User/check_user.php'
    data = {
        'user_namel' : user,
        'passwordl' : password
    }

    try:
        response = requests.post(api_url, data=data)
        if response.json()['success'] == True:
            User = str(user)
            thread1.start()
            thread2.start()
            main_program_running = True
            show_login_interface = False  # Hide the login interface
        else:
            print('user or password was wrong!!!')
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
def draw_rounded_rect(surface, rect, border_color, fill_color, radius):
    x, y, width, height = rect
    
    # Draw the filled white interior
    pygame.draw.rect(surface, fill_color, (x + radius, y, width - 2 * radius, height))
    pygame.draw.rect(surface, fill_color, (x, y + radius, width, height - 2 * radius))
    pygame.draw.ellipse(surface, fill_color, (x, y, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, fill_color, (x + width - 2 * radius, y, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, fill_color, (x, y + height - 2 * radius, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, fill_color, (x + width - 2 * radius, y + height - 2 * radius, 2 * radius, 2 * radius))
    
    # Draw the border
    pygame.draw.rect(surface, border_color, (x + radius, y, width - 2 * radius, height))
    pygame.draw.rect(surface, border_color, (x, y + radius, width, height - 2 * radius))
    
    # Draw the four filled circles at the corners
    pygame.draw.ellipse(surface, border_color, (x, y, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, border_color, (x + width - 2 * radius, y, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, border_color, (x, y + height - 2 * radius, 2 * radius, 2 * radius))
    pygame.draw.ellipse(surface, border_color, (x + width - 2 * radius, y + height - 2 * radius, 2 * radius, 2 * radius))

def pygame_interface():
    pygame.init()

    screen_width = 800
    screen_height = 600
    screen = pygame.display.set_mode((screen_width, screen_height))
    pygame.display.set_caption("Digital Detectives")

    # Update your color values with green shades
    green = (0, 128, 0)
    white = (255, 255, 255)
    light_green = (173, 255, 173)
    black = (0, 0, 0)
    blue = (0, 128, 255)
    gray = (85, 94, 80)

    # Font
    font = pygame.font.Font(None, 28)

    # Hộp văn bản
    class TextBox:
        def __init__(self, x, y, width, height, label='', max_length=20, is_password=False):
            self.rect = pygame.Rect(x, y, width, height)
            self.color = gray
            self.text = ""
            self.label = label
            self.max_length = max_length
            self.active = False
            self.txt_surface = font.render(self.text, True, self.color)
            self.is_password = is_password

        def handle_event(self, event):
            if event.type == pygame.MOUSEBUTTONDOWN:
                if self.rect.collidepoint(event.pos):
                    self.active = not self.active
                    self.color = green if self.active else gray
                else:
                    self.active = False
                    self.color = gray
            if event.type == pygame.KEYDOWN and self.active:
                if event.key == pygame.K_RETURN:
                    self.active = False
                    self.color = gray
                elif event.key == pygame.K_BACKSPACE:
                    self.text = self.text[:-1]
                else:
                    self.text += event.unicode
                    # Kiểm tra và cắt văn bản nếu vượt quá độ dài tối đa
                    if len(self.text) > self.max_length:
                        self.text = self.text[:self.max_length]
                self.txt_surface = font.render(self.text, True, self.color)

        def draw(self, screen):
            pygame.draw.rect(screen, self.color, self.rect, 2)
            if self.is_password:
                # If it's a password field, display asterisks
                password_display = "*" * len(self.text)
                self.txt_surface = font.render(password_display, True, self.color)
            else:
                self.txt_surface = font.render(self.text, True, self.color)

            screen.blit(self.txt_surface, (self.rect.x + 5, self.rect.y + 5))
            label_surface = font.render(self.label, True, black)
            label_rect = label_surface.get_rect(topleft=(self.rect.x, self.rect.y - 30))
            screen.blit(label_surface, label_rect)

    #username and password fields
    box_width = 250  # Choose a consistent width for both boxes
    username_box = TextBox((screen_width - box_width) // 2, 180, box_width, 30, 'Username:')
    password_box = TextBox((screen_width - box_width) // 2, 250, box_width, 30, 'Password:', is_password=True)

    # Nút đăng nhập
    button_x = (screen_width - 120) // 2
    login_button = pygame.Rect(button_x, 320, 120, 40)


    enclosing_rect = pygame.Rect(240, 100, 320, 350)  # Adjust the dimensions as needed
    border_radius = 25  # Adjust the radius for rounded corners as needed

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            if show_login_interface:
                username_box.handle_event(event)
                password_box.handle_event(event)

                if event.type == pygame.KEYDOWN and event.key == pygame.K_RETURN:
                    user = username_box.text
                    password = password_box.text
                    login(user, password)

                if event.type == pygame.MOUSEBUTTONDOWN:
                    mouse_pos = pygame.mouse.get_pos()
                    if login_button.collidepoint(mouse_pos):
                        user = username_box.text
                        password = password_box.text
                        login(user, password)
                        
                elif event.type == pygame.KEYDOWN and event.key == pygame.K_TAB:
                    if username_box.active:
                        username_box.active = False
                        password_box.active = True
                    elif password_box.active:
                        password_box.active = False
                        username_box.active = True
        screen.fill(light_green)
        
        if show_login_interface:
            draw_rounded_rect(screen, enclosing_rect, white, white, border_radius)
            username_box.draw(screen)
            password_box.draw(screen)
            draw_rounded_rect(screen, login_button, green, green, 12)
            #pygame.draw.rect(screen, green, login_button)
            text_surface = font.render("Log in", True, white)
            text_rect = text_surface.get_rect(center=login_button.center)
            screen.blit(text_surface, text_rect)

        if main_program_running:
            # Display a message in the center of the window
            message = "The main program is running. Look at CMD to supervise!!"
            message_surface = font.render(message, True, black)
            message_rect = message_surface.get_rect(center=(screen_width // 2, screen_height // 2))
            screen.blit(message_surface, message_rect)

        pygame.display.flip()

    pygame.quit()
    sys.exit()
###############################################################################################################################################################




###############################################################################################################################################################
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

GPIO.setup(22, GPIO.OUT)
GPIO.setup(24, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(38, GPIO.OUT)
GPIO.setup(40, GPIO.OUT)

#Init Garden
###############################################################################################################################################################
class Sensor:
    def __init__(self, sensor_link, sensor_id, sensor_address, node_soil, node_air, node_temp, battery, node_soil_old, node_air_old, node_temp_old, battery_old, airWarning, soilWarning, batteryWarning):
        self.sensor_link = sensor_link
        self.sensor_id = sensor_id
        self.node_soil = node_soil
        self.node_air = node_air
        self.node_temp = node_temp
        self.battery = battery
        self.node_soil_old = node_soil_old
        self.node_air_old = node_air_old
        self.node_temp_old = node_temp_old
        self.battery_old = battery_old
        self.airWarning = airWarning
        self.soilWarning = soilWarning
        self.batteryWarning = batteryWarning
        self.sensor_address = sensor_address

        
class Motor:
     def __init__(self, motor_link, motor_id, motor_address, node_auto, pump_1, pump_2, pump_3, data_transmit_motor, pumpTime, dailyPump, pump_1_old, pump_2_old, pump_3_old):
        self.motor_link = motor_link
        self.motor_id = motor_id
        self.motor_address = motor_address
        self.node_auto = node_auto
        self.pump_1 = pump_1
        self.pump_2 = pump_2
        self.pump_3 = pump_3
        self.data_transmit_motor = data_transmit_motor
        self.pumpTime = pumpTime
        self.dailyPump = dailyPump
        self.pump_1_old = pump_1_old
        self.pump_2_old = pump_2_old
        self.pump_3_old = pump_3_old
###############################################################################################################################################################
        
        
#Bao mat
###############################################################################################################################################################
# Key 128-bit co dinh
key = bytes([0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F])

def decodez(encrypted_data, key):
    cipher = AES.new(key, AES.MODE_ECB)
    decrypted_data = cipher.decrypt(encrypted_data)
    real_data = decrypted_data.decode('utf-8')
    return real_data

def encodez(transfer_data, key):
    plaintext = transfer_data.encode('utf-8')
    cipher = Cipher(algorithms.AES(key), modes.ECB(), backend=default_backend())
    encryptor = cipher.encryptor()
    encrypted_data = encryptor.update(plaintext) + encryptor.finalize() 
    return encrypted_data
###############################################################################################################################################################

#Core 1
###############################################################################################################################################################
def receive_data(key):
    print("Raspberry's receiving node: ")

    try:
        receive_data = ''
        data = ser.readline()
        if len(data) == 20 and data[17:20].decode() == 'EEE':
            real_data = decodez(data[1:17], key)
            receive_data = chr(data[0]) + real_data
        print(receive_data)
        
    except KeyboardInterrupt:
        ser.close()
    return receive_data
###############################################################################################################################################################
def Backup_data(data):
    with open("data.txt", "a") as file:
        file.write(data + "\n")
        print("Da luu du lieu thanh cong")
###############################################################################################################################################################    
def analyze_receive_node_s(data):
    moisture_s = int(data[1:4])/10
    temp_s = int(data[4:7])/10
    humidity_s = int(data[7:10])/10
    Battery_s = round(int(data[10:13])/10)
    sensor_address = int(data[13:16])
    return moisture_s, temp_s, humidity_s, Battery_s, sensor_address
    
def analyze_receive_node_m(data):
    Watering_engine_m1 = data[1]
    Mist_engine_m1 = data[2]
    Motor_3_m1 = data[3]
    motor_address = data[4:7]
    return Watering_engine_m1, Mist_engine_m1, Motor_3_m1, motor_address
###############################################################################################################################################################   


#Core 2
###############################################################################################################################################################
def Check_receive(data):
    check = 0
    for i in range (16):
        check = ord(data[i]) ^ check
    if ord(data[16]) == 10 and check == 10:
        data_check = '1'
    elif ord(data[16]) != check:
        data_check = '0'
    else:
        data_check = '1'  
    return data_check
###############################################################################################################################################################
def Response_data(Address, data):
    print("Raspberry's sending sensor: ")
    data_transmit = Address + '\x17' + data + 'E'
    print(data_transmit)
        
    try:
        ser.write(data_transmit.encode())
        ser.flush()
        print("Da phan hoi ve sensor")
            
    except KeyboardInterrupt:
        ser.close()   
###############################################################################################################################################################
def Analyze_data_receive_m(receive_data, Motor):
    Motor.pump_1, Motor.pump_2, Motor.pump_3, Motor.motor_address = analyze_receive_node_m(receive_data)
    
def Analyze_data_receive_s(receive_data, Sensor):
    Sensor.node_soil, Sensor.node_temp, Sensor.node_air, Sensor.battery, Sensor.sensor_address = analyze_receive_node_s(receive_data)
    Check_data = Check_receive(receive_data)
    Response_data(Sensor.sensor_id, Check_data)    
###############################################################################################################################################################
def transmit_server_sensor(sensor_id, node_air, node_soil, node_temp, battery, User):
    api_url = 'https://digitaldetectives.top/Main/Working/update_sensor.php' 
                                                                                                      
    data = {
        'user' : User,
        'sensor_id' : sensor_id,
        'node_air' : node_air,
        'node_soil' : node_soil,
        'node_temp' : node_temp,
        'battery' : battery,
    }
    
    try:
        response = requests.post(api_url, data=data)

        if response.status_code == 200:
            print('Du lieu sensor' + sensor_id + ' da duoc gui len co so du lieu')
        else:
            print('Co loi xay ra khi gui du lieu')
        print(response.request.body)
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
        
def transmit_server_motor(motor_id, pump_1, pump_2, pump_3, User): 
    api_url = 'https://digitaldetectives.top/Main/Working/update_motor.php' 
            
    data = {
        'user' : User,
        'motor_id' : motor_id,
        'pump_1' : pump_1,
        'pump_2' : pump_2,
        'pump_3' : pump_3,
    }

    try:
        response = requests.post(api_url, data=data)

        if response.status_code == 200:
            print('Du lieu motor' + motor_id + ' da duoc gui len co so du lieu')
        else:
            print('Co loi xay ra khi gui du lieu')
        print(response.request.body)
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
###############################################################################################################################################################
def Check_and_transmit_server_s(Sensor, User):
    if ((Sensor.node_air != Sensor.node_air_old) or (Sensor.node_soil != Sensor.node_soil_old) or (Sensor.node_temp != Sensor.node_temp_old) or (Sensor.battery != Sensor.battery_old)):
        transmit_server_sensor(str(Sensor.sensor_address) + '23', Sensor.node_air, Sensor.node_soil, Sensor.node_temp, Sensor.battery, User)
        Sensor.node_soil_old = Sensor.node_soil
        Sensor.node_air_old = Sensor.node_air
        Sensor.node_PH_old = Sensor.node_temp
        Sensor.battery_old = Sensor.battery
def Check_and_transmit_server_m(Motor, User):
    transmit_server_motor(str(Motor.motor_address) + '23', Motor.pump_1, Motor.pump_2, Motor.pump_3, User)
###############################################################################################################################################################


#Core 3
###############################################################################################################################################################
def receive_server_motor(motor_id, User):
    api_url = 'https://digitaldetectives.top/Main/Working/get_motor.php'
    

    data = {
        'user' : User,
        'motor_id' : motor_id
    }

    try:
        response = requests.get(api_url, params=data)

        if response.status_code == 200:
            print('Du lieu ' + motor_id + ' da duoc gui tu server ve station thanh cong')
        else:
            print('Co loi xay ra khi lay du lieu')
        
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
        
    receive = response.json()
    node_auto = receive['node_auto']
    Watering_engine = receive['pump_1']
    Mist_engine = receive['pump_2']
    Motor_3 = receive['pump_3']
    pumpTime = receive['pumpTime']
    dailyPump = receive['dailyPump']
    motor_link = receive['linked']
    return node_auto, Watering_engine, Mist_engine, Motor_3, pumpTime, dailyPump, motor_link

def receive_server_sensor(sensor_id, User):
    api_url = 'https://digitaldetectives.top/Main/Working/get_sensor.php'

    data = {
        'user' : User,
        'sensor_id' : sensor_id
    }

    try:
        response = requests.get(api_url, params=data)

        if response.status_code == 200:
            print('Du lieu ' + sensor_id + ' da duoc gui tu server ve station thanh cong')
        else:
            print('Co loi xay ra khi lay du lieu')
        
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
        
    receive = response.json()
    airWarning = receive['airWarning']
    soilWarning = receive['soilWarning']
    batteryWarning = receive['batteryWarning']
    sensor_link = receive['linked']
    return airWarning, soilWarning, batteryWarning, sensor_link
###############################################################################################################################################################
def Receive_server_sensor(Sensor, User):
    Sensor.airWarning, Sensor.soilWarning, Sensor.batteryWarning, Sensor.sensor_link = receive_server_sensor(str(Sensor.sensor_address) + '23', User)
def Receive_server_motor(Motor, User):
    Motor.node_auto, Motor.pump_1, Motor.pump_2, Motor.pump_3, Motor.pumpTime, Motor.dailyPump, Motor.motor_link = receive_server_motor(str(Motor.motor_address) + '23', User)
###############################################################################################################################################################


#Core 4
###############################################################################################################################################################
def Data_motor_real(node_auto, pump_1, pump_2, pump_3, pumpTime):
    if int(pumpTime) < 10:
        data = node_auto + pump_1 + pump_2 + pump_3 + '0' + pumpTime + '0'*10                   #Manual
    else:
        data = node_auto + pump_1 + pump_2 + pump_3 + pumpTime + '0'*10  
    return data
###############################################################################################################################################################
def Data_motor(Address_motor, encrypted_data):  
    data = Address_motor.encode('utf-8') + b'\x17S' + encrypted_data + b'EEE'                   #Manual
    return data
###############################################################################################################################################################
def Transmit_motor(data):
    print("Raspberry's sending motor: ")
    print(data)

    try:
        ser.write(data)
        ser.flush()
        print("Da gui du lieu ve dong co")
        
    except KeyboardInterrupt:
        ser.close()     
###############################################################################################################################################################
def Check_and_transmit_motor_mode_3(Sensor, Motor, key):
    if int(Motor.node_auto) == 1:
        if int(Sensor.node_air) < int(Sensor.airWarning):
            Motor.pump_1 = '1'
        if int(Sensor.node_soil) < int(Sensor.soilWarning):
            Motor.pump_2 = '1'
        if int(Sensor.battery) < int(Sensor.batteryWarning):
            Motor.pump_3 = '1'
    if Motor.pump_1 != Motor.pump_1_old or Motor.pump_2 != Motor.pump_2_old or Motor.pump_3 != Motor.pump_3_old:
        encrypted_data = encodez(Data_motor_real(Motor.node_auto, Motor.pump_1, Motor.pump_2, Motor.pump_3, Motor.pumpTime), key)
        Motor.data_transmit_motor = Data_motor(Motor.motor_id, encrypted_data)
        Transmit_motor(Motor.data_transmit_motor)
        Motor.pump_1_old = Motor.pump_1
        Motor.pump_2_old = Motor.pump_2
        Motor.pump_3_old = Motor.pump_3
def Check_and_transmit_motor_mode_2(Motor, key):
    if Motor.pump_1 != Motor.pump_1_old or Motor.pump_2 != Motor.pump_2_old or Motor.pump_3 != Motor.pump_3_old:
        encrypted_data = encodez(Data_motor_real(Motor.node_auto, Motor.pump_1, Motor.pump_2, Motor.pump_3, Motor.pumpTime), key)
        Motor.data_transmit_motor = Data_motor(Motor.motor_id, encrypted_data)
        Transmit_motor(Motor.data_transmit_motor)
        Motor.pump_1_old = Motor.pump_1
        Motor.pump_2_old = Motor.pump_2
        Motor.pump_3_old = Motor.pump_3
###############################################################################################################################################################
def Check_mode_and_send(quantity, mode, Motor_list, Sensor_list, motor_count, sensor_count, key):
    for h in range (quantity):
        if mode[h] == '2':
            for k in range (motor_count): 
                if Motor_list[k].motor_link == str(h+1):
                    Check_and_transmit_motor_mode_2(Motor_list[k], key)
                    break
        if mode[h] == '3':
            for m in range (motor_count):
                if Motor_list[m].motor_link == str(h+1):
                    for n in range (sensor_count):
                        if Sensor_list[n].sensor_link == str(h+1):
                            Check_and_transmit_motor_mode_3(Sensor_list[n], Motor_list[m], key)
                            break
###############################################################################################################################################################



#Them node moi
###############################################################################################################################################################
def receive_data_connect():
    print("Raspberry's receiving node: ")

    try:
        receive_data = ''
        s = ser.readline()
        data = s.decode()
        for i in range (0, len(data)):
            receive_data += data[i]
            if data[i] == 'E':
                break
        print(receive_data)
            
    except KeyboardInterrupt:
        ser.close()
    return receive_data

###############################################################################################################################################################
def add_node_server(device_id, typez, User):
    api_url = 'https://digitaldetectives.top/Main/Connect/newDevice.php'
                                                                                  
    data = {
        'user' : User,
        'device' : device_id,
        'type' : typez
    }
    
    try:
        response = requests.post(api_url, data=data)

        if response.status_code == 200:
            print('Du lieu ' + device_id + ' da duoc gui len co so du lieu')
        else:
            print('Co loi xay ra khi gui du lieu')
        print(response.request.body)
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
###############################################################################################################################################################
def Response_connect(Address, data):
    print("Raspberry's sending node connect: ")
    data_transmit = Address + '\x17' + data + '\x01\x01E'
    print(data_transmit)
        
    try:
        ser.write(data_transmit.encode())
        ser.flush()
        print("Da phan hoi ve node")
            
    except KeyboardInterrupt:
        ser.close()   
###############################################################################################################################################################
        
        

#Lay du lieu Garden
###############################################################################################################################################################
def Receive_garden_data(User):
    api_url = 'https://digitaldetectives.top/Main/Working/numberOfGarden.php'

    data = {
        'user' : User,
    }

    try:
        response = requests.get(api_url, params=data)

        if response.status_code == 200:
            print('')
            #print('Du lieu garden da duoc gui tu server ve station thanh cong')
        else:
            print('Co loi xay ra khi lay du lieu')
        
    except requests.exceptions.RequestException as e:
        print('Loi ket noi', str(e))
        
    receive = response.json()
    quantity = receive['quantity']
    mode = receive['mode']
    return quantity, mode
###############################################################################################################################################################           
        
        
#Thoi gian thuc        
###############################################################################################################################################################
def Time_now():
    current_time = datetime.datetime.now()
    hour = str(current_time.hour).zfill(2)
    minute = str(current_time.minute).zfill(2)
    time = hour + minute
    return time
###############################################################################################################################################################   
 
 
 
###############################################################################################################################################################
Sensor_list = [Sensor(0, '21', 261, 70, 75, 20, 90, 70, 75, 25, 90, 50, 50, 20) for _ in range(100)]
Motor_list = [Motor(0, '21', 262, 0, 0, 1, 0, "12345667890", 15, 2000, 0, 1, 1) for _ in range(100)]
sensor_count = 0
motor_count = 0

def Thread_1():
    global Sensor_list, Motor_list, sensor_count, motor_count, User, key
    while True:
        check_data_transmit = False  
        while not check_data_transmit:
            print("Dang cho du lieu tu node...")
            receive_node = receive_data(key)
            
            try:
                if GPIO.input(24) == GPIO.LOW:                                        #An nut
                    GPIO.output(22, GPIO.HIGH)
                    check_data_transmit_connect = False
                    while not check_data_transmit_connect:
                        print("Dang cho du lieu tu node connect...")
                        receive_node_connect = receive_data_connect()
                        if len(receive_node_connect) == 4 or GPIO.input(24) == GPIO.LOW:
                            check_data_transmit_connect = True
                             
                    if receive_node_connect[0] == '1':
                        a = int.from_bytes(receive_node_connect[1:3].encode(), byteorder="big")
                        Sensor_list[sensor_count] = Sensor('0', receive_node_connect[1:3], a, 70, 75, 20, 90, 70, 75, 25, 90, 50, 50, 20)
                        sensor_count = sensor_count + 1
                        add_node_server(str(a), '1', User)
                        if sensor_count < 100:
                            Response_connect(receive_node_connect[1:3], '11')
                        else:
                            Response_connect(receive_node_connect[1:3], '10')
                            
                    if receive_node_connect[0] == '2':
                        b = int.from_bytes(receive_node_connect[1:3].encode(), byteorder="big")
                        Motor_list[motor_count] = Motor('0', receive_node_connect[1:3], b, 0, 0, 1, 0, "12345667890", 15, 2000, 0, 1, 1)
                        motor_count = motor_count + 1
                        add_node_server(str(b), '2', User)
                        if motor_count < 100:
                            Response_connect(receive_node_connect[1:3], '21')
                        else:
                            Response_connect(receive_node_connect[1:3], '20')
                    GPIO.output(22, GPIO.LOW)
            
                else:                                                                #Khong an nut
                    GPIO.output(22, GPIO.LOW)
                    if len(receive_node) == 17:
                        check_data_transmit = True
            except:
                print("Ngung ket noi")
                print("##########################################################################################################")
                
        try:
            Backup_data(receive_node)
            if receive_node[0] == '1':
                sensor_address_local = int(receive_node[13:16])
                for i in Sensor_list:
                    if int(i.sensor_address) == sensor_address_local:
                        Analyze_data_receive_s(receive_node, i)
                        Check_and_transmit_server_s(i, User)
                        
            if receive_node[0] == '2':
                motor_address_local = int(receive_node[4:7])
                for j in range (motor_count):
                    if int(Motor_list[j].motor_address) == motor_address_local:
                        Analyze_data_receive_m(receive_node, Motor_list[j])
                        Check_and_transmit_server_m(Motor_list[j], User)
            GPIO.output(38, GPIO.LOW)    
        except:
            GPIO.output(38, GPIO.HIGH)
            print("Loi ham")
            print("##########################################################################################################") 
        check_data_transmit = False
                                                                         
def Thread_2():                    
    global Sensor_list, Motor_list, sensor_count, motor_count, User, key
    while True:
        try:
            quantity, mode = Receive_garden_data(User)
            
            if quantity > 0:
                if sensor_count != 0:
                    for j in range(sensor_count):
                        Receive_server_sensor(Sensor_list[j], User)
                    
                if motor_count != 0:
                    for i in range(motor_count):
                        Receive_server_motor(Motor_list[i], User)
                        if Motor_list[i].dailyPump == Time_now():
                            encrypted_data = encodez(Data_motor_real(Motor_list[i].node_auto, '1', '1', '1', Motor_list[i].pumpTime), key)
                            Motor_list[i].data_transmit_motor = Data_motor(Motor_list[i].motor_id, encrypted_data)
                            Transmit_motor(Motor_list[i].data_transmit_motor)        
                Check_mode_and_send(quantity, mode, Motor_list, Sensor_list, motor_count, sensor_count, key)
            GPIO.output(40, GPIO.LOW)
        except:
            GPIO.output(40, GPIO.HIGH)
            print("Loi ham")
            print("##########################################################################################################")


#main
pygame_thread = threading.Thread(target=pygame_interface)
thread1 = threading.Thread(target=Thread_1)
thread2 = threading.Thread(target=Thread_2)

pygame_thread.start()

pygame_thread.join()
thread1.join()
thread2.join()
         
#End






