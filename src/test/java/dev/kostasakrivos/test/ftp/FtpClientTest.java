package dev.kostasakrivos.test.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.junit.After;
import org.junit.Before;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;


public class FtpClientTest {

    private FakeFtpServer fakeFtpServer;

    private FtpClient ftpClient;

    @Before
    public void setup() throws IOException {
        // Instantiating a Mock FTP server and adding a user...
        fakeFtpServer = new FakeFtpServer();
        UserAccount user = new UserAccount("user", "password", "/data");
        fakeFtpServer.addUserAccount(user);

        // Initializing a "UNIX" file system into the mock FTP server...
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/temp-ftp-file.txt", "hello ftp client-server!"));

        // Setting the name and the file system...
        fakeFtpServer.setSystemName(FTPClientConfig.SYST_UNIX);
        fakeFtpServer.setFileSystem(fileSystem);

        // Setting the port... 0 (zero) means the server will choose a free port
        fakeFtpServer.setServerControlPort(0);

        // Starting the server...
        fakeFtpServer.start();

        // Testing connection with our FtpClient...
        ftpClient = FtpClient.init("localhost", fakeFtpServer.getServerControlPort(), user.getUsername(), user.getPassword());
        ftpClient.open();
    }

    @After
    public void teardown() throws IOException {
        // Closing the FTP client...
        ftpClient.close();

        // Shutting down the FTP server...
        fakeFtpServer.stop();
    }
}