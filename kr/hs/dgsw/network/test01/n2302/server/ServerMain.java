package kr.hs.dgsw.network.test01.n2302.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5050);    // 서버 시작(클라이언트 접속 대기)
            InputStream is = null;        // 클라이언트 메시지 입력 받음
            DataInputStream dis = new DataInputStream(is);

            OutputStream os = null;

            System.out.println("서버가 시작되었습니다.");

            while(true) {
                Socket sc = ss.accept(); // 요청이 오기까지 기다림
                System.out.println(sc.getInetAddress() + ": 접속하였습니다."); // getInetAddress -> 연결된 주소 반환
                is = sc.getInputStream();
                os = sc.getOutputStream();

                new ServerThread(
                        sc,
                        new PrintWriter(os, true),
                        new BufferedReader(new InputStreamReader(is))
                );
            }

        } catch (Exception ex) {}
    }
}

