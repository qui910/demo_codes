package com.example.pcap4j;

import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.pcap4j.core.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Pcap4j网卡抓包测试.
 * Junit4参数化测试
 * <p>
 *     目前无法测试，需要以root权限启动才能获取网卡数据
 * </p>
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 16:19
 * @since 1.8
 **/
@Slf4j
@RunWith(Parameterized.class)
public class DumpPacketsTest {
    /**
     * 网卡名称
     */
    private final String nifName;
    /**
     * 采集数据量
     */
    private final int count;

    public DumpPacketsTest(String nifName, int count) {
        this.nifName = nifName;
        this.count = count;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"wlo1", 0}
        });
    }

    @Test
    public void testDumpFromNetInterface() throws PcapNativeException, NotOpenException {
        // 1. 获取主机网卡列表
        List<PcapNetworkInterface> caps = Pcaps.findAllDevs();

        for (PcapNetworkInterface pc:caps) {
            log.info("网卡名称:-->{}，是否运行:--->{}",pc.getName(),pc.isRunning());
        }

        // 根据网卡名获取网卡
        PcapNetworkInterface nif = Pcaps.getDevByName(nifName);
        if (nif == null)  {
            log.error("无当前网卡信息 - {}",nifName);
            return;
        }
        // 2. 打开一个句柄
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10;
        int snapLen = 65536;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);

        // 4. 通过handle创建一个dumper
        final PcapDumper dumper = handle.dumpOpen("dump.pcap");

        final AtomicLong dumped = new AtomicLong(0);
        try {
            // 5. 设置filter过滤经过443端口的TCP包
            handle.setFilter("tcp port 443", BpfProgram.BpfCompileMode.OPTIMIZE);

            // 6. prepare listener
            PacketListener listener = packet -> {
                try {
                    log.info("当前以太网帧，长度：[{}]，数据：[{}]",
                            packet.getRawData().length,byteArrayToHex(packet.getRawData()));
//                        IpV4Packet ipV4Packet=packet.get(IpV4Packet.class);
                    dumper.dump(packet);
                    dumped.incrementAndGet();
                }
                catch (NotOpenException ignore) {
                }
            };

            // 7. 开启循环
            try {
                handle.loop(count, listener);
            } catch (InterruptedException e) {
                log.error("dump网卡数据异常");
            }

            // Print out handle statistics
            PcapStat stats = handle.getStats();
            log.info("导出的以太网数据帧数量: {}",dumped.get());
            log.info("接收的以太网数据帧数量: {}",stats.getNumPacketsReceived());
            log.info("丢失的以太网数据帧数量: {}",stats.getNumPacketsDropped());
            log.info("网卡中丢失的以太网数据帧数量: {}",stats.getNumPacketsDroppedByIf());
            // Supported by WinPcap only
            if (Platform.isWindows()) {
                log.info("捕获的以太网数据帧数量: {}",stats.getNumPacketsCaptured());
            }
        }
        finally {
            dumper.close();
            handle.close();
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
