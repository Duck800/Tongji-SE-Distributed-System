//2151133 孙韩雅 q1
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateData {
    //定义三个组的分割点
    private static final double GROUP_1_THRESHOLD = 0.46;
    private static final double GROUP_3_THRESHOLD = 0.72;
    private static final double TERMINATION_THRESHOLD = 0.91;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Random rand = new Random(244);
        List<Double> valuesGroup1 = new ArrayList<>();
        List<Double> valuesGroup2 = new ArrayList<>();
        List<Double> valuesGroup3 = new ArrayList<>();

        int count = 0; //记录数对个数
        int grp1start = 0; //记录Grp1的起始字节位置

        int ValuesGreaterThanHalf = 0;
        int PairsGreaterThanHalf = 0;

        while (true) {
            //循环生成随机数并进行分组
            double d1 = rand.nextDouble();
            double d2 = rand.nextDouble();
            if (d1 > TERMINATION_THRESHOLD && d2 > TERMINATION_THRESHOLD) {
                break;
            }
            else if (d1 < GROUP_1_THRESHOLD && d2 < GROUP_1_THRESHOLD) {
                valuesGroup1.add(d1);
                valuesGroup1.add(d2);
            } else if (d1 > GROUP_3_THRESHOLD && d2 > GROUP_3_THRESHOLD) {
                valuesGroup3.add(d1);
                valuesGroup3.add(d2);
            } else {
                valuesGroup2.add(d1);
                valuesGroup2.add(d2);
            }
            count++;
            if(d1 > 0.5 && d2 > 0.5){
                ValuesGreaterThanHalf += 2;
                PairsGreaterThanHalf += 1;
            }
            else if(d1 > 0.5 || d2 > 0.5){
                ValuesGreaterThanHalf += 1;
            }
        }

        int grp2start = grp1start + valuesGroup1.size() * 8;//记录Grp2的起始字节位置
        int grp3start = grp2start + valuesGroup2.size() * 8;//记录Grp3的起始字节位置

        try (RandomAccessFile ranFile = new RandomAccessFile("h2q1.dat","rw");
             OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ranFile.getFD()), StandardCharsets.UTF_8)) {
            //写入h2q1.dat文件

            //文件头信息
            writer.write(count+" ");//所有数对个数
            writer.write(valuesGroup1.size()/2+" ");//Grp1所含数对个数
            writer.write(grp1start+" ");//Grp1所含起始字节位置
            writer.write(valuesGroup2.size()/2+" ");//Grp2所含数对个数
            writer.write(grp2start+" ");//Grp2所含起始字节位置
            writer.write(valuesGroup3.size()/2+" ");//Grp3所含数对个数
            writer.write(grp3start+" \n");//Grp3所含起始字节位置
            writer.write("——————————————————————————————————————————————————————————————————————————————————————————\n");

            //数对内容
            for (Double value : valuesGroup1) {
                byte[] bytes = doubleToBytes(value);
                writer.write(Arrays.toString(bytes) + (valuesGroup1.indexOf(value) % 2 == 0?" ":"\n"));
            }
            writer.write("——————————————————————————————————————————————————————————————————————————————————————————\n");
            for (Double value : valuesGroup2) {
                byte[] bytes = doubleToBytes(value);
                writer.write(Arrays.toString(bytes) + (valuesGroup2.indexOf(value) % 2 == 0?" ":"\n"));
            }
            writer.write("——————————————————————————————————————————————————————————————————————————————————————————\n");
            for (Double value : valuesGroup3) {
                byte[] bytes = doubleToBytes(value);
                writer.write(Arrays.toString(bytes) + (valuesGroup3.indexOf(value) % 2 == 0?" ":"\n"));
            }
            writer.write("——————————————————————————————————————————————————————————————————————————————————————————\n");

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.print("整体时长：" + duration + "ms\n");
            System.out.print("数对中大于0.5的double数值个数：" + ValuesGreaterThanHalf + "个\n");
            System.out.print("数对中大于0.5的double数对个数：" + PairsGreaterThanHalf + "对\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] doubleToBytes(double value) {
        //转换为字节数组
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putDouble(value);
        return buffer.array();
    }
}