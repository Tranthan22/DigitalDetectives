import requests
import json

# Thông tin cần thiết
server_key = "AAAAofTKesM:APA91bFC0_uPDA5OaT7NnFCochG6pg_ylbVYaVj6zAR1NwGO0cVXwXFsNFO0kf1hPTmRBN7dH_QRZQMxD22uoBmEEEH7QbJVfCZ08SVANcHUQc4Rs0HFejQ4AOjdyi3wLato1Oah6g1d"
#device_token = "cF7qhbQAR5SHnTk-LORmwl:APA91bE8SQH1P3hrPnZCg3eEJUKnxnEbzTYRn1J6eyedJ_m45zX7fWqUJ_Mpyg9PTC_LnvDeDrSXoBvms99TU6DjTWcMtelQz39_d7QDx9ODbJCMPwgINFVkHeKbQ37h5GkswK7hO8Aj"
device_token = "ea-7vq18Q2a8ofrKrQlz7t:APA91bFLGKZqpEOZenrPUCmFiHsUmcH37qJMIInSJpvKoc_Q43v4SxyF4ZilYibOhrRfNmnX8YxzMAK04WDNFMVtZj5SYVV6kifO4itVeHGAZYZBrA9I8ieFfXQY6YFlzkCrgnCVh0Nz"

notification_title = "than dep trai"
notification_body = "tien xau trai"

# Xây dựng payload JSON
payload = {
    "to": device_token,
    "data": {
        "title": notification_title,
        "body": notification_body
    }
}

# Định dạng payload thành JSON
json_payload = json.dumps(payload)

# Xây dựng header
headers = {
    "Authorization": "key=" + server_key,
    "Content-Type": "application/json"
}

# Gửi yêu cầu POST tới FCM API
response = requests.post("https://fcm.googleapis.com/fcm/send", data=json_payload, headers=headers)

# Xử lý kết quả
if response.status_code == 200:
    print("Thông báo đã được gửi thành công!")
else:
    print("Gửi thông báo thất bại. Mã phản hồi:", response.status_code)
    print("Nội dung phản hồi:", response.text)
