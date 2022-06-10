package kr.hs.dgsw.network.test01.n2302.server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket sc;
    private PrintWriter pw;
    private BufferedReader br;

    public ServerThread(Socket sc, PrintWriter pw, BufferedReader br) {
        this.sc = sc;
        this.pw = pw;
        this.br = br;

        this.start();
    }

    @Override
    public void run() {
        while(true) {
            process();
        }
    }

    private void LoginReq(String IdPassword){
        System.out.println(IdPassword);
        String[] LoginToken = IdPassword.split(" ");
        String idCheck = LoginToken[0];
        String passwordCheck = LoginToken[1];

        if(idCheck.equals("admin") && passwordCheck.equals("1234")){
            pw.println("true");
        }else{
            pw.println("false");
        }
    }

    private void listFile(String path){
        File dir = new File("/Users/seonghun/공부/spring/network/src/kr/hs/dgsw/network/test01/n2302/server/"+path);
        if(dir.exists()) {
            File[] files = dir.listFiles();
            String file = "";
            for (File name : files) {
                file = file.concat(name.getName() + " ");
            }
            pw.println("fileList>"+file);
        }else{
            pw.println("fileList>"+"undefined");
        }
    }

     private void uploadFile(String path,Socket sc) throws IOException {
        String[] pathList = path.split(" ");
        System.out.println(path);
        File file = new File("/Users/seonghun/공부/spring/network/src/kr/hs/dgsw/network/test01/n2302/server/img/"+pathList[pathList.length - 1]);

        byte[] bytes = new byte[1024];
        int readbit;
        InputStream is = sc.getInputStream();
        BufferedInputStream bir = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bir);

        // 이미 존재하는지 확인
        if(file.exists()){
            pw.println("fileUpload>true"+" "+path);

            String run = br.readLine();
            if("no".equals(run)) {
                return;
            }
        }else {
            pw.println("fileUpload>false"+" "+path);
        }

        String bool = br.readLine();
        System.out.println(bool);
        if(bool.equals("true")){
            int allReadByte = 0;
            FileOutputStream fos = new FileOutputStream(file);

            while ((readbit = dis.read(bytes)) != -1) {
                // 파일 전송
                fos.write(bytes, 0, readbit);
                if(readbit != 1024){
                    break;
                }
            }
            fos.flush();
        }

        pw.println("end");
    }

    private void downloadFile(String fileName, Socket sc) throws IOException {
        File file = new File("/Users/seonghun/공부/spring/network/src/kr/hs/dgsw/network/test01/n2302/server/img/"+fileName);

        if(file.exists()){
            pw.println("download>true"+" "+fileName);
            byte[] bytes = new byte[1024];
            int readbit = 0;
            FileInputStream fis = new FileInputStream(file);


            OutputStream os = sc.getOutputStream();
            BufferedOutputStream bor = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bor);

            while ((readbit = fis.read(bytes)) != -1) {
                // 파일 전송
                dos.write(bytes, 0, readbit);
                if(readbit != 1024){
                    break;
                }
            }
            dos.flush();

        }else{
            pw.println("download>false"+" "+fileName);
        }
    }

    private void process() {
        try {
            while (true) {
                String input = br.readLine();
                if(input == null) {
                    continue;
                }
                String[] tokenParser = input.split(">");

                switch (tokenParser[0]){
                    case "LOGIN":
                        LoginReq(tokenParser[1]);
                        break;
                    case "파일목록":
                        listFile(tokenParser[1]);
                        break;
                    case "업로드":
                        uploadFile(tokenParser[1], sc);
                        break;
                    case "다운로드":
                        downloadFile(tokenParser[1], sc);
                        break;
                    case "exit":
                        pw.println("exit>");
                    default:
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
