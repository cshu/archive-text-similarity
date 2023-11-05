package io.github.cshu.sse;

import java.util.*;

public record SimResult(
    String error, String hash, String name, ArrayList<SimPair> others, String type) {}
