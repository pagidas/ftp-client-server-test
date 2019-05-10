package dev.kostasakrivos.test.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.PrintCommandListener;

import java.io.*;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class FtpClient {

    private String server;
    private int port;
    private String user;
    private String password;

    private FTPClient ftp;

    private FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;

        // Instantiating the ftp client...
        ftp = new FTPClient();
    }

    public static FtpClient init(String server, int port, String user, String password) {
        return new FtpClient(server, port, user, password);
    }

    public void open() throws IOException {
        // Binding the command listener to the client to print the responses...
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        // Connecting...
        ftp.connect(server, port);

        // Exception handling...
        int reply = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP server!");
        }

        // Logging in after connection has been made successfully...
        ftp.login(user, password);
    }

    public void close() throws IOException {
        // Disconnecting...
        ftp.disconnect();
    }

    public Collection<String> listFiles(String path) throws IOException {
        FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftp.retrieveFile(source, out);
    }

    public void uploadFile(File file, String path) throws IOException {
        ftp.storeFile(path, new FileInputStream(file));
    }
}