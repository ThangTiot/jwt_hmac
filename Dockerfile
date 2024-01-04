#Image cơ sở để build
FROM openjdk:17

#Cổng port để gọi container này
EXPOSE 8080

#Tạo thư mục làm việc trên container
#Đây như 1 thư mục ảo được tạo ra để chạy toàn bộ dứ án of mình trong đó
WORKDIR /app

#Copy file chạy của dự án sau khi được build ra vào thư mục chạy của container
COPY build/libs/luvina-jwt-0.0.1-SNAPSHOT.jar app.jar

#Lệnh mặc định chạy khi container chạy
#"java" để khởi động JVM
#"-jar" báo cho JVM biết rằng sẽ thực hiện chạy file jar để nó tìm đến main class để chạy
#CMD["java", "-jar", "app.jar"]

#Xác định lệnh mà container sẽ chạy khi khởi động
#Như bên dưới thì nó sẽ chạy câu lênh: "java -jar /app.jar"
ENTRYPOINT ["java","-jar","app.jar"]