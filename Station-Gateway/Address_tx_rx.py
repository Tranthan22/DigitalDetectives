import time
import serial
import RPi.GPIO as GPIO

NODE_SENSOR_1 = "1"
NODE_SENSOR_2 = "2"
NODE_MOTOR_1 = "3"
NODE_MOTOR_2 = "4"

ser = serial.Serial(
    port = '/dev/ttyS0',
    baudrate = 9600,
    parity = serial.PARITY_NONE,
    stopbits = serial.STOPBITS_ONE,
    bytesize = serial.EIGHTBITS,
    timeout = 1
)

def transmit_node(Address, data):
    ser.write((Address + data).encode())
    
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)


GPIO.setup(16, GPIO.OUT) 
GPIO.setup(18, GPIO.OUT)
GPIO.output(16, GPIO.LOW)
GPIO.output(18, GPIO.LOW)

print("Raspberry's sending: ")

data = "hehe"

try:
    while True:
        transmit_node(NODE_SENSOR_1, data)
        time.sleep(1)
        ser.flush()
        time.sleep(1)
        
except KeyboardInterrupt:
    ser.close()


