import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String SERVER_HOST = "localhost";
    private static String SERVER_PORT = "12345";

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.print("Enter server host ip:");
            SERVER_HOST = scanner.next();
            System.out.print("Enter the port：");
            SERVER_PORT = scanner.next();

            Socket socket = new Socket(SERVER_HOST, Integer.parseInt(SERVER_PORT));
            System.out.println("Connected to server.");

            // 传输文件
            System.out.println("Enter the uploaded file's path:");
            String filePath = scanner.next();
            uploadFile(socket, filePath);

            /*// 下载文件
            String fileName = "file.txt";
            String savePath = "path/to/save";
            downloadFile(socket, fileName, savePath);*/

            // 关闭连接
            socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void uploadFile(Socket socket, String filePath) throws IOException {
        File file = new File(filePath);

        try (OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            // 发送文件名和文件大小
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeLong(file.length());

            // 分块传输文件数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            System.out.println("File uploaded successfully.");
        }
    }

    private static void downloadFile(Socket socket, String fileName, String savePath) throws IOException {
        try (InputStream inputStream = socket.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(savePath + File.separator + fileName);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            // 发送下载文件的文件名
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(fileName);

            // 接收文件数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
            bufferedOutputStream.flush();

            System.out.println("File downloaded successfully.");
        }
    }
}