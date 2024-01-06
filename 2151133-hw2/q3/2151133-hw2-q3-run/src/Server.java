import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    static Scanner scanner = new Scanner(System.in);
    private static final int SERVER_PORT = 929;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started. Waiting for client connection...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                // 创建一个线程处理客户端请求
                Thread thread = new Thread(() -> {
                    try {
                        handleClient(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) throws IOException {
        try (InputStream inputStream = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            // 接收文件名和文件大小
            String fileName = dataInputStream.readUTF();
            long fileSize = dataInputStream.readLong();
            System.out.println("Received file: " + fileName + ", size: " + fileSize);

            // 创建文件输出流
            System.out.println("Where will the file to be saved?");
            String filePath = scanner.next();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            // 接收文件数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
            bufferedOutputStream.flush();
            System.out.println("File saved successfully.");

            // 关闭连接
            socket.close();
            System.out.println("Client disconnected.");
        }
    }
}
