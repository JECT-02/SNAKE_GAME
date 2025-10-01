
package com.snakegame.net;

import java.io.*;
import java.net.Socket;

public class SocketWrapper implements AutoCloseable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int id;

    public SocketWrapper(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void println(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
