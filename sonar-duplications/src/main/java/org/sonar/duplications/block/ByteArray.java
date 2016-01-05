/*
 * SonarQube :: Duplications
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.duplications.block;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Represents hash for {@link Block}. Immutable.
 * 
 * TODO Godin: would be better to rename to BlockHash,
 * also maybe we can incorporate it into Block to reduce memory footprint during detection of duplicates
 */
public final class ByteArray {

  private final byte[] bytes;

  /**
   * Cache for hash code.
   */
  private int hash;

  public ByteArray(String hexString) {
    int len = hexString.length();
    this.bytes = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
    }
  }

  public ByteArray(byte[] bytes) {
    this.bytes = new byte[bytes.length];
    System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
  }

  public ByteArray(long value) {
    this.bytes = new byte[] {
      (byte) (value >>> 56),
      (byte) (value >>> 48),
      (byte) (value >>> 40),
      (byte) (value >>> 32),
      (byte) (value >>> 24),
      (byte) (value >>> 16),
      (byte) (value >>> 8),
      (byte) value};
  }

  public ByteArray(int value) {
    this.bytes = new byte[] {
      (byte) (value >>> 24),
      (byte) (value >>> 16),
      (byte) (value >>> 8),
      (byte) value};
  }

  public ByteArray(int[] intArray) {
    ByteBuffer bb = ByteBuffer.allocate(intArray.length * 4);
    for (int i : intArray) {
      bb.putInt(i);
    }
    this.bytes = bb.array();
  }

  public byte[] getBytes() {
    return bytes;
  }

  public int[] toIntArray() {
    // Pad the size to multiple of 4
    int size = (bytes.length / 4) + (bytes.length % 4 == 0 ? 0 : 1);
    ByteBuffer bb = ByteBuffer.allocate(size * 4);
    bb.put(bytes);
    bb.rewind();
    IntBuffer ib = bb.asIntBuffer();
    int[] result = new int[size];
    ib.get(result);
    return result;
  }

  private static final String HEXES = "0123456789abcdef";

  public String toHexString() {
    StringBuilder hex = new StringBuilder(2 * bytes.length);
    for (byte b : bytes) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F));
    }
    return hex.toString();
  }

  @Override
  public String toString() {
    return toHexString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ByteArray other = (ByteArray) o;
    return Arrays.equals(bytes, other.bytes);
  }

  @Override
  public int hashCode() {
    int h = hash;
    int len = bytes.length;
    if (h == 0 && len > 0) {
      h = Arrays.hashCode(bytes);
      hash = h;
    }
    return h;
  }

}
