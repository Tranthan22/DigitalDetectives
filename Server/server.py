import requests

api_url = 'http://localhost/.iot/update_data.php'

#node_id=node_02&node_auto=false&node_temp=30.7&node_humidity=70&node_PH=8&bump_1=OFF&bump_2=ON
data = {
    'node_id' : 'node_02',
    'node_auto' : 'false',
    'node_temp' : '34.2',
    'node_humidity' : '56',
    'node_PH' : '5',
    'bump_1' : 'ON',
    'bump_2' : 'ON'
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
    
