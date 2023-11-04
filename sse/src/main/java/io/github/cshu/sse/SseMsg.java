package io.github.cshu.sse;

import java.io.*;
import java.sql.*;
import java.util.*;

// it seems Google JSON Style Guide uses camelCased so using java-style naming seems fine
// https://stackoverflow.com/questions/5543490/json-naming-convention-snake-case-camelcase-or-pascalcase

public class SseMsg {
  String type;
  SseMsg() {}
}
