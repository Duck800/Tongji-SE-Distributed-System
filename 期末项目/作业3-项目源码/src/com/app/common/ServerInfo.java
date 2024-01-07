package com.app.common;

public class ServerInfo {
    // 存储关于服务器列表的信息
    // 每个服务器有一个端口用于与queryClient通信，一个端口与dataNode通信
    public int serverNum = 1;
    // public final int[] queryPortList = new int[]{2717,4399};
    public final int queryPort = 16450;
    public final int dataPort = 8081;
    public final String[] ipList = new String[] { "100.81.248.231" };
    public final String[] filePathList = new String[] {
            "G:/1.Tongji/Junior/分布式系统/" };
}
