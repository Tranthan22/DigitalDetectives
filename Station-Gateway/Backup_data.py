def save_data_to_file(data):
    with open("data.txt", "a") as file:
        file.write(data + "\n")
        print("Data saved successfully.")

# Ví dụ về việc sử dụng hàm save_data_to_file:
data_to_save = "Hello, world!"
print(type(data_to_save))
save_data_to_file(data_to_save)