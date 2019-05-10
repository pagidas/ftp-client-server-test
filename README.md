# FTP client-server Test

This project tests [apache's commons-net](https://commons.apache.org/proper/commons-net/)
FTP client, using [MockFtpServer](http://mockftpserver.sourceforge.net/)
> The MockFtpServer project provides mock/dummy FTP server 
implementations that can be very useful for testing of FTP client code.

## Getting Started

Although **FTPClient** and **FakeFtpServer** are classes
contained in libraries not in JDK, we are using [Maven](https://maven.apache.org/)
to make sure that those dependencies have to be downloaded 
in order to test the project.

### Prerequisites

Assuming you have either Oracle's JDK 8+ or OpenJDK 8+ 
installed and set in your local machine, then follow these
instructions to get maven according to your OS

**Maven:**

Follow [apache's instructions](https://maven.apache.org/install.html)
to get the source code and manually set its path

Else, you can use a package manager to these things for you

| Unix OS       | Package Managers |
| ------------- |---------------- |
| Debian Linux  | **APT-GET** which uses DPKG |
| Fendora Linux | **DNF** (or the old YUM) which uses RPM |
| Mac OS X      | **Homebrew** |

### Installing

To clone this repository:

```shell
git clone https://github.com/pagidas/ftp-client-server-test.git
```

Change directory to the root of the project and run:

```shell
mvn compile
```

which will download all the dependencies needed to run the project
and compile the source code

```shelll
[INFO] BUILD SUCCESS
[INFO] ---------------------------------------
[INFO] Total time:  1.990 s
[INFO] Finished at: 2019-04-28T13:52:04+03:00
[INFO] ---------------------------------------
```

## Running the Tests

Test are run using maven's maven's _**phase**_ with the
help of **JUnit** framework

```shelll
mvn test
```
Before we run any **@Test**, first and foremost, we have to:

* Initialize the **FakeFtpServer** and its **FileSystem**
containing a dummy file
* Create a **UserAccount** so that not anyone can access
the server
* Start the server by setting the port
* And lastly connect via our **FtpClient**

This a test by itself: 

**@Before** that is executed before all tests.

And after all tests, **@After** is executed which closes the client and the server.

An example of a **@Test** is written as follows:

```java
public class FtpClientTest {
    /* ... */
    
    @Test
    public void testListFiles() throws IOException {
        Collection<String> files = ftpClient.listFiles("");
        assertTrue(files.contains("temp-ftp-file.txt"));
    }
}
```

It tests if **FtpClient.listFiles()** method lists the dummy
file at context root we created in the server's filesystem.

The output of the maven's phase _**test**_ is:

```shell
220 Service ready for new user. (MockFtpServer 2.7.1; see http://mockftpserver.sourceforge.net)
USER user
331 User name okay, need password.
PASS password
230 User logged in, proceed.
SYST
215 "UNIX"
PORT 127,0,0,1,250,137
200 PORT completed.
LIST 
150 File status okay; about to open data connection.
226 Closing data connection. Requested file action successful.
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.392 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

Logs are held by the framework **SLF4J** default _(NOP)_
logger implementation.
