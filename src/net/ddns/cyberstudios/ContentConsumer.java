package net.ddns.cyberstudios;

@FunctionalInterface
public interface ContentConsumer {
    String inner(String text);
}
