import requests
import random
#api_url = 'http://localhost/.iot/firebase_v1.php'
#api_url = 'https://digitaldectectives.azdigi.blog/main/firebase_v1.php'
#api_url = 'https://digitaldectectives.azdigi.blog/main/change_mode.php'
#api_url = 'https://digitaldectectives.azdigi.blog/message/messHistory.php'
api_url = 'https://digitaldectectives.azdigi.blog/setting/setBumpTime.php'



#node_id=node_02&node_auto=false&node_temp=30.7&node_humidity=70&node_PH=8&bump_1=OFF&bump_2=ON
a = random.randint(0,100)
b = random.randint(40,70)
c = random.randint(3,12)
data = {
    'user' : 'tien',
    'node_id' : 'garden_3',
    #'node_auto' : 'false',
    #'node_auto' : '0',
    #'node_air' : '60',
    #'node_soil' : '10',
    #'node_PH' : '30',
    #'bump_1' : '0',
    #'bump_2' : '0',
    #'bump_3' : '0',
    #'battery' : '50',
    'bumpTime' : '5'
} 
    
try:
    response = requests.post(api_url, data=data)

    if response.status_code == 200:
        print('Du lieu da duoc gui len co so du lieu')
    else:
        print('Co loi xay ra khi gui du lieu')
    # Xem dữ liệu request
    #print(response.request.headers)  # In ra các thông tin header của request
    #print(response.request.url)      # In rGa URL mà request được gửi tới
    #print(response.request.method)   # In ra phương thức HTTP của request
    print(response.request.body)     # In ra nội dung body của request (trong trường hợp này là dữ liệu đã được mã hóa)
    #receive = response.json()
    #print(receive)

    # Xem dữ liệu response
    #print(response.status_code)     # In ra mã trạng thái HTTP của response
    #print(response.headers)         # In ra các thông tin header của response
    print(response.text)            # In ra nội dung phản hồi từ server (đối với phản hồi dạng text)
except requests.exceptions.RequestException as e:
    print('Loi ket noi', str(e))
    
